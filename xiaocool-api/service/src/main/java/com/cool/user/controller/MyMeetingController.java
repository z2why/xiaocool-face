package com.cool.user.controller;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cool.admin.mapper.MeetingMapper;
import com.cool.admin.service.IUserForDetailService;
import com.cool.admin.service.IUserService;
import com.cool.admin.service.impl.MeetingServiceImpl;
import domain.Result;
import entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.annotation.Resource;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/AboutMeeting")
public class MyMeetingController {
    @Resource
    private MeetingServiceImpl MeetingService;

    @Resource
    private IUserForDetailService userForDetailService;

    @Resource
    private IUserService userService;

    @Resource
    private MeetingMapper meetingMapper;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;//操作key-value都是字符串


    @PostMapping("/finishMeeting")
    public Result finishMeeting(@RequestBody UserDetail userDetail){
           userForDetailService.finishMeeting(userDetail.getMeetingId().toString(), userDetail.getSignOutDate().toString());
           return Result.success("会议已结束");
    }


    //保存与更新
    @PostMapping
    public Result save(@RequestBody UserDetail userDetail) {

        return Result.success(userForDetailService.saveOrUpdate(userDetail));
    }
    //签到率
    @GetMapping("/SignNum")
    public Result SignNum(@RequestParam String meetingId) {
        QueryWrapper<UserDetail> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("meeting_id",meetingId);
        Long allcount=userForDetailService.count(queryWrapper);
        queryWrapper.eq("participate_status",0);
        Long signcount=userForDetailService.count(queryWrapper);
        Map<String,Long> map=new HashMap<>();
        map.put("allcount",allcount);
        map.put("signcount",signcount);
        return Result.success(map);
    }



    @GetMapping("/findMember")
    public Result selectMemberPage(
            @RequestParam Integer pageNum,
            @RequestParam Integer pageSize,
            @RequestParam(defaultValue = "") String code,
            @RequestParam(defaultValue = "") String userId,
            @RequestParam(defaultValue = "") String createUser,
            @RequestParam(defaultValue = "") String checkStatus

    ) {
        IPage<MeetingMember> MeetingMembers = MeetingService.selectMemberPage(pageNum, pageSize, code, createUser,userId,checkStatus);
        return Result.success(MeetingMembers);
    }



    @GetMapping("/findMeeting")
    public Object selectUserMeetingPage(
            @RequestParam Integer pageNum,
            @RequestParam Integer pageSize,
            @RequestParam(defaultValue = "") String code,
            @RequestParam(defaultValue = "") String userId,
            @RequestParam(defaultValue = "") String participateStatus,
            @RequestParam(defaultValue = "") String startDate,
            @RequestParam(defaultValue = "") String supplementarySign,
            @RequestParam(defaultValue = "") String myMeeting
    ) {

      List<UserMeeting> userMeetings = MeetingService.selectUserMeetingPage(pageNum, pageSize, code, userId, participateStatus,startDate, supplementarySign,myMeeting);
        return userMeetings;
    }

    @GetMapping("/signMeeting")
    public Object selectSignMeetingPage(
            @RequestParam Integer pageNum,
            @RequestParam Integer pageSize,
            @RequestParam(defaultValue = "") String userId,
            @RequestParam(defaultValue = "") String meetingId

    ) {
        List<UserMeeting> userMeetings = MeetingService.selectSignMeetingPage(pageNum, pageSize, userId, meetingId);
        return userMeetings;
    }




    @GetMapping("/join")
    @Transactional
    public Result joinMeeting(@RequestParam(defaultValue = "") String code,
                              @RequestParam(defaultValue = "") String userId
    ) {
        if (!"".equals(code) && !"".equals(userId)) {
            MeetingMember meeting = MeetingService.selectMeeting(code, userId);
            if (meeting != null) {
                System.out.print(meeting);
                return Result.success(meeting);
            } else {
                QueryWrapper<Meeting> wrapper = new QueryWrapper<>();
                wrapper.eq("`show`",1);
                MeetingService.getOne(wrapper.eq("code",code));
                Meeting meeting1 = MeetingService.getOne(wrapper);
                if (meeting1 != null) {
                    return Result.success(meeting1);}
                return Result.error("500", "会议号不存在！");
            }
        }
        return Result.error("500", "请填写会议号!");
    }


