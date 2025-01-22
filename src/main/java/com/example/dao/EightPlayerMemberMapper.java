package com.example.dao;

import com.example.entity.EightPlayerMember;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface EightPlayerMemberMapper {
    @Insert("INSERT INTO eight_player_member(match_id, player_name, player_id, " +
            "win_count, total_score, total_rounds, is_mvp, is_svp, created_at, updated_at) " +
            "VALUES(#{matchId}, #{playerName}, #{playerId}, #{winCount}, #{totalScore}, " +
            "#{totalRounds}, #{isMvp}, #{isSvp}, #{createdAt}, #{updatedAt})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(EightPlayerMember member);
    
    @Insert({
        "<script>",
        "INSERT INTO eight_player_member(match_id, player_name, player_id, " +
        "win_count, total_score, total_rounds, is_mvp, is_svp, created_at, updated_at) VALUES ",
        "<foreach collection='members' item='item' separator=','>",
        "(#{item.matchId}, #{item.playerName}, #{item.playerId}, " +
        "#{item.winCount}, #{item.totalScore}, #{item.totalRounds}, " +
        "#{item.isMvp}, #{item.isSvp}, #{item.createdAt}, #{item.updatedAt})",
        "</foreach>",
        "</script>"
    })
    int batchInsert(@Param("members") List<EightPlayerMember> members);
    
    @Select("SELECT * FROM eight_player_member WHERE match_id = #{matchId}")
    List<EightPlayerMember> findByMatchId(@Param("matchId") Long matchId);
    
    @Update("UPDATE eight_player_member SET win_count = #{winCount}, " +
            "total_score = #{totalScore}, total_rounds = #{totalRounds}, " +
            "is_mvp = #{isMvp}, is_svp = #{isSvp}, updated_at = #{updatedAt} " +
            "WHERE id = #{id}")
    int updateById(EightPlayerMember member);
} 