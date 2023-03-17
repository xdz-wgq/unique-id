-- 存储 Segment 信息的表
CREATE TABLE `leaf_alloc` (
                              `biz_tag` varchar(128) NOT NULL COMMENT '业务标识',
                              `max_id` bigint(20) DEFAULT NULL COMMENT 'Segment 最大 ID',
                              `step` int(11) DEFAULT NULL COMMENT '每次加载 ID 的数量',
                              `description` varchar(256) DEFAULT NULL COMMENT '描述',
                              `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                              PRIMARY KEY (`biz_tag`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='Leaf Alloc';

-- 存储 ID 号段的表
CREATE TABLE `leaf_alloc_buffer` (
                                     `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '记录编号',
                                     `biz_tag` varchar(128) NOT NULL COMMENT '业务标识',
                                     `max_id` bigint(20) DEFAULT NULL COMMENT '最大 ID',
                                     `step` int(11) DEFAULT NULL COMMENT '步长',
                                     `min_id` bigint(20) DEFAULT NULL COMMENT '最小 ID',
                                     `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                     PRIMARY KEY (`id`),
                                     KEY `idx_biz_tag_max_id` (`biz_tag`,`max_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='Leaf Alloc Buffer';
