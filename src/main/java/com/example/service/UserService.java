package com.example.service;

import com.example.entity.User;
import com.example.entity.LeaderboardUser;
import com.github.pagehelper.PageInfo;

public interface UserService {
    void save(User user);
    User findByOpenId(String openid);
    void updateUser(User user);
    void updateLoginTime(String openid);
    void updateStats(String openid, int totalScore, int totalWins, int totalGames, int level);
    PageInfo<LeaderboardUser> getLeaderboard(Integer pageNum, Integer pageSize);
    PageInfo<User> findByNickname(String nickname, Integer pageNum, Integer pageSize);
} 