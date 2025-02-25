package cn.iocoder.yudao.module.tylx.dal.mysql.forumlikes;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.tylx.dal.dataobject.forumlikes.ForumLikesDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 论坛点赞 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface ForumLikesMapper extends BaseMapperX<ForumLikesDO> {

    default List<ForumLikesDO> selectListByForumId(Long forumId) {
        return selectList(ForumLikesDO::getForumId, forumId);
    }

    default int deleteByForumId(Long forumId) {
        return delete(ForumLikesDO::getForumId, forumId);
    }

}