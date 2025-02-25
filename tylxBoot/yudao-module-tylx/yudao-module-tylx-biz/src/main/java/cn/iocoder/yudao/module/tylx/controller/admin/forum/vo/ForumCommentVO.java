package cn.iocoder.yudao.module.tylx.controller.admin.forum.vo;

import cn.iocoder.yudao.module.system.dal.dataobject.user.AdminUserDO;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fhs.core.trans.anno.Trans;
import com.fhs.core.trans.constant.TransType;
import com.fhs.core.trans.vo.VO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.io.Serializable;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;
import cn.iocoder.yudao.framework.excel.core.annotations.DictFormat;
import cn.iocoder.yudao.framework.excel.core.convert.DictConvert;

@Schema(description = "管理后台 - 论坛评论 Response VO")
@Data
@ExcelIgnoreUnannotated
public class ForumCommentVO implements VO {



    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

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

    @Schema(description = "用户编号", example = "29501")
    @Trans(type = TransType.SIMPLE, target = AdminUserDO.class, fields = "nickname,avatar", ref = "userName,avatar")
    @ExcelProperty("用户编号")
    private Long userId;

    @Schema(description = "用户名称", example = "userName")
    @ExcelProperty("用户名称")
    private String userName;

    @Schema(description = "用户头像", example = "avatar")
    @ExcelProperty("用户头像")
    private String avatar;


    @Schema(description = "分类")
    @ExcelProperty(value = "分类", converter = DictConvert.class)
    @DictFormat("forum_classify") // TODO 代码优化：建议设置到对应的 DictTypeConstants 枚举类中
    private Integer classify;

    @Schema(description = "阅读量")
    @ExcelProperty("阅读量")
    private Integer view;

    @Schema(description = "点赞量")
    @ExcelProperty("点赞量")
    private Integer likes;

    @Schema(description = "评论量")
    @ExcelProperty("评论量")
    private Long comment;

}
