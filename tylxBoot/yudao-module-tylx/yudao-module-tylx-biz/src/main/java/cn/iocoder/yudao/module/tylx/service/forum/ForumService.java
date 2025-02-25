package cn.iocoder.yudao.module.tylx.service.forum;

import java.util.*;
import javax.validation.*;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.module.tylx.controller.admin.forum.vo.*;
import cn.iocoder.yudao.module.tylx.dal.dataobject.forum.ForumDO;
import cn.iocoder.yudao.module.tylx.dal.dataobject.forumcomment.ForumCommentDO;
import cn.iocoder.yudao.module.tylx.dal.dataobject.forumlikes.ForumLikesDO;
import cn.iocoder.yudao.module.tylx.dal.dataobject.forumreward.ForumRewardDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

/**
 * 论坛 Service 接口
 *
 * @author 芋道源码
 */
public interface ForumService {

    /**
     * 创建论坛
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createForum(@Valid ForumSaveReqVO createReqVO);

    /**
     * 更新论坛
     *
     * @param updateReqVO 更新信息
     */
    void updateForum(@Valid ForumSaveReqVO updateReqVO);

    /**
     * 删除论坛
     *
     * @param id 编号
     */
    void deleteForum(Long id);

    /**
     * 获得论坛
     *
     * @param id 编号
     * @return 论坛
     */
    ForumDO getForum(Long id);

    /**
     * 获得论坛分页
     *
     * @param pageReqVO 分页查询
     * @return 论坛分页
     */
    PageResult<ForumDO> getForumPage(ForumPageReqVO pageReqVO);

    // ==================== 子表（论坛评论） ====================

    /**
     * 获得论坛评论列表
     *
     * @param forumId 论坛编号
     * @return 论坛评论列表
     */
    List<ForumCommentDO> getForumCommentListByForumId(Long forumId);

    // ==================== 子表（论坛点赞） ====================

    /**
     * 获得论坛点赞列表
     *
     * @param forumId 论坛编号
     * @return 论坛点赞列表
     */
    List<ForumLikesDO> getForumLikesListByForumId(Long forumId);

    // ==================== 子表（论坛打赏） ====================

    /**
     * 获得论坛打赏列表
     *
     * @param forumId 论坛编号
     * @return 论坛打赏列表
     */
    List<ForumRewardDO> getForumRewardListByForumId(Long forumId);

    Long createLikes(cn.iocoder.yudao.module.tylx.dal.dataobject.forumlikes.ForumLikesDO createReqVO);

    CommonResult<Boolean> isLike(Long id);

    Long createComment(cn.iocoder.yudao.module.tylx.dal.dataobject.forumcomment.ForumCommentDO createReqVO);

    /**
     * 创建打赏
     * @param createReqVO
     * @return
     */
    String createReward(cn.iocoder.yudao.module.tylx.dal.dataobject.forumreward.ForumRewardDO createReqVO);
}
