package com.cool.uaa.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;


@Service
public class WeChatServiceImpl {
    @Value("${wechat.app-id}")
    private String appId;

    @Value("${wechat.app-secret}")
    private String appSecret;

    @Value("${wechat.redirect-uri}")
    private String redirectUri;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    // 获取微信登录URL
    public String getWeChatLoginUrl() {
        return "https://open.weixin.qq.com/connect/qrconnect?appid=" + appId + "&redirect_uri=" + redirectUri +
                "&response_type=code&scope=snsapi_login&state=STATE#wechat_redirect";
    }

    // 使用code获取AccessToken和OpenId
    public JsonNode getAccessTokenAndOpenId(String code) throws Exception {
        String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + appId + "&secret=" + appSecret +
                "&code=" + code + "&grant_type=authorization_code";
        String response = restTemplate.getForObject(url, String.class);
        return objectMapper.readTree(response);
    }

    // 使用AccessToken和OpenId获取用户信息
    public JsonNode getUserInfo(String accessToken, String openId) throws Exception {
        String url = "https://api.weixin.qq.com/sns/userinfo?access_token=" + accessToken + "&openid=" + openId;
        String response = restTemplate.getForObject(url, String.class);
        return objectMapper.readTree(response);
    }
}
