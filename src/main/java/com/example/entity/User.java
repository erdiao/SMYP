package com.example.entity;

import lombok.Data;

import java.util.Date;

@Data
public class User {
    private Long id;
    private String openid;
    private String unionid;
    private String nickname;
    private String avatarUrl;
    private Integer gender;
    private String country;
    private String province;
    private String city;
    private String language;
    
    // 业务字段
    private Integer totalGames;
    private Integer totalWins;
    private Integer totalScore;
    private Integer totalTrainingGames;
    private Integer level;
    
    // 系统字段
    private Date createdAt;
    private Date updatedAt;
    private Date lastLoginAt;
    private Integer status;
} 