package com.example.mapper;

import com.example.entity.EightPlayerMatch;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface EightPlayerMatchMapper {
    
    /**
     * 查询已完成但未结算的比赛
     * status = 1 表示已结束
     * is_settled = 0 表示未结算
     * deleted = 0 表示未删除
     */
    @Select("SELECT * FROM eight_player_match WHERE status = 1 AND is_settled = 0 AND deleted = 0")
    List<EightPlayerMatch> findFinishedButNotSettled();
    
    /**
     * 更新比赛为已结算状态
     */
    @Update("UPDATE eight_player_match SET is_settled = 1 WHERE id = #{matchId}")
    void updateSettleStatus(Long matchId);

    /**
     * 创建新比赛
     */
    @Insert("INSERT INTO eight_player_match(match_name, status, creator_id, creator_name, total_rounds, finished_rounds, created_at, updated_at, deleted, is_settled) " +
            "VALUES(#{matchName}, #{status}, #{creatorId}, #{creatorName}, #{totalRounds}, #{finishedRounds}, #{createdAt}, #{updatedAt}, #{deleted}, 0)")
    void insert(EightPlayerMatch match);
} 