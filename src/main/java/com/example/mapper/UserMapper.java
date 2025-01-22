package com.example.mapper;

import com.example.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface UserMapper {
    
    /**
     * 根据openid查询用户
     */
    @Select("SELECT * FROM users WHERE openid = #{openid}")
    User findByOpenId(String openid);
    
    /**
     * 更新用户统计数据
     */
    @Update("UPDATE users SET total_score = #{totalScore}, total_wins = #{totalWins}, level = #{level} WHERE openid = #{openid}")
    void updateStats(String openid, int totalScore, int totalWins, int level);
} 