package com.cool.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import entity.UserDetail;

/**
 * <p>
 *  服务类
 * </p>
 *
 */
public interface IUserForDetailService extends IService<UserDetail> {

    void delete(String meetingId);

    void finishMeeting(String meetingId, String signOutDate);
}
