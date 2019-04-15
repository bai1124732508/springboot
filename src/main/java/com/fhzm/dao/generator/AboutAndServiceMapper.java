package com.fhzm.dao.generator;

import com.fhzm.entity.generator.AboutAndService;
import com.fhzm.entity.generator.BaseQueryVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * * All rights Reserved, Designed By http://www.fenghuaapp.com
 * @Description: AboutAndServiceMapper
 * @date: 2019-04-04 10:52:46
 * @version: V1.0  
 * @Copyright: 2019 http://www.fenghuaapp.com/ Inc. All rights reserved.
 * 注意：本内容仅限于风华正茂科技(北京)有限公司内部传阅，禁止外泄以及用于其他的商业目的
 */
public interface AboutAndServiceMapper {
    int deleteByPrimaryKey(String id);

    int insert(AboutAndService record);

    int insertSelective(AboutAndService record);

    AboutAndService selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(AboutAndService record);

    int updateByPrimaryKeyWithBLOBs(AboutAndService record);

    int updateByPrimaryKey(AboutAndService record);


    /**
     * 获取关于我们列表
     * @param vo
     * @return
     */
    List<AboutAndService> getAboutList(BaseQueryVo vo);

    /**
     * 更新
     * @param idList
     * @param status
     * @param fied
     */
    void updateAboutState(@Param("list")List<String> idList, @Param("status")String status, @Param("fied")String fied);

    /**
     * 删除 通过语言的ISD、
     *
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