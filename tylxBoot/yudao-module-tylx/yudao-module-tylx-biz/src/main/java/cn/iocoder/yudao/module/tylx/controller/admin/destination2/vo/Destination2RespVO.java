package cn.iocoder.yudao.module.tylx.controller.admin.destination2.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;

@Schema(description = "管理后台 - 目的地树表 Response VO")
@Data
@ExcelIgnoreUnannotated
public class Destination2RespVO {

    @Schema(description = "用户ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "32031")
    @ExcelProperty("用户ID")
    private Long id;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

    @Schema(description = "目的地名称", example = "赵六")
    @ExcelProperty("目的地名称")
    private String name;

    @Schema(description = "首页图片")
    @ExcelProperty("首页图片")
    private String picture;

    @Schema(description = "目的地描述")
    @ExcelProperty("目的地描述")
    private String detail;

    @Schema(description = "父级编号", example = "8349")
    @ExcelProperty("父级编号")
    private Long parentId;

}