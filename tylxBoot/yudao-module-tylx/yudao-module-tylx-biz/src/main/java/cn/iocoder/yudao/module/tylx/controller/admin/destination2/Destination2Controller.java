package cn.iocoder.yudao.module.tylx.controller.admin.destination2;

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

import cn.iocoder.yudao.module.tylx.controller.admin.destination2.vo.*;
import cn.iocoder.yudao.module.tylx.dal.dataobject.destination2.Destination2DO;
import cn.iocoder.yudao.module.tylx.service.destination2.Destination2Service;

@Tag(name = "管理后台 - 目的地树表")
@RestController
@RequestMapping("/tylx/destination2")
@Validated
public class Destination2Controller {

    @Resource
    private Destination2Service destination2Service;

    @PostMapping("/create")
    @Operation(summary = "创建目的地树表")
    public CommonResult<Long> createDestination2(@Valid @RequestBody Destination2SaveReqVO createReqVO) {
        return success(destination2Service.createDestination2(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新目的地树表")
    public CommonResult<Boolean> updateDestination2(@Valid @RequestBody Destination2SaveReqVO updateReqVO) {
        destination2Service.updateDestination2(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除目的地树表")
    @Parameter(name = "id", description = "编号", required = true)
    public CommonResult<Boolean> deleteDestination2(@RequestParam("id") Long id) {
        destination2Service.deleteDestination2(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得目的地树表")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    public CommonResult<Destination2RespVO> getDestination2(@RequestParam("id") Long id) {
        Destination2DO destination2 = destination2Service.getDestination2(id);
        return success(BeanUtils.toBean(destination2, Destination2RespVO.class));
    }

    @GetMapping("/list")
    @Operation(summary = "获得目的地树表列表")
    public CommonResult<List<Destination2RespVO>> getDestination2List(@Valid Destination2ListReqVO listReqVO) {
        List<Destination2DO> list = destination2Service.getDestination2List(listReqVO);
        return success(BeanUtils.toBean(list, Destination2RespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出目的地树表 Excel")
    @ApiAccessLog(operateType = EXPORT)
    public void exportDestination2Excel(@Valid Destination2ListReqVO listReqVO,
              HttpServletResponse response) throws IOException {
        List<Destination2DO> list = destination2Service.getDestination2List(listReqVO);
        // 导出 Excel
        ExcelUtils.write(response, "目的地树表.xls", "数据", Destination2RespVO.class,
                        BeanUtils.toBean(list, Destination2RespVO.class));
    }

}
