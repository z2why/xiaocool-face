package com.cool.user.controller;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.Quarter;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.cool.admin.service.impl.MeetingServiceImpl;
import com.cool.user.service.Impl.EchartsServiceImpl;
import domain.Result;
import entity.Meeting;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/echarts")
public class EchartsController {

    @Resource
    private EchartsServiceImpl echartsService;
    @Resource
    private MeetingServiceImpl MeetingService;


    @GetMapping
    public Result Allnum(@RequestParam(defaultValue = "") String meetingId,
                         @RequestParam(defaultValue = "") String value,
                         @RequestParam(defaultValue = "") String alrnum,
                         @RequestParam(defaultValue = "") String later,
                         @RequestParam(defaultValue = "") String early


    )
        {
            Map<String, Object> values = new HashMap<>();
            values.put("Allnum",echartsService.findAllNum(meetingId,value));
            values.put("Alrnum",echartsService.findAlrNum(meetingId,alrnum));
            values.put("LATERnum",echartsService.findLATERnum(meetingId,later));
            values.put("Earlynum",echartsService.findEarlynum(meetingId,early));

            return Result.success(values);
        }

    @GetMapping("/hello")
    public Result getAllNumData(@RequestParam(defaultValue = "") String meetingId,
                                @RequestParam(defaultValue = "") String alrnum,
                                @RequestParam(defaultValue = "") String later,
                                @RequestParam(defaultValue = "") String early) {

        // 验证参数（这里只是示例，你可能需要更复杂的验证）
        if (StringUtils.isBlank(meetingId)) {
            // 处理无效的meetingId，例如返回错误或默认数据
            return Result.error("meetingId无效");
        }

        // 创建并填充JSON数组
        JSONArray values = new JSONArray();
        values.add(createJsonValue("成功签到", echartsService.findAlrNum(meetingId,alrnum)));
        values.add(createJsonValue("迟到", echartsService.findLATERnum(meetingId,later)));
        values.add(createJsonValue("早退", echartsService.findEarlynum(meetingId,early)));

        // 返回结果
        return Result.success(values);
    }

    // 辅助方法，用于创建JSON对象
    private JSONObject createJsonValue(String name, int value) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.set("name", name);
        jsonObject.set("value", value);
        return jsonObject;
    }


    @GetMapping("/members")
    public Result members() {
        List<Meeting> list = MeetingService.list();
        int q1 = 0; // 第一季度
        int q2 = 0; // 第二季度
        int q3 = 0; // 第三季度
        int q4 = 0; // 第四季度
        for (Meeting meeting : list) {
            LocalDateTime createTime= meeting.getCreateTime();
            ZonedDateTime zdt = createTime.atZone(ZoneId.systemDefault());
            Date QWQ =  Date.from(zdt.toInstant());
            Quarter quarter = DateUtil.quarterEnum(QWQ);
            switch (quarter) {
                case Q1: q1 += 1; break;
                case Q2: q2 += 1; break;
                case Q3: q3 += 1; break;
                case Q4: q4 += 1; break;
                default: break;
            }
            }
        return Result.success(CollUtil.newArrayList(q1, q2, q3, q4));
    }

}

