package com.example.dao;

import com.example.entity.EightPlayerRound;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface EightPlayerRoundMapper {
    @Insert("INSERT INTO eight_player_round(match_id, round_num, " +
            "player1_name, player1_id, player2_name, player2_id, " +
            "player3_name, player3_id, player4_name, player4_id, " +
            "left_score, right_score, result, status, created_at, updated_at) " +
            "VALUES(#{matchId}, #{roundNum}, " +
            "#{player1Name}, #{player1Id}, #{player2Name}, #{player2Id}, " +
            "#{player3Name}, #{player3Id}, #{player4Name}, #{player4Id}, " +
            "#{leftScore}, #{rightScore}, #{result}, #{status}, #{createdAt}, #{updatedAt})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(EightPlayerRound round);
    
    @Select("SELECT * FROM eight_player_round WHERE match_id = #{matchId} ORDER BY round_num")
    List<EightPlayerRound> findByMatchId(@Param("matchId") Long matchId);
    
    @Select("SELECT * FROM eight_player_round WHERE id = #{id}")
    EightPlayerRound findById(@Param("id") Long id);
    
    @Update("UPDATE eight_player_round SET left_score = #{leftScore}, " +
            "right_score = #{rightScore}, result = #{result}, status = #{status}, " +
            "updated_at = #{updatedAt} WHERE id = #{id}")
    int updateById(EightPlayerRound round);
    
    @Insert({
        "<script>",
        "INSERT INTO eight_player_round(match_id, round_num, " +
        "player1_name, player1_id, player2_name, player2_id, " +
        "player3_name, player3_id, player4_name, player4_id, " +
        "left_score, right_score, result, status, created_at, updated_at) VALUES ",
        "<foreach collection='rounds' item='item' separator=','>",
        "(#{item.matchId}, #{item.roundNum}, " +
        "#{item.player1Name}, #{item.player1Id}, #{item.player2Name}, #{item.player2Id}, " +
        "#{item.player3Name}, #{item.player3Id}, #{item.player4Name}, #{item.player4Id}, " +
        "#{item.leftScore}, #{item.rightScore}, #{item.result}, #{item.status}, " +
        "#{item.createdAt}, #{item.updatedAt})",
        "</foreach>",
        "</script>"
    })
    int batchInsert(@Param("rounds") List<EightPlayerRound> rounds);
    
    @Update("UPDATE eight_player_round SET left_score = #{leftScore}, " +
            "right_score = #{rightScore}, result = #{result}, status = #{status}, " +
            "updated_at = #{updatedAt} WHERE id = #{id}")
    int updateScore(EightPlayerRound round);
    
    /**
     * 查询用户参与的所有对局
     * @param playerId 玩家ID（openid）
     * @return 对局列表
     */
    @Select("SELECT r.*, m.match_name as matchName FROM eight_player_round r " +
            "JOIN eight_player_match m ON r.match_id = m.id " +
            "WHERE (r.player1_id = #{playerId} OR r.player2_id = #{playerId} " +
            "OR r.player3_id = #{playerId} OR r.player4_id = #{playerId}) " +
            "AND m.deleted = 0 " +
            "ORDER BY r.created_at DESC")
    List<EightPlayerRound> findByPlayerId(@Param("playerId") String playerId);
} 