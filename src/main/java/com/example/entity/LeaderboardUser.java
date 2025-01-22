package com.example.entity;

import lombok.Data;

@Data
public class LeaderboardUser {
    private Long id;
    private String openid;
    private String nickname;
    private String avatarUrl;
    private Integer totalScore;
    private Integer totalWins;
    private Integer totalGames;
    private Integer level;
} 