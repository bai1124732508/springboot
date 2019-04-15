package com.fhzm.entity.generator;

import java.util.Date;

import com.fhzm.common.DateUtil;
import com.fhzm.common.file.Term;
import com.fhzm.common.file.TermManager;
import lombok.Data;

/**
 * * All rights Reserved, Designed By http://www.fenghuaapp.com
 * @Description: Table sys_log
 * @date: 2019-03-26 15:03:12
 * @version: V1.0  
 * @Copyright: 2019 http://www.fenghuaapp.com/ Inc. All rights reserved.
 * 注意：本内容仅限于风华正茂科技(北京)有限公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Data
public class SysLog {
    /**
     * 标识
     * Column: id
     */
    private String id;

    /**
     * 日志描述
     * Column: description
     */
    private String description;

    /**
     * 日志描述英文
     * Column: description_en
     */
    private String descriptionEn;

    /**
     * 日志类别
     * Column: method
     */
    private String method;

    /**
     * 日志的类型
     * Column: log_type
     */
    private Integer logType;

    /**
     * 操作ip
     * Column: ip
     */
    private String ip;

    /**
     * ip 所在地
     * Column: ip_address
     */
    private String ipAddress;

    /**
     * 异常标识码
     * Column: exception_code
     */
    private String exceptionCode;

    /**
     * 异常原因
     * Column: exception_detail
     */
    private String exceptionDetail;

    /**
     * 用户标识
     * Column: create_id
     */
    private String createId;

    /**
     * Column: create_name
     */
    private String createName;

    /**
     * CURRENT_TIMESTAMP
     * Column: ctime
     */
    private Date ctime;

    /**
     * 设备类型 0.pc 1.app
     * Column: equipment_type
     */
    private Integer equipmentType;

    /**
     * 操作的id 例如更改角色的id
     * Column: obj_id
     */
    private String objId;

    /**
     * 参数
     * Column: params
     */
    private String params;

    /**
     * 角色名称
     */
    private String authGroupName;
    /**
     * 设备类型 0.pc 1.app
     */
    private String equipmentTypeStr;
    /**
     * 日志类型
     */
    private String logTypeStr;

    /**
     * 日志类型
     */
    private String logTypeENStr;

    /**
     * 创建时间 字符串
     */
    private String ctimeStr;

    public String getCtimeStr() {
        if (this.getCtime()!=null) {
            ctimeStr = DateUtil.date2String(this.getCtime());
        }
        return ctimeStr;
    }
    public String getLogTypeStr() {
        Term term = TermManager.getTerm("SysLog_logType", String.valueOf(this.getLogType()));
        if( term != null ) {
            logTypeStr = term.getName();
        }
        return logTypeStr;
    }
    public String getLogTypeENStr (){
        Term term = TermManager.getTerm("SysLog_logType", String.valueOf(this.getLogType()));
        if( term != null ) {
            logTypeENStr = term.getDisplayValue();
        }
        return logTypeENStr;
    }

    public String getEquipmentTypeStr() {
        Term term = TermManager.getTerm("SysLog_equipmentType", String.valueOf(this.getEquipmentType()));
        if( term != null ) {
            equipmentTypeStr = term.getName();
        }
        return equipmentTypeStr;
    }
}