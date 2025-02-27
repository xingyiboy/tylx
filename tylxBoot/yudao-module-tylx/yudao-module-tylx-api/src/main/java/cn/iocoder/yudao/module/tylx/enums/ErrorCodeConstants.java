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

}



