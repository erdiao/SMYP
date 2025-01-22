package com.example.dao;

import com.example.entity.User;
import com.example.entity.LeaderboardUser;
import org.apache.ibatis.annotations.*;

import java.util.Date;
import java.util.List;

@Mapper
public interface UserMapper {
    @Insert("INSERT INTO users (openid, unionid, nickname, avatar_url, gender, " +
            "country, province, city, language, total_games, total_wins, " +
            "total_score, level, created_at, updated_at, last_login_at, status) " +
            "VALUES (#{openid}, #{unionid}, #{nickname}, #{avatarUrl}, #{gender}, " +
            "#{country}, #{province}, #{city}, #{language}, #{totalGames}, " +
            "#{totalWins}, #{totalScore}, #{level}, #{createdAt}, #{updatedAt}, " +
            "#{lastLoginAt}, 1)")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(User user);
    
    @Select("SELECT * FROM users WHERE openid = #{openid} AND status = 1")
    User findByOpenId(@Param("openid") String openid);
    
    @Select({"<script>",
            "SELECT * FROM users WHERE status = 1",
            "<if test='nickname != null and nickname != \"\"'>",
            " AND nickname LIKE CONCAT('%', #{nickname}, '%')",
            "</if>",
            "</script>"})
    List<User> findByNickname(@Param("nickname") String nickname);
    
    @Update("UPDATE users SET nickname = #{nickname}, avatar_url = #{avatarUrl}, " +
            "gender = #{gender}, country = #{country}, province = #{province}, " +
            "city = #{city}, language = #{language}, updated_at = #{updatedAt} " +
            "WHERE openid = #{openid}")
    void updateUserInfo(User user);
    
    @Update("UPDATE users SET last_login_at = #{lastLoginAt}, " +
            "updated_at = #{updatedAt} WHERE openid = #{openid}")
    void updateLoginTime(@Param("openid") String openid, 
                        @Param("lastLoginAt") Date lastLoginAt,
                        @Param("updatedAt") Date updatedAt);
    
    /**
     * 更新用户统计信息
     * @param openid 用户openid
     * @param totalScore 总分
     * @param totalWins 总胜场
     * @param totalGames 总场数
     * @param level 等级
     * @param updatedAt 更新时间
     * @return 更新行数
     */
    @Update("UPDATE users SET total_score = #{totalScore}, " +
            "total_wins = #{totalWins}, total_games = #{totalGames}, " +
            "level = #{level}, updated_at = #{updatedAt} " +
            "WHERE openid = #{openid}")
    int updateStats(@Param("openid") String openid, 
                   @Param("totalScore") int totalScore, 
                   @Param("totalWins") int totalWins,
                   @Param("totalGames") int totalGames,
                   @Param("level") int level,
                   @Param("updatedAt") Date updatedAt);
    
    @Select("SELECT id, openid, nickname, avatar_url as avatarUrl, total_score as totalScore, " +
            "total_wins as totalWins, total_games as totalGames, level FROM users WHERE status = 1 " +
            "ORDER BY level DESC, total_score DESC, total_wins DESC")
    List<LeaderboardUser> getLeaderboard();
} 