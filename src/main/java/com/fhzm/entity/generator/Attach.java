package com.fhzm.entity.generator;

import java.util.Date;
import lombok.Data;

/**
 * * All rights Reserved, Designed By http://www.fenghuaapp.com
 * @Description: Table attach
 * @date: 2019-03-28 13:31:43
 * @version: V1.0  
 * @Copyright: 2019 http://www.fenghuaapp.com/ Inc. All rights reserved.
 * 注意：本内容仅限于风华正茂科技(北京)有限公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Data
public class Attach {
    /**
     * 附件ID
     * Column: attach_id
     */
    private String attachId;

    /**
     * 上传时间
     * Column: ctime
     */
    private Date ctime;

    /**
     * 附件名称
     * Column: name
     */
    private String name;

    /**
     * 附件格式
     * Column: type
     */
    private String type;

    /**
     * 附件大小
     * Column: size
     */
    private String size;

    /**
     * 附件扩展名
     * Column: extension
     */
    private String extension;

    /**
     * 图片的md5值
     * Column: md5
     */
    private String md5;

    /**
     * 附件哈希值
     * Column: hash
     */
    private String hash;

    /**
     * 统一的is_del
     * Column: is_del
     */
    private Boolean isDel;

    /**
     * 保存路径
     * Column: save_path
     */
    private String savePath;

    /**
     * 保存名称
     * Column: save_name
     */
    private String saveName;

    /**
     * 附件保存的域ID（用于拆分附件存储到不同的服务器）
     * Column: save_domain
     */
    private Byte saveDomain;

    /**
     * 来源类型，0：网站；1：手机网页版；2：android；3：iphone
     * Column: from
     */
    private Byte from;

    /**
     * 图片宽度
     * Column: width
     */
    private Integer width;

    /**
     * 图片高度
     * Column: height
     */
    private Integer height;

    /**
     * Column: mime
     */
    private String mime;
}