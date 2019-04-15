package com.fhzm.entity.generator;

import java.util.Date;

import com.fhzm.common.file.Term;
import com.fhzm.common.file.TermManager;
import io.swagger.models.auth.In;
import lombok.Data;

/**
 * * All rights Reserved, Designed By http://www.fenghuaapp.com
 * @Description: Table help
 * @date: 2019-04-04 11:00:45
 * @version: V1.0  
 * @Copyright: 2019 http://www.fenghuaapp.com/ Inc. All rights reserved.
 * 注意：本内容仅限于风华正茂科技(北京)有限公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Data
public class Help {
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
     * Column: language
     */
    private String language;

    /**
     * 帮助标题
     * Column: title
     */
    private String title;

    /**
     * 被查看的次数
     * Column: count
     */
    private Integer count;

    /**
     * 0.使用帮助1.常见问题
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


    private String  langName;

    private String typeStr;

    public String getTypeStr() {
        Term term = TermManager.getTerm("help_type", String.valueOf(this.getType()));
        if( term != null ) {
            typeStr = term.getName();
        }
        return typeStr;
    }


}
