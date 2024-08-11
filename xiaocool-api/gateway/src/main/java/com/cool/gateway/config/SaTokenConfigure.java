package com.cool.gateway.config;

import cn.dev33.satoken.context.SaHolder;
import cn.dev33.satoken.reactor.filter.SaReactorFilter;
import cn.dev33.satoken.router.SaHttpMethod;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import domain.Result;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * [Sa-Token 权限认证] 配置类
 * @author click33
 */
@Configuration
public class SaTokenConfigure{
    // 注册 Sa-Token全局过滤器
    @Bean
    public SaReactorFilter getSaReactorFilter() {
        return new SaReactorFilter()
                // 拦截地址
                .addInclude("/**")    /* 拦截全部path */
                // 开放地址
                .addExclude("/favicon.png","/uaa/oauth2/**","/uaa/user/getCode","/uaa/user/register"
                ,"service/wss/webrtc")

                // 鉴权方法：每次访问进入
                .setAuth(obj -> {
                    // 登录校验 -- 拦截所有路由，并排除/user/doLogin 用于开放登录
                    System.out.println("全局验证------：");
                    SaRouter.match("/**").check(r -> StpUtil.checkLogin());

                    // 权限认证 -- 不同模块, 校验不同权限
                    System.out.println("全局权限验证------：");
                    SaRouter.match("/uaa/**",r -> StpUtil.checkRole("user"));
                    SaRouter.match("/service/**",r -> StpUtil.checkRole("user"));


                })
                // 异常处理方法：每次setAuth函数出现异常时进入
                // 异常处理函数：每次认证函数发生异常时执行此函数
                .setError(e ->
                        new SaResult(401,e.getMessage(),"")

                )

//                .setError(e -> {
//                    return Result.error("401",e.getMessage());
//                })
                // 前置函数：在每次认证函数之前执行
                .setBeforeAuth(obj -> {
                    System.out.println("--------设置跨域请求头--------------");
                    SaHolder.getResponse()
                            .setHeader("Access-Control-Allow-Credentials","true")
                            // ---------- 设置跨域响应头 ----------
                            // 允许指定域访问跨域资源
                            .setHeader("Access-Control-Allow-Origin", "*")
                            // 允许所有请求方式
                            .setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS")
                            // 允许的header参数
                            .setHeader("Access-Control-Allow-Headers", "*")
                            // 有效时间
                            .setHeader("Access-Control-Max-Age", "3600")
                    ;

//                     如果是预检请求，则立即返回到前端
                    SaRouter.match(SaHttpMethod.OPTIONS).free(r -> {
                        System.out.println("--------OPTIONS预检请求，不做处理");
                    }).back();
                })
                ;

    }
}