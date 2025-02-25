package cn.iocoder.yudao.module.tylx.dal.mysql.forumcomment;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.tylx.dal.dataobject.forumcomment.ForumCommentDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 论坛评论 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface ForumCommentMapper extends BaseMapperX<ForumCommentDO> {

    default List<ForumCommentDO> selectListByForumId(Long forumId) {
        return selectList(ForumCommentDO::getForumId, forumId);
    }

    default int deleteByForumId(Long forumId) {
        return delete(ForumCommentDO::getForumId, forumId);
    }

}