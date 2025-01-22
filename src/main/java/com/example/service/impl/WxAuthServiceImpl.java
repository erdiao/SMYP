package com.example.service.impl;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import com.example.service.WxAuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class WxAuthServiceImpl implements WxAuthService {

    @Autowired
    private WxMaService wxMaService;

    @Override
    public String getOpenId(String code) {
        try {
            WxMaJscode2SessionResult session = wxMaService.getUserService().getSessionInfo(code);
            return session.getOpenid();
        } catch (Exception e) {
            log.error("获取openid失败: {}", e.getMessage(), e);
            throw new RuntimeException("获取openid失败", e);
        }
    }
} 