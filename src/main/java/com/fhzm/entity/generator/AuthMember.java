package com.fhzm.entity.generator;

import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

/**
 * * All rights Reserved, Designed By http://www.fenghuaapp.com
 * @Description: Table auth_member
 * @date: 2019-04-04 10:39:04
 * @version: V1.0  
 * @Copyright: 2019 http://www.fenghuaapp.com/ Inc. All rights reserved.
 * 注意：本内容仅限于风华正茂科技(北京)有限公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Data
public class AuthMember {
    /**
     * 用户的id
     * Column: uid
     */
    private String uid;

    /**
     * 用户的登陆账号
     * Column: username
     */
    private String username;

    /**
     * 用户的登陆密码
     * Column: password
     */
    private String password;

    /**
     * 性别
     * Column: sex
     */
    private String sex;

    /**
     * 用户的昵称
     * Column: nickname
     */
    private String nickname;

    /**
     * 手机号
     * Column: phone
     */
    private String phone;

    /**
     * 邮箱
     * Column: email
     */
    private String email;

    /**
     * 用户启用禁用状态，1启用，2为禁用
     * Column: status
     */
    private Integer status;

    /**
     * 创建时间
     * Column: ctime
     */
    private Date ctime;

    /**
     * 更新时间
     * Column: utime
     */
    private Date utime;

    /**
     * 国家id
     * Column: country
     */
    private Integer country;

    /**
     * 省份
     * Column: province
     */
    private Integer province;

    /**
     * 城市
     * Column: city
     */
    private Integer city;

    /**
     * 余额
     * Column: balance
     */
    private BigDecimal balance;

    /**
     * 是否删除，0为未删除，1为删除
     * Column: is_remove
     */
    private Integer isRemove;

    /**
     * 登陆次数
     * Column: login_count
     */
    private Integer loginCount;

    /**
     * 最后登录时间
     * Column: last_login_time
     */
    private Date lastLoginTime;

    /**
     * 创建人id
     * Column: cuid
     */
    private String cuid;

    /**
     * 用户地址
     * Column: address
     */
    private String address;

    /**
     * 是否是后台添加 0不是（前台注册-用户列表）1 是（后台成员）
     * Column: is_manage
     */
    private Integer isManage;

    /**
     * 所属角色 名称
     */
    private String authGroupName;


}