package com.example.service.impl;

import com.example.dao.EightPlayerMatchMapper;
import com.example.dao.EightPlayerMemberMapper;
import com.example.dao.EightPlayerRoundMapper;
import com.example.entity.EightPlayerMatch;
import com.example.entity.EightPlayerMember;
import com.example.entity.EightPlayerRound;
import com.example.service.EightPlayerMatchService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class EightPlayerMatchServiceImpl implements EightPlayerMatchService {
    
    @Autowired
    private EightPlayerMatchMapper matchMapper;
    
    @Autowired
    private EightPlayerMemberMapper memberMapper;
    
    @Autowired
    private EightPlayerRoundMapper roundMapper;
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public EightPlayerMatch createMatch(EightPlayerMatch match) {
        try {
            // 设置初始值
            match.setStatus(0);
            match.setTotalRounds(match.getRounds() != null ? match.getRounds().size() : 0);
            match.setFinishedRounds(0);
            match.setDeleted(0);
            match.setCreatedAt(new Date());
            match.setUpdatedAt(new Date());
            
            log.info("创建比赛, 总轮次: {}", match.getTotalRounds());
            
            // 保存比赛信息
            matchMapper.insert(match);
            
            // 保存成员信息
            if (match.getMembers() != null) {
                log.info("保存成员信息, 成员数量: {}", match.getMembers().size());
                Date now = new Date();
                for (EightPlayerMember member : match.getMembers()) {
                    member.setMatchId(match.getId());
                    member.setWinCount(0);
                    member.setTotalScore(0);
                    member.setTotalRounds(0);
                    member.setIsMvp(0);
                    member.setIsSvp(0);
                    member.setCreatedAt(now);
                    member.setUpdatedAt(now);
                }
                memberMapper.batchInsert(match.getMembers());
            }
            
            // 保存所有轮次对局信息
            if (match.getRounds() != null && !match.getRounds().isEmpty()) {
                log.info("保存对局信息, 对局数量: {}", match.getRounds().size());
                Date now = new Date();
                
                // 改为单条插入以确保每条数据都能正确保存
                for (EightPlayerRound round : match.getRounds()) {
                    round.setMatchId(match.getId());
                    round.setStatus(0);
                    round.setCreatedAt(now);
                    round.setUpdatedAt(now);
                    try {
                        roundMapper.insert(round);
                        log.info("保存第{}轮对局成功", round.getRoundNum());
                    } catch (Exception e) {
                        log.error("保存第{}轮对局失败: {}", round.getRoundNum(), e.getMessage());
                        throw e;
                    }
                }
            }
            
            // 验证保存结果
            List<EightPlayerRound> savedRounds = roundMapper.findByMatchId(match.getId());
            log.info("实际保存的对局数量: {}", savedRounds.size());
            
            match.setRounds(savedRounds);
            return match;
        } catch (Exception e) {
            log.error("创建比赛失败: ", e);
            throw e;
        }
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public EightPlayerRound updateRound(Long matchId, Long roundId, Integer leftScore, Integer rightScore, boolean isUpdate, Integer finishedRounds) {
        try {
            // 查询对局信息
            EightPlayerRound round = roundMapper.findById(roundId);
            if (round == null || !round.getMatchId().equals(matchId)) {
                throw new IllegalArgumentException("对局不存在");
            }
            
            // 更新比分和结果
            round.setLeftScore(leftScore);
            round.setRightScore(rightScore);
            if (leftScore > rightScore) {
                round.setResult(1);
            } else if (leftScore < rightScore) {
                round.setResult(-1);
            } else {
                round.setResult(0);
            }
            round.setStatus(1);
            round.setUpdatedAt(new Date());
            
            // 保存对局信息
            roundMapper.updateById(round);
            
            // 更新比赛信息
            EightPlayerMatch match = matchMapper.findById(matchId);
            match.setFinishedRounds(finishedRounds);
            match.setUpdatedAt(new Date());
            matchMapper.updateById(match);
            
            // 更新成员战绩，传入 isUpdate 参数
            updateMemberStats(round, isUpdate);
            
            return round;
        } catch (Exception e) {
            log.error("更新对局失败: ", e);
            throw e;
        }
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public EightPlayerMatch finishMatch(Long id) {
        try {
            // 查询比赛信息
            EightPlayerMatch match = matchMapper.findById(id);
            if (match == null) {
                throw new IllegalArgumentException("比赛不存在");
            }
            
            // 更新比赛状态
            match.setStatus(1);
            match.setUpdatedAt(new Date());
            matchMapper.updateById(match);
            
            // 查询所有对局
            List<EightPlayerRound> rounds = roundMapper.findByMatchId(id);
            
            // 重新计算每个成员的总分
            List<EightPlayerMember> members = memberMapper.findByMatchId(id);
            for (EightPlayerMember member : members) {
                int totalScore = 0;
                int winCount = 0;
                int totalRounds = 0;
                
                // 遍历所有对局，重新计算总分和胜场
                for (EightPlayerRound round : rounds) {
                    boolean isInTeam1 = member.getPlayerName().equals(round.getPlayer1Name()) || 
                                      member.getPlayerName().equals(round.getPlayer2Name());
                    boolean isInTeam2 = member.getPlayerName().equals(round.getPlayer3Name()) || 
                                      member.getPlayerName().equals(round.getPlayer4Name());
                    
                    if ((isInTeam1 || isInTeam2) && (round.getLeftScore() > 0 || round.getRightScore() > 0)) {
                        totalRounds++;
                        if (isInTeam1) {
                            totalScore += round.getLeftScore();
                            if (round.getResult() == 1) {
                                winCount++;
                            }
                        } else {
                            totalScore += round.getRightScore();
                            if (round.getResult() == -1) {
                                winCount++;
                            }
                        }
                    }
                }
                
                // 更新成员数据
                member.setTotalScore(totalScore);
                member.setWinCount(winCount);
                member.setTotalRounds(totalRounds);
                member.setUpdatedAt(new Date());
            }
            
            // 计算MVP和SVP
            calculateMvpAndSvp(members);
            
            // 更新成员信息
            for (EightPlayerMember member : members) {
                memberMapper.updateById(member);
            }
            
            match.setMembers(members);
            return match;
        } catch (Exception e) {
            log.error("结束比赛失败: ", e);
            throw e;
        }
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public EightPlayerRound updateRoundScore(Long matchId, Long roundId, Integer leftScore, Integer rightScore) {
        try {
            // 查询对局信息
            EightPlayerRound round = roundMapper.findById(roundId);
            if (round == null || !round.getMatchId().equals(matchId)) {
                throw new IllegalArgumentException("对局不存在");
            }
            
            // 更新比分和结果
            round.setLeftScore(leftScore);
            round.setRightScore(rightScore);
            if (leftScore > rightScore) {
                round.setResult(1);
            } else if (leftScore < rightScore) {
                round.setResult(-1);
            } else {
                round.setResult(0);
            }
            round.setStatus(1);
            round.setUpdatedAt(new Date());
            
            // 只更新分数相关字段
            roundMapper.updateScore(round);
            
            return round;
        } catch (Exception e) {
            log.error("更新对局分数失败: ", e);
            throw e;
        }
    }
    
    private void updateMemberStats(EightPlayerRound round, boolean isUpdate) {
        // 更新左侧玩家战绩
        updatePlayerStats(round.getPlayer1Name(), round.getMatchId(), round.getResult() == 1, round.getLeftScore(), isUpdate);
        updatePlayerStats(round.getPlayer2Name(), round.getMatchId(), round.getResult() == 1, round.getLeftScore(), isUpdate);
        
        // 更新右侧玩家战绩
        updatePlayerStats(round.getPlayer3Name(), round.getMatchId(), round.getResult() == -1, round.getRightScore(), isUpdate);
        updatePlayerStats(round.getPlayer4Name(), round.getMatchId(), round.getResult() == -1, round.getRightScore(), isUpdate);
    }
    
    private void updatePlayerStats(String playerName, Long matchId, boolean isWin, Integer score, boolean isUpdate) {
        if (playerName == null) {
            log.warn("玩家名称空，跳过统计");
            return;
        }
        
        List<EightPlayerMember> members = memberMapper.findByMatchId(matchId);
        for (EightPlayerMember member : members) {
            if (playerName.equals(member.getPlayerName())) {
                member.setTotalRounds(member.getTotalRounds() + 1);
                // 不管是否是更新操作，都累加分数
                member.setTotalScore(member.getTotalScore() + score);
                if (isWin) {
                    member.setWinCount(member.getWinCount() + 1);
                }
                member.setUpdatedAt(new Date());
                memberMapper.updateById(member);
                break;
            }
        }
    }
    
    private void calculateMvpAndSvp(List<EightPlayerMember> members) {
        if (members == null || members.isEmpty()) {
            return;
        }
        
        // 计算MVP（胜率最高的玩家）
        EightPlayerMember mvp = members.get(0);
        double maxWinRate = calculateWinRate(mvp);
        
        // 计算SVP（得分最高的玩家）
        EightPlayerMember svp = members.get(0);
        int maxScore = svp.getTotalScore();
        
        for (EightPlayerMember member : members) {
            double winRate = calculateWinRate(member);
            if (winRate > maxWinRate || (winRate == maxWinRate && member.getTotalScore() > mvp.getTotalScore())) {
                maxWinRate = winRate;
                mvp = member;
            }
            
            if (member.getTotalScore() > maxScore) {
                maxScore = member.getTotalScore();
                svp = member;
            }
        }
        
        mvp.setIsMvp(1);
        svp.setIsSvp(1);
    }
    
    private double calculateWinRate(EightPlayerMember member) {
        if (member.getTotalRounds() == 0) {
            return 0;
        }
        return (double) member.getWinCount() / member.getTotalRounds();
    }
    
    @Override
    public Page<EightPlayerMatch> listMatches(String openid, Integer pageNum, Integer pageSize) {
        try {
            if (openid == null || openid.trim().isEmpty()) {
                throw new IllegalArgumentException("openid不能为空");
            }
            
            // 设置默认值
            pageNum = pageNum == null ? 1 : pageNum;
            pageSize = pageSize == null ? 10 : pageSize;
            
            // 设置分页参数
            PageHelper.startPage(pageNum, pageSize);
            
            // 查询数据
            List<EightPlayerMatch> matches = matchMapper.findByCreatorId(openid);
            
            // 返回分页结果
            return (Page<EightPlayerMatch>) matches;
        } catch (Exception e) {
            log.error("查询比赛列表失败: ", e);
            throw e;
        }
    }
    
    @Override
    public EightPlayerMatch getMatchDetail(Long id) {
        try {
            // 查询比赛基本信息
            EightPlayerMatch match = matchMapper.findById(id);
            if (match == null) {
                throw new IllegalArgumentException("比赛不存在");
            }
            
            // 查询参赛成员
            List<EightPlayerMember> members = memberMapper.findByMatchId(id);
            match.setMembers(members);
            
            // 查询对局信息
            List<EightPlayerRound> rounds = roundMapper.findByMatchId(id);
            match.setRounds(rounds);
            
            return match;
        } catch (Exception e) {
            log.error("查询比赛详情失败: ", e);
            throw e;
        }
    }
    
    @Override
    public EightPlayerMatch getCurrentMatch(String openid) {
        try {
            if (openid == null || openid.trim().isEmpty()) {
                throw new IllegalArgumentException("openid不能为空");
            }
            return matchMapper.findCurrentMatch(openid);
        } catch (Exception e) {
            log.error("查询进行中的比赛失败: ", e);
            throw e;
        }
    }
    
    @Override
    @Transactional
    public void saveMemberResults(List<EightPlayerMember> members) {
        Date now = new Date();
        for (EightPlayerMember member : members) {
            member.setCreatedAt(now);
            member.setUpdatedAt(now);
            memberMapper.insert(member);
        }
    }
    
    @Override
    public List<EightPlayerMatch> findFinishedButNotSettled() {
        return matchMapper.findFinishedButNotSettled();
    }
    
    @Override
    public PageInfo<EightPlayerRound> findUserRounds(String openid, Integer pageNum, Integer pageSize) {
        try {
            if (openid == null || openid.trim().isEmpty()) {
                throw new IllegalArgumentException("openid不能为空");
            }
            
            // 设置默认值
            pageNum = pageNum == null ? 1 : pageNum;
            pageSize = pageSize == null ? 10 : pageSize;
            
            // 设置分页参数
            PageHelper.startPage(pageNum, pageSize);
            
            // 查询数据
            List<EightPlayerRound> rounds = roundMapper.findByPlayerId(openid);
            
            // 返回分页结果
            return new PageInfo<>(rounds);
        } catch (Exception e) {
            log.error("查询用户对局列表失败: ", e);
            throw e;
        }
    }
}