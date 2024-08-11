package com.cool.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import entity.UserDetail;

public interface IEchartsService extends IService<UserDetail> {
    Integer findAllNum(String meetingId,String value);
}
