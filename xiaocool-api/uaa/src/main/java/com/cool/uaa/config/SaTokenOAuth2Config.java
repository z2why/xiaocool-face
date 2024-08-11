package com.cool.uaa.config;

import cn.dev33.satoken.oauth2.config.SaOAuth2Config;
import cn.hutool.core.util.StrUtil;
import com.cool.uaa.service.impl.UserServiceImpl;
import domain.Result;
import dto.UserDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.servlet.ModelAndView;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Configuration
public class SaTokenOAuth2Config {
    @Resource
    private UserServiceImpl userService;


    @Primary
    @Bean
    public SaOAuth2Config getConfig() {
        SaOAuth2Config config = new SaOAuth2Config();

        config.setIsCode(true);
        config.setIsImplicit(true);
        config.setIsPassword(true);
        config.setIsClient(true);

        config.setDoLoginHandle((name, pwd) -> {
            if (StrUtil.isBlank(name) || StrUtil.isBlank(pwd)) {
                return Result.error("登录失败");
            }
            UserDTO userDTO = new UserDTO();
            userDTO.setUsername(name);
            userDTO.setPassword(pwd);
            userDTO.setLoginType("usernameLogin");

            return userService.doLogin(userDTO);
        });

        config.setNotLoginView(() -> {
            return new ModelAndView("login.html");
        });

        config.setConfirmView((clientId, scope) -> {
            Map<String, Object> map = new HashMap<>();
            map.put("clientId", clientId);
            map.put("scope", scope);
            return new ModelAndView("confirm.html", map);
        });

        return config;
    }




}
