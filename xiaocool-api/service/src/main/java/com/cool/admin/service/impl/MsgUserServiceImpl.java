package com.cool.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cool.admin.mapper.MessageMapper;
import com.cool.admin.mapper.MsgUserMapper;
import com.cool.admin.service.IMsgUserService;
import entity.Message;
import entity.MsgUser;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class MsgUserServiceImpl extends ServiceImpl<MsgUserMapper, MsgUser> implements IMsgUserService {
    @Resource
    private MsgUserMapper msgUserMapper;
    @Resource
    private MessageMapper messageMapper;

    public List<MsgUser>  selectUserMsgs(String meetingId) {
        QueryWrapper<MsgUser> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("meeting_id",meetingId);
        queryWrapper.orderByAsc("sendTime");
        return msgUserMapper.getMsg(queryWrapper);
    }



    public void delete(String meetingId){
        QueryWrapper<Message> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("meeting_id",meetingId);
        List<Message> messages=messageMapper.selectList(queryWrapper);
        if (messages.size()!=0){
            List<Integer> ids = messages.stream().map(Message::getMsgId).collect(Collectors.toList());
            messageMapper.deleteBatchIds(ids);
        }
    }

}