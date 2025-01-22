package com.example.dao;

import com.example.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface UserDao {

    @Select("SELECT * FROM users WHERE openid = #{openId}")
    User findByOpenId(String openId);

    @Update("UPDATE users SET total_score = #{totalScore}, total_wins = #{totalWins}, level = #{level} WHERE openid = #{openId}")
    void updateUserStats(String openId, int totalScore, int totalWins, int level);
} 