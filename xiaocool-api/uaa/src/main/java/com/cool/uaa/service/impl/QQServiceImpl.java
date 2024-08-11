package com.cool.uaa.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.JsonNode;

@Service
public class QQServiceImpl {
    @Value("${qq.app-id}")
    private String appId;

    @Value("${qq.app-key}")
    private String appKey;

    @Value("${qq.redirect-uri}")
    private String redirectUri;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    // 获取QQ登录URL
    public String getQQLoginUrl() {
        return "https://graph.qq.com/oauth2.0/authorize?response_type=code&client_id=" + appId +
                "&redirect_uri=" + redirectUri + "&state=STATE";
    }

    // 使用code获取AccessToken
    public String getAccessToken(String code) throws Exception {
        String url = "https://graph.qq.com/oauth2.0/token?grant_type=authorization_code&client_id=" + appId +
                "&client_secret=" + appKey + "&code=" + code + "&redirect_uri=" + redirectUri;
        String response = restTemplate.getForObject(url, String.class);
        return parseAccessToken(response);
    }

    // 使用AccessToken获取OpenId
    public String getOpenId(String accessToken) throws Exception {
        String url = "https://graph.qq.com/oauth2.0/me?access_token=" + accessToken;
        String response = restTemplate.getForObject(url, String.class);
        return parseOpenId(response);
    }

    // 使用AccessToken和OpenId获取用户信息
    public JsonNode getUserInfo(String accessToken, String openId) throws Exception {
        String url = "https://graph.qq.com/user/get_user_info?access_token=" + accessToken + "&oauth_consumer_key=" +
                appId + "&openid=" + openId;
        String response = restTemplate.getForObject(url, String.class);
        return objectMapper.readTree(response);
    }

    // 解析AccessToken
    private String parseAccessToken(String response) {
        String[] items = response.split("&");
        for (String item : items) {
            if (item.startsWith("access_token=")) {
                return item.split("=")[1];
            }
        }
        throw new RuntimeException("Invalid response: " + response);
    }

    // 解析OpenId
    private String parseOpenId(String response) throws JsonProcessingException {
        String json = response.substring(response.indexOf("{"), response.indexOf("}") + 1);
        JsonNode node = objectMapper.readTree(json);
        return node.get("openid").asText();
    }
}
