package com.example.entity;

import lombok.Data;
import java.util.Date;

@Data
public class EightPlayerRound {
    private Long id;
    private Long matchId;
    private String matchName;
    private Integer roundNum;
    private String player1Name;
    private String player1Id;
    private String player2Name;
    private String player2Id;
    private String player3Name;
    private String player3Id;
    private String player4Name;
    private String player4Id;
    private Integer leftScore;
    private Integer rightScore;
    private Integer result;
    private Integer status;
    private Date createdAt;
    private Date updatedAt;
}