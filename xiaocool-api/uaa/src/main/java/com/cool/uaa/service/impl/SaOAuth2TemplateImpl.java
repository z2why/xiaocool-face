package com.cool.uaa.service.impl;

import cn.dev33.satoken.oauth2.logic.SaOAuth2Template;
import cn.dev33.satoken.oauth2.model.SaClientModel;
import cn.dev33.satoken.stp.StpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * Sa-Token OAuth2.0 整合实现
 */
@Component
public class SaOAuth2TemplateImpl extends SaOAuth2Template {


    @Autowired
    private GiteeServiceImpl giteeService;

    // 根据 id 获取 Client 信息
    @Override
    public SaClientModel getClientModel(String clientId) {
        if ("xiaocool".equals(clientId)) {
            return new SaClientModel()
                    .setClientId("xiaocool")
                    .setClientSecret("hello-world-tomorrow-will-better")
                    .setAllowUrl("*")
                    .setContractScope("userinfo")
                    .setIsAutoMode(true);

        }

        return null;
    }

    // 根据 ClientId 和 LoginId 获取 openid
    @Override
    public String getOpenid(String clientId, Object loginId) {
        // 此为模拟数据，真实环境需要从数据库查询
        return "gr_SwoIN0MC1ewxHX_vfCW3BothWDZMMtx__";
    }

    // 重写 Access-Token 生成策略：复用登录会话的 Token
    @Override
    public String randomAccessToken(String clientId, Object loginId, String scope) {
        return StpUtil.createLoginSession(loginId);
    }

}
