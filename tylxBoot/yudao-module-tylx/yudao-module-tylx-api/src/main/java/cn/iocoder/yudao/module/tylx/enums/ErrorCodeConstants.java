package cn.iocoder.yudao.module.tylx.enums;

import cn.iocoder.yudao.framework.common.exception.ErrorCode;

/**
 * tylx 错误码枚举类
 *
 * tylx 系统，使用 1-040-000-000 段
 */
public interface ErrorCodeConstants {
    // ========== 目的地
    ErrorCode DESTINATION_NOT_EXISTS = new ErrorCode(1-040-000-000, "目的地不存在");

    // ========== 论坛
    ErrorCode FORUM_NOT_EXISTS = new ErrorCode(1-040-000-001, "论坛不存在");
    // ========== 队伍 TODO 补充编号 ==========
    ErrorCode TEAM_NOT_EXISTS = new ErrorCode(1-040-000-002, "队伍不存在");

    ErrorCode DESTINATION2_NOT_EXISTS = new ErrorCode(1-040-000-002, "目的地树表不存在");
    ErrorCode DESTINATION2_EXITS_CHILDREN = new ErrorCode(1-040-000-002, "存在存在子目的地树表，无法删除");
    ErrorCode DESTINATION2_PARENT_NOT_EXITS = new ErrorCode(1-040-000-002,"父级目的地树表不存在");
    ErrorCode DESTINATION2_PARENT_ERROR = new ErrorCode(1-040-000-002, "不能设置自己为父目的地树表");
    ErrorCode DESTINATION2_NAME_DUPLICATE = new ErrorCode(1-040-000-002, "已经存在该目的地名称的目的地树表");
    ErrorCode DESTINATION2_PARENT_IS_CHILD = new ErrorCode(1-040-000-002, "不能设置自己的子Destination2为父Destination2");

}



