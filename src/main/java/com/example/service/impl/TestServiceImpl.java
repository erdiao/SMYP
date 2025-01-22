package com.example.service.impl;

import com.example.dao.TestMapper;
import com.example.entity.Test;
import com.example.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TestServiceImpl implements TestService {
    
    @Autowired
    private TestMapper testMapper;
    
    @Override
    @Transactional(readOnly = true)
    public List<Test> findAll() {
        return testMapper.findAll();
    }
} 