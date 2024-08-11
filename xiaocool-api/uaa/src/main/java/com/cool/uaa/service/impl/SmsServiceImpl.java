package com.cool.uaa.service.impl;

import com.cool.uaa.config.util.RedisUtils;
import com.cool.uaa.config.util.SmsCodeUtil;
import com.cool.uaa.config.util.SmsUtil;
import com.cool.uaa.service.ISmsService;
import com.tencentcloudapi.sms.v20190711.models.SendStatus;
import domain.Constants;
import domain.SmsLengthEnum;
import domain.SmsResponseCodeEnum;
import exception.BaseException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.cool.uaa.config.properties.SmsProperties;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * description: 短信业务
 *
 * @author RenShiWei
 * Date: 2020/9/15 22:06
 **/
@Service
@Slf4j
@RequiredArgsConstructor
public class SmsServiceImpl implements ISmsService {

    public static final String CAPTCHA_CODE = "captcha-sms-";

    @Resource
    private SmsProperties smsProperties;

    @Autowired
    private  RedisUtils redisUtils;

    /** 腾讯云短信模板id-短信验证码 */
    //登录模板
    @Value("${sms-config.templateIds.code.login}")
    private String templateIdCodeLogin;
    //注册模板
    @Value("${sms-config.templateIds.code.register}")
    private String templateIdCodeRegister;

    /**
     * description: 发送短信验证码
     *
     * @param phone 手机号
     * @author RenShiWei
     * Date: 2020/9/16 10:11
     * @return
     */
    @Override
    public boolean sendSmsCode(String phone,String type) {
        // redis缓存的键
        final String smsKey = SmsCodeUtil.createSmsCacheKey(phone,CAPTCHA_CODE);

        //下发手机号码，采用 e.164 标准，+[国家或地区码][手机号]  示例如：+8613711112222， 其中前面有一个+号 ，86为国家码，13711112222为手机号，最多不要超过200个手机号
        String[] phoneNumbers = {"+86" + phone};
        //模板参数: 若无模板参数，则设置为空（第一个参数为随机验证码，第二个参数为有效时间）
        final String smsRandomCode = SmsCodeUtil.createSmsRandomCode(SmsLengthEnum.SMS_LENGTH_6);
        String[] templateParams = {smsRandomCode,
                smsProperties.getExpireTime()};
        SendStatus[] sendStatuses;
        if (type.equals("login")){
            sendStatuses = SmsUtil.sendSms(smsProperties, templateIdCodeLogin, templateParams, phoneNumbers);
        }
        else{
            sendStatuses = SmsUtil.sendSms(smsProperties, templateIdCodeRegister, templateParams, phoneNumbers);
        }
        String resCode = sendStatuses[0].getCode();
        if (resCode.equals(SmsResponseCodeEnum.OK.getCode())) {
            // 返回ok，缓存
            return redisUtils.set(smsKey, smsRandomCode, Long.parseLong(smsProperties.getExpireTime()), TimeUnit.MINUTES);
        } else {
            log.error("【短信业务-发送失败】phone:" + phone + "errMsg:" + sendStatuses[0].getMessage());
            throw new BaseException(Constants.SMS_NOT_SEND,"发送失败");
        }

    }

    /**
     * description:验证短信验证码
     *
     * @param phone 手机号
     * @param code  验证码
     * @author RenShiWei
     * Date: 2020/9/16 10:11
     * @return
     */
    @Override
    public boolean verifySmsCode(String phone, String code) {
        // redis缓存的键
        final String smsKey = SmsCodeUtil.createSmsCacheKey(phone,CAPTCHA_CODE);
        // 如果key存在（存在并且未过期）
        if (redisUtils.hasKey(smsKey)) {
            String cacheCode = redisUtils.get(smsKey).toString();
            if (cacheCode.equals(code)) {
                //验证码正确
                //删除验证码缓存
                return redisUtils.del(smsKey);
            } else {
                log.error("【短信业务认证失败】验证码错误。phone：" + phone);
                return false;
            }
        } else {
            log.error("【短信业务认证失败】验证码失效。phone：" + phone);
            return false;
        }
    }

}


