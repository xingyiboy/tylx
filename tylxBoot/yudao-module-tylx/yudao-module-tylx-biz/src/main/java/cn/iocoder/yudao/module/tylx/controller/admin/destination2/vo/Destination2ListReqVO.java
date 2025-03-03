package cn.iocoder.yudao.module.tylx.controller.admin.destination2.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import java.time.LocalDateTime;
import org.springframework.format.annotation.DateTimeFormat;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 目的地树表列表 Request VO")
@Data
public class Destination2ListReqVO {

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

    @Schema(description = "目的地名称", example = "赵六")
    private String name;

    @Schema(description = "首页图片")
    private String picture;

    @Schema(description = "目的地描述")
    private String detail;

    @Schema(description = "父级编号", example = "8349")
    private Long parentId;

}