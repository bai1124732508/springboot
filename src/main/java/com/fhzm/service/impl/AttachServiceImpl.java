package com.fhzm.service.impl;

import com.fhzm.dao.generator.AttachMapper;
import com.fhzm.entity.generator.Attach;
import com.fhzm.service.AttachService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("attachService")
@Transactional
public class AttachServiceImpl implements AttachService {
    @Autowired
    private AttachMapper attachMapper;
    @Override
    public Attach getAttachByHashcode(String hash) throws Exception {
        return  attachMapper.getAttachByHashcode(hash);
    }
}
