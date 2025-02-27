package cn.iocoder.yudao.module.tylx.service.destination;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import cn.iocoder.yudao.module.tylx.controller.admin.destination.vo.*;
import cn.iocoder.yudao.module.tylx.dal.dataobject.destination.DestinationDO;
import cn.iocoder.yudao.module.tylx.dal.dataobject.destinationfood.DestinationFoodDO;
import cn.iocoder.yudao.module.tylx.dal.dataobject.destinationscenery.DestinationSceneryDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import cn.iocoder.yudao.module.tylx.dal.mysql.destination.DestinationMapper;
import cn.iocoder.yudao.module.tylx.dal.mysql.destinationfood.DestinationFoodMapper;
import cn.iocoder.yudao.module.tylx.dal.mysql.destinationscenery.DestinationSceneryMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.tylx.enums.ErrorCodeConstants.*;

/**
 * 目的地 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class DestinationServiceImpl implements DestinationService {

    @Resource
    private DestinationMapper destinationMapper;
    @Resource
    private DestinationFoodMapper destinationFoodMapper;
    @Resource
    private DestinationSceneryMapper destinationSceneryMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createDestination(DestinationSaveReqVO createReqVO) {
        // 插入
        DestinationDO destination = BeanUtils.toBean(createReqVO, DestinationDO.class);
        destinationMapper.insert(destination);

        // 插入子表
        createDestinationFoodList(destination.getId(), createReqVO.getDestinationFoods());
        createDestinationSceneryList(destination.getId(), createReqVO.getDestinationScenerys());
        // 返回
        return destination.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateDestination(DestinationSaveReqVO updateReqVO) {
        // 校验存在
        validateDestinationExists(updateReqVO.getId());
        // 更新
        DestinationDO updateObj = BeanUtils.toBean(updateReqVO, DestinationDO.class);
        destinationMapper.updateById(updateObj);

        // 更新子表
        updateDestinationFoodList(updateReqVO.getId(), updateReqVO.getDestinationFoods());
        updateDestinationSceneryList(updateReqVO.getId(), updateReqVO.getDestinationScenerys());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteDestination(Long id) {
        // 校验存在
        validateDestinationExists(id);
        // 删除
        destinationMapper.deleteById(id);

        // 删除子表
        deleteDestinationFoodByDestinationId(id);
        deleteDestinationSceneryByDestinationId(id);
    }

    private void validateDestinationExists(Long id) {
        if (destinationMapper.selectById(id) == null) {
            throw exception(DESTINATION_NOT_EXISTS);
        }
    }

    @Override
    public DestinationDO getDestination(Long id) {
        return destinationMapper.selectById(id);
    }

    @Override
    public PageResult<DestinationDO> getDestinationPage(DestinationPageReqVO pageReqVO) {
        return destinationMapper.selectPage(pageReqVO);
    }
    @Override
    public PageResult<DestinationSceneryDO> getDestinationPageScenery(DestinationPageReqVO pageReqVO) {
        LambdaQueryWrapper<DestinationSceneryDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(DestinationSceneryDO::getIsHome,1);
        PageParam pageParam = new PageParam();
        pageParam.setPageNo(pageReqVO.getPageNo());
        pageParam.setPageSize(pageReqVO.getPageSize());
        return destinationSceneryMapper.selectPage(pageParam,wrapper);
    }

    @Override
    public CommonResult<DestinationSceneryDO> getScenery(Long id) {
        return CommonResult.success(destinationSceneryMapper.selectById(id));
    }

    @Override
    public CommonResult<DestinationFoodDO> getFood(Long id) {
        return CommonResult.success(destinationFoodMapper.selectById(id));
    }

    // ==================== 子表（特色美食） ====================

    @Override
    public List<DestinationFoodDO> getDestinationFoodListByDestinationId(Long destinationId) {
        return destinationFoodMapper.selectListByDestinationId(destinationId);
    }

    private void createDestinationFoodList(Long destinationId, List<DestinationFoodDO> list) {
        list.forEach(o -> o.setDestinationId(destinationId));
        destinationFoodMapper.insertBatch(list);
    }

    private void updateDestinationFoodList(Long destinationId, List<DestinationFoodDO> list) {
        deleteDestinationFoodByDestinationId(destinationId);
		list.forEach(o -> o.setId(null).setUpdater(null).setUpdateTime(null)); // 解决更新情况下：1）id 冲突；2）updateTime 不更新
        createDestinationFoodList(destinationId, list);
    }

    private void deleteDestinationFoodByDestinationId(Long destinationId) {
        destinationFoodMapper.deleteByDestinationId(destinationId);
    }

    // ==================== 子表（热门景点） ====================

    @Override
    public List<DestinationSceneryDO> getDestinationSceneryListByDestinationId(Long destinationId) {
        return destinationSceneryMapper.selectListByDestinationId(destinationId);
    }



    private void createDestinationSceneryList(Long destinationId, List<DestinationSceneryDO> list) {
        list.forEach(o -> o.setDestinationId(destinationId));
        destinationSceneryMapper.insertBatch(list);
    }

    private void updateDestinationSceneryList(Long destinationId, List<DestinationSceneryDO> list) {
        deleteDestinationSceneryByDestinationId(destinationId);
		list.forEach(o -> o.setId(null).setUpdater(null).setUpdateTime(null)); // 解决更新情况下：1）id 冲突；2）updateTime 不更新
        createDestinationSceneryList(destinationId, list);
    }

    private void deleteDestinationSceneryByDestinationId(Long destinationId) {
        destinationSceneryMapper.deleteByDestinationId(destinationId);
    }

}
