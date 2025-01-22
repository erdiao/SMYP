package com.example.entity;

import lombok.Data;

@Data
public class WxUserInfo {
    private String openId;
    private String nickName;
    private String avatarUrl;
    private Integer gender;  // 0-未知，1-男，2-女
    private String country;
    private String province;
    private String city;
    private String language;
} 