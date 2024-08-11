package com.cool.uaa.service;

import com.baomidou.mybatisplus.extension.service.IService;
import domain.Result;
import dto.UserDTO;
import entity.User;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 小飞
 * @since 2024-04-16
 */
public interface UserService extends IService<User> {

    User register(UserDTO userDTO);

    Result doLogin(UserDTO userDTO);
}
