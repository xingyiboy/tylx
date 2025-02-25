package cn.iocoder.yudao.module.tylx.controller.admin.destination.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;
import cn.iocoder.yudao.module.tylx.dal.dataobject.destinationfood.DestinationFoodDO;
import cn.iocoder.yudao.module.tylx.dal.dataobject.destinationscenery.DestinationSceneryDO;

@Schema(description = "管理后台 - 目的地新增/修改 Request VO")
@Data
public class DestinationSaveReqVO {

    @Schema(description = "用户ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "29364")
    private Long id;

    @Schema(description = "目的地名称", example = "芋艿")
    private String name;

    @Schema(description = "首页图片")
    private String picture;

    @Schema(description = "目的地描述")
    private String detail;

    @Schema(description = "特色美食列表")
    private List<DestinationFoodDO> destinationFoods;

    @Schema(description = "热门景点列表")
    private List<DestinationSceneryDO> destinationScenerys;

}