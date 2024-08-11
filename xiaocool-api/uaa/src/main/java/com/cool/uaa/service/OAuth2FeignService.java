package com.cool.uaa.service;

import domain.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@FeignClient(value = "uaa",path = "/oauth2")  //指定调用哪个微服务
public interface OAuth2FeignService {


    @GetMapping("/authorize/gitee")//哪个地址
    void giteeAuthorize();

    @GetMapping("/hello")
    String hello();
}
