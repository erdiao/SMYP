package com.example.entity;

import lombok.Data;
import java.util.Date;
import java.util.List;

@Data
public class EightPlayerMatch {
    private Long id;
    private String matchName;           // 比赛名称
    private Integer status;             // 比赛状态：0-进行中，1-已结束
    private String creatorId;           // 创建人ID（微信openid）
    private String creatorName;         // 创建人昵称
    private Integer totalRounds;        // 总回合数
    private Integer finishedRounds;     // 已完成回合数
    private Integer deleted;            // 是否删除：0-否，1-是
    private Integer isSettled;          // 是否结算：0-否，1-是
    private Date createdAt;             // 创建时间
    private Date updatedAt;             // 更新时间
    private String userNames;  // 新增字段,存储用户名称列表,使用|分隔
    
    // 非数据库字段，用于传输数据
    private List<EightPlayerMember> members;
    private List<EightPlayerRound> rounds;
} 