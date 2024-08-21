package com.cool.uaa.controller;


import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.CircleCaptcha;
import cn.hutool.captcha.generator.RandomGenerator;
import cn.hutool.core.util.StrUtil;
import com.cool.uaa.config.util.RedisUtils;
import com.cool.uaa.service.OAuth2FeignService;
import com.cool.uaa.service.impl.UserServiceImpl;
import domain.Constants;
import domain.Result;
import dto.UserDTO;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.TimeUnit;


/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author 小飞
 * @since 2024-04-16
 */
@RestController
@RequestMapping("/user/")
@Api(tags = "用户信息管理")
@Slf4j
public class UserController {
    public static final String CAPTCHA_SESSION_KEY = "captcha-";


    @Resource
    private UserServiceImpl userService;

    @Autowired
    private OAuth2FeignService OAuth2FeignService;

    @Autowired
    private RedisUtils redisUtils;


    //注册
    @PostMapping("register")
    public Result register(@RequestBody UserDTO userDTO) {
        String username = userDTO.getUsername();
        String password = userDTO.getPassword();
        if (StrUtil.isBlank(username) || StrUtil.isBlank(password)) {
            return Result.error(Constants.CODE_400, "参数错误");
        }
        return Result.success(userService.register(userDTO));
    }


    //验证码
    @RequestMapping("getCode")
    public void getCode(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 随机生成 4 位验证码
        RandomGenerator randomGenerator = new RandomGenerator("1345678AaBbEePpQqFfGHhKkrRtT", 4);
        // 定义图片的显示大小
        //验证码工具类
        CircleCaptcha circleCaptcha = CaptchaUtil.createCircleCaptcha(120, 46, 4, 10);
        response.setContentType("image/jpeg");
        response.setHeader("Pragma", "No-cache");
        try {
            // 调用父类的 setGenerator() 方法，设置验证码的类型
            circleCaptcha.setGenerator(randomGenerator);
            // 输出到页面
            circleCaptcha.write(response.getOutputStream());
            // 打印日志
            log.info("生成的验证码:{}", circleCaptcha.getCode());
            String verifyCode = circleCaptcha.getCode();
            String redisKey = "Captcha:" + CAPTCHA_SESSION_KEY + verifyCode;
            redisUtils.set(redisKey, verifyCode, 5, TimeUnit.MINUTES);

            // 关闭流
            response.getOutputStream().close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @SaCheckLogin
    @GetMapping("isLogin")
    public Result isLogin() {
        return Result.success("当前的登录状态为", StpUtil.isLogin());
    }

    /**
     * 查询token信息
     */
    @GetMapping("tokenInfo")
    public Result tokenInfo() {
        return Result.success(StpUtil.getTokenInfo());
    }



    /**
     * 查询token信息
     */
    @GetMapping("userInfo")
    public Result userInfo() {
        return Result.success(StpUtil.getExtra("user"));
    }

    /**
     * 获取角色集合
     */
    @GetMapping("getRole")
    public Result getRole() {
        return Result.success(StpUtil.getRoleList());
    }



    @GetMapping("logout")
    public Result logout(String id) {
        //强制下线
        StpUtil.logout(id);                         // 强制指定账号注销下线
        return Result.success();
    }

}

