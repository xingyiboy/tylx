package cn.iocoder.yudao.module.tylx.controller.admin.destination2.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;

@Schema(description = "管理后台 - 目的地树表新增/修改 Request VO")
@Data
public class Destination2SaveReqVO {

    @Schema(description = "用户ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "32031")
    private Long id;

    @Schema(description = "目的地名称", example = "赵六")
    private String name;

    @Schema(description = "首页图片")
    private String picture;

    @Schema(description = "目的地描述")
    private String detail;

    @Schema(description = "父级编号", example = "8349")
    private Long parentId;

}