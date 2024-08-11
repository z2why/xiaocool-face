/**
 * @projectName: sa-tkenTest
 * @author: 小飞
 * @description: TODO
 * @date: 2024/4/16 20:16
 * @version: 1.0
 */
package com.cool.uaa.service.impl;

import cn.dev33.satoken.stp.StpInterface;
import cn.dev33.satoken.stp.StpUtil;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;
import java.util.List;

@Component
public class StpInterfaceImpl implements StpInterface {
    @Resource
    private UserServiceImpl userService;




    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        return null;
    }

    /**
     * 返回一个账号所拥有的角色标识集合
     */
    @Override
    public List<String> getRoleList(Object loginId,String loginType) {
        // 本 list 仅做模拟，实际项目中要根据具体业务逻辑来查询角色

//        userService.findRole((String) loginId)
        return (List<String>) StpUtil.getSession().get("roleList");
    }
}
