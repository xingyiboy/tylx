package cn.iocoder.yudao.module.tylx.service.team;

import cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import cn.iocoder.yudao.module.tylx.controller.admin.team.vo.*;
import cn.iocoder.yudao.module.tylx.dal.dataobject.team.TeamDO;
import cn.iocoder.yudao.module.tylx.dal.dataobject.teamchat.TeamChatDO;
import cn.iocoder.yudao.module.tylx.dal.dataobject.teammember.TeamMemberDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import cn.iocoder.yudao.module.tylx.dal.mysql.team.TeamMapper;
import cn.iocoder.yudao.module.tylx.dal.mysql.teamchat.TeamChatMapper;
import cn.iocoder.yudao.module.tylx.dal.mysql.teammember.TeamMemberMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.tylx.enums.ErrorCodeConstants.*;

/**
 * 队伍 Service 实现类
 *
 * @author 管理员
 */
@Service
@Validated
public class TeamServiceImpl implements TeamService {

    @Resource
    private TeamMapper teamMapper;
    @Resource
    private TeamChatMapper teamChatMapper;
    @Resource
    private TeamMemberMapper teamMemberMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createTeam(TeamSaveReqVO createReqVO) {
        // 插入
        createReqVO.setUserId(SecurityFrameworkUtils.getLoginUserId());
        TeamDO team = BeanUtils.toBean(createReqVO, TeamDO.class);
        teamMapper.insert(team);

        //初始插入队长
        TeamMemberDO teamMember = new TeamMemberDO();
        teamMember.setUserId(SecurityFrameworkUtils.getLoginUserId());
        teamMember.setTeamId(team.getId());
        teamMemberMapper.insert(teamMember);

        // 插入子表
        if(createReqVO.getTeamChats()!=null){
            createTeamChatList(team.getId(), createReqVO.getTeamChats());
        }
        if(createReqVO.getTeamMembers()!=null){
            createTeamMemberList(team.getId(), createReqVO.getTeamMembers());
        }

        // 返回
        return team.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateTeam(TeamSaveReqVO updateReqVO) {
        // 校验存在
        validateTeamExists(updateReqVO.getId());
        // 更新
        TeamDO updateObj = BeanUtils.toBean(updateReqVO, TeamDO.class);
        teamMapper.updateById(updateObj);

        // 更新子表
        updateTeamChatList(updateReqVO.getId(), updateReqVO.getTeamChats());
        updateTeamMemberList(updateReqVO.getId(), updateReqVO.getTeamMembers());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteTeam(Long id) {
        // 校验存在
        validateTeamExists(id);
        // 删除
        teamMapper.deleteById(id);

        // 删除子表
        deleteTeamChatByTeamId(id);
        deleteTeamMemberByTeamId(id);
    }

    private void validateTeamExists(Long id) {
        if (teamMapper.selectById(id) == null) {
            throw exception(TEAM_NOT_EXISTS);
        }
    }

    @Override
    public TeamDO getTeam(Long id) {
        return teamMapper.selectById(id);
    }

    @Override
    public PageResult<TeamDO> getTeamPage(TeamPageReqVO pageReqVO) {
        return teamMapper.selectPage(pageReqVO);
    }

    @Override
    public PageResult<TeamDO> getTeamJoinPage(TeamPageReqVO pageReqVO) {
        PageParam pageParam = new PageParam();
        pageParam.setPageNo(pageReqVO.getPageNo());
        pageParam.setPageSize(pageReqVO.getPageSize());
        LambdaQueryWrapper<TeamMemberDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(TeamMemberDO::getUserId, SecurityFrameworkUtils.getLoginUserId());
        PageResult<TeamMemberDO> teamMemberDOPageResult = teamMemberMapper.selectPage(pageParam, queryWrapper);
        List<TeamMemberDO> teamMemberDOS = teamMemberDOPageResult.getList();
        PageResult<TeamDO> teamDOPageResult = new PageResult<>();
        teamDOPageResult.setTotal(teamMemberDOPageResult.getTotal());
        List<TeamDO> teamDOList = new ArrayList<>();
        for (cn.iocoder.yudao.module.tylx.dal.dataobject.teammember.TeamMemberDO teamMemberDO : teamMemberDOS) {
            teamDOList.add(teamMapper.selectById(teamMemberDO.getTeamId()));
        }
        teamDOPageResult.setList(teamDOList);
        return teamDOPageResult;
    }

    // ==================== 子表（队伍聊天） ====================

    @Override
    public List<TeamChatDO> getTeamChatListByTeamId(Long teamId) {
        return teamChatMapper.selectListByTeamId(teamId);
    }

    private void createTeamChatList(Long teamId, List<TeamChatDO> list) {
        list.forEach(o -> o.setTeamId(teamId));
        teamChatMapper.insertBatch(list);
    }

    private void updateTeamChatList(Long teamId, List<TeamChatDO> list) {
        deleteTeamChatByTeamId(teamId);
		list.forEach(o -> o.setId(null).setUpdater(null).setUpdateTime(null)); // 解决更新情况下：1）id 冲突；2）updateTime 不更新
        createTeamChatList(teamId, list);
    }

    private void deleteTeamChatByTeamId(Long teamId) {
        teamChatMapper.deleteByTeamId(teamId);
    }

    // ==================== 子表（队伍队员） ====================

    @Override
    public List<TeamMemberDO> getTeamMemberListByTeamId(Long teamId) {
        return teamMemberMapper.selectListByTeamId(teamId);
    }

    @Override
    public String joinTeam(Long id) {
        TeamDO teamDO = teamMapper.selectById(id);
        LambdaQueryWrapper<TeamMemberDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(TeamMemberDO::getTeamId, id);
        List<TeamMemberDO> teamMemberDOS = teamMemberMapper.selectList(queryWrapper);
        if(teamMemberDOS.size() > teamDO.getMaxNumber()){
            return "队伍已满";
        }
        queryWrapper.eq(TeamMemberDO::getUserId, SecurityFrameworkUtils.getLoginUserId());
        if(teamMemberMapper.selectList(queryWrapper).size() > 0){
            return "您已加入该队伍";
        }
        TeamMemberDO teamMemberDO = new TeamMemberDO();
        teamMemberDO.setTeamId(id);
        teamMemberDO.setUserId(SecurityFrameworkUtils.getLoginUserId());
        teamMemberMapper.insert(teamMemberDO);
        return "加入成功";
    }

    @Override
    public String createChat(TeamChatDO createReqVO) {
        createReqVO.setUserId(SecurityFrameworkUtils.getLoginUserId());
        if(StringUtils.isBlank(createReqVO.getContent())){
            return "内容不能为空";
        }
        teamChatMapper.insert(createReqVO);
        return "发送成功";
    }




    private void createTeamMemberList(Long teamId, List<TeamMemberDO> list) {
        list.forEach(o -> o.setTeamId(teamId));
        teamMemberMapper.insertBatch(list);
    }

    private void updateTeamMemberList(Long teamId, List<TeamMemberDO> list) {
        deleteTeamMemberByTeamId(teamId);
		list.forEach(o -> o.setId(null).setUpdater(null).setUpdateTime(null)); // 解决更新情况下：1）id 冲突；2）updateTime 不更新
        createTeamMemberList(teamId, list);
    }

    private void deleteTeamMemberByTeamId(Long teamId) {
        teamMemberMapper.deleteByTeamId(teamId);
    }

}
