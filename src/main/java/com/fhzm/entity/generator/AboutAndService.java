package com.fhzm.entity.generator;

import java.util.Date;
import lombok.Data;

/**
 * * All rights Reserved, Designed By http://www.fenghuaapp.com
 * @Description: Table about_and_service
 * @date: 2019-04-04 10:52:46
 * @version: V1.0  
 * @Copyright: 2019 http://www.fenghuaapp.com/ Inc. All rights reserved.
 * 注意：本内容仅限于风华正茂科技(北京)有限公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Data
public class AboutAndService {
    /**
     * 标识
     * Column: id
     */
    private String id;

    /**
     * 用户标识
     * Column: create_id
     */
    private String createId;

    /**
     * 关于我们里边的图片
     * Column: logo
     */
    private String logo;

    /**
     * 被查看的次数
     * Column: count
     */
    private Integer count;

    /**
     * 0.关于我们  1.服务协议
     * Column: type
     */
    private Integer type;

    /**
     * 0.显示 1.不显示
     * Column: is_show
     */
    private Integer isShow;

    /**
     * 创建时间
     * Column: ctime
     */
    private Date ctime;

    /**
     * Column: utime
     */
    private Date utime;

    /**
     * Column: info
     */
    private String info;


    private String authMemberName;

    private String langName;
}