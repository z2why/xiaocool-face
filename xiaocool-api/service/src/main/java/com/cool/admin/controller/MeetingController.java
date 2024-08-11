package com.cool.admin.controller;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cool.admin.service.impl.MeetingServiceImpl;
import com.cool.admin.service.impl.MsgUserServiceImpl;
import com.cool.admin.service.impl.UserForDetailServiceImpl;
import domain.Result;
import entity.Meeting;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.List;

/**
 *会议对象相关接口
 * @author
 * @since
 */
@Slf4j
@RestController
@RequestMapping("/meeting/MyMeeting")
public class MeetingController {

    @Resource
    private MeetingServiceImpl meetingService;
    @Resource
    private MsgUserServiceImpl msgUserService;
    @Resource
    private UserForDetailServiceImpl userForDetailService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;//操作key-value都是字符串

    //保存与更新
    @PostMapping
    public Result save(@RequestBody Meeting meeting ){
//        meetingService.saveOrUpdate(meeting);
//        RefreshRedis(MEETING_KEY);
        return Result.success(meetingService.saveOrUpdate(meeting));
    }

    //查询所有数据
    @GetMapping
    public Result findAll(){
        return Result.success(meetingService.list());
    }


    //删除数据
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id){
        userForDetailService.delete(id.toString());
        msgUserService.delete(id.toString());
        meetingService.removeById(id);
        return Result.success();

    }

    @GetMapping("/{id}")
    public Result findOne(@PathVariable Integer id){
//        meetingService.getById(id);
//        RefreshRedis(MEETING_KEY);

        return Result.success(meetingService.getById(id));

    }

    @PostMapping ("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids){

        return  Result.success(meetingService.removeByIds(ids));

    }

    @GetMapping("/type")
    public Result findBycreateUser(@RequestParam(defaultValue = "") String  createUser) {
        QueryWrapper<Meeting> queryWrapper = new QueryWrapper<>();
        if (!"".equals(createUser)) {
            queryWrapper.eq("create_user", createUser)
                    .last("order by id desc");

        }
        return Result.success(meetingService.list(queryWrapper));
    }



    //分页查询
    @GetMapping("/page")
    public Result findPage(
            @RequestParam Integer pageNum,
            @RequestParam Integer pageSize,
            @RequestParam(defaultValue = "") String code
    ){
        IPage<Meeting> page = new Page<>(pageNum,pageSize);
        QueryWrapper<Meeting> queryWarpper =new QueryWrapper<>();
        if (!"".equals(code)){
            queryWarpper.like("code",code);
        }
        queryWarpper.orderByAsc("`show`").orderByDesc("start_date");

        return Result.success(meetingService.page(page,queryWarpper));
    }


    //    导入和导出
    @GetMapping("/export")
    public void export(HttpServletResponse response) throws Exception {
        // 从数据库查询出所有的数据
        List<Meeting> list = meetingService.list();
        // 通过工具类创建writer 写出到磁盘路径
//        ExcelWriter writer = ExcelUtil.getWriter(filesUploadPath + "/用户信息.xlsx");
        // 在内存操作，写出到浏览器
        ExcelWriter writer = ExcelUtil.getWriter(true);
        //自定义标题别名
        writer.addHeaderAlias("name", "会议名称");
        writer.addHeaderAlias("code", "会议编号");
        writer.addHeaderAlias("stage", "会议状态");
        writer.addHeaderAlias("address", "地点");
        writer.addHeaderAlias("startDate", "开始时间");
        writer.addHeaderAlias("endDate", "结束时间");
        writer.addHeaderAlias("show", "展示状态");
        writer.addHeaderAlias("createUser", "会议发起人");

        // 一次性写出list内的对象到excel，使用默认样式，强制输出标题
        writer.write(list, true);

        // 设置浏览器响应的格式
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("用户信息", "UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");
        ServletOutputStream out = response.getOutputStream();
        writer.flush(out, true);
        out.close();
        writer.close();
    }

    //导入
    @PostMapping("/import")
    public Result imp(MultipartFile file) throws Exception {
        InputStream inputStream = file.getInputStream();
        ExcelReader reader = ExcelUtil.getReader(inputStream);
        // 方式1：(推荐) 通过 javabean的方式读取Excel内的对象，但是要求表头必须是英文，跟javabean的属性要对应起来
        List<Meeting> list = reader.readAll(Meeting.class);
        List<Meeting> Meetings = CollUtil.newArrayList();
        meetingService.saveBatch(Meetings);
        return Result.success(true);
    }


    //分页查询
    @GetMapping("/findCreate")
    public Result findCreate(
            @RequestParam Integer pageNum,
            @RequestParam Integer pageSize,
            @RequestParam(defaultValue = "") String createUser
    ){
        IPage<Meeting> page = new Page<>(pageNum,pageSize);
        QueryWrapper<Meeting> queryWarpper =new QueryWrapper<>();
        if (!"".equals(createUser)){
            queryWarpper.like("create_user",createUser);
        }
        queryWarpper.select().orderByDesc("create_time");
        queryWarpper.select().orderByDesc("`show`");
        return Result.success(meetingService.page(page,queryWarpper));
    }

    //删除缓存
    private void RefreshRedis(String key){
        stringRedisTemplate.delete(key);
    }
}
