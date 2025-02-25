package cn.iocoder.yudao.module.tylx.service.forum;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils;
import cn.iocoder.yudao.module.system.dal.dataobject.user.AdminUserDO;
import cn.iocoder.yudao.module.system.dal.mysql.user.AdminUserMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import cn.iocoder.yudao.module.tylx.controller.admin.forum.vo.*;
import cn.iocoder.yudao.module.tylx.dal.dataobject.forum.ForumDO;
import cn.iocoder.yudao.module.tylx.dal.dataobject.forumcomment.ForumCommentDO;
import cn.iocoder.yudao.module.tylx.dal.dataobject.forumlikes.ForumLikesDO;
import cn.iocoder.yudao.module.tylx.dal.dataobject.forumreward.ForumRewardDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import cn.iocoder.yudao.module.tylx.dal.mysql.forum.ForumMapper;
import cn.iocoder.yudao.module.tylx.dal.mysql.forumcomment.ForumCommentMapper;
import cn.iocoder.yudao.module.tylx.dal.mysql.forumlikes.ForumLikesMapper;
import cn.iocoder.yudao.module.tylx.dal.mysql.forumreward.ForumRewardMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.tylx.enums.ErrorCodeConstants.*;

/**
 * 论坛 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class ForumServiceImpl implements ForumService {

    @Resource
    private ForumMapper forumMapper;
    @Resource
    private ForumCommentMapper forumCommentMapper;
    @Resource
    private ForumLikesMapper forumLikesMapper;
    @Resource
    private ForumRewardMapper forumRewardMapper;
    @Resource
    private AdminUserMapper adminUserMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createForum(ForumSaveReqVO createReqVO) {
        // 插入
        ForumDO forum = BeanUtils.toBean(createReqVO, ForumDO.class);
        forum.setUserId(SecurityFrameworkUtils.getLoginUserId());
        forumMapper.insert(forum);

        // 插入子表
        if(createReqVO.getForumComments()!=null) {
            createForumCommentList(forum.getId(), createReqVO.getForumComments());
        }
        if(createReqVO.getForumLikess()!=null) {
            createForumLikesList(forum.getId(), createReqVO.getForumLikess());
        }
        if(createReqVO.getForumRewards()!=null) {
            createForumRewardList(forum.getId(), createReqVO.getForumRewards());
        }

        // 返回
        return forum.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateForum(ForumSaveReqVO updateReqVO) {
        // 校验存在
        validateForumExists(updateReqVO.getId());
        // 更新
        ForumDO updateObj = BeanUtils.toBean(updateReqVO, ForumDO.class);
        forumMapper.updateById(updateObj);

        // 更新子表
        updateForumCommentList(updateReqVO.getId(), updateReqVO.getForumComments());
        updateForumLikesList(updateReqVO.getId(), updateReqVO.getForumLikess());
        updateForumRewardList(updateReqVO.getId(), updateReqVO.getForumRewards());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteForum(Long id) {
        // 校验存在
        validateForumExists(id);
        // 删除
        forumMapper.deleteById(id);

        // 删除子表
        deleteForumCommentByForumId(id);
        deleteForumLikesByForumId(id);
        deleteForumRewardByForumId(id);
    }

    private void validateForumExists(Long id) {
        if (forumMapper.selectById(id) == null) {
            throw exception(FORUM_NOT_EXISTS);
        }
    }

    @Override
    public ForumDO getForum(Long id) {
        ForumDO forumDO = forumMapper.selectById(id);
        forumDO.setView(forumDO.getView()+1);
        forumMapper.updateById(forumDO);
        return forumDO;
    }

    @Override
    public PageResult<ForumDO> getForumPage(ForumPageReqVO pageReqVO) {
        return forumMapper.selectPage(pageReqVO);
    }

    // ==================== 子表（论坛评论） ====================

    @Override
    public List<ForumCommentDO> getForumCommentListByForumId(Long forumId) {
        return forumCommentMapper.selectListByForumId(forumId);
    }

    private void createForumCommentList(Long forumId, List<ForumCommentDO> list) {
        list.forEach(o -> o.setForumId(forumId));
        forumCommentMapper.insertBatch(list);
    }

    private void updateForumCommentList(Long forumId, List<ForumCommentDO> list) {
        deleteForumCommentByForumId(forumId);
		list.forEach(o -> o.setId(null).setUpdater(null).setUpdateTime(null)); // 解决更新情况下：1）id 冲突；2）updateTime 不更新
        createForumCommentList(forumId, list);
    }

    private void deleteForumCommentByForumId(Long forumId) {
        forumCommentMapper.deleteByForumId(forumId);
    }

    // ==================== 子表（论坛点赞） ====================

    @Override
    public List<ForumLikesDO> getForumLikesListByForumId(Long forumId) {
        return forumLikesMapper.selectListByForumId(forumId);
    }

    private void createForumLikesList(Long forumId, List<ForumLikesDO> list) {
        list.forEach(o -> o.setForumId(forumId));
        forumLikesMapper.insertBatch(list);
    }

    private void updateForumLikesList(Long forumId, List<ForumLikesDO> list) {
        deleteForumLikesByForumId(forumId);
		list.forEach(o -> o.setId(null).setUpdater(null).setUpdateTime(null)); // 解决更新情况下：1）id 冲突；2）updateTime 不更新
        createForumLikesList(forumId, list);
    }

    private void deleteForumLikesByForumId(Long forumId) {
        forumLikesMapper.deleteByForumId(forumId);
    }

    // ==================== 子表（论坛打赏） ====================

    @Override
    public List<ForumRewardDO> getForumRewardListByForumId(Long forumId) {
        return forumRewardMapper.selectListByForumId(forumId);
    }

    /**
     * 创建点赞
     *
     * @param createReqVO
     * @return
     */
    @Override
    public Long createLikes(ForumLikesDO createReqVO) {
        ForumDO forumDO = forumMapper.selectById(createReqVO.getForumId());
        LambdaQueryWrapper<ForumLikesDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ForumLikesDO::getForumId, createReqVO.getForumId());
        queryWrapper.eq(ForumLikesDO::getUserId, SecurityFrameworkUtils.getLoginUserId());
        List<ForumLikesDO> forumLikesDOS = forumLikesMapper.selectList(queryWrapper);
        if(forumLikesDOS.size()>0){
            forumDO.setLikes(forumDO.getLikes() - 1);
            forumLikesMapper.deleteById(forumLikesDOS.get(0).getId());
            forumMapper.updateById(forumDO);
            return null;
        }else {
            forumDO.setLikes(forumDO.getLikes() + 1);
            createReqVO.setUserId(SecurityFrameworkUtils.getLoginUserId());
            forumLikesMapper.insert(createReqVO);
            forumMapper.updateById(forumDO);
            return createReqVO.getId();
        }

    }

    /**
     * 创建评论
     *
     * @param createReqVO
     * @return
     */
    @Override
    public Long createComment(ForumCommentDO createReqVO) {
        ForumDO forumDO = forumMapper.selectById(createReqVO.getForumId());
        forumDO.setComment(forumDO.getComment() + 1);
        forumMapper.updateById(forumDO);
        createReqVO.setUserId(SecurityFrameworkUtils.getLoginUserId());
        forumCommentMapper.insert(createReqVO);
        return createReqVO.getId();
    }

    /**
     * 创建打赏
     *
     * @param createReqVO
     * @return
     */
    @Override
    public String createReward(ForumRewardDO createReqVO) {
        AdminUserDO loginUser = adminUserMapper.selectById(SecurityFrameworkUtils.getLoginUserId());
        // 判断用户是否有足够的钱
        if (loginUser.getMoney().compareTo(createReqVO.getMoney()) < 0) {
            // 如果钱不够，返回提示
            return "余额不足";
        }

        // 扣除指定的钱
        BigDecimal remainingMoney = loginUser.getMoney().subtract(createReqVO.getMoney());

        // 更新用户余额（假设这里调用了一个方法保存余额）
        loginUser.setMoney(remainingMoney);
        adminUserMapper.updateById(loginUser);
        // 创建打赏
        createReqVO.setUserId(SecurityFrameworkUtils.getLoginUserId());
        forumRewardMapper.insert(createReqVO);
        return "打赏成功";
    }

    /**
     * 判断用户是否点赞
     *
     * @param id
     * @return
     */
    @Override
    public CommonResult<Boolean> isLike(Long id) {
        LambdaQueryWrapper<ForumLikesDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ForumLikesDO::getForumId, id);
        queryWrapper.eq(ForumLikesDO::getUserId, SecurityFrameworkUtils.getLoginUserId());
        List<ForumLikesDO> forumLikesDOS = forumLikesMapper.selectList(queryWrapper);
        if(forumLikesDOS.size()>0){
            return CommonResult.success(true);
        }
        return CommonResult.success(false);
    }

    private void createForumRewardList(Long forumId, List<ForumRewardDO> list) {
        list.forEach(o -> o.setForumId(forumId));
        forumRewardMapper.insertBatch(list);
    }

    private void updateForumRewardList(Long forumId, List<ForumRewardDO> list) {
        deleteForumRewardByForumId(forumId);
		list.forEach(o -> o.setId(null).setUpdater(null).setUpdateTime(null)); // 解决更新情况下：1）id 冲突；2）updateTime 不更新
        createForumRewardList(forumId, list);
    }

    private void deleteForumRewardByForumId(Long forumId) {
        forumRewardMapper.deleteByForumId(forumId);
    }

}
