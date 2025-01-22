package com.example.mapper;

import com.example.entity.EightPlayerMember;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface EightPlayerMemberMapper {
    
    /**
     * 查询比赛的所有成员
     */
    @Select("SELECT * FROM eight_player_member WHERE match_id = #{matchId}")
    List<EightPlayerMember> findByMatchId(Long matchId);
} 