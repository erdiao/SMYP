CREATE TABLE IF NOT EXISTS eight_player_match (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    match_name VARCHAR(100) NOT NULL COMMENT '比赛名称',
    status INT NOT NULL DEFAULT 0 COMMENT '比赛状态：0-进行中，1-已结束',
    creator_id VARCHAR(100) NOT NULL COMMENT '创建人ID（微信openid）',
    creator_name VARCHAR(100) NOT NULL COMMENT '创建人昵称',
    total_rounds INT NOT NULL COMMENT '总回合数',
    finished_rounds INT NOT NULL DEFAULT 0 COMMENT '已完成回合数',
    deleted INT NOT NULL DEFAULT 0 COMMENT '是否删除：0-否，1-是',
    is_settled INT NOT NULL DEFAULT 0 COMMENT '是否结算：0-否，1-是',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) COMMENT='八人转比赛表'; 