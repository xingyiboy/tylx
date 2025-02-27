package cn.iocoder.yudao.module.tylx.controller.admin.team;

import cn.hutool.core.date.DateUtil;
import cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils;
import cn.iocoder.yudao.module.system.dal.dataobject.user.AdminUserDO;
import cn.iocoder.yudao.module.system.dal.mysql.user.AdminUserMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.thoughtworks.xstream.core.SecurityUtils;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Operation;

import javax.validation.constraints.*;
import javax.validation.*;
import javax.servlet.http.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.*;
import java.io.IOException;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;

import cn.iocoder.yudao.framework.apilog.core.annotation.ApiAccessLog;
import static cn.iocoder.yudao.framework.apilog.core.enums.OperateTypeEnum.*;

import cn.iocoder.yudao.module.tylx.controller.admin.team.vo.*;
import cn.iocoder.yudao.module.tylx.dal.dataobject.team.TeamDO;
import cn.iocoder.yudao.module.tylx.dal.dataobject.teamchat.TeamChatDO;
import cn.iocoder.yudao.module.tylx.dal.dataobject.teammember.TeamMemberDO;
import cn.iocoder.yudao.module.tylx.service.team.TeamService;

@Tag(name = "管理后台 - 队伍")
@RestController
@RequestMapping("/tylx/team")
@Validated
public class TeamController {

    @Resource
    private TeamService teamService;
    @Resource
    private AdminUserMapper adminUserMapper;
    @Resource
    private cn.iocoder.yudao.module.tylx.dal.mysql.teammember.TeamMemberMapper teamMemberMapper;

    @PostMapping("/create")
    @Operation(summary = "创建队伍")
    public CommonResult<Long> createTeam(@Valid @RequestBody TeamSaveReqVO createReqVO) {
        return success(teamService.createTeam(createReqVO));
    }

