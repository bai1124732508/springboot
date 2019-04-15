package com.fhzm.app.controller;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.fhzm.common.CacheManager;
import com.fhzm.common.ReturnResult;
import com.fhzm.common.Validator;
import com.fhzm.entity.generator.AuthMember;
import com.fhzm.entity.generator.Configs;
import com.fhzm.service.AuthMemberService;
import com.fhzm.service.ConfigsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.mail.Authenticator;
import javax.mail.Message.RecipientType;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.*;


/**
 * 本工具类用于发送手机验证码、发送邮箱验证码等公共操作
 * * All rights Reserved, Designed By http://www.fenghuaapp.com
 * @Description: AboutAndServiceMapper
 * @date: 2019-03-29 14:14:20
 * @version: V1.0
 * @Copyright: 2019 http://www.fenghuaapp.com/ Inc. All rights reserved.
 * 注意：本内容仅限于风华正茂科技(北京)有限公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Controller
@Slf4j
@Api(tags = "APP工具类控制器",description="APP工具类控制器",hidden=true)
@RequestMapping("app/common")
public class AppCommonController {

    //产品名称:云通信短信API产品,开发者无需替换
    static final String product = "Dysmsapi";
    //产品域名,开发者无需替换
    static final String domain = "dysmsapi.aliyuncs.com";

    @Value("${file.path}")
    private String filePath;//虚拟路径
    @Autowired
    private ConfigsService configsService;
    @Autowired
    private AuthMemberService authMemberService;



    @GetMapping("get_code")
    @ResponseBody
    @ApiOperation(value = "获取手机或邮箱验证码" ,  notes="获取手机或邮箱验证码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "account", value = "用户的账号（邮箱号或者手机号）", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "type", value = "是否需要验证是否在数据库中存在", required = true, paramType = "query", dataType = "boolean"),
            @ApiImplicitParam(name = "sendType", value = "0注册验证码   1找回验证码  2登录", required = true, paramType = "query", dataType = "String")
    })
    public ReturnResult getCode(String account,boolean type,String sendType) throws IOException {
        try{
            /**
             * 判断是邮箱还是 手机号
             */
            if( Validator.isEmail( account )  || Validator.isPhone( account ) ){
                if(type){ //需要验证是否在数据库中存在
                    log.debug("查询系统是否存在该账号");
                    AuthMember authMember = authMemberService.getAppIsExistenceByAccount(account);
                    if(authMember == null ){ //如果不存在返回错误信息
                        return ReturnResult.error("0", "没有该用户！");
                    }
                }
                log.debug("开始发送验证码！");
                int random=(int)(Math.random()*1000000.0);
                String code = String.format("%06d", random);
                Map<String, String> search = new HashMap<>();
                if( Validator.isEmail( account ) ){ //邮箱
                    sendEmailCode(code,search,account,sendType);
                }
                if( Validator.isPhone( account ) ){ //手机
                    boolean flag = sendPhoneCode(code,search,account,sendType);
                    if(!flag ){
                        return ReturnResult.error("0", "手机验证码发送失败！");
                    }
                }
                return ReturnResult.ok("1", "操作成功", null);
            }else{
                return ReturnResult.error("0", "邮箱号或手机号格式不正确！");
            }
        }catch (Exception e) {
            e.printStackTrace();
            return ReturnResult.error("操作失败");
        }
    }






    private boolean sendPhoneCode(String code, Map<String, String> search, String account, String sendType)  throws Exception{
        search.put("group", "'6'");
        List<Configs> list = configsService.getConfigsListBySearch(search);
        String accessKeyId = "" ;
        String accessKeySecret = "";
        String signName = "";
        String templateCode = "";
        int codeId = 15;
        if ("1".equals(sendType)) {//登录
            codeId = 12;
        } else if ("2".equals(sendType)) {//注册
            codeId = 13;
        } else if ("3".equals(sendType)) {//忘记密码
            codeId = 14;
        }
        for (Configs conf : list) {
            switch (conf.getId()) {
                case 9:
                    accessKeyId = conf.getValue();
                    break;
                case 10:
                    accessKeySecret = conf.getValue();
                    break;
                case 11:
                    signName = conf.getValue();
                    break;
                default:
                    break;
            }
            if(codeId == conf.getId()){
                templateCode = conf.getValue();
            }
        }
        log.debug("accessKeyId" + accessKeyId );
        log.debug("accessKeySecret" + accessKeySecret );
        log.debug("signName" + signName );
        log.debug("templateCode" + templateCode );

        //可自助调整超时时间
        System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
        System.setProperty("sun.net.client.defaultReadTimeout", "10000");

        //初始化acsClient,暂不支持region化
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
        DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
        IAcsClient acsClient = new DefaultAcsClient(profile);

        //组装请求对象-具体描述见控制台-文档部分内容
        SendSmsRequest request = new SendSmsRequest();
        //必填:待发送手机号
        request.setPhoneNumbers(account);
        //必填:短信签名-可在短信控制台中找到
        request.setSignName(signName);
        //必填:短信模板-可在短信控制台中找到
        request.setTemplateCode(templateCode);
        //可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
        request.setTemplateParam("{\"code\":\""+code+"\"}");
        //选填-上行短信扩展码(无特殊需求用户请忽略此字段)
        //request.setSmsUpExtendCode("90997");
        //可选:outId为提供给业务方扩展字段,最终在短信回执消息中将此值带回给调用者
        //request.setOutId("yourOutId");
        //hint 此处可能会抛出异常，注意catch
        SendSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);
        if(sendSmsResponse.getCode() != null && sendSmsResponse.getCode().equals("OK")) {
            CacheManager.setData(account+code+sendType,code,30 * 60);//key-value-生命周期 秒数30分钟
            return true;
        }else{
            return false;
        }
    }


    private void sendEmailCode(String code, Map<String, String> search, String account, String sendType) throws Exception{
        search.put("group", "'7'");
        List<Configs> list = configsService.getConfigsListBySearch(search);
        String emailSmtpHost = "";
        String emailFromPeo = "";
        String emailFromPassword = "";
        String emailFromCode = "";
        String emailSubject = "";
        for (Configs conf : list) {
            switch (conf.getId()) {
                case 16:
                    emailSmtpHost = conf.getValue();
                    break;
                case 17:
                    emailFromPeo = conf.getValue();
                    break;
                case 18:
                    emailFromPassword = conf.getValue();
                    break;
                case 19:
                    emailSubject = conf.getValue();
                    break;
                case 20:
                    emailFromCode = conf.getValue();
                    break;
                default:
                    break;
            }
        }
        log.debug("emailSmtpHost" + emailSmtpHost );
        log.debug("emailFromPeo" + emailFromPeo );
        log.debug("emailFromPassword" + emailFromPassword );
        log.debug("emailSubject" + emailSubject );
        log.debug("emailFromCode" + emailFromCode );
        log.debug("为" + emailSmtpHost + "配置mail session对象");
        Properties props = new Properties();
        props.put("mail.smtp.host", emailSmtpHost);
        props.put("mail.smtp.starttls.enable","false");//使用 STARTTLS安全连接
        props.put("mail.smtp.auth", "true");
        props.put("mail.debug", "false");
        // 设置发送邮件的邮件服务器的属性（这里使用网易的smtp服务器）
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.socketFactory.fallback", "false");
        // 需要经过授权，也就是有户名和密码的校验，这样才能通过验证（一定要有这一条）
        props.setProperty("mail.smtp.port", "465");
        props.setProperty("mail.smtp.socketFactory.port", "465");
        // 使用验证
        Session mailSession = Session.getInstance(props,new MyAuthenticator(emailFromPeo,emailFromPassword));
        // 第二步：编写消息
        InternetAddress fromAddress = new InternetAddress(emailFromPeo);
        InternetAddress toAddress = new InternetAddress(account);
        MimeMessage message = new MimeMessage(mailSession);
        message.setFrom(fromAddress);
        message.addRecipient(RecipientType.TO, toAddress);
        message.setSentDate(Calendar.getInstance().getTime());
        message.setSubject(emailSubject);
        message.setContent("英威腾验证码："+code, emailFromCode);
        // 第三步：发送消息
        Transport transport = mailSession.getTransport("smtp");
        transport.send(message, message.getRecipients(RecipientType.TO));
        CacheManager.setData(account+code+sendType,code,30 * 60);//key-value-生命周期 秒数30分钟
    }
}

class MyAuthenticator extends Authenticator {
    String userName="";
    String password="";
    public MyAuthenticator(){
        super();
    }
    public MyAuthenticator(String userName,String password){
        this.userName=userName;
        this.password=password;
    }
    protected PasswordAuthentication getPasswordAuthentication(){
        return new PasswordAuthentication(userName, password);
    }
}
