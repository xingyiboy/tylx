package cn.iocoder.yudao.module.tylx.service.destination;

import java.util.*;
import javax.validation.*;
import cn.iocoder.yudao.module.tylx.controller.admin.destination.vo.*;
import cn.iocoder.yudao.module.tylx.dal.dataobject.destination.DestinationDO;
import cn.iocoder.yudao.module.tylx.dal.dataobject.destinationfood.DestinationFoodDO;
import cn.iocoder.yudao.module.tylx.dal.dataobject.destinationscenery.DestinationSceneryDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

/**
 * 目的地 Service 接口
 *
 * @author 芋道源码
 */
public interface DestinationService {

    /**
     * 创建目的地
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createDestination(@Valid DestinationSaveReqVO createReqVO);

    /**
     * 更新目的地
     *
     * @param updateReqVO 更新信息
     */
    void updateDestination(@Valid DestinationSaveReqVO updateReqVO);

    /**
     * 删除目的地
     *
     * @param id 编号
     */
    void deleteDestination(Long id);

    /**
     * 获得目的地
     *
     * @param id 编号
     * @return 目的地
     */
    DestinationDO getDestination(Long id);

    /**
     * 获得目的地分页
     *
     * @param pageReqVO 分页查询
     * @return 目的地分页
     */
    PageResult<DestinationDO> getDestinationPage(DestinationPageReqVO pageReqVO);

    // ==================== 子表（特色美食） ====================

    /**
     * 获得特色美食列表
     *
     * @param destinationId 目的地编号
     * @return 特色美食列表
     */
    List<DestinationFoodDO> getDestinationFoodListByDestinationId(Long destinationId);

    // ==================== 子表（热门景点） ====================

    /**
     * 获得热门景点列表
     *
     * @param destinationId 目的地编号
     * @return 热门景点列表
     */
    List<DestinationSceneryDO> getDestinationSceneryListByDestinationId(Long destinationId);

}