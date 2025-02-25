package cn.iocoder.yudao.module.tylx.dal.dataobject.forumreward;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 论坛打赏 DO
 *
 * @author 芋道源码
 */
@TableName("tylx_forum_reward")
@KeySequence("tylx_forum_reward_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ForumRewardDO extends BaseDO {

    /**
     * 用户ID
     */
    @TableId
    private Long id;
    /**
     * 论坛编号
     */
    private Long forumId;
    /**
     * 用户编号
     */
    private Long userId;
    /**
     * 打赏金额
     */
    private BigDecimal money;

}