package com.example.entity;

import lombok.Data;
import java.util.Date;

@Data
public class EightPlayerMember {
    private Long id;
    private Long matchId;
    private String playerName;
    private String playerId;
    private Integer winCount;
    private Integer totalScore;
    private Integer totalRounds;
    private Integer isMvp;
    private Integer isSvp;
    private Date createdAt;
    private Date updatedAt;
}