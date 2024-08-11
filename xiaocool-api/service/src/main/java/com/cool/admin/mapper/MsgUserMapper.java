package com.cool.admin.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import entity.MsgUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MsgUserMapper extends BaseMapper<MsgUser> {

    List<MsgUser> getMsg(@Param(Constants.WRAPPER) Wrapper<MsgUser> wrapper);
}