    @PostMapping("/createChat")
    @Operation(summary = "创建聊天内容")
    public CommonResult<String> createChat(@Valid @RequestBody TeamChatDO createReqVO) {
        return success(teamService.createChat(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新队伍")
    public CommonResult<Boolean> updateTeam(@Valid @RequestBody TeamSaveReqVO updateReqVO) {
        teamService.updateTeam(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除队伍")
    @Parameter(name = "id", description = "编号", required = true)
    public CommonResult<Boolean> deleteTeam(@RequestParam("id") Long id) {
        teamService.deleteTeam(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得队伍")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    public CommonResult<TeamRespVO> getTeam(@RequestParam("id") Long id) {
        TeamDO team = teamService.getTeam(id);
        TeamRespVO teamRespVO = BeanUtils.toBean(team, TeamRespVO.class);
        AdminUserDO adminUserDO = adminUserMapper.selectById(teamRespVO.getUserId());
        teamRespVO.setIsTeamMember(false);
        teamRespVO.setAvatar(adminUserDO.getAvatar());
        teamRespVO.setUserName(adminUserDO.getNickname());
        teamRespVO.setGender(adminUserDO.getSex()==1?"男":"女");
        if (adminUserDO.getBirthDate() != null) {
            LocalDateTime currentDate = LocalDateTime.now(); // 获取当前时间
            LocalDateTime birthDate = adminUserDO.getBirthDate();
            Period period = Period.between(birthDate.toLocalDate(), currentDate.toLocalDate());
            teamRespVO.setAge(period.getYears());
        }

        LambdaQueryWrapper<TeamMemberDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(TeamMemberDO::getTeamId, teamRespVO.getId());
        List<TeamMemberDO> teamMemberDOS = teamMemberMapper.selectList(queryWrapper);
        teamRespVO.setCurrentNumber(teamMemberDOS.size());
        LambdaQueryWrapper<TeamMemberDO> queryWrapper2 = new LambdaQueryWrapper<>();
        queryWrapper2.eq(TeamMemberDO::getTeamId, teamRespVO.getId());
        List<TeamMemberDO> teamMemberDOS1 = teamMemberMapper.selectList(queryWrapper2);
        List<TeamMemberVO> teamMemberVOList = BeanUtils.toBean(teamMemberDOS1, TeamMemberVO.class);
        for (TeamMemberVO teamMemberVO : teamMemberVOList) {
            if(teamMemberVO.getUserId().toString().equals(SecurityFrameworkUtils.getLoginUserId().toString())){
                teamRespVO.setIsTeamMember(true);
            }
            AdminUserDO adminUserDO1 = adminUserMapper.selectById(teamMemberVO.getUserId());
            if(adminUserDO1.getAvatar() != null){
                teamMemberVO.setAvatar(adminUserDO1.getAvatar());
            }

            teamMemberVO.setUserName(adminUserDO1.getNickname());
        }
        teamRespVO.setTeamMemberList(teamMemberVOList);
        return success(teamRespVO);
    }

    @PostMapping("/join")
    @Operation(summary = "加入队伍")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    public CommonResult<String> joinTeam(@RequestParam("id") Long id) {
        return success(teamService.joinTeam(id));
    }

    @GetMapping("/page")
    @Operation(summary = "获得队伍分页")
    public CommonResult<PageResult<TeamRespVO>> getTeamPage(@Valid TeamPageReqVO pageReqVO) {
        PageResult<TeamDO> pageResult = teamService.getTeamPage(pageReqVO);
        PageResult<TeamRespVO> bean = BeanUtils.toBean(pageResult, TeamRespVO.class);
        List<TeamRespVO> list = bean.getList();
        for (TeamRespVO teamRespVO : list) {
            AdminUserDO adminUserDO = adminUserMapper.selectById(teamRespVO.getUserId());
            if (adminUserDO.getAvatar() != null){
                teamRespVO.setAvatar(adminUserDO.getAvatar());
            }
            teamRespVO.setUserName(adminUserDO.getNickname());
            teamRespVO.setGender(adminUserDO.getSex()==1?"男":"女");
            LocalDateTime currentDate = LocalDateTime.now(); // 获取当前时间
            if(adminUserDO.getBirthDate() != null){
                LocalDateTime birthDate = adminUserDO.getBirthDate();
                Period period = Period.between(birthDate.toLocalDate(), currentDate.toLocalDate());
                teamRespVO.setAge(period.getYears());
            }
            LambdaQueryWrapper<TeamMemberDO> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(TeamMemberDO::getTeamId, teamRespVO.getId());
            List<TeamMemberDO> teamMemberDOS = teamMemberMapper.selectList(queryWrapper);
            teamRespVO.setCurrentNumber(teamMemberDOS.size());
        }
        bean.setList(list);
        return success(bean);
    }

    @GetMapping("/Mypage")
    @Operation(summary = "获得我创建的队伍分页")
    public CommonResult<PageResult<TeamRespVO>> getTeamMyPage(@Valid TeamPageReqVO pageReqVO) {
        pageReqVO.setUserId(SecurityFrameworkUtils.getLoginUserId());
        PageResult<TeamDO> pageResult = teamService.getTeamPage(pageReqVO);
        PageResult<TeamRespVO> bean = BeanUtils.toBean(pageResult, TeamRespVO.class);
        List<TeamRespVO> list = bean.getList();
        for (TeamRespVO teamRespVO : list) {
            AdminUserDO adminUserDO = adminUserMapper.selectById(teamRespVO.getUserId());
            if (adminUserDO.getAvatar() != null){
                teamRespVO.setAvatar(adminUserDO.getAvatar());
            }
            teamRespVO.setUserName(adminUserDO.getNickname());
            teamRespVO.setGender(adminUserDO.getSex()==1?"男":"女");
            LocalDateTime currentDate = LocalDateTime.now(); // 获取当前时间
            if(adminUserDO.getBirthDate() != null){
                LocalDateTime birthDate = adminUserDO.getBirthDate();
                Period period = Period.between(birthDate.toLocalDate(), currentDate.toLocalDate());
                teamRespVO.setAge(period.getYears());
            }
            LambdaQueryWrapper<TeamMemberDO> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(TeamMemberDO::getTeamId, teamRespVO.getId());
            List<TeamMemberDO> teamMemberDOS = teamMemberMapper.selectList(queryWrapper);
            teamRespVO.setCurrentNumber(teamMemberDOS.size());
        }
        bean.setList(list);
        return success(bean);
    }

    @GetMapping("/Joinpage")
    @Operation(summary = "获得我加入的队伍分页")
    public CommonResult<PageResult<TeamRespVO>> getTeamJoinPage(@Valid TeamPageReqVO pageReqVO) {
        PageResult<TeamDO> pageResult = teamService.getTeamJoinPage(pageReqVO);
        PageResult<TeamRespVO> bean = BeanUtils.toBean(pageResult, TeamRespVO.class);
        List<TeamRespVO> list = bean.getList();
        for (TeamRespVO teamRespVO : list) {
            AdminUserDO adminUserDO = adminUserMapper.selectById(teamRespVO.getUserId());
            if (adminUserDO.getAvatar() != null){
                teamRespVO.setAvatar(adminUserDO.getAvatar());
            }
            teamRespVO.setUserName(adminUserDO.getNickname());
            teamRespVO.setGender(adminUserDO.getSex()==1?"男":"女");
            LocalDateTime currentDate = LocalDateTime.now(); // 获取当前时间
            if(adminUserDO.getBirthDate() != null){
                LocalDateTime birthDate = adminUserDO.getBirthDate();
                Period period = Period.between(birthDate.toLocalDate(), currentDate.toLocalDate());
                teamRespVO.setAge(period.getYears());
            }
            LambdaQueryWrapper<TeamMemberDO> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(TeamMemberDO::getTeamId, teamRespVO.getId());
            List<TeamMemberDO> teamMemberDOS = teamMemberMapper.selectList(queryWrapper);
            teamRespVO.setCurrentNumber(teamMemberDOS.size());
        }
        bean.setList(list);
        return success(bean);
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出队伍 Excel")
    @ApiAccessLog(operateType = EXPORT)
    public void exportTeamExcel(@Valid TeamPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<TeamDO> list = teamService.getTeamPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "队伍.xls", "数据", TeamRespVO.class,
                        BeanUtils.toBean(list, TeamRespVO.class));
    }

    // ==================== 子表（队伍聊天） ====================

    @GetMapping("/team-chat/list-by-team-id")
    @Operation(summary = "获得队伍聊天列表")
    @Parameter(name = "teamId", description = "队伍编号")
    public CommonResult<List<TeamChatVO>> getTeamChatListByTeamId(@RequestParam("teamId") Long teamId) {
        List<TeamChatDO> teamChatList = teamService.getTeamChatListByTeamId(teamId);
        List<TeamChatVO> teamChatVOList = BeanUtils.toBean(teamChatList, TeamChatVO.class);
        for (TeamChatVO teamChatVO : teamChatVOList) {
            AdminUserDO adminUserDO = adminUserMapper.selectById(teamChatVO.getUserId());
            if(adminUserDO.getAvatar() != null){
                teamChatVO.setAvatar(adminUserDO.getAvatar());
            }
            teamChatVO.setUserName(adminUserDO.getNickname());
            if(teamChatVO.getUserId().toString().equals(SecurityFrameworkUtils.getLoginUserId().toString())){
                teamChatVO.setIsMyChat(true);
            }else{
                teamChatVO.setIsMyChat(false);
            }
            TeamDO team = teamService.getTeam(teamChatVO.getTeamId());
            if(team.getUserId().toString().equals(teamChatVO.getUserId().toString())){
                teamChatVO.setIsOwner(true);
            }else {
                teamChatVO.setIsOwner(false);
            }
        }
        return success(teamChatVOList);
    }

    // ==================== 子表（队伍队员） ====================

    @GetMapping("/team-member/list-by-team-id")
    @Operation(summary = "获得队伍队员列表")
    @Parameter(name = "teamId", description = "队伍编号")
    public CommonResult<List<TeamMemberDO>> getTeamMemberListByTeamId(@RequestParam("teamId") Long teamId) {
        return success(teamService.getTeamMemberListByTeamId(teamId));
    }

}
