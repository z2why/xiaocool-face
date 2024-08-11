package com.cool.admin.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import entity.Meeting;
import entity.MeetingMember;
import entity.UserMeeting;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


@Mapper
public interface MeetingMapper extends BaseMapper<Meeting> {


     Page<Meeting> findPage(Page<Meeting> page, @Param("code") String code);

     IPage<UserMeeting> selectUserMeetingPage(Page<UserMeeting> page, @Param(Constants.WRAPPER) Wrapper<Meeting> wrapper);

     IPage<UserMeeting> selectSignMeetingPage(Page<UserMeeting> page, @Param(Constants.WRAPPER) Wrapper<Meeting> wrapper);

     IPage<MeetingMember> selectMemberPage(Page<MeetingMember> page, @Param(Constants.WRAPPER) Wrapper<Meeting> wrapper);

     MeetingMember selectMeeting(@Param(Constants.WRAPPER) Wrapper<Meeting> wrapper);

}