    //手动签到，获得地址
    @PostMapping("/manualSign")
    public Result manualSign(@RequestBody UserDetail userDetail){
        userForDetailService.saveOrUpdate(userDetail);
        return Result.success();
    }


    @GetMapping("/getUdid")
    public Result getUdid(@RequestParam String userId,@RequestParam String meetingId){
        QueryWrapper<UserDetail> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("user_id",userId);
        queryWrapper.eq("meeting_id",meetingId);
        Long udid=userForDetailService.getOne(queryWrapper).getId();

        return Result.success(udid);
    }

    //线下签到
    @GetMapping("/AllSign")
    public Result AllSign(@RequestParam String name,@RequestParam String code){
        LocalDateTime localDateTime = LocalDateTime.now();
        //时间转字符串
        String format = localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        LocalDateTime dateTime = LocalDateTime.parse(format, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        QueryWrapper<User> queryWrapper=new QueryWrapper<>();
        QueryWrapper<Meeting> wra=new QueryWrapper<>();
        wra.eq("code",code);
        Meeting meeting=meetingMapper.selectOne(wra);
        queryWrapper.eq("name",name);
        try {
            User user=userService.getOne(queryWrapper);
            if (user!=null){
                QueryWrapper<UserDetail> wrapper=new QueryWrapper<>();
                wrapper.eq("user_id",user.getId());
                wrapper.eq("meeting_id",meeting.getId());
                UserDetail userDetail = userForDetailService.getOne(wrapper);
                if (userDetail!=null){
                    if (userDetail.getParticipateStatus()==0){

                    if (meeting.getStartDate().isBefore(dateTime)){     //会议开始时间在当前时间之前
                        userDetail.setParticipateStatus(2);
                        userDetail.setSignInDate(dateTime);
                        userForDetailService.saveOrUpdate(userDetail);
                        return Result.success(user);
                    }
                    userDetail.setParticipateStatus(1);
                    userDetail.setSignInDate(dateTime);
                    userForDetailService.saveOrUpdate(userDetail);
                    return Result.success(user);
                }
                    return Result.error("500","您已签到,请勿重复签到");
                }
                return Result.error("500","您非本会议成员！请先报名");

            }

            return Result.error("500","有误");

        }
        catch (Exception e) {
            e.printStackTrace();
        }
       return Result.error("500","有误");
    }




    //删除缓存
    private void RefreshRedis(String key){
        stringRedisTemplate.delete(key);
    }

    //导入
    @PostMapping("/import")
    public Result imp(MultipartFile file,
                      @RequestParam Long meetingId) throws Exception {
        InputStream in = file.getInputStream();
//        //io流给ExcelReader
        ExcelReader excelReader=ExcelUtil.getReader(in);
        //2.第二种导入方式
        //忽略第一行头(第一行是中文的情况),直接读取表的内容
        List<List<Object>> list = excelReader.read(1);
        List<UserDetail> listUser = CollUtil.newArrayList();
        for (List<Object> row: list) {
            User user =new User();
            QueryWrapper<User> wrapper=new QueryWrapper<>();
            wrapper.eq("id", Long.valueOf(row.get(0).toString()));
            wrapper.eq("name", row.get(1));
            user=userService.getOne(wrapper);
            if (user!=null){
            UserDetail userDetail=new UserDetail();
            QueryWrapper<UserDetail> queryWrapper=new QueryWrapper<>();
            queryWrapper.eq("meeting_id",meetingId);
            queryWrapper.eq("user_id", Long.valueOf(row.get(0).toString()));
            userDetail=userForDetailService.getOne(queryWrapper);
            if (userDetail==null){
                userDetail=new UserDetail();
                userDetail.setMeetingId(meetingId);
                userDetail.setCheckStatus(1L);
                userDetail.setUserId(Long.valueOf(row.get(0).toString()));
                listUser.add(userDetail);
            }
            if(userDetail.getCheckStatus()==0) {
                userDetail.setCheckStatus(1L);
                listUser.add(userDetail);
            }
        }
        }
        //批量注册进数据库
        userForDetailService.saveOrUpdateBatch(listUser);
        return Result.success("添加成功");
    }






}
