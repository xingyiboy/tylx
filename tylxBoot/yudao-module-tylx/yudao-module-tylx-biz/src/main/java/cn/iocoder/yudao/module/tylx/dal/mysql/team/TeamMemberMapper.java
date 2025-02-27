package cn.iocoder.yudao.module.tylx.dal.mysql.teammember;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.tylx.dal.dataobject.teammember.TeamMemberDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 队伍队员 Mapper
 *
 * @author 管理员
 */
@Mapper
public interface TeamMemberMapper extends BaseMapperX<TeamMemberDO> {

    default List<TeamMemberDO> selectListByTeamId(Long teamId) {
        return selectList(TeamMemberDO::getTeamId, teamId);
    }

    default int deleteByTeamId(Long teamId) {
        return delete(TeamMemberDO::getTeamId, teamId);
    }

}