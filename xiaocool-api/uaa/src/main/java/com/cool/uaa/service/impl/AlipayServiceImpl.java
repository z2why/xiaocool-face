package com.cool.uaa.service.impl;

import com.alipay.api.*;
import com.alipay.api.request.AlipaySystemOauthTokenRequest;
import com.alipay.api.request.AlipayUserInfoShareRequest;
import com.alipay.api.response.AlipaySystemOauthTokenResponse;
import com.alipay.api.response.AlipayUserInfoShareResponse;
import dto.UserDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
public class AlipayServiceImpl {
    @Value("${alipay.app-id}")
    private String appId;
    @Value("${alipay.redirect-uri}")
    private String redirectUri;
    @Value("${alipay.gateway}")
    private String alipayGateway;
    @Value("${alipay.merchant-private-key}")
    private String privateKey;
    @Value("${alipay.alipayCertPath}")
    private String alipayCertPath;
    @Value("${alipay.rootCertPath}")
    private String alipayRootCertPath;
    @Value("${alipay.appCertPath}")
    private String appCertPath;

    public String getAuthorizationUrl() {
        String state = UUID.randomUUID().toString();
        return  "https://openauth.alipay.com/oauth2/publicAppAuthorize.htm?" +
                "app_id=" + appId +
                "&scope=auth_user" +  // 请求用户信息授权
                "&redirect_uri=" + redirectUri +
                "&state=" + state;
    }

    public String getAccessToken(String authCode) throws AlipayApiException {
        CertAlipayRequest certAlipayRequest=getAlipayConfig();
        DefaultAlipayClient alipayClient = new DefaultAlipayClient(certAlipayRequest);
        AlipaySystemOauthTokenRequest request = new AlipaySystemOauthTokenRequest();
        request.setCode(authCode);
        request.setGrantType("authorization_code");

        try {
            AlipaySystemOauthTokenResponse response = alipayClient.certificateExecute(request);
            return response.getAccessToken();
        } catch (AlipayApiException e) {
            e.printStackTrace();
            throw new RuntimeException("获取 Access Token 失败");
        }
    }
//certificateExecute
    public UserDTO getUserInfo(String accessToken) throws AlipayApiException {
        CertAlipayRequest certAlipayRequest=getAlipayConfig();
        DefaultAlipayClient alipayClient = new DefaultAlipayClient(certAlipayRequest);
        AlipayUserInfoShareRequest request = new AlipayUserInfoShareRequest();
        try {
            request.putOtherTextParam("auth_token", accessToken);
            AlipayUserInfoShareResponse response = alipayClient.certificateExecute(request);
            if (response.isSuccess()) {
                log.info("=========获得的数据{}==========",response);
                log.info("=========获得的数据.BODY{}==========",response.getBody());
                UserDTO userDTO = new UserDTO();
                userDTO.setUsername(response.getOpenId().substring(0, 10));
                userDTO.setNickname(response.getNickName());
                userDTO.setAvatar(response.getAvatar());
                userDTO.setOpenId(response.getOpenId());
                userDTO.setLoginType("Alipay");
                return userDTO;
            } else {
                throw new RuntimeException("获取用户信息失败");
            }
        } catch (AlipayApiException e) {
            e.printStackTrace();
            throw new RuntimeException("获取用户信息失败");
        }
    }

    public CertAlipayRequest getAlipayConfig(){
        CertAlipayRequest certAlipayRequest = new CertAlipayRequest();

        certAlipayRequest.setServerUrl(alipayGateway);

        certAlipayRequest.setAppId(appId);

        certAlipayRequest.setPrivateKey(privateKey);

        certAlipayRequest.setFormat("json");

        certAlipayRequest.setCharset("UTF-8");

        certAlipayRequest.setSignType("RSA2");

        certAlipayRequest.setCertPath(appCertPath);

        certAlipayRequest.setAlipayPublicCertPath(alipayCertPath);

        certAlipayRequest.setRootCertPath(alipayRootCertPath);

        return certAlipayRequest;
    }


}
