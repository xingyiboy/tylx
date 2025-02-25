package cn.iocoder.yudao.module.tylx.dal.mysql.destinationscenery;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.tylx.dal.dataobject.destinationscenery.DestinationSceneryDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 热门景点 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface DestinationSceneryMapper extends BaseMapperX<DestinationSceneryDO> {

    default List<DestinationSceneryDO> selectListByDestinationId(Long destinationId) {
        return selectList(DestinationSceneryDO::getDestinationId, destinationId);
    }

    default int deleteByDestinationId(Long destinationId) {
        return delete(DestinationSceneryDO::getDestinationId, destinationId);
    }

}