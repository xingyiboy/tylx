package cn.iocoder.yudao.module.tylx.service.destination2;

import java.util.*;
import javax.validation.*;
import cn.iocoder.yudao.module.tylx.controller.admin.destination2.vo.*;
import cn.iocoder.yudao.module.tylx.dal.dataobject.destination2.Destination2DO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

/**
 * 目的地树表 Service 接口
 *
 * @author 管理员
 */
public interface Destination2Service {

    /**
     * 创建目的地树表
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createDestination2(@Valid Destination2SaveReqVO createReqVO);

    /**
     * 更新目的地树表
     *
     * @param updateReqVO 更新信息
     */
    void updateDestination2(@Valid Destination2SaveReqVO updateReqVO);

    /**
     * 删除目的地树表
     *
     * @param id 编号
     */
    void deleteDestination2(Long id);

    /**
     * 获得目的地树表
     *
     * @param id 编号
     * @return 目的地树表
     */
    Destination2DO getDestination2(Long id);

    /**
     * 获得目的地树表列表
     *
     * @param listReqVO 查询条件
     * @return 目的地树表列表
     */
    List<Destination2DO> getDestination2List(Destination2ListReqVO listReqVO);

}