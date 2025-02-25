package cn.iocoder.yudao.module.tylx.dal.mysql.forumreward;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.tylx.dal.dataobject.forumreward.ForumRewardDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 论坛打赏 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface ForumRewardMapper extends BaseMapperX<ForumRewardDO> {

    default List<ForumRewardDO> selectListByForumId(Long forumId) {
        return selectList(ForumRewardDO::getForumId, forumId);
    }

    default int deleteByForumId(Long forumId) {
        return delete(ForumRewardDO::getForumId, forumId);
    }

}