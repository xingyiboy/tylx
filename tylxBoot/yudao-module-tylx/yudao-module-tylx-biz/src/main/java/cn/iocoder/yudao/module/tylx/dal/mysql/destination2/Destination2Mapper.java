package cn.iocoder.yudao.module.tylx.dal.mysql.destination2;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.tylx.dal.dataobject.destination2.Destination2DO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.tylx.controller.admin.destination2.vo.*;

/**
 * 目的地树表 Mapper
 *
 * @author 管理员
 */
@Mapper
public interface Destination2Mapper extends BaseMapperX<Destination2DO> {

    default List<Destination2DO> selectList(Destination2ListReqVO reqVO) {
        return selectList(new LambdaQueryWrapperX<Destination2DO>()
                .betweenIfPresent(Destination2DO::getCreateTime, reqVO.getCreateTime())
                .likeIfPresent(Destination2DO::getName, reqVO.getName())
                .eqIfPresent(Destination2DO::getPicture, reqVO.getPicture())
                .eqIfPresent(Destination2DO::getDetail, reqVO.getDetail())
                .eqIfPresent(Destination2DO::getParentId, reqVO.getParentId())
                .orderByDesc(Destination2DO::getId));
    }

	default Destination2DO selectByParentIdAndName(Long parentId, String name) {
	    return selectOne(Destination2DO::getParentId, parentId, Destination2DO::getName, name);
	}

    default Long selectCountByParentId(Long parentId) {
        return selectCount(Destination2DO::getParentId, parentId);
    }

}