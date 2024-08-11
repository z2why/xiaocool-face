package com.cool.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.cool.admin.mapper.UserDetailMapper;
import com.cool.admin.service.IUserForDetailService;
import entity.UserDetail;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author
 * @since
 */
@Service
public class UserForDetailServiceImpl extends ServiceImpl<UserDetailMapper, UserDetail> implements IUserForDetailService {
    @Resource
    private UserDetailMapper userDetailMapper;

    @Override
    public void delete(String meetingId){
        QueryWrapper<UserDetail> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("meeting_id",meetingId);
        List<UserDetail> meetingMembers= userDetailMapper.selectList(queryWrapper);
        if (meetingMembers.size()!=0){
            List<Long> ids = meetingMembers.stream().map(UserDetail::getId).collect(Collectors.toList());
            userDetailMapper.deleteBatchIds(ids);
        }
    }

    @Override
    public void finishMeeting(String meetingId,String signOutDate){
        userDetailMapper.finishMeeting(meetingId,signOutDate);
    }



}
