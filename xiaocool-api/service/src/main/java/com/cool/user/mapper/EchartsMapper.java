package com.cool.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import entity.UserDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface EchartsMapper extends BaseMapper<UserDetail> {

    @Select("SELECT count(*) from user_detail")
    Integer Allnum(@Param("value")String value);

    @Select("SELECT count(*) FROM user_detail WHERE participate_status=1 AND meeting_id=#{meetingId}")
    Integer ALRnum(@Param("alrnum")String alrnum,String meetingId);

    @Select("SELECT count(*) FROM user_detail WHERE participate_status=2 AND meeting_id=#{meetingId}")
    Integer LATERnum(@Param("later")String later,String meetingId);

    @Select("SELECT count(*) FROM user_detail WHERE participate_status=3 AND meeting_id=#{meetingId}")
    Integer Earlynum(@Param("early")String early,String meetingId);
}
