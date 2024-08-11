package com.cool.admin.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cool.admin.mapper.UserMapper;
import com.cool.admin.service.IUserService;
import entity.User;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {
    @Resource
    private UserMapper userMapper;

    @Override
    public Page<User> findPage(Page<User> page, String username) {
        return userMapper.findPage(page, username);
    }

}
