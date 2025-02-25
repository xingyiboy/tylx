package cn.iocoder.yudao.module.tylx.dal.mysql.forum;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.tylx.dal.dataobject.forum.ForumDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.tylx.controller.admin.forum.vo.*;

/**
 * 论坛 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface ForumMapper extends BaseMapperX<ForumDO> {

    default PageResult<ForumDO> selectPage(ForumPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<ForumDO>()
                .betweenIfPresent(ForumDO::getCreateTime, reqVO.getCreateTime())
                .eqIfPresent(ForumDO::getPicture, reqVO.getPicture())
                .likeIfPresent(ForumDO::getTitle, reqVO.getTitle())
                .likeIfPresent(ForumDO::getContent, reqVO.getContent())
                .likeIfPresent(ForumDO::getIntroduce, reqVO.getIntroduce())
                .eqIfPresent(ForumDO::getUserId, reqVO.getUserId())
                .eqIfPresent(ForumDO::getDestinationId, reqVO.getDestinationId())
                .eqIfPresent(ForumDO::getView, reqVO.getView())
                .eqIfPresent(ForumDO::getLikes, reqVO.getLikes())
                .eqIfPresent(ForumDO::getComment, reqVO.getComment())
                .eqIfPresent(ForumDO::getIsHome, reqVO.getIsHome())
                .orderByDesc(ForumDO::getId));
    }

}