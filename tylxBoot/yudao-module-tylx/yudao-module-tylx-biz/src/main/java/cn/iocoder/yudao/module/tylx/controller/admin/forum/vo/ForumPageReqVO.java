package cn.iocoder.yudao.module.tylx.controller.admin.forum.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 论坛分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ForumPageReqVO extends PageParam {

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

    @Schema(description = "首页图片")
    private String picture;

    @Schema(description = "首页标题")
    private String title;

    @Schema(description = "内容")
    private String content;

    @Schema(description = "简介")
    private String introduce;

    @Schema(description = "用户编号", example = "23932")
    private Long userId;

    @Schema(description = "目的地编号", example = "31938")
    private Long destinationId;

    @Schema(description = "阅读量")
    private Integer view;

    @Schema(description = "点赞量")
    private Integer likes;

    @Schema(description = "评论量")
    private Integer comment;

    @Schema(description = "是否精选")
    private Integer isHome;

}