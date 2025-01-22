package com.example.controller;

import com.example.common.Result;
import com.example.entity.EightPlayerMatch;
import com.example.entity.EightPlayerRound;
import com.example.service.EightPlayerMatchService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/matches")
@Tag(name = "八人转比赛", description = "八人转比赛相关接口")
public class EightPlayerMatchController {
    
    @Autowired
    private EightPlayerMatchService matchService;
    
    @Operation(summary = "创建比赛", description = "创建新的八人转比赛")
    @RequestMapping(value = "", method = {RequestMethod.GET, RequestMethod.POST})
    public Result<?> createMatch(@RequestBody EightPlayerMatch match) {
        try {
            log.info("创建比赛: {}", match);
            EightPlayerMatch savedMatch = matchService.createMatch(match);
            return Result.success(savedMatch);
        } catch (Exception e) {
            log.error("创建比赛失败: ", e);
            return Result.error("创建比赛失败");
        }
    }
    
    @Operation(summary = "更新对局信息", description = "更新对局比分和结果")
    @RequestMapping(value = "/{matchId}/rounds/{roundId}", method = {RequestMethod.GET, RequestMethod.POST})
    public Result<?> updateRound(
            @Parameter(description = "比赛ID", example = "1")
            @PathVariable Long matchId,
            @Parameter(description = "对局ID", example = "1")
            @PathVariable Long roundId,
            @Parameter(description = "比分信息")
            @RequestBody ScoreRequest request) {
        try {
            log.info("更新对局: matchId={}, roundId={}, score={}", matchId, roundId, request);
            EightPlayerRound round = matchService.updateRound(matchId, roundId, 
                    request.getLeftScore(), request.getRightScore(), 
                    request.getIsUpdate() != null && request.getIsUpdate(),
                    request.getFinishedRounds());
            return Result.success(round);
        } catch (Exception e) {
            log.error("更新对局失败: ", e);
            return Result.error("更新对局失败");
        }
    }
    
    @Operation(summary = "结束比赛", description = "结束比赛并计算MVP/SVP")
    @RequestMapping(value = "/{id}/finish", method = {RequestMethod.GET, RequestMethod.POST})
    public Result<?> finishMatch(@PathVariable Long id) {
        try {
            matchService.finishMatch(id);
            return Result.success();
        } catch (Exception e) {
            log.error("结束比赛失败: ", e);
            return Result.error("结束比赛失败");
        }
    }
    
    @Operation(summary = "更新对局分数", description = "轻量级更新对局分数")
    @RequestMapping(value = "/{matchId}/rounds/{roundId}/score", method = {RequestMethod.GET, RequestMethod.POST})
    public Result<?> updateRoundScore(
            @Parameter(description = "比赛ID", example = "1")
            @PathVariable Long matchId,
            @Parameter(description = "对局ID", example = "1")
            @PathVariable Long roundId,
            @Parameter(description = "比分信息")
            @RequestBody ScoreRequest request) {
        try {
            log.info("更新对局分数: matchId={}, roundId={}, score={}", matchId, roundId, request);
            EightPlayerRound round = matchService.updateRoundScore(matchId, roundId, 
                    request.getLeftScore(), request.getRightScore());
            return Result.success(round);
        } catch (Exception e) {
            log.error("更新对局分数失败: ", e);
            return Result.error("更新对局分数失败");
        }
    }
    
    @Operation(summary = "查询比赛列表", description = "查询用户创建的八人转比赛列表")
    @RequestMapping(value = "/list", method = {RequestMethod.GET, RequestMethod.POST})
    public Result<?> listMatches(
            @Parameter(description = "查询参数")
            @RequestBody(required = false) MatchListRequest request) {
        try {
            log.info("查询比赛列表: {}", request);
            Page<EightPlayerMatch> page = matchService.listMatches(
                    request.getOpenid(), 
                    request.getPageNum(), 
                    request.getPageSize());
            return Result.success(page);
        } catch (Exception e) {
            log.error("查询比赛列表失败: ", e);
            return Result.error("查询比赛列表失败");
        }
    }
    
    @Operation(summary = "查询比赛详情", description = "查询八人转比赛详情，包括比赛信息、参赛成员和对局信息")
    @RequestMapping(value = "/{id}", method = {RequestMethod.GET, RequestMethod.POST})
    public Result<?> getMatchDetail(
            @Parameter(description = "比赛ID", example = "1")
            @PathVariable Long id) {
        try {
            log.info("查询比赛详情: id={}", id);
            EightPlayerMatch match = matchService.getMatchDetail(id);
            return Result.success(match);
        } catch (Exception e) {
            log.error("查询比赛详情失败: ", e);
            return Result.error("查询比赛详情失败");
        }
    }
    
    @Operation(summary = "查询进行中的比赛", description = "查询用户最新一条进行中的八人转比赛")
    @PostMapping("/current")
    public Result<?> getCurrentMatch(
            @Parameter(description = "查询参数")
            @RequestBody CurrentMatchRequest request) {
        try {
            log.info("查询进行中的比赛，参数: {}", request);
            if (request == null || request.getOpenid() == null) {
                return Result.error("参数错误：openid不能为空");
            }
            EightPlayerMatch match = matchService.getCurrentMatch(request.getOpenid());
            return Result.success(match);
        } catch (Exception e) {
            log.error("查询进行中的比赛失败: ", e);
            return Result.error("查询进行中的比赛失败");
        }
    }
    
    @Operation(summary = "查询用户参与的对局", description = "查询用户参与的所有对局，包括作为任意位置的选手参与的对局")
    @RequestMapping(value = "/user-rounds", method = {RequestMethod.GET, RequestMethod.POST})
    public Result<?> getUserRounds(@RequestBody UserRoundsRequest request) {
        try {
            log.info("查询用户参与的对局: {}", request);
            if (request == null || request.getOpenid() == null) {
                return Result.error("参数错误：openid不能为空");
            }
            PageInfo<EightPlayerRound> rounds = matchService.findUserRounds(
                    request.getOpenid(),
                    request.getPageNum(),
                    request.getPageSize());
            return Result.success(rounds);
        } catch (Exception e) {
            log.error("查询用户参与的对局失败: ", e);
            return Result.error("查询用户参与的对局失败");
        }
    }
}

@Data
class ScoreRequest {
    @Schema(description = "左侧得分", example = "21")
    private Integer leftScore;
    
    @Schema(description = "右侧得分", example = "19")
    private Integer rightScore;
    
    @Schema(description = "是否直接更新分数（true-直接更新，false-累加）", example = "true")
    private Boolean isUpdate;

    @Schema(description = "已完成的对局数", example = "1")
    private Integer finishedRounds;
}

@Data
class MatchListRequest {
    @Schema(description = "用户openid")
    private String openid;
    
    @Schema(description = "页码", example = "1")
    private Integer pageNum;
    
    @Schema(description = "每页条数", example = "10")
    private Integer pageSize;
}

@Data
class CurrentMatchRequest {
    @Schema(description = "创建人ID", example = "wx_123456789")
    private String openid;
}

@Data
class UserRoundsRequest {
    @Schema(description = "用户openid")
    private String openid;
    
    @Schema(description = "页码", example = "1")
    private Integer pageNum;
    
    @Schema(description = "每页条数", example = "10")
    private Integer pageSize;
} 