package com.cool.admin.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cool.admin.mapper.MeetingMapper;
import com.cool.admin.service.IMeetingService;
import com.cool.admin.service.IMsgUserService;
import entity.Meeting;
import entity.MeetingMember;
import entity.UserMeeting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author
 * @since
 */
@Service
public class MeetingServiceImpl extends ServiceImpl<MeetingMapper, Meeting> implements IMeetingService {

    @Resource
    private MeetingMapper meetingMapper;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;//操作key-value都是字符串

    LocalDateTime t1 =LocalDateTime.now();
    String t0= t1.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
//    String t2= t1.plusDays(2).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

    public Page<Meeting> findPage(Page<Meeting> page, String code) {
        return meetingMapper.findPage(page, code);
    }



    public IPage<MeetingMember>
     selectMemberPage(int pageNum, int pageSize,String code, String createUser,String userId,String checkStatus) {
        QueryWrapper<Meeting> wrapper  = new QueryWrapper<>();
        Page<MeetingMember> page = new Page<>(pageNum, pageSize, true);
        if (!"".equals(code)) {
            wrapper.eq("code", code);
            wrapper.eq("`show`",1);
        }
        if (!"".equals(userId)) {
            wrapper.eq("user_id", userId);
        }
        if (!"".equals(checkStatus)) {
            wrapper.eq("check_status", checkStatus);
        }
       if(!"".equals(createUser)){
           wrapper.eq("create_user",createUser);
           wrapper.ne("participate_status",1);
//           wrapper.eq("stage",2);
           wrapper.eq("supplementary_sign",1);

       }

        wrapper.select().orderByDesc("participate_status");

        //        List<MeetingMember> records = MemberPage.getRecords();
        return meetingMapper.selectMemberPage(page, wrapper);
    }


    //报名页面
    public List<UserMeeting>
        selectSignMeetingPage(int pageNum, int pageSize,
                              String userId, String meetingId){
        QueryWrapper<Meeting> wrapper  = new QueryWrapper<>();
        Page<UserMeeting> page = new Page<>(pageNum, pageSize, false);

        if (!"".equals(meetingId)) {
            wrapper.ge("start_date", t0);
            wrapper.eq("`show`",1);
            wrapper.ne("`stage`",2);
            wrapper.notInSql("id", "SELECT meeting_id FROM user_detail WHERE user_id = " + userId + " AND `check_status` = 1");
                    }

        wrapper.eq("`show`",1);
        IPage<UserMeeting> UserMeetingPage = meetingMapper.selectSignMeetingPage(page, wrapper);
        return UserMeetingPage.getRecords();
    }



    public List<UserMeeting>
    selectUserMeetingPage(int pageNum, int pageSize, String code, String userId,
                                                     String participateStatus, String startDate,
                                                     String supplementarySign, String myMeeting) {
        QueryWrapper<Meeting> wrapper  = new QueryWrapper<>();
        Page<UserMeeting> page = new Page<>(pageNum, pageSize, false);
        if (!"".equals(code)) {
            wrapper.like("code", code);
        }

        if (!"".equals(myMeeting)){
            wrapper.select("distinct(meeting_id)");


        if (!"".equals(participateStatus) ) {
            wrapper.ne("participate_status", participateStatus);
        }

        if (!"".equals(startDate)) {
             wrapper.ge("start_date", t0);
        }

        if (!"".equals(userId)) {
             wrapper.eq("user_id", userId);
        }
            wrapper.select().orderByDesc("create_time");


        }

        if (!"".equals(supplementarySign)){
            wrapper.le("start_date", t0);
        }

        wrapper.eq("`show`",1);
        IPage<UserMeeting> UserMeetingPage = meetingMapper.selectUserMeetingPage(page, wrapper);
        List<UserMeeting> records = UserMeetingPage.getRecords();
        return records;
    }



    public MeetingMember
    selectMeeting(String code, String userId) {
        QueryWrapper<Meeting> wrapper  = new QueryWrapper<>();
        if (!"".equals(code)) {
            wrapper.eq("code", code);
        }
        if (!"".equals(userId)) {
            wrapper.eq("user_id", userId);
        }
        wrapper.eq("`show`",1);
        MeetingMember l1 = meetingMapper.selectMeeting(wrapper);
        return l1;
    }




}

