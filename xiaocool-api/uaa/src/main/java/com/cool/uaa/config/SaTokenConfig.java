package com.cool.uaa.config;


import cn.dev33.satoken.context.SaHolder;
import cn.dev33.satoken.filter.SaServletFilter;
import cn.dev33.satoken.jwt.StpLogicJwtForSimple;
import cn.dev33.satoken.same.SaSameUtil;
import cn.dev33.satoken.stp.StpLogic;
import cn.dev33.satoken.util.SaResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@Slf4j
public class SaTokenConfig implements WebMvcConfigurer {

    /**
     * jwt生成
     * @return
     */
    @Bean
    public StpLogic stpLogicJWT(){
        return new StpLogicJwtForSimple();
    }


        // 注册 Sa-Token 全局过滤器
        @Bean
        public SaServletFilter getSaServletFilter() {
            return new SaServletFilter()
                    .addInclude("/**")
                    .addExclude("/favicon.png","/oauth2/doLogin")
                    .setAuth(obj -> {
                        // 校验 Same-Token 身份凭证     —— 以下两句代码可简化为：SaSameUtil.checkCurrentRequestToken();
                        String token = SaHolder.getRequest().getHeader(SaSameUtil.SAME_TOKEN);
                        SaSameUtil.checkToken(token);

                    })
                    .setError(e -> SaResult.error(e.getMessage()))
                    ;
        }


}