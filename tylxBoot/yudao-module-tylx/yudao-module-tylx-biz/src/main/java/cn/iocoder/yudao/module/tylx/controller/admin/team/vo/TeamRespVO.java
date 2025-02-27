package cn.iocoder.yudao.module.tylx.controller.admin.team.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;
import cn.iocoder.yudao.framework.excel.core.annotations.DictFormat;
import cn.iocoder.yudao.framework.excel.core.convert.DictConvert;

@Schema(description = "管理后台 - 队伍 Response VO")
@Data
@ExcelIgnoreUnannotated
public class TeamRespVO {

    @Schema(description = "用户ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "24171")
    @ExcelProperty("用户ID")
    private Long id;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

    @Schema(description = "目的地编号", example = "10343")
    @ExcelProperty("目的地编号")
    private Long destinationId;

    @Schema(description = "用户编号", example = "6176")
    @ExcelProperty("用户编号")
    private Long userId;

    @Schema(description = "简介")
    @ExcelProperty("简介")
    private String detail;

    @Schema(description = "内容")
    @ExcelProperty("内容")
    private String content;

    @Schema(description = "状态 0招募中 1招募结束", example = "1")
    @ExcelProperty(value = "状态 0招募中 1招募结束", converter = DictConvert.class)
    @DictFormat("tylx_team_status") // TODO 代码优化：建议设置到对应的 DictTypeConstants 枚举类中
    private Integer status;

    @Schema(description = "详细地址")
    @ExcelProperty("详细地址")
    private String area;

    @Schema(description = "最大组队数")
    @ExcelProperty("最大组队数")
    private Integer maxNumber;

    @Schema(description = "开始时间")
    @ExcelProperty("开始时间")
    private LocalDateTime startTime;

    @Schema(description = "结束时间")
    @ExcelProperty("结束时间")
    private LocalDateTime endTime;

    @Schema(description = "用户名")
    @ExcelProperty("用户名")
    private String userName;

    @Schema(description = "当前队伍人数")
    @ExcelProperty("当前队伍人数")
    private Integer currentNumber;

    @Schema(description = "头像")
    @ExcelProperty("头像")
    private String avatar;

    @Schema(description = "性别", example = "1")
    @ExcelProperty(value = "性别" )
    private String gender;

    @Schema(description = "年龄", example = "1")
    @ExcelProperty("年龄")
    private Integer age;

    @Schema(description = "队伍成员")
    @ExcelIgnore
    private List<TeamMemberVO> teamMemberList;

    @Schema(description = "是否是队伍成员")
    @ExcelIgnore
    private Boolean isTeamMember;
}
