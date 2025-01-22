package com.example.component;

import com.example.service.TestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TestRunner implements CommandLineRunner {

    @Autowired
    private TestService testService;

    @Override
    public void run(String... args) {
        log.info("开始测试查询test表...");
        try {
            testService.findAll().forEach(test -> 
                log.info("查询结果: {}", test)
            );
        } catch (Exception e) {
            log.error("查询test表出错: ", e);
        }
    }
} 