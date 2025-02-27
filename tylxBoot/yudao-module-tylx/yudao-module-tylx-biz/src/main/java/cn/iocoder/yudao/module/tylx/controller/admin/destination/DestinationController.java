package cn.iocoder.yudao.module.tylx.controller.admin.destination;

import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Operation;

import javax.validation.constraints.*;
import javax.validation.*;
import javax.servlet.http.*;
import java.util.*;
import java.io.IOException;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;

import cn.iocoder.yudao.framework.apilog.core.annotation.ApiAccessLog;
import static cn.iocoder.yudao.framework.apilog.core.enums.OperateTypeEnum.*;

import cn.iocoder.yudao.module.tylx.controller.admin.destination.vo.*;
import cn.iocoder.yudao.module.tylx.dal.dataobject.destination.DestinationDO;
import cn.iocoder.yudao.module.tylx.dal.dataobject.destinationfood.DestinationFoodDO;
import cn.iocoder.yudao.module.tylx.dal.dataobject.destinationscenery.DestinationSceneryDO;
import cn.iocoder.yudao.module.tylx.service.destination.DestinationService;

@Tag(name = "管理后台 - 目的地")
@RestController
@RequestMapping("/tylx/destination")
@Validated
public class DestinationController {

    @Resource
    private DestinationService destinationService;

    @PostMapping("/create")
    @Operation(summary = "创建目的地")
    public CommonResult<Long> createDestination(@Valid @RequestBody DestinationSaveReqVO createReqVO) {
        return success(destinationService.createDestination(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新目的地")
    public CommonResult<Boolean> updateDestination(@Valid @RequestBody DestinationSaveReqVO updateReqVO) {
        destinationService.updateDestination(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除目的地")
    @Parameter(name = "id", description = "编号", required = true)
    public CommonResult<Boolean> deleteDestination(@RequestParam("id") Long id) {
        destinationService.deleteDestination(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得目的地")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    public CommonResult<DestinationRespVO> getDestination(@RequestParam("id") Long id) {
        DestinationDO destination = destinationService.getDestination(id);
        return success(BeanUtils.toBean(destination, DestinationRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得目的地分页")
    public CommonResult<PageResult<DestinationRespVO>> getDestinationPage(@Valid DestinationPageReqVO pageReqVO) {
        PageResult<DestinationDO> pageResult = destinationService.getDestinationPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, DestinationRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出目的地 Excel")
    @ApiAccessLog(operateType = EXPORT)
    public void exportDestinationExcel(@Valid DestinationPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<DestinationDO> list = destinationService.getDestinationPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "目的地.xls", "数据", DestinationRespVO.class,
                        BeanUtils.toBean(list, DestinationRespVO.class));
    }

    // ==================== 子表（特色美食） ====================

    @GetMapping("/destination-food/list-by-destination-id")
    @Operation(summary = "获得特色美食列表")
    @Parameter(name = "destinationId", description = "目的地编号")
    public CommonResult<List<DestinationFoodDO>> getDestinationFoodListByDestinationId(@RequestParam("destinationId") Long destinationId) {
        return success(destinationService.getDestinationFoodListByDestinationId(destinationId));
    }

    // ==================== 子表（热门景点） ====================

    @GetMapping("/destination-scenery/list-by-destination-id")
    @Operation(summary = "获得热门景点列表")
    @Parameter(name = "destinationId", description = "目的地编号")
    public CommonResult<List<DestinationSceneryDO>> getDestinationSceneryListByDestinationId(@RequestParam("destinationId") Long destinationId) {
        return success(destinationService.getDestinationSceneryListByDestinationId(destinationId));
    }

    @GetMapping("/getScenery")
    @Operation(summary = "获得景点")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    public CommonResult<DestinationSceneryDO> getScenery(@RequestParam("id") Long id) {
        return destinationService.getScenery(id);
    }
    @GetMapping("/getFood")
    @Operation(summary = "获得美食")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    public CommonResult<DestinationFoodDO> getFood(@RequestParam("id") Long id) {
        return destinationService.getFood(id);
    }

    @GetMapping("/pageScenery")
    @Operation(summary = "获得景点分页")
    public CommonResult<PageResult<DestinationSceneryDO>> getDestinationPageScenery(@Valid DestinationPageReqVO pageReqVO) {
        PageResult<DestinationSceneryDO> pageResult = destinationService.getDestinationPageScenery(pageReqVO);
        return success(pageResult);
    }

}
