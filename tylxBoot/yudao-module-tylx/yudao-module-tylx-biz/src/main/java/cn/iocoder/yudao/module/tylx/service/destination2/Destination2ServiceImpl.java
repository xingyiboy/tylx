package cn.iocoder.yudao.module.tylx.service.destination2;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import cn.iocoder.yudao.module.tylx.controller.admin.destination2.vo.*;
import cn.iocoder.yudao.module.tylx.dal.dataobject.destination2.Destination2DO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import cn.iocoder.yudao.module.tylx.dal.mysql.destination2.Destination2Mapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.tylx.enums.ErrorCodeConstants.*;

/**
 * 目的地树表 Service 实现类
 *
 * @author 管理员
 */
@Service
@Validated
public class Destination2ServiceImpl implements Destination2Service {

    @Resource
    private Destination2Mapper destination2Mapper;

    @Override
    public Long createDestination2(Destination2SaveReqVO createReqVO) {
        // 校验父级编号的有效性
        validateParentDestination2(null, createReqVO.getParentId());
        // 校验目的地名称的唯一性
        validateDestination2NameUnique(null, createReqVO.getParentId(), createReqVO.getName());

        // 插入
        Destination2DO destination2 = BeanUtils.toBean(createReqVO, Destination2DO.class);
        destination2Mapper.insert(destination2);
        // 返回
        return destination2.getId();
    }

    @Override
    public void updateDestination2(Destination2SaveReqVO updateReqVO) {
        // 校验存在
        validateDestination2Exists(updateReqVO.getId());
        // 校验父级编号的有效性
        validateParentDestination2(updateReqVO.getId(), updateReqVO.getParentId());
        // 校验目的地名称的唯一性
        validateDestination2NameUnique(updateReqVO.getId(), updateReqVO.getParentId(), updateReqVO.getName());

        // 更新
        Destination2DO updateObj = BeanUtils.toBean(updateReqVO, Destination2DO.class);
        destination2Mapper.updateById(updateObj);
    }

    @Override
    public void deleteDestination2(Long id) {
        // 校验存在
        validateDestination2Exists(id);
        // 校验是否有子目的地树表
        if (destination2Mapper.selectCountByParentId(id) > 0) {
            throw exception(DESTINATION2_EXITS_CHILDREN);
        }
        // 删除
        destination2Mapper.deleteById(id);
    }

    private void validateDestination2Exists(Long id) {
        if (destination2Mapper.selectById(id) == null) {
            throw exception(DESTINATION2_NOT_EXISTS);
        }
    }

    private void validateParentDestination2(Long id, Long parentId) {
        if (parentId == null || Destination2DO.PARENT_ID_ROOT.equals(parentId)) {
            return;
        }
        // 1. 不能设置自己为父目的地树表
        if (Objects.equals(id, parentId)) {
            throw exception(DESTINATION2_PARENT_ERROR);
        }
        // 2. 父目的地树表不存在
        Destination2DO parentDestination2 = destination2Mapper.selectById(parentId);
        if (parentDestination2 == null) {
            throw exception(DESTINATION2_PARENT_NOT_EXITS);
        }
        // 3. 递归校验父目的地树表，如果父目的地树表是自己的子目的地树表，则报错，避免形成环路
        if (id == null) { // id 为空，说明新增，不需要考虑环路
            return;
        }
        for (int i = 0; i < Short.MAX_VALUE; i++) {
            // 3.1 校验环路
            parentId = parentDestination2.getParentId();
            if (Objects.equals(id, parentId)) {
                throw exception(DESTINATION2_PARENT_IS_CHILD);
            }
            // 3.2 继续递归下一级父目的地树表
            if (parentId == null || Destination2DO.PARENT_ID_ROOT.equals(parentId)) {
                break;
            }
            parentDestination2 = destination2Mapper.selectById(parentId);
            if (parentDestination2 == null) {
                break;
            }
        }
    }

    private void validateDestination2NameUnique(Long id, Long parentId, String name) {
        Destination2DO destination2 = destination2Mapper.selectByParentIdAndName(parentId, name);
        if (destination2 == null) {
            return;
        }
        // 如果 id 为空，说明不用比较是否为相同 id 的目的地树表
        if (id == null) {
            throw exception(DESTINATION2_NAME_DUPLICATE);
        }
        if (!Objects.equals(destination2.getId(), id)) {
            throw exception(DESTINATION2_NAME_DUPLICATE);
        }
    }

    @Override
    public Destination2DO getDestination2(Long id) {
        return destination2Mapper.selectById(id);
    }

    @Override
    public List<Destination2DO> getDestination2List(Destination2ListReqVO listReqVO) {
        return destination2Mapper.selectList(listReqVO);
    }

}