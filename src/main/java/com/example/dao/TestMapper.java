package com.example.dao;

import com.example.entity.Test;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface TestMapper {
    @Select("SELECT * FROM test")
    List<Test> findAll();
} 