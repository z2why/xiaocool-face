package com.cool.uaa.service;

/**
 * description: 短信业务
 *
 * @author RenShiWei
 * Date: 2020/9/15 22:06
 **/
public interface ISmsService {

    /**
     * description: 发送短信验证码
     *
     * @param phone 手机号
     * @author RenShiWei
     * Date: 2020/9/16 10:11
     * @return
     */
    boolean sendSmsCode(String phone,String type);

    /**
     * description:验证短信验证码
     *
     * @param phone 手机号
     * @param code  验证码
     * @author RenShiWei
     * Date: 2020/9/16 10:11
     * @return
     */
    boolean verifySmsCode(String phone, String code);

}
