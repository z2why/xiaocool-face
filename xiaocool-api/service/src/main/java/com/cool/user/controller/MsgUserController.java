package com.cool.user.controller;

import com.cool.admin.mapper.MessageMapper;
import com.cool.admin.service.impl.MsgUserServiceImpl;
import domain.Result;
import entity.Message;
import entity.MsgUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/message")
public class MsgUserController {
    @Resource
    private MsgUserServiceImpl msgUserService;
    @Resource
    private MessageMapper messageMapper;

//    @PostMapping
//    public Result save(@RequestBody Message message) {
//        QueryWrapper<Message> queryWrapper=new QueryWrapper<>();
//        queryWrapper.eq("user_id",message.getUserId());
//        queryWrapper.eq("meeting_id",message.getMeetingId());
//        if (messageMapper.selectOne(queryWrapper)!=null){
//        return Result.success(messageMapper.update(message,queryWrapper));
//    }
//      return Result.success(messageMapper.insert(message));
//
//    }


    @PostMapping
    public Result save(@RequestBody Message message) {
        return Result.success(messageMapper.insert(message));
    }

    @GetMapping("/getMsg")
    public Result getMsg(@RequestParam String meetingId){
        List<MsgUser> UserMsgs = msgUserService.selectUserMsgs(meetingId);
        return Result.success(UserMsgs);
    }



}
