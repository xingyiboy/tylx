package cn.iocoder.yudao.module.tylx.controller.admin.team.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "管理后台 - 队伍成员 Response VO")
@Data
@ExcelIgnoreUnannotated
public class TeamChatVO {

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
    /**
     * 用户名
     */
    private String userName;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 是否是我发的
     */
    private Boolean isMyChat;

    /**
     * 发送时间
     */
    private Long createTime;

    /**
     * 是否是队长
     */
    private Boolean isOwner;
}
