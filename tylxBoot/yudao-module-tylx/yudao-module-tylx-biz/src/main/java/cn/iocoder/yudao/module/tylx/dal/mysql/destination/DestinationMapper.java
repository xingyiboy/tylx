package cn.iocoder.yudao.module.tylx.dal.mysql.destination;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.tylx.dal.dataobject.destination.DestinationDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.tylx.controller.admin.destination.vo.*;

/**
 * 目的地 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface DestinationMapper extends BaseMapperX<DestinationDO> {

    default PageResult<DestinationDO> selectPage(DestinationPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<DestinationDO>()
                .betweenIfPresent(DestinationDO::getCreateTime, reqVO.getCreateTime())
                .likeIfPresent(DestinationDO::getName, reqVO.getName())
                .eqIfPresent(DestinationDO::getPicture, reqVO.getPicture())
                .likeIfPresent(DestinationDO::getDetail, reqVO.getDetail())
                .orderByDesc(DestinationDO::getId));
    }

}