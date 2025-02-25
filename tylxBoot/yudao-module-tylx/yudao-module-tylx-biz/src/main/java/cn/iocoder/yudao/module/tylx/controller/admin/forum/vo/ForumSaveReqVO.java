package cn.iocoder.yudao.module.tylx.controller.admin.forum.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;
import cn.iocoder.yudao.module.tylx.dal.dataobject.forumcomment.ForumCommentDO;
import cn.iocoder.yudao.module.tylx.dal.dataobject.forumlikes.ForumLikesDO;
import cn.iocoder.yudao.module.tylx.dal.dataobject.forumreward.ForumRewardDO;

@Schema(description = "管理后台 - 论坛新增/修改 Request VO")
@Data
public class ForumSaveReqVO {

    @Schema(description = "用户ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "19968")
    private Long id;

    @Schema(description = "首页图片")
    private String picture;

    @Schema(description = "首页标题")
    private String title;

    @Schema(description = "内容")
    private String content;

    @Schema(description = "简介")
    private String introduce;

    @Schema(description = "用户编号", example = "23932")
    private Long userId;

    @Schema(description = "目的地编号", example = "31938")
    private Long destinationId;

    @Schema(description = "阅读量")
    private Integer view;

    @Schema(description = "点赞量")
    private Integer likes;

    @Schema(description = "评论量")
    private Integer comment;

    @Schema(description = "是否精选")
    private Integer isHome;

    @Schema(description = "论坛评论列表")
    private List<ForumCommentDO> forumComments;

    @Schema(description = "论坛点赞列表")
    private List<ForumLikesDO> forumLikess;

    @Schema(description = "论坛打赏列表")
    private List<ForumRewardDO> forumRewards;

}