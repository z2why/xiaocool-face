package com.cool.uaa.controller;


import cn.dev33.satoken.oauth2.logic.SaOAuth2Handle;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.StrUtil;
import com.cool.uaa.config.util.RedisUtils;
import com.cool.uaa.service.impl.*;
import com.fasterxml.jackson.databind.JsonNode;
import domain.Constants;
import domain.Result;
import dto.UserDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/oauth2")
public class OAuth2Controller {
    public static final String CAPTCHA_SESSION_KEY = "captcha-";

    @Autowired
    private GiteeServiceImpl giteeService;

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private EmailCaptchaServiceImpl emailService;

    @Autowired
    private SmsServiceImpl smsService;

    @Autowired
    private WeChatServiceImpl weChatService;

    @Autowired
    private QQServiceImpl qqService;

    @Autowired
    private AlipayServiceImpl alipayService;

    @Autowired
    private RedisUtils redisUtils;



    @PostMapping("/doLogin")
    public Object doLogin(@RequestBody UserDTO userDTO) {
        try {
            String redisKey="Captcha:" + CAPTCHA_SESSION_KEY + userDTO.getCode();
            if (redisUtils.hasKey(redisKey)){
                String captchaCode=redisUtils.get(redisKey).toString();
                if (userDTO.getCode().equals(captchaCode)){
                    redisUtils.del(redisKey);
                    if (StrUtil.isBlank(userDTO.getUsername()) || StrUtil.isBlank(userDTO.getPassword())) {
                        return Result.error(Constants.CODE_500,"登录失败");
                    }
                    userDTO.setLoginType("usernameLogin");
                    return userService.doLogin(userDTO);
                }
                else {
                    return Result.error(Constants.CODE_400, "验证码错误或已过期");
                }
            }
            else {
                return Result.error(Constants.CODE_400, "验证码错误或已过期");
            }

        }
          catch (Exception e) {
            throw new RuntimeException(e);}
    }



    @PostMapping("/captchaLogin")
    public Result CaptchaLogin(@RequestBody UserDTO userDTO,HttpServletResponse response) throws IOException {
        return userService.doLogin(userDTO);
    }



    @GetMapping("/authorize")
    public Object authorize() {
        return SaOAuth2Handle.serverRequest();
    }


    /**
     * Gitee模块
     */
    @GetMapping("/authorize/gitee")
    public void giteeAuthorize(HttpServletResponse response) throws IOException {
        String url = giteeService.getAuthorizationUrl();
        response.sendRedirect(url);
    }


    @GetMapping("/gitee/callback")
    public void  handleGiteeCallback(@RequestParam("code") String code,
                                      HttpServletResponse response,
                                      HttpServletRequest request) {
        try {
            String accessToken = giteeService.getAccessToken(code);
            //从第三方平台获取用户信息
            Map<String, Object> userInfo = giteeService.getUserInfo(accessToken);
            // 模拟使用 userService 处理用户信息
            UserDTO userDTO = new UserDTO();
            userDTO.setLoginType("gitee");
            userDTO.setAvatar(String.valueOf(userInfo.get("avatar_url")));
            userDTO.setUsername(String.valueOf(userInfo.get("login")));
            userDTO.setEmail(String.valueOf(userInfo.get("email")));
            userDTO.setOpenId(String.valueOf(userInfo.get("id")));
            userService.doLogin(userDTO);
            for (String role:userDTO.getRole()){
                if (role.equals("admin")){
                    response.sendRedirect("https://xiaoku.store/admin/user?token="+StpUtil.getTokenValue());
                    return;
                }
            }
            response.sendRedirect("https://xiaoku.store/AddMeeting?token="+StpUtil.getTokenValue());
        } catch (Exception e) {
            throw new RuntimeException("登录失败", e);
        }
    }



    /**
     * QQ模块
     */
    // 获取QQ登录URL
    @GetMapping("/authorize/qq")
    public void getQQLoginUrl(HttpServletResponse response) throws IOException {
        String url = qqService.getQQLoginUrl();
        response.sendRedirect(url);
    }

    // QQ回调
    @GetMapping("/qq/callback")
    public void qqCallback(@RequestParam("code") String code,
                           HttpServletResponse response,
                           HttpServletRequest request) {
        try {
            String accessToken = qqService.getAccessToken(code);
            //获得qq号
            JsonNode node = qqService.getOpenId(accessToken);
            String openId= node.get("openid").asText();
            String unionId= node.get("unionid").asText();
            log.info("========openId:{}==========",openId);
            log.info("========unionid:{}=========",unionId);
            Map<String, Object> userInfo = qqService.getUserInfo(accessToken, openId);
            UserDTO userDTO = new UserDTO();
            userDTO.setLoginType("QQ");
            userDTO.setAvatar(String.valueOf(userInfo.get("figureurl_qq_1")));
            userDTO.setNickname(String.valueOf(userInfo.get("nickname")));
            userDTO.setUsername(unionId.substring(4, 14));
            userDTO.setOpenId(unionId);
            // 这里可以根据unionId查询用户是否已存在，未存在则创建用户
            userService.doLogin(userDTO);
            for (String role:userDTO.getRole()){
                if (role.equals("admin")){
                    response.sendRedirect("https://xiaoku.store/admin/user?token="+StpUtil.getTokenValue());
                    return;
                }
            }
            response.sendRedirect("https://xiaoku.store/AddMeeting?token="+StpUtil.getTokenValue());
        } catch (Exception e) {
            throw new RuntimeException("登录失败", e);
        }
    }



//    支付宝
       @GetMapping("/authorize/alipay")
       public void alipayAuthorize(HttpServletResponse response) throws IOException {
       String url = alipayService.getAuthorizationUrl();
       response.sendRedirect(url);

    }


//      支付宝回调
       @GetMapping("/alipay/callback")
      public void AliPayCallback(@RequestParam("auth_code") String authCode,
                       HttpServletResponse response,
                       HttpServletRequest request) {
    try {
        String accessToken = alipayService.getAccessToken(authCode);
        // 获取用户信息
        UserDTO userDTO  = alipayService.getUserInfo(accessToken);
        userService.doLogin(userDTO);
        for (String role:userDTO.getRole()){
            if (role.equals("admin")){
                response.sendRedirect("https://xiaoku.store/admin/user?token="+StpUtil.getTokenValue());
                return;
            }
        }
        response.sendRedirect("https://xiaoku.store/AddMeeting?token="+StpUtil.getTokenValue());
    } catch (Exception e) {
        throw new RuntimeException("登录失败", e);
    }
}


    /**
     * 短信模块
     */
    // 发送短信验证码
//    sendSmsCode type为login/register
    @GetMapping("/sms-captcha")
    public Result sendSmsCaptcha(@RequestParam String phone,@RequestParam String type) {
        if (smsService.sendSmsCode(phone,type))
        {
            return Result.success("发送成功");
        }
        else {
            return Result.error("发送失败");
        }

    }

    //验证短信验证码
    @GetMapping("/verifySmsCaptcha")
    public Result verifySmsCaptcha(@RequestParam String phone,@RequestParam String code){
        if (smsService.verifySmsCode(phone,code))
        {
            return Result.success("校验成功");
        }
        else {
            return Result.error("校验失败");
        }
    }


    /**
     * 邮箱模块
     */
    //发送邮箱验证码
    @GetMapping("/email-captcha")
    public Result sendEmailCaptcha(@RequestParam String email,@RequestParam String type) {
        emailService.sendEmailCaptcha(email,type);
        return Result.success("发送成功");
    }

    //验证邮箱验证码
    @GetMapping("/verifyEmailCaptcha")
    public Result verifyEmailCaptcha(@RequestParam String email,@RequestParam String code){
        if (emailService.verifyEmailCode(email,code))
        {
            return Result.success("校验成功");
        }
        else {
            return Result.error("校验失败");
        }
    }

    /**
     * 微信模块
     */
    // 获取微信登录URL
    @GetMapping("/authorize/wechat")
    public String getWeChatLoginUrl() {
        return weChatService.getWeChatLoginUrl();
    }

    // 微信回调
    @GetMapping("/wechat/callback")
    public String weChatCallback(@RequestParam String code) {
        try {
            JsonNode tokenResponse = weChatService.getAccessTokenAndOpenId(code);
            String accessToken = tokenResponse.get("access_token").asText();
            String openId = tokenResponse.get("openid").asText();

            JsonNode userInfo = weChatService.getUserInfo(accessToken, openId);
            String unionId = userInfo.get("unionid").asText();

            // 这里可以根据unionId查询用户是否已存在，未存在则创建用户
            StpUtil.login(unionId);

            return "登录成功，Access Token: " + StpUtil.getTokenValue();
        } catch (Exception e) {
            e.printStackTrace();
            return "登录失败";
        }
    }



}
