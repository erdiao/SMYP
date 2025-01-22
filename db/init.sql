CREATE TABLE `users` (
`id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
`openid` varchar(32) NOT NULL COMMENT '微信用户唯一标识',
`unionid` varchar(32) DEFAULT NULL COMMENT '微信开放平台唯一标识',
`nickname` varchar(50) NOT NULL COMMENT '用户昵称',
`avatar_url` varchar(255) NOT NULL COMMENT '头像URL',
`gender` tinyint(1) DEFAULT '0' COMMENT '性别 0-未知 1-男 2-女',
`country` varchar(50) NOT NULL COMMENT '国家',
`province` varchar(50) NOT NULL COMMENT '省份',
`city` varchar(50) NOT NULL COMMENT '城市',
`language` varchar(20) DEFAULT NULL COMMENT '语言',

-- 业务字段
`total_games` int(11) NOT NULL DEFAULT '0' COMMENT '总场次',
`total_wins` int(11) NOT NULL DEFAULT '0' COMMENT '胜场数',
`total_score` int(11) NOT NULL DEFAULT '0' COMMENT '总得分',
`total_training_games` int(11) NOT NULL DEFAULT '0' COMMENT '总训练场数',
`level` int(11) NOT NULL DEFAULT '1' COMMENT '用户等级',

-- 系统字段
`created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
`updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
`last_login_at` timestamp NULL DEFAULT NULL COMMENT '最后登录时间',
`status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '状态 1-正常 0-禁用',

PRIMARY KEY (`id`),
UNIQUE KEY `uk_openid` (`openid`),
KEY `idx_unionid` (`unionid`),
KEY `idx_created_at` (`created_at`),
KEY `idx_updated_at` (`updated_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户信息表';

八人转主表（eight_player_match）：
CREATE TABLE `eight_player_match` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `match_name` varchar(64) NOT NULL COMMENT '比赛名称',
  `status` tinyint NOT NULL DEFAULT '0' COMMENT '比赛状态：0-进行中，1-已结束',
  `creator_id` varchar(64) DEFAULT NULL COMMENT '创建人ID（微信openid）',
  `creator_name` varchar(32) DEFAULT NULL COMMENT '创建人昵称',
  `total_rounds` int NOT NULL DEFAULT '0' COMMENT '总回合数',
  `finished_rounds` int NOT NULL DEFAULT '0' COMMENT '已完成回合数',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint NOT NULL DEFAULT '0' COMMENT '是否删除：0-否，1-是',
  `is_settled` int NOT NULL DEFAULT '0' COMMENT '是否结算：0-否，1-是',
  PRIMARY KEY (`id`),
  KEY `idx_creator_id` (`creator_id`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='八人转主表';

八人转成员表（eight_player_member）：
CREATE TABLE `eight_player_member` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `match_id` bigint NOT NULL COMMENT '关联的比赛ID',
  `player_name` varchar(32) NOT NULL COMMENT '玩家名称',
  `player_id` varchar(64) DEFAULT NULL COMMENT '玩家ID（微信openid）',
  `win_count` int NOT NULL DEFAULT '0' COMMENT '胜利场数',
  `total_score` int NOT NULL DEFAULT '0' COMMENT '总得分',
  `total_rounds` int NOT NULL DEFAULT '0' COMMENT '参与总场数',
  `is_mvp` tinyint NOT NULL DEFAULT '0' COMMENT '是否MVP：0-否，1-是',
  `is_svp` tinyint NOT NULL DEFAULT '0' COMMENT '是否SVP：0-否，1-是',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_match_player` (`match_id`, `player_name`),
  KEY `idx_player_id` (`player_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='八人转成员表';

3.对局记录表（eight_player_round）：
CREATE TABLE `eight_player_round` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `match_id` bigint NOT NULL COMMENT '关联的比赛ID',
  `round_num` int NOT NULL COMMENT '回合序号',
  `player1_name` varchar(32) NOT NULL COMMENT '位置1玩家名称',
  `player1_id` varchar(64) DEFAULT NULL COMMENT '位置1玩家ID',
  `player2_name` varchar(32) NOT NULL COMMENT '位置2玩家名称',
  `player2_id` varchar(64) DEFAULT NULL COMMENT '位置2玩家ID',
  `player3_name` varchar(32) NOT NULL COMMENT '位置3玩家名称',
  `player3_id` varchar(64) DEFAULT NULL COMMENT '位置3玩家ID',
  `player4_name` varchar(32) NOT NULL COMMENT '位置4玩家名称',
  `player4_id` varchar(64) DEFAULT NULL COMMENT '位置4玩家ID',
  `left_score` int DEFAULT NULL COMMENT '左侧得分',
  `right_score` int DEFAULT NULL COMMENT '右侧得分',
  `result` tinyint DEFAULT NULL COMMENT '对局结果：1-左侧胜利，-1-左侧失败，0-平局',
  `status` tinyint NOT NULL DEFAULT '0' COMMENT '状态：0-进行中，1-已完成',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_match_round` (`match_id`, `round_num`),
  KEY `idx_match_status` (`match_id`, `status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='八人转对局记录表';

CREATE TABLE `test` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `name` varchar(50) NOT NULL COMMENT '名称',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='测试表';