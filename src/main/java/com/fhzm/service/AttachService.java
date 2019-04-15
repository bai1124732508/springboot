package com.fhzm.service;

import com.fhzm.entity.generator.Attach;

public interface AttachService {
    /**
     * 根据hash值获取文件
     * @param hash
     * @return
     * @throws Exception
     */
    Attach getAttachByHashcode(String hash) throws Exception;
}
