package cn.iocoder.yudao.module.tylx.controller.admin.team.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import cn.iocoder.yudao.module.tylx.dal.dataobject.teamchat.TeamChatDO;
import cn.iocoder.yudao.module.tylx.dal.dataobject.teammember.TeamMemberDO;

@Schema(description = "管理后台 - 队伍新增/修改 Request VO")
@Data
public class TeamSaveReqVO {

    @Schema(description = "用户ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "24171")
    private Long id;

    @Schema(description = "用户编号", example = "6176")
    private Long userId;

    @Schema(description = "目的地编号", example = "10343")
    private Long destinationId;


    @Schema(description = "简介")
    private String detail;

    @Schema(description = "内容")
    private String content;

    @Schema(description = "状态 0招募中 1招募结束", example = "1")
    private Integer status;

    @Schema(description = "详细地址")
    private String area;

    @Schema(description = "最大组队数")
    private Integer maxNumber;

    @Schema(description = "开始时间")
    private LocalDateTime startTime;

    @Schema(description = "结束时间")
    private LocalDateTime endTime;

    @Schema(description = "队伍聊天列表")
    private List<TeamChatDO> teamChats;

    @Schema(description = "队伍队员列表")
    private List<TeamMemberDO> teamMembers;

}