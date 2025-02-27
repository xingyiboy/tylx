package cn.iocoder.yudao.module.tylx.service.team;

import java.util.*;
import javax.validation.*;
import cn.iocoder.yudao.module.tylx.controller.admin.team.vo.*;
import cn.iocoder.yudao.module.tylx.dal.dataobject.team.TeamDO;
import cn.iocoder.yudao.module.tylx.dal.dataobject.teamchat.TeamChatDO;
import cn.iocoder.yudao.module.tylx.dal.dataobject.teammember.TeamMemberDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

/**
 * 队伍 Service 接口
 *
 * @author 管理员
 */
public interface TeamService {

    /**
     * 创建队伍
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createTeam(@Valid TeamSaveReqVO createReqVO);

    /**
     * 更新队伍
     *
     * @param updateReqVO 更新信息
     */
    void updateTeam(@Valid TeamSaveReqVO updateReqVO);

    /**
     * 删除队伍
     *
     * @param id 编号
     */
    void deleteTeam(Long id);

    /**
     * 获得队伍
     *
     * @param id 编号
     * @return 队伍
     */
    TeamDO getTeam(Long id);

    /**
     * 获得队伍分页
     *
     * @param pageReqVO 分页查询
     * @return 队伍分页
     */
    PageResult<TeamDO> getTeamPage(TeamPageReqVO pageReqVO);

    // ==================== 子表（队伍聊天） ====================

    /**
     * 获得队伍聊天列表
     *
     * @param teamId 队伍编号
     * @return 队伍聊天列表
     */
    List<TeamChatDO> getTeamChatListByTeamId(Long teamId);

    // ==================== 子表（队伍队员） ====================

    /**
     * 获得队伍队员列表
     *
     * @param teamId 队伍编号
     * @return 队伍队员列表
     */
    List<TeamMemberDO> getTeamMemberListByTeamId(Long teamId);

    String joinTeam(Long id);

    String createChat(cn.iocoder.yudao.module.tylx.dal.dataobject.teamchat.TeamChatDO createReqVO);

    PageResult<TeamDO> getTeamJoinPage(TeamPageReqVO pageReqVO);
}
