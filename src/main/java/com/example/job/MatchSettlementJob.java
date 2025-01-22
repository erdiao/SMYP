package com.example.job;

import com.example.dao.EightPlayerMatchMapper;
import com.example.dao.EightPlayerMemberMapper;
import com.example.dao.UserMapper;
import com.example.entity.EightPlayerMatch;
import com.example.entity.EightPlayerMember;
import com.example.entity.EightPlayerRound;
import com.example.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Date;

@Slf4j
@Component
public class MatchSettlementJob {

    @Autowired
    private EightPlayerMatchMapper matchMapper;
    
    @Autowired
    private EightPlayerMemberMapper memberMapper;
    
    @Autowired
    private UserMapper userMapper;

    @Scheduled(cron = "0 0/5 * * * ?")  // 每5分钟执行一次
    public void settleMatches() {
        try {
            log.info("开始结算比赛...");
            
            // 查找已完成但未结算的比赛
            List<EightPlayerMatch> matches = matchMapper.findFinishedButNotSettled();
            
            if (matches.isEmpty()) {
                log.info("没有需要结算的比赛");
                return;
            }
            
            for (EightPlayerMatch match : matches) {
                try {
                    // 获取比赛中的选手
                    List<EightPlayerMember> players = getPlayersInMatch(match);
                    
                    // 一次性获取所有有效对局
                    List<EightPlayerRound> validRounds = matchMapper.findValidRoundsByMatchId(match.getId());
                    
                    // 统计每个玩家的对局数
                    Map<String, Integer> playerGamesMap = new HashMap<>();
                    for (EightPlayerRound round : validRounds) {
                        countPlayerGame(playerGamesMap, round.getPlayer1Id());
                        countPlayerGame(playerGamesMap, round.getPlayer2Id());
                        countPlayerGame(playerGamesMap, round.getPlayer3Id());
                        countPlayerGame(playerGamesMap, round.getPlayer4Id());
                    }

                    for (EightPlayerMember player : players) {
                        // 从Map中获取玩家对局数
                        int playerGames = playerGamesMap.getOrDefault(player.getPlayerId(), 0);
                        
                        // 记录选手的分数、胜场和对局数增加情况
                        log.info("更新选手: {} - 分数: {}, 胜场: {}, 对局数: {}", 
                                player.getPlayerId(), player.getTotalScore(), player.getWinCount(), playerGames);

                        // 更新用户的分数、胜场和对局数
                        updateUserStats(player.getPlayerId(), player.getTotalScore(), player.getWinCount(), playerGames);

                        // 记录更新后的用户表信息
                        log.info("数据库中更新后的选手数据: {}", player);
                    }

                    // 更新比赛状态为已结算
                    matchMapper.updateStatus(match.getId(), match.getStatus(), 1);
                    log.info("比赛[{}]结算成功", match.getId());
                } catch (Exception e) {
                    log.error("比赛[{}]结算失败: {}", match.getId(), e.getMessage());
                }
            }
            
            log.info("比赛结算完成，共处理{}场比赛", matches.size());
        } catch (Exception e) {
            log.error("比赛结算任务执行失败: ", e);
        }
    }

    // 定义 getPlayersInMatch 方法
    private List<EightPlayerMember> getPlayersInMatch(EightPlayerMatch match) {
        // 从成员表中查询与比赛ID匹配的成员
        return memberMapper.findByMatchId(match.getId());
    }

    private void updateUserStats(String openid, int score, int wins, int games) {
        try {
            // 获取用户当前信息
            User user = userMapper.findByOpenId(openid);
            if (user == null) {
                log.error("未找到用户: {}", openid);
                return;
            }

            // 计算新的统计数据，处理null值
            int currentTotalScore = user.getTotalScore() != null ? user.getTotalScore() : 0;
            int currentTotalWins = user.getTotalWins() != null ? user.getTotalWins() : 0;
            int currentTotalGames = user.getTotalGames() != null ? user.getTotalGames() : 0;
            
            int newTotalScore = currentTotalScore + score;
            int newTotalWins = currentTotalWins + wins;
            int newTotalGames = currentTotalGames + games;
            // 计算新等级：总分/5000 向上取整
            int newLevel = (int) Math.ceil(newTotalScore / 5000.0);

            // 记录更新前的信息
            log.info("用户 {} 更新前 - 总分: {}, 总胜场: {}, 总场数: {}, 等级: {}", 
                    openid, currentTotalScore, currentTotalWins, currentTotalGames, user.getLevel());

            // 更新用户统计信息
            int updated = userMapper.updateStats(openid, newTotalScore, newTotalWins, newTotalGames, newLevel, new Date());
            if (updated > 0) {
                log.info("用户 {} 更新后 - 总分: {}, 总胜场: {}, 总场数: {}, 等级: {}", 
                        openid, newTotalScore, newTotalWins, newTotalGames, newLevel);
            } else {
                log.error("用户 {} 统计信息更新失败", openid);
            }
        } catch (Exception e) {
            log.error("更新用户 {} 统计信息时发生错误: {}", openid, e.getMessage());
        }
    }

    /**
     * 统计玩家对局数
     * @param playerGamesMap 玩家对局数统计Map
     * @param playerId 玩家ID
     */
    private void countPlayerGame(Map<String, Integer> playerGamesMap, String playerId) {
        if (playerId != null) {
            playerGamesMap.merge(playerId, 1, Integer::sum);
        }
    }
} 