package cn.iocoder.yudao.module.tylx.dal.mysql.teamchat;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.tylx.dal.dataobject.teamchat.TeamChatDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 队伍聊天 Mapper
 *
 * @author 管理员
 */
@Mapper
public interface TeamChatMapper extends BaseMapperX<TeamChatDO> {

    default List<TeamChatDO> selectListByTeamId(Long teamId) {
        return selectList(TeamChatDO::getTeamId, teamId);
    }

    default int deleteByTeamId(Long teamId) {
        return delete(TeamChatDO::getTeamId, teamId);
    }

}