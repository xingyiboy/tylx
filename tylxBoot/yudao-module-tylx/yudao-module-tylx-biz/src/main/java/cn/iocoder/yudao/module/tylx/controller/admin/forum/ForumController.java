package cn.iocoder.yudao.module.tylx.controller.admin.forum;

import cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils;
import cn.iocoder.yudao.module.system.dal.dataobject.user.AdminUserDO;
import cn.iocoder.yudao.module.system.dal.mysql.user.AdminUserMapper;
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

import cn.iocoder.yudao.module.tylx.controller.admin.forum.vo.*;
import cn.iocoder.yudao.module.tylx.dal.dataobject.forum.ForumDO;
import cn.iocoder.yudao.module.tylx.dal.dataobject.forumcomment.ForumCommentDO;
import cn.iocoder.yudao.module.tylx.dal.dataobject.forumlikes.ForumLikesDO;
import cn.iocoder.yudao.module.tylx.dal.dataobject.forumreward.ForumRewardDO;
import cn.iocoder.yudao.module.tylx.service.forum.ForumService;

@Tag(name = "管理后台 - 论坛")
@RestController
@RequestMapping("/tylx/forum")
@Validated
public class ForumController {

    @Resource
    private ForumService forumService;
    @Resource
    private AdminUserMapper adminUserMapper;

    @PostMapping("/createLikes")
    @Operation(summary = "点赞-如果有点赞就是取消点赞")
    public CommonResult<Long> createForum(@Valid @RequestBody ForumLikesDO createReqVO) {
        return success(forumService.createLikes(createReqVO));
    }

    @GetMapping("/isLike")
    @Operation(summary = "判断用户是否点赞过")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    public CommonResult<Boolean> isLike(@RequestParam("id") Long id) {
        return forumService.isLike(id);
    }

    @PostMapping("/createComment")
    @Operation(summary = "创建评论")
    public CommonResult<Long> createComment(@Valid @RequestBody ForumCommentDO createReqVO) {
        return success(forumService.createComment(createReqVO));
    }

    @PostMapping("/createReward")
    @Operation(summary = "创建打赏-返回提示消息String")
    public CommonResult<String> createReward(@Valid @RequestBody ForumRewardDO createReqVO) {
        return success(forumService.createReward(createReqVO));
    }

    @PostMapping("/create")
    @Operation(summary = "创建论坛")
    @PreAuthorize("@ss.hasPermission('tylx:forum:create')")
    public CommonResult<Long> createForum(@Valid @RequestBody ForumSaveReqVO createReqVO) {
        return success(forumService.createForum(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新论坛")
    @PreAuthorize("@ss.hasPermission('tylx:forum:update')")
    public CommonResult<Boolean> updateForum(@Valid @RequestBody ForumSaveReqVO updateReqVO) {
        forumService.updateForum(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除论坛")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('tylx:forum:delete')")
    public CommonResult<Boolean> deleteForum(@RequestParam("id") Long id) {
        forumService.deleteForum(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得论坛")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    public CommonResult<ForumRespVO> getForum(@RequestParam("id") Long id) {
        ForumDO forum = forumService.getForum(id);
        ForumRespVO forumRespVO = BeanUtils.toBean(forum, ForumRespVO.class);
        AdminUserDO adminUserDO = adminUserMapper.selectById(forumRespVO.getUserId());
        if (adminUserDO.getAvatar() != null) {
            forumRespVO.setAvatar(adminUserDO.getAvatar());
        }
        if (adminUserDO.getNickname() != null) {
            forumRespVO.setUserName(adminUserDO.getNickname());
        }
        return success(forumRespVO);
    }

    @GetMapping("/page")
    @Operation(summary = "获得论坛分页")
    public CommonResult<PageResult<ForumRespVO>> getForumPage(@Valid ForumPageReqVO pageReqVO) {
        PageResult<ForumDO> pageResult = forumService.getForumPage(pageReqVO);
        PageResult<ForumRespVO> bean = BeanUtils.toBean(pageResult, ForumRespVO.class);
        List<ForumRespVO> list = bean.getList();
        for (ForumRespVO forumRespVO : list) {
            AdminUserDO adminUserDO = adminUserMapper.selectById(forumRespVO.getUserId());
            if (adminUserDO != null) {
                if (adminUserDO.getAvatar() != null){
                    forumRespVO.setAvatar(adminUserDO.getAvatar());
                }
                if( adminUserDO.getNickname() != null){
                    forumRespVO.setUserName(adminUserDO.getNickname());
                }
            }
        }
        bean.setList(list);
        return success(bean);
    }

    @GetMapping("/Mypage")
    @Operation(summary = "获得我的论坛分页")
    public CommonResult<PageResult<ForumRespVO>> getForumMyPage(@Valid ForumPageReqVO pageReqVO) {
        pageReqVO.setUserId(SecurityFrameworkUtils.getLoginUserId());
        PageResult<ForumDO> pageResult = forumService.getForumPage(pageReqVO);
        PageResult<ForumRespVO> bean = BeanUtils.toBean(pageResult, ForumRespVO.class);
        List<ForumRespVO> list = bean.getList();
        for (ForumRespVO forumRespVO : list) {
            AdminUserDO adminUserDO = adminUserMapper.selectById(forumRespVO.getUserId());
            if (adminUserDO != null) {
                if (adminUserDO.getAvatar() != null){
                    forumRespVO.setAvatar(adminUserDO.getAvatar());
                }
                if( adminUserDO.getNickname() != null){
                    forumRespVO.setUserName(adminUserDO.getNickname());
                }
            }
        }
        bean.setList(list);
        return success(bean);
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出论坛 Excel")
    @PreAuthorize("@ss.hasPermission('tylx:forum:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportForumExcel(@Valid ForumPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<ForumDO> list = forumService.getForumPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "论坛.xls", "数据", ForumRespVO.class,
                        BeanUtils.toBean(list, ForumRespVO.class));
    }

    // ==================== 子表（论坛评论） ====================

    @GetMapping("/forum-comment/list-by-forum-id")
    @Operation(summary = "获得论坛评论列表")
    @Parameter(name = "forumId", description = "论坛编号")
    public CommonResult<List<ForumCommentVO>> getForumCommentListByForumId(@RequestParam("forumId") Long forumId) {
        List<ForumCommentDO> forumCommentListByForumId = forumService.getForumCommentListByForumId(forumId);
        List<ForumCommentVO> bean = BeanUtils.toBean(forumCommentListByForumId, ForumCommentVO.class);
        for (ForumCommentVO forumCommentVO : bean) {
            AdminUserDO adminUserDO = adminUserMapper.selectById(forumCommentVO.getUserId());
            if (adminUserDO != null) {
                if (adminUserDO.getAvatar() != null){
                    forumCommentVO.setAvatar(adminUserDO.getAvatar());
                }
                if (adminUserDO.getNickname() != null){
                    forumCommentVO.setUserName(adminUserDO.getNickname());
                }

            }
        }

        return success(bean);
    }

    // ==================== 子表（论坛点赞） ====================

    @GetMapping("/forum-likes/list-by-forum-id")
    @Operation(summary = "获得论坛点赞列表")
    @Parameter(name = "forumId", description = "论坛编号")
    @PreAuthorize("@ss.hasPermission('tylx:forum:query')")
    public CommonResult<List<ForumLikesDO>> getForumLikesListByForumId(@RequestParam("forumId") Long forumId) {
        return success(forumService.getForumLikesListByForumId(forumId));
    }

    // ==================== 子表（论坛打赏） ====================

    @GetMapping("/forum-reward/list-by-forum-id")
    @Operation(summary = "获得论坛打赏列表")
    @Parameter(name = "forumId", description = "论坛编号")
    @PreAuthorize("@ss.hasPermission('tylx:forum:query')")
    public CommonResult<List<ForumRewardDO>> getForumRewardListByForumId(@RequestParam("forumId") Long forumId) {
        return success(forumService.getForumRewardListByForumId(forumId));
    }

}
