package com.fhzm.common;

import java.io.Serializable;

/**
 * 版权所有：风华正茂（北京）科技有限公司
 * 作　　者：贺更新
 * 版　　本：1.0
 * 创建日期：2016年9月17日
 * 功能描述：__________审核辅助类_____________
 * 
 * 修改历史记录
 * 	作者				时间					版本					备注
 *  贺更新			2016年9月17日			1.0					创建
 */
public class AuditExpand implements Serializable {
	/**
	 * 对应Terms.xml的EtcAuditInfo_type
	 */
	private String type;
	/**
	 * 审核对象的表名
	 */
	private String tableName;
	/**
	 * 是否含有图片
	 */
	private boolean isHaveImg;
	
	/**
	 * 需要修改的状态字段名称
	 */
	private String filedName;
	/**
	 * 通过状态的值
	 */
	private String passValue;
	/**
	 * 不通过状态的值
	 */
	private String nopassValue;
	/**
	 * 代表当前对象name的字段属性名称
	 */
	private String nameFileName;
	/**
	 * 代表当前对象auditType[审核类型]的属性名称
	 */
	private String auditTypeFileName;
	/**
	 * 是否保存审核记录
	 */
	private boolean saveRecord;
	
	
	private String tableId;
	
	private String reason;
	    

    public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}
	
	
	public String getTableId() {
		return tableId;
	}
	public void setTableId(String tableId) {
		this.tableId = tableId;
	}
	/**
	 * @return the tableName
	 */
	public String getTableName() {
		return tableName;
	}
	/**
	 * @param tableName the tableName to set
	 */
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	/**
	 * @return the filedName
	 */
	public String getFiledName() {
		return filedName;
	}
	/**
	 * @param filedName the filedName to set
	 */
	public void setFiledName(String filedName) {
		this.filedName = filedName;
	}
	
	/**
	 * @return the passValue
	 */
	public String getPassValue() {
		return passValue;
	}
	/**
	 * @param passValue the passValue to set
	 */
	public void setPassValue(String passValue) {
		this.passValue = passValue;
	}
	/**
	 * @return the nopassValue
	 */
	public String getNopassValue() {
		return nopassValue;
	}
	/**
	 * @param nopassValue the nopassValue to set
	 */
	public void setNopassValue(String nopassValue) {
		this.nopassValue = nopassValue;
	}
	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}
	/**
	 * @return the nameFileName
	 */
	public String getNameFileName() {
		return nameFileName;
	}
	/**
	 * @param nameFileName the nameFileName to set
	 */
	public void setNameFileName(String nameFileName) {
		this.nameFileName = nameFileName;
	}
	/**
	 * @return the auditTypeFileName
	 */
	public String getAuditTypeFileName() {
		return auditTypeFileName;
	}
	/**
	 * @param auditTypeFileName the auditTypeFileName to set
	 */
	public void setAuditTypeFileName(String auditTypeFileName) {
		this.auditTypeFileName = auditTypeFileName;
	}
	/**
	 * @return the isHaveImg
	 */
	public boolean isHaveImg() {
		return isHaveImg;
	}
	/**
	 * @param isHaveImg the isHaveImg to set
	 */
	public void setHaveImg(boolean isHaveImg) {
		this.isHaveImg = isHaveImg;
	}
	/**
	 * @return the saveRecord
	 */
	public boolean isSaveRecord() {
		return saveRecord;
	}
	/**
	 * @param saveRecord the saveRecord to set
	 */
	public void setSaveRecord(boolean saveRecord) {
		this.saveRecord = saveRecord;
	}
	/**
	 * @param type
	 * @param tableName
	 * @param tableSaleName
	 * @param isHaveImg
	 * @param filedName
	 * @param filedValue
	 * @param nameFileName
	 * @param auditTypeFileName
	 */
	public AuditExpand(String type, String tableName,boolean isHaveImg, String filedName,
			String passValue,String nopassValue, String nameFileName, String auditTypeFileName,String tableId) {
		super();
		this.type = type;
		this.tableName = tableName;
		this.isHaveImg = isHaveImg;
		this.filedName = filedName;
		this.passValue = passValue;
		this.nopassValue = nopassValue;
		this.nameFileName = nameFileName;
		this.auditTypeFileName = auditTypeFileName;
		this.saveRecord = true;
		this.tableId=tableId;
	}
	
}
