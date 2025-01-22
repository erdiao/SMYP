package com.example.common.response;

import lombok.Data;

@Data
public class LoginResponse {
    private String openid;
    
    public LoginResponse(String openid) {
        this.openid = openid;
    }
} 