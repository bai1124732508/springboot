package com.fhzm.entity.generator;

import lombok.Data;

/**
 * * All rights Reserved, Designed By http://www.fenghuaapp.com
 * @Description: Table world_area
 * @date: 2019-03-27 10:11:04
 * @version: V1.0  
 * @Copyright: 2019 http://www.fenghuaapp.com/ Inc. All rights reserved.
 * 注意：本内容仅限于风华正茂科技(北京)有限公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Data
public class WorldArea {
    /**
     * ID
     * Column: id
     */
    private Integer id;

    /**
     * 父id
     * Column: pid
     */
    private Integer pid;

    /**
     * 简称
     * Column: short_name
     */
    private String shortName;

    /**
     * 名称
     * Column: name
     */
    private String name;

    /**
     * 全称
     * Column: merger_name
     */
    private String mergerName;

    /**
     * 层级 0 1 2 省市区县
     * Column: level
     */
    private Byte level;

    /**
     * 拼音
     * Column: pinyin
     */
    private String pinyin;

    /**
     * 长途区号
     * Column: phone_code
     */
    private String phoneCode;

    /**
     * 邮编
     * Column: zip_code
     */
    private String zipCode;

    /**
     * 首字母
     * Column: first
     */
    private String first;

    /**
     * 经度
     * Column: lng
     */
    private String lng;

    /**
     * 纬度
     * Column: lat
     */
    private String lat;

    /**
     * Column: area_code
     */
    private String areaCode;
}