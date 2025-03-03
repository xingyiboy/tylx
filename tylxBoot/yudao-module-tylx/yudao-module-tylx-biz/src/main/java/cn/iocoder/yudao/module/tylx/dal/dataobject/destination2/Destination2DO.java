package cn.iocoder.yudao.module.tylx.dal.dataobject.destination2;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 目的地树表 DO
 *
 * @author 管理员
 */
@TableName("tylx_destination")
@KeySequence("tylx_destination2_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Destination2DO extends BaseDO {

    public static final Long PARENT_ID_ROOT = 0L;

    /**
     * 用户ID
     */
    @TableId
    private Long id;
    /**
     * 目的地名称
     */
    private String name;
    /**
     * 首页图片
     */
    private String picture;
    /**
     * 目的地描述
     */
    private String detail;
    /**
     * 父级编号
     */
    private Long parentId;

}
