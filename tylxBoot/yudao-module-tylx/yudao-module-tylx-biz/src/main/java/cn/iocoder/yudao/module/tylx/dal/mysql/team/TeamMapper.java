package cn.iocoder.yudao.module.tylx.dal.mysql.team;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.tylx.dal.dataobject.team.TeamDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.tylx.controller.admin.team.vo.*;

/**
 * 队伍 Mapper
 *
 * @author 管理员
 */
@Mapper
public interface TeamMapper extends BaseMapperX<TeamDO> {

    default PageResult<TeamDO> selectPage(TeamPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<TeamDO>()
                .betweenIfPresent(TeamDO::getCreateTime, reqVO.getCreateTime())
                .eqIfPresent(TeamDO::getDestinationId, reqVO.getDestinationId())
                .eqIfPresent(TeamDO::getUserId, reqVO.getUserId())
                .likeIfPresent(TeamDO::getDetail, reqVO.getDetail())
                .likeIfPresent(TeamDO::getContent, reqVO.getContent())
                .eqIfPresent(TeamDO::getStatus, reqVO.getStatus())
                .likeIfPresent(TeamDO::getArea, reqVO.getArea())
                .eqIfPresent(TeamDO::getMaxNumber, reqVO.getMaxNumber())
                .betweenIfPresent(TeamDO::getStartTime, reqVO.getStartTime())
                .betweenIfPresent(TeamDO::getEndTime, reqVO.getEndTime())
                .orderByDesc(TeamDO::getId));
    }

}