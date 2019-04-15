package com.fhzm.common;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.fhzm.entity.generator.IpVo;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.boot.context.embedded.EmbeddedServletContainerInitializedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@Slf4j
public class IpgetUtil implements ApplicationListener<EmbeddedServletContainerInitializedEvent> {

    private  int serverPort;
    
    private static  String hostaddr;

    @Override
    public void onApplicationEvent(EmbeddedServletContainerInitializedEvent event) {
        this.serverPort = event.getEmbeddedServletContainer().getPort();
  
    }

    public int getPort() {
        return this.serverPort;
    }
  //ip 的 获取 如下 

	public static  String getHostaddr() {
		try {
			hostaddr = InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			hostaddr="0.0.0.1";
		}
		return hostaddr;
	}

	public  void setHostaddr(String hostaddr) {
		hostaddr = hostaddr;
	}
    
	
	public static String getHostIp(){
		return getHostaddr();
	}

	  //获得访问者IP方法
	public static String getIp(HttpServletRequest request){

		  String ip = request.getHeader("x-forwarded-for");
		   if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			   ip = request.getHeader("X-real-ip");
		   }
		   if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			   ip = request.getHeader("Proxy-Client-IP");
		   }
		   if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			   ip = request.getHeader("WL-Proxy-Client-IP");
		   }
		   if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			   ip = request.getRemoteAddr();
		   }
		   if(StringUtils.isNotBlank(ip)) {
			   ip = ip.split(",")[0];
		   }

		return ip;
	}

	/**
	 * 根据ip 获取地址
	 * @param ip
	 * @return
	 */
	public static String getAddressByIp(String ip){
		log.info("ip地址："+ip);
		RestTemplate restTemplate = new RestTemplate();
		//查询阿里
		String url = "http://ip.taobao.com/service/getIpInfo.php?ip="+ip;
		String object = restTemplate.getForObject(url, String.class,ip);
		log.info("ip地址返回json数据："+object);
		if(StringUtil.isNotNull(object)){
			IpVo ipVo = new Gson().fromJson(object, IpVo.class);
			StringBuffer address = new StringBuffer();
			// XX表示内网
			if(ipVo.getCode()==0 && !ipVo.getData().getCountry().equals("XX") ){ //国家不能为空
				address.append(ipVo.getData().getCountry());
				if(!ipVo.getData().getRegion().equals("XX") ){ //省份不为空
					address.append(ipVo.getData().getRegion());
					if(!ipVo.getData().getCity().equals("XX") ){ //城市不为空
						address.append(ipVo.getData().getCity());
					}
				}
			}
			log.info("ip地址返回Address数据："+address.toString());
			return address.toString();
		}else{
			return "";
		}
	}

   /**
	* 判断内外网  true 外网 false 内网
	* @param request
	* @return
	*/
	public static Boolean isOutside(HttpServletRequest request) {
		String type =  request.getHeader("X-header-type");
		System.out.println("X-header-type---------------------"+type);
		if("nginx".equals(type)){
			System.out.println("外网访问------------");
			return true;
		}else{
			System.out.println("内网访问------------");
			return false;
		}
	}



}
