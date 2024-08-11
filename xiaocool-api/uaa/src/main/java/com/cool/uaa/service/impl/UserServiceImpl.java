package com.cool.uaa.service.impl;

import cn.dev33.satoken.stp.SaLoginConfig;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cool.uaa.mapper.Oauth2Mapper;
import com.cool.uaa.mapper.UserMapper;
import com.cool.uaa.service.UserService;
import domain.Constants;
import domain.Result;
import dto.UserDTO;
import entity.Oauth2;
import entity.User;
import exception.BaseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static org.apache.xmlbeans.impl.common.XBeanDebug.LOG;


@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Resource
    private UserMapper userMapper;

    @Autowired
    private Oauth2Mapper oauth2Mapper;


    @Override
    public Result doLogin(UserDTO userDTO){
        if (userDTO.getLoginType().equals("usernameLogin")){
            User user = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getUsername, userDTO.getUsername()));
            if (user == null || !Objects.equals(user.getPassword(),userDTO.getPassword() )){
                return Result.error(Constants.CODE_600, "用户名或密码错误");
            }
            userDTO.setAvatar(user.getAvatar());
            userDTO.setUserId(user.getId());
            userDTO.setName(user.getName());
            userDTO.setNickname(user.getNickname());
            //先把之前那些token全都注销了
            StpUtil.logout(user.getUsername());
            StpUtil.login(user.getUsername());
        }
        if (userDTO.getLoginType().equals("email")){
            User user = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getEmail, userDTO.getEmail()));
            if (user!=null){
                //先把之前那些token全都注销了
                StpUtil.logout(user.getUsername());
                StpUtil.login(user.getUsername());
                userDTO.setUserId(user.getId());
                BeanUtil.copyProperties(user, userDTO, true);

            }
            else {
                //通过邮箱注册
                userDTO.setUsername(userDTO.getEmail());
                user=register(userDTO);
                userDTO.setUserId(user.getId());
                BeanUtil.copyProperties(user, userDTO, true);

            }
        }
        if (userDTO.getLoginType().equals("phone")){
            User user = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getTelephone, userDTO.getTelephone()));
            if (user!=null){
                //先把之前那些token全都注销了
                StpUtil.logout(user.getUsername());
                StpUtil.login(user.getUsername());
                userDTO.setUserId(user.getId());
                BeanUtil.copyProperties(user, userDTO, true);

            }
            else {
                return Result.error(Constants.CODE_600, "用户还未注册!请先完成注册");
            }
        }

        if (userDTO.getLoginType().equals("gitee")){
            Oauth2 oauth2=oauth2Mapper.selectOne(new LambdaQueryWrapper<Oauth2>().eq(Oauth2::getOpenId,userDTO.getOpenId()));
            if (oauth2!=null){
                User user=userMapper.selectById(oauth2.getUserId());
                userDTO.setUsername(user.getUsername());
                userDTO.setAvatar(user.getAvatar());
                userDTO.setUserId(user.getId());
                userDTO.setName(user.getName());
                userDTO.setNickname(user.getNickname());
            Map<String, Object> userinfo=new HashMap<>();
            userinfo.put("user",userDTO);

            //先把之前那些token全都注销了
            StpUtil.logout(user.getUsername());
            StpUtil.login(user.getUsername(), SaLoginConfig.setExtraData(userinfo));
            }
            else {
                userDTO= findUse(userDTO);
            }
        }

        List<String> roleList = findRole(userDTO.getUsername());
        StpUtil.getSession().set("roleList", roleList);
        userDTO.setRole(roleList);
        String token=StpUtil.getTokenValue();
        userDTO.setToken(token);
        return Result.success(userDTO);
    }


    @Override
    public User register(UserDTO userDTO) {
        // 用户密码 md5加密
//        userDTO.setPassword(SecureUtil.md5(userDTO.getPassword()));
        if (userDTO.getPassword()==null){
            //随机生成一个密码
            userDTO.setPassword(RandomUtil.randomString (10));
        }
        User one = getUserInfo(userDTO);
        if (one == null) {
            one = new User();
            BeanUtil.copyProperties(userDTO, one, true);
            one.setProfile("<p>还没有填写哦😃请填写您的个人简介！</p>");
            if (one.getName() == null) {
                one.setName(one.getUsername());
            }
            save(one);  // 把 copy完之后的用户对象存储到数据库
            userMapper.setUserRole(one.getUsername(),"user");
        } else {
            throw new BaseException(Constants.CODE_600, "用户已存在");
        }
        return one;
    }



    private User getUserInfo(UserDTO userDTO) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", userDTO.getUsername());
        queryWrapper.eq("password", userDTO.getPassword());
        User one;
        try {
            one = getOne(queryWrapper); // 从数据库查询用户信息
        } catch (Exception e) {
            LOG.error(e);
            throw new BaseException(Constants.CODE_500, "系统错误");
        }
        return one;
    }


    //第三方登录，寻找user，可以通过手机号或者email等
    public UserDTO findUse(UserDTO userDTO){
            if (userDTO.getEmail()!=null){
               User user=userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getEmail, userDTO.getEmail()));
                if (user!=null){
                    userDTO.setUsername(user.getUsername());
                    userDTO.setAvatar(user.getAvatar());
                    userDTO.setUserId(user.getId());
                    userDTO.setName(user.getName());
                    userDTO.setNickname(user.getNickname());
                    Oauth2 oauth2=new Oauth2();
                    oauth2.setUserId(user.getId());
                    oauth2.setOpenId(userDTO.getOpenId());
                    oauth2.setClientName(userDTO.getLoginType());
                    oauth2Mapper.insert(oauth2);
                    Map<String, Object> userinfo=new HashMap<>();
                    userinfo.put("user",userDTO);
                    StpUtil.login(user.getUsername(), SaLoginConfig.setExtraData(userinfo));
                    return userDTO;
                }
            }
            //如果找不到，则注册一个新的账号

         register(userDTO);
         Integer userId=getOne(new QueryWrapper<User>().eq("id",userDTO.getUsername())).getId();
         Oauth2 oauth2=new Oauth2();
         oauth2.setUserId(userId);
         oauth2.setOpenId(userDTO.getOpenId());
         oauth2.setClientName(userDTO.getLoginType());
         oauth2Mapper.insert(oauth2);
         userDTO.setUserId(userId);
         userDTO.setNickname(userDTO.getUsername());
         userDTO.setName(userDTO.getUsername());
         return userDTO;
        }





    public List<String> findRole(String username){
        return userMapper.findRole(username);
    }




}
