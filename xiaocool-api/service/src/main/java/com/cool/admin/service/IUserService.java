package com.cool.admin.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import entity.User;

public interface IUserService extends IService<User> {



    Page<User> findPage(Page<User> page, String username);

}
