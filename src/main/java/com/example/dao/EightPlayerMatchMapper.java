package com.example.dao;

import com.example.entity.EightPlayerMatch;
import com.example.entity.EightPlayerRound;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface EightPlayerMatchMapper {
    @Insert("INSERT INTO eight_player_match(match_name, status, creator_id, creator_name, " +
            "total_rounds, finished_rounds, created_at, updated_at, deleted, is_settled) " +
            "VALUES(#{matchName}, #{status}, #{creatorId}, #{creatorName}, " +
            "#{totalRounds}, #{finishedRounds}, #{createdAt}, #{updatedAt}, #{deleted}, 0)")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(EightPlayerMatch match);
    
    @Select("SELECT * FROM eight_player_match WHERE id = #{id} AND deleted = 0")
    EightPlayerMatch findById(@Param("id") Long id);
    
    @Update("UPDATE eight_player_match SET status = #{status}, " +
            "total_rounds = #{totalRounds}, finished_rounds = #{finishedRounds}, " +
            "is_settled = #{isSettled}, " +
            "updated_at = #{updatedAt} WHERE id = #{id}")
    int updateById(EightPlayerMatch match);
    
    @Select("SELECT m.id, m.match_name, m.status, m.creator_id, u.nickname as creator_name, " +
            "m.total_rounds, m.finished_rounds, m.created_at, m.updated_at, m.deleted, m.is_settled, " +
            "GROUP_CONCAT(DISTINCT em.player_name SEPARATOR '|') as user_names " +
            "FROM (" +
            "SELECT * FROM eight_player_match WHERE creator_id = #{openid} AND deleted = 0 " +
            "UNION " +
            "SELECT m.* FROM eight_player_match m " +
            "JOIN eight_player_member em ON m.id = em.match_id " +
            "WHERE em.player_id = #{openid} AND m.deleted = 0" +
            ") m " +
            "LEFT JOIN users u ON m.creator_id = u.openid " +
            "LEFT JOIN eight_player_member em ON m.id = em.match_id " +
            "GROUP BY m.id, m.match_name, m.status, m.creator_id, u.nickname, " +
            "m.total_rounds, m.finished_rounds, m.created_at, m.updated_at, m.deleted, m.is_settled " +
            "ORDER BY m.updated_at DESC")
    List<EightPlayerMatch> findByCreatorId(@Param("openid") String openid);
    
    @Select("SELECT m.id, m.match_name, m.status, m.creator_id, u.nickname as creator_name, " +
            "m.total_rounds, m.finished_rounds, m.created_at, m.updated_at, m.deleted, m.is_settled " +
            "FROM eight_player_match m " +
            "LEFT JOIN users u ON m.creator_id = u.openid " +
            "WHERE m.creator_id = #{openid} AND m.status = 0 " +
            "AND m.deleted = 0 ORDER BY m.created_at DESC LIMIT 1")
    EightPlayerMatch findCurrentMatch(@Param("openid") String openid);
    
    @Select("SELECT m.id, m.match_name, m.status, m.creator_id, u.nickname as creator_name, " +
            "m.total_rounds, m.finished_rounds, m.created_at, m.updated_at, m.deleted, m.is_settled " +
            "FROM eight_player_match m " +
            "LEFT JOIN users u ON m.creator_id = u.openid " +
            "WHERE m.status = 1 AND m.is_settled = 0 AND m.deleted = 0")
    List<EightPlayerMatch> findFinishedButNotSettled();
    
    @Update("UPDATE eight_player_match SET status = #{status}, is_settled = #{isSettled} WHERE id = #{id}")
    int updateStatus(@Param("id") Long id, 
                    @Param("status") Integer status, 
                    @Param("isSettled") Integer isSettled);
    
    /**
     * 统计玩家在特定比赛中的有效对局数
     * @param matchId 比赛ID
     * @param playerId 玩家ID
     * @return 有效对局数
     */
    @Select("SELECT COUNT(DISTINCT r.id) " +
            "FROM eight_player_round r " +
            "WHERE r.match_id = #{matchId} " +
            "AND (r.player1_id = #{playerId} OR r.player2_id = #{playerId} " +
            "OR r.player3_id = #{playerId} OR r.player4_id = #{playerId}) " +
            "AND (r.left_score != 0 OR r.right_score != 0)")
    int countPlayerRounds(@Param("matchId") Long matchId, @Param("playerId") String playerId);
    
    /**
     * 查找比赛中的有效对局（双方分数不为0）
     * @param matchId 比赛ID
     * @return 有效对局列表
     */
    @Select("SELECT * FROM eight_player_round " +
            "WHERE match_id = #{matchId} " +
            "AND (left_score != 0 OR right_score != 0)")
    List<EightPlayerRound> findValidRoundsByMatchId(@Param("matchId") Long matchId);
}