package cn.iocoder.yudao.module.tylx.dal.dataobject.team;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 队伍 DO
 *
 * @author 管理员
 */
@TableName("tylx_team")
@KeySequence("tylx_team_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TeamDO extends BaseDO {

    /**
     * 用户ID
     */
    @TableId
    private Long id;
    /**
     * 目的地编号
     */
    private Long destinationId;
    /**
     * 用户编号
     */
    private Long userId;
    /**
     * 简介
     */
    private String detail;
    /**
     * 内容
     */
    private String content;
    /**
     * 状态 0招募中 1招募结束
     *
     * 枚举 {@link TODO tylx_team_status 对应的类}
     */
    private Integer status;
    /**
     * 详细地址
     */
    private String area;
    /**
     * 最大组队数
     */
    private Integer maxNumber;
    /**
     * 开始时间
     */
    private LocalDateTime startTime;
    /**
     * 结束时间
     */
    private LocalDateTime endTime;

}