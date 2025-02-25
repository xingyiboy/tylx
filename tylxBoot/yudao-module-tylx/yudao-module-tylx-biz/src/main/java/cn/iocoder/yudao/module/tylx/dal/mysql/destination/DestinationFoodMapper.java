package cn.iocoder.yudao.module.tylx.dal.mysql.destinationfood;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.tylx.dal.dataobject.destinationfood.DestinationFoodDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 特色美食 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface DestinationFoodMapper extends BaseMapperX<DestinationFoodDO> {

    default List<DestinationFoodDO> selectListByDestinationId(Long destinationId) {
        return selectList(DestinationFoodDO::getDestinationId, destinationId);
    }

    default int deleteByDestinationId(Long destinationId) {
        return delete(DestinationFoodDO::getDestinationId, destinationId);
    }

}