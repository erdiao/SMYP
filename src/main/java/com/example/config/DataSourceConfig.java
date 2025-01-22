package com.example.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.mybatis.spring.annotation.MapperScan;

@Configuration
@EnableTransactionManagement
@MapperScan("com.example.dao")
public class DataSourceConfig {
    // Spring Boot 会自动配置 DataSource，这里只需要开启事务管理和Mapper扫描
} 