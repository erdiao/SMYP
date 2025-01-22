package com.example.service;

import com.example.entity.EightPlayerMatch;
import com.example.entity.EightPlayerMember;
import com.example.entity.EightPlayerRound;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface EightPlayerMatchService {
    /**
     * 创建比赛
     */
    EightPlayerMatch createMatch(EightPlayerMatch match);
    
    /**
     * 更新对局信息
     * @param matchId 比赛ID
     * @param roundId 对局ID
     * @param leftScore 左侧得分
     * @param rightScore 右侧得分
     * @param isUpdate 是否直接更新分数（true-直接更新，false-累加）
     * @param finishedRounds 已完成的对局数
     * @return 更新后的对局信息
     */
    EightPlayerRound updateRound(Long matchId, Long roundId, Integer leftScore, Integer rightScore, boolean isUpdate, Integer finishedRounds);
    
    /**
     * 结束比赛
     */
    EightPlayerMatch finishMatch(Long id);
    
    /**
     * 更新对局分数（轻量级）
     * @param matchId 比赛ID
     * @param roundId 对局ID
     * @param leftScore 左侧得分
     * @param rightScore 右侧得分
     * @return 更新后的对局信息
     */
    EightPlayerRound updateRoundScore(Long matchId, Long roundId, Integer leftScore, Integer rightScore);
    
    /**
     * 查询八人转比赛列表
     * @param openid 创建人ID
     * @param pageNum 页码
     * @param pageSize 每页条数
     * @return 分页结果
     */
    Page<EightPlayerMatch> listMatches(String openid, Integer pageNum, Integer pageSize);
    
    /**
     * 查询八人转比赛详情
     * @param id 比赛ID
     * @return 比赛详情，包括比赛信息、参赛成员和对局信息
     */
    EightPlayerMatch getMatchDetail(Long id);
    
    /**
     * 查询进行中的八人转比赛
     * @param openid 创建人ID
     * @return 进行中的比赛信息
     */
    EightPlayerMatch getCurrentMatch(String openid);
    
    void saveMemberResults(List<EightPlayerMember> members);
    
    /**
     * 查找已完成但未结算的比赛
     */
    List<EightPlayerMatch> findFinishedButNotSettled();
    
    /**
     * 查询用户参与的所有对局
     * @param openid 用户ID
     * @param pageNum 页码
     * @param pageSize 每页大小
     * @return 分页的对局信息
     */
    PageInfo<EightPlayerRound> findUserRounds(String openid, Integer pageNum, Integer pageSize);
} 