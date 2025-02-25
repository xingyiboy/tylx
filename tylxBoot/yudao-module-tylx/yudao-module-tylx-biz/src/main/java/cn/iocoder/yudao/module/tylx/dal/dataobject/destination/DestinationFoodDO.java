package cn.iocoder.yudao.module.tylx.dal.dataobject.destinationfood;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 特色美食 DO
 *
 * @author 芋道源码
 */
@TableName("tylx_destination_food")
@KeySequence("tylx_destination_food_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DestinationFoodDO extends BaseDO {

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
     * 美食名称
     */
    private String name;
    /**
     * 首页图片
     */
    private String picture;
    /**
     * 简介
     */
    private String detail;
    /**
     * 内容
     */
    private String content;

}