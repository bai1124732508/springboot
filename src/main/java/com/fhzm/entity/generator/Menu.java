package com.fhzm.entity.generator;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.Data;

/**
 * * All rights Reserved, Designed By http://www.fenghuaapp.com
 * @Description: Table menu
 * @date: 2019-04-04 10:16:16
 * @version: V1.0  
 * @Copyright: 2019 http://www.fenghuaapp.com/ Inc. All rights reserved.
 * 注意：本内容仅限于风华正茂科技(北京)有限公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Data
public class Menu {
    /**
     * 文档ID
     * Column: id
     */
    private String id;

    /**
     * 标题
     * Column: title
     */
    private String title;

    /**
     * 上级分类ID
     * Column: pid
     */
    private String pid;

    /**
     * 排序（同级有效）
     * Column: sort
     */
    private Integer sort;

    /**
     * 链接地址
     * Column: url
     */
    private String url;

    /**
     * 是否隐藏 1是 2否
     * Column: hide
     */
    private Integer hide;

    /**
     * 提示
     * Column: tip
     */
    private String tip;

    /**
     * 分组
     * Column: group_id
     */
    private String groupId;

    /**
     * 是否仅开发者模式可见 1是 2否
     * Column: is_dev
     */
    private Integer isDev;

    /**
     * 状态,默认为1 开启，2为禁用
     * Column: status
     */
    private Integer status;

    /**
     * Column: icon
     */
    private String icon;

    /**
     * Column: level
     */
    private Integer level;

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
     * 权限id 集合
     */
    private List<Menu> ruleCustomList;

    /**
     * 该菜单下的所有分组
     */
    private List<String> groupStringList = new ArrayList<>();

    private boolean checked;

    public List<String> getGroupStringList() {
        if (this.getLevel() == 1) {
            if (ruleCustomList != null) {
                for (Menu menu : ruleCustomList) {
                    if (!groupStringList.contains(menu.getGroupId())) {
                        groupStringList.add(menu.getGroupId());
                    }
                }
            }
        }
        return groupStringList;
    }


}