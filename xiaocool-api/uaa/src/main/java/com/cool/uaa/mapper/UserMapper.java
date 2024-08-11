package com.cool.uaa.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import dto.UserRoleDTO;
import entity.User;
import org.apache.ibatis.annotations.*;

import java.util.List;


@Mapper
public interface UserMapper extends BaseMapper<User> {


    @Select("SELECT sys_role.role FROM sys_role,sys_user_role,`user`\n" +
            "WHERE sys_role.id=sys_user_role.role_id AND`user`.id=sys_user_role.user_id\n" +
            "AND `user`.username= #{username}")
    List<String> findRole(String username);

   @Insert("INSERT INTO sys_user_role (user_id, role_id)\n"+
           "SELECT `user`.id, sys_role.id\n"+
           "FROM `user` JOIN sys_role ON 1=1 \n"+
           " WHERE `user`.username = #{username} AND sys_role.role = #{role}\n")
    void setUserRole(String username,String role);

}
