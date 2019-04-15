package com.fhzm.entity.generator;

import lombok.Data;

import java.util.Date;

/**
 * * All rights Reserved, Designed By http://www.fenghuaapp.com
 * @Description: Table auth_group
 * @date: 2019-03-21 16:29:37
 * @version: V1.0  
 * @Copyright: 2019 http://www.fenghuaapp.com/ Inc. All rights reserved.
 * 注意：本内容仅限于风华正茂科技(北京)有限公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Data
public class AuthGroup {
    /**
     * Column: id
     */
    private String id;

    /**
     * Column: title
     */
    private String title;

    /**
     * Column: module
     */
    private String module;

    /**
     * Column: status
     */
    private String status;

    /**
     * Column: type
     */
    private String type;

    /**
     * 组描述
     * Column: info
     */
    private String info;

    /**
     * Column: pid
     */
    private String pid;

    /**
     * Column: num
     */
    private Integer num;

    /**
     * Column: leavel
     */
    private Integer leavel;

    /**
     * 是否是叶子节点1是0否
     * Column: isleaf
     */
    private Boolean isleaf;

    /**
     * 层级id 逗号分隔
     * Column: leavelidds
     */
    private String leavelidds;

    /**
     * 是否是隐藏角色0否1是
     * Column: ishide
     */
    private Boolean ishide;

    /**
     * Column: sort
     */
    private Boolean sort;

    /**
     * Column: rules
     */
    private String rules;

    /**
     * 创建时间
     * Column: ctime
     */
    private Date ctime;

    /**
     * 修改时间
     * Column: utime
     */
    private Date utime;
    /**
     * 用户数量
     */
    private Integer userCount;


}