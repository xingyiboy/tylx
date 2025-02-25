package cn.iocoder.yudao.module.tylx.controller.admin.forum.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;
import cn.iocoder.yudao.framework.excel.core.annotations.DictFormat;
import cn.iocoder.yudao.framework.excel.core.convert.DictConvert;

@Schema(description = "管理后台 - 论坛 Response VO")
@Data
@ExcelIgnoreUnannotated
public class ForumRespVO {

    @Schema(description = "用户ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "19968")
    @ExcelProperty("用户ID")
    private Long id;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

    @Schema(description = "首页图片")
    @ExcelProperty("首页图片")
    private String picture;

    @Schema(description = "首页标题")
    @ExcelProperty("首页标题")
    private String title;

    @Schema(description = "内容")
    @ExcelProperty("内容")
    private String content;

    @Schema(description = "简介")
    @ExcelProperty("简介")
    private String introduce;

    @Schema(description = "用户编号", example = "23932")
    @ExcelProperty("用户编号")
    private Long userId;

    @Schema(description = "目的地编号", example = "31938")
    @ExcelProperty("目的地编号")
    private Long destinationId;

    @Schema(description = "阅读量")
    @ExcelProperty("阅读量")
    private Integer view;

    @Schema(description = "点赞量")
    @ExcelProperty("点赞量")
    private Integer likes;

    @Schema(description = "评论量")
    @ExcelProperty("评论量")
    private Integer comment;

    @Schema(description = "是否精选")
    @ExcelProperty(value = "是否精选", converter = DictConvert.class)
    @DictFormat("is_home") // TODO 代码优化：建议设置到对应的 DictTypeConstants 枚举类中
    private Integer isHome;

    private String userName;

    private String avatar;

}
