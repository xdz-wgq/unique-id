package com.xdz.uniqueid.service.leaf.segment.db.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("leaf_alloc")
public class LeafAlloc {
    @TableId
    private String bizTag;
    private long maxId;
    private int step;
    private String description;
}
