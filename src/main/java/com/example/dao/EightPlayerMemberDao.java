package com.example.dao;

import com.example.entity.EightPlayerMember;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface EightPlayerMemberDao {

    @Select("SELECT * FROM eight_player_member WHERE match_id = #{matchId}")
    List<EightPlayerMember> findByMatchId(Long matchId);
} 