package com.example.controller;

import com.example.common.Result;
import com.example.entity.User;
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
@RequestMapping("/api/users")
@Tag(name = "用户管理", description = "用户相关接口")
public class UserController {

    @Autowired
    private UserService userService;

    @Operation(summary = "保存用户信息")
    @PostMapping
    public Result<?> save(@RequestBody User user) {
        userService.save(user);
        return Result.success();
    }

    @Operation(summary = "根据昵称查询用户", description = "模糊查询用户信息")
    @PostMapping("/search")
    public Result<?> searchByNickname(@RequestBody SearchRequest request) {
        try {
            if (request.getNickname() == null || request.getNickname().trim().isEmpty()) {
                return Result.error("昵称不能为空");
            }
            
            PageInfo<User> pageInfo = userService.findByNickname(
                request.getNickname().trim(),
                request.getPageNum(),
                request.getPageSize()
            );
            
            return Result.success(pageInfo);
        } catch (Exception e) {
            log.error("查询用户失败: ", e);
            return Result.error("查询用户失败");
        }
    }
}

@Data
class SearchRequest {
    @Schema(description = "用户昵称", required = true)
    private String nickname;
    
    @Schema(description = "页码，从1开始", example = "1")
    private Integer pageNum = 1;
    
    @Schema(description = "每页数量", example = "10")
    private Integer pageSize = 10;
} 