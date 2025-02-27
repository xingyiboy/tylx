package cn.iocoder.yudao.module.tylx.dal.dataobject.teamchat;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 队伍聊天 DO
 *
 * @author 管理员
 */
@TableName("tylx_team_chat")
@KeySequence("tylx_team_chat_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TeamChatDO extends BaseDO {

    /**
     * 用户ID
     */
    @TableId
    private Long id;
    /**
     * 队伍编号
     */
    private Long teamId;
    /**
     * 用户编号
     */
    private Long userId;
    /**
     * 聊天内容
     */
    private String content;

}