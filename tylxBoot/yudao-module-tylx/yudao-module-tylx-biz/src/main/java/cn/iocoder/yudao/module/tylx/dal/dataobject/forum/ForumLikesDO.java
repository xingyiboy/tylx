package cn.iocoder.yudao.module.tylx.dal.dataobject.forumlikes;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 论坛点赞 DO
 *
 * @author 芋道源码
 */
@TableName("tylx_forum_likes")
@KeySequence("tylx_forum_likes_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ForumLikesDO extends BaseDO {

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

}