package com.example.service.impl;

import com.example.dao.UserMapper;
import com.example.entity.User;
import com.example.entity.LeaderboardUser;
import com.example.service.UserService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public void save(User user) {
        Date now = new Date();
        user.setCreatedAt(now);
        user.setUpdatedAt(now);
        user.setLastLoginAt(now);
        userMapper.insert(user);
    }

    @Override
    public User findByOpenId(String openid) {
        return userMapper.findByOpenId(openid);
    }

    @Override
    public void updateUser(User user) {
        user.setUpdatedAt(new Date());
        userMapper.updateUserInfo(user);
    }

    @Override
    public void updateLoginTime(String openid) {
        Date now = new Date();
        userMapper.updateLoginTime(openid, now, now);
    }

    @Override
    public void updateStats(String openid, int totalScore, int totalWins, int totalGames, int level) {
        userMapper.updateStats(openid, totalScore, totalWins, totalGames, level, new Date());
    }

    @Override
    public PageInfo<LeaderboardUser> getLeaderboard(Integer pageNum, Integer pageSize) {
        // 设置分页参数
        PageHelper.startPage(pageNum, pageSize);
        // 查询排行榜数据
        List<LeaderboardUser> leaderboard = userMapper.getLeaderboard();
        // 返回分页信息
        return new PageInfo<>(leaderboard);
    }

    @Override
    public PageInfo<User> findByNickname(String nickname, Integer pageNum, Integer pageSize) {
        pageNum = pageNum == null ? 1 : pageNum;
        pageSize = pageSize == null ? 10 : pageSize;
        
        PageHelper.startPage(pageNum, pageSize);
        List<User> users = userMapper.findByNickname(nickname);
        return new PageInfo<>(users);
    }
} 