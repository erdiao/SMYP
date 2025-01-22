package com.example.controller;

import com.example.common.Result;
import com.example.common.response.LoginResponse;
import com.example.service.WxAuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/wx")
@Tag(name = "微信登录", description = "微信登录相关接口")
public class WxAuthController {
    
    @Autowired
    private WxAuthService wxAuthService;
    
    @Operation(summary = "登录获取openid", description = "通过微信登录code获取openid")
    @PostMapping("/login")
    public Result<?> login(@RequestBody LoginRequest request) {
        try {
            log.info("微信登录请求, code: {}", request.getCode());
            if (request.getCode() == null || request.getCode().trim().isEmpty()) {
                return Result.error("code不能为空");
            }
            
            String openid = wxAuthService.getOpenId(request.getCode().trim());
            return Result.success(new LoginResponse(openid));
        } catch (Exception e) {
            log.error("微信登录失败: ", e);
            return Result.error("微信登录失败");
        }
    }
}

@Data
class LoginRequest {
    private String code;
} 