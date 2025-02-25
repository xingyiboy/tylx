package cn.iocoder.yudao.module.tylx.dal.dataobject.destinationscenery;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 热门景点 DO
 *
 * @author 芋道源码
 */
@TableName("tylx_destination_scenery")
@KeySequence("tylx_destination_scenery_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DestinationSceneryDO extends BaseDO {

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
     * 景点名称
     */
    private String name;
    /**
     * 景点详情
     */
    private String content;
    /**
     * 首页图片
     */
    private String picture;
    /**
     * 简介
     */
    private String detail;
    /**
     * 是否精选
     *
     * 枚举 {@link TODO is_home 对应的类}
     */
    private Integer isHome;

}