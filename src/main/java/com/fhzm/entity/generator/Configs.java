package com.fhzm.entity.generator;

import java.util.Date;
import java.util.List;

import com.fhzm.common.file.Term;
import lombok.Data;

/**
 * * All rights Reserved, Designed By http://www.fenghuaapp.com
 * @Description: Table configs
 * @date: 2019-04-04 10:26:12
 * @version: V1.0  
 * @Copyright: 2019 http://www.fenghuaapp.com/ Inc. All rights reserved.
 * 注意：本内容仅限于风华正茂科技(北京)有限公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Data
public class Configs {
    /**
     * 配置ID
     * Column: id
     */
    private Integer id;

    /**
     * 配置类型
     * Column: type
     */
    private Integer type;

    /**
     * 配置说明
     * Column: title
     */
    private String title;

    /**
     * 配置分组
     * Column: group
     */
    private Integer group;

    /**
     * 配置分组
     * Column: group_title
     */
    private String groupTitle;

    /**
     * 配置值
     * Column: extra
     */
    private String extra;

    /**
     * 配置说明
     * Column: remark
     */
    private String remark;

    /**
     * 验证规则
     * Column: datatype
     */
    private String datatype;

    /**
     * 空值是提醒的字符串
     * Column: nullmsg
     */
    private String nullmsg;

    /**
     * 错误时提醒的字符串
     * Column: errormsg
     */
    private String errormsg;

    /**
     * 文本框的字符串
     * Column: placeholder
     */
    private String placeholder;

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
     * 状态
     * Column: status
     */
    private Byte status;

    /**
     * 排序
     * Column: sort
     */
    private Short sort;

    /**
     * 配置值
     * Column: value
     */
    private String value;



    private List<Attach> attachList;


    private List<Term> termList;
}