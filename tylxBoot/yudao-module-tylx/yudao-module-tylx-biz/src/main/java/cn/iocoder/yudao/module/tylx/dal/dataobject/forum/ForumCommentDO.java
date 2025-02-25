package cn.iocoder.yudao.module.tylx.dal.dataobject.forumcomment;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 论坛评论 DO
 *
 * @author 芋道源码
 */
@TableName("tylx_forum_comment")
@KeySequence("tylx_forum_comment_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ForumCommentDO extends BaseDO {

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
     * 评论内容
     */
    private String content;
    /**
     * 用户编号
     */
    private Long userId;

}