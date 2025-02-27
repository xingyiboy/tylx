package cn.iocoder.yudao.module.tylx.controller.admin.team.vo;

import cn.iocoder.yudao.framework.excel.core.annotations.DictFormat;
import cn.iocoder.yudao.framework.excel.core.convert.DictConvert;
import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "管理后台 - 队伍成员 Response VO")
@Data
@ExcelIgnoreUnannotated
public class TeamMemberVO {

    /**
     * 队伍编号
     */
    private Long teamId;
    /**
     * 用户编号
     */
    private Long userId;

    private String userName;

    private String avatar;
}
