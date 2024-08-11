package com.cool.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import entity.UserDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author
 * @since
 */
@Mapper
public interface UserDetailMapper extends BaseMapper<UserDetail> {

    int  finishMeeting(@Param("meetingId") String meetingId,@Param("signOutDate")String signOutDate);
}
