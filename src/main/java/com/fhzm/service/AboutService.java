package com.fhzm.service;

import com.fhzm.entity.generator.AboutAndService;
import com.fhzm.entity.generator.BaseQueryVo;
import com.github.pagehelper.PageInfo;

import java.util.List;
import java.util.Map;

public interface AboutService {

    /**
     * 获取关于我们列表
     * @param vo
     * @return
     */
    PageInfo<AboutAndService> getAboutList(BaseQueryVo vo);

    /**
     * 更新状态
     * @param idList
     * @param status
     * @param is_show
     */
    void updateAboutState(List<String> idList, String status, String is_show);

    /**
     * 删除通过语言的id
     * @param languageId
     */
    void deleteByLanguageId(String languageId);

    /**
     * 通过语言获取 服务协议 或者 关于我们
     * @param search
     * @return
     */
    AboutAndService getAboutAndServiceBySearch(Map<String,String> search);
}
