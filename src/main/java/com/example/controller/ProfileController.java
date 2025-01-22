package com.example.controller;

import com.example.common.Result;
import com.example.entity.User;
import com.example.entity.LeaderboardUser;
import com.example.service.UserService;
import com.github.pagehelper.PageInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/profile")
@Tag(name = "用户信息", description = "用户信息相关接口")
public class ProfileController {
    
    @Autowired
    private UserService userService;
    
    @Operation(summary = "获取用户信息", description = "根据openId获取用户信息")
    @PostMapping("/info")
    public Result<?> getProfile(@RequestBody ProfileRequest request) {
        try {
            log.info("获取用户信息请求: {}", request);
            if (request == null || request.getOpenId() == null) {
                return Result.error("openId不能为空");
            }
            
            // 从数据库查询用户信息
            User user = userService.findByOpenId(request.getOpenId());
            if (user == null) {
                return Result.error("用户不存在");
            }
            
            return Result.success(user);
        } catch (Exception e) {
            log.error("获取用户信息失败: ", e);
            return Result.error("获取用户信息失败");
        }
    }
    
    @Operation(summary = "更新用户信息", description = "更新用户基本信息")
    @PostMapping("/update")
    public Result<?> updateProfile(@RequestBody User user) {
        try {
            log.info("更新用户信息请求: {}", user);
            if (user == null || user.getOpenid() == null) {
                return Result.error("用户信息不完整");
            }
            
            // 更新用户信息到数据库
            userService.updateUser(user);
            
            return Result.success(user);
        } catch (Exception e) {
            log.error("更新用户信息失败: ", e);
            return Result.error("更新用户信息失败");
        }
    }

    @Operation(summary = "保存用户重要信息", description = "保存用户的重要信息（昵称、头像、性别）")
    @PostMapping("/saveImportantInfo")
    public Result<?> saveImportantInfo(@RequestBody ImportantInfoRequest request) {
        try {
            log.info("保存用户重要信息请求: {}", request);
            if (request == null || request.getOpenId() == null) {
                return Result.error("openId不能为空");
            }
            
            // 验证必填字段
            if (request.getNickname() == null || request.getNickname().trim().isEmpty()) {
                return Result.error("昵称不能为空");
            }
            if (request.getAvatarUrl() == null || request.getAvatarUrl().trim().isEmpty()) {
                return Result.error("头像URL不能为空");
            }
            if (request.getGender() == null || request.getGender() < 0 || request.getGender() > 2) {
                return Result.error("性别参数无效");
            }
            
            // 从数据库查询用户信息
            User user = userService.findByOpenId(request.getOpenId());
            if (user == null) {
                return Result.error("用户不存在");
            }
            
            // 更新用户重要信息
            user.setNickname(request.getNickname().trim());
            user.setAvatarUrl(request.getAvatarUrl().trim());
            user.setGender(request.getGender());
            userService.updateUser(user);
            
            log.info("保存用户重要信息成功: {}", user);
            return Result.success(user);
        } catch (Exception e) {
            log.error("保存用户重要信息失败: ", e);
            return Result.error("保存用户重要信息失败");
        }
    }

    @Operation(summary = "获取排行榜", description = "获取用户排行榜信息，按等级倒序排序")
    @PostMapping("/leaderboard")
    public Result<?> getLeaderboard(@RequestBody LeaderboardRequest request) {
        try {
            log.info("获取排行榜请求: {}", request);
            if (request == null) {
                return Result.error("请求参数不能为空");
            }
            
            // 获取排行榜数据
            PageInfo<LeaderboardUser> leaderboard = userService.getLeaderboard(request.getPageNum(), request.getPageSize());
            return Result.success(leaderboard);
        } catch (Exception e) {
            log.error("获取排行榜失败: ", e);
            return Result.error("获取排行榜失败");
        }
    }

    @Operation(summary = "获取用户列表", description = "分页获取用户列表")
    @PostMapping("/list")
    public Result<?> getUserList(@RequestBody ListRequest request) {
        try {
            log.info("获取用户列表请求: {}", request);
            if (request == null) {
                return Result.error("请求参数不能为空");
            }
            
            // 获取用户列表
            PageInfo<User> pageInfo = userService.findByNickname(
                "",  // 空字符串表示查询所有用户
                request.getPageNum(),
                request.getPageSize()
            );
            
            return Result.success(pageInfo);
        } catch (Exception e) {
            log.error("获取用户列表失败: ", e);
            return Result.error("获取用户列表失败");
        }
    }
}

@Data
@Schema(description = "获取用户信息请求")
class ProfileRequest {
    @Schema(description = "用户openId", required = true)
    private String openId;
}

@Data
@Schema(description = "保存用户重要信息请求")
class ImportantInfoRequest {
    @Schema(description = "用户openId", required = true)
    private String openId;
    
    @Schema(description = "用户昵称", required = true)
    private String nickname;
    
    @Schema(description = "用户头像URL", required = true)
    private String avatarUrl;
    
    @Schema(description = "用户性别：0-未知，1-男性，2-女性", required = true)
    private Integer gender;
}

@Data
@Schema(description = "排行榜请求")
class LeaderboardRequest {
    @Schema(description = "页码，从1开始", required = true)
    private Integer pageNum = 1;
    
    @Schema(description = "每页数量", required = true)
    private Integer pageSize = 10;
}

@Data
@Schema(description = "列表请求参数")
class ListRequest {
    @Schema(description = "页码，从1开始", required = true)
    private Integer pageNum = 1;
    
    @Schema(description = "每页数量", required = true)
    private Integer pageSize = 10;
} 