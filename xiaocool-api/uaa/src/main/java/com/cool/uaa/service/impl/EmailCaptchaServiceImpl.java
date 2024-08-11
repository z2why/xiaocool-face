package com.cool.uaa.service.impl;

import cn.hutool.extra.mail.Mail;
import cn.hutool.extra.mail.MailAccount;
import com.cool.uaa.service.EmailCaptchaService;
import domain.Constants;
import exception.BaseException;
import io.lettuce.core.RedisException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import com.cool.uaa.config.util.RedisUtils;
import com.cool.uaa.config.util.VerifyCodeUtil;
import com.cool.uaa.config.properties.EmailProperties;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailCaptchaServiceImpl implements EmailCaptchaService {

    public static final String CAPTCHA_CODE = "captcha-email-";
    @Resource
    private EmailProperties emailProperties;
    @Autowired
    private  RedisUtils redisUtils;
    @Autowired
    private TemplateEngine templateEngine;

    /**
     * 发送邮件验证码
     * @param email 邮箱
     * @param type  类型
     */
    @Override
    public void sendEmailCaptcha(String email,String type) {
        // 验证邮件配置是否完整
        validateEmailProperties();

        // 验证邮箱格式
        if (!VerifyCodeUtil.checkEmail(email)) {
            throw new BaseException(Constants.CODE_600, "发送邮箱格式错误");
        }

        // 生成或获取验证码
        String captcha = getCaptcha(email);

        // 生成邮件内容
        String content = generateEmailContent(captcha,type);

        // 发送邮件
        List<String> list = Collections.singletonList(email);
        sendEmail(list, content);
    }

    /**
     * 校验邮件验证码
     */
    public boolean verifyEmailCode(String email,String code) {
        String redisKey = "EmailCaptcha:" + CAPTCHA_CODE + email;
        // 如果key存在（存在并且未过期）
        if (redisUtils.hasKey(redisKey)) {
            String cacheCode = redisUtils.get(redisKey).toString();
            if (cacheCode.equals(code)) {
                //删除验证码缓存
                return redisUtils.del(redisKey);
            } else {
                //验证码不正确
                log.error("【邮箱验证码认证失败】验证码错误。email：" + email);
                return false;
            }
        } else {
            log.error("【邮箱验证码认证失败】验证码失效。email：" + email);
            return false;
        }

    }


        /**
         * 判断邮件配置是否完整
         */
    private void validateEmailProperties() {
        if (emailProperties.getUser() == null || emailProperties.getPassword() == null ||
                emailProperties.getFrom() == null || emailProperties.getHost() == null ||
                emailProperties.getPort() == null) {
            throw new BaseException(Constants.CODE_600, "邮件配置不完整");
        }
    }

    /**
     * 获取验证码
     *
     * @param email 邮箱地址，用于生成和存储验证码。
     * @return {@link String} 返回生成的验证码。
     */
    private String getCaptcha(String email) {
        // 使用 EmailCaptcha 前缀生成Redis键名
        String redisKey = "EmailCaptcha:" + CAPTCHA_CODE + email;

        // 打印生成的 Redis 键名，检查是否正确
        System.out.println("Generated Redis Key: " + redisKey);

        // 尝试从Redis获取现有的验证码
        Object oldCode = redisUtils.get(redisKey);
        if (oldCode == null) {
            // 如果验证码不存在，生成新的验证码
            String captcha = VerifyCodeUtil.generateVerifyCode();
            // 将新生成的验证码存储到Redis，并设置过期时间
            boolean saveResult = redisUtils.set(redisKey, captcha, emailProperties.getExpireTime());
            if (!saveResult) {
                // 如果存储失败，抛出异常
                throw new RedisException("储存失败");
            }
            return captcha;
        } else {
            // 如果验证码存在，重置其在Redis中的过期时间
            boolean expireResult = redisUtils.expire(redisKey, emailProperties.getExpireTime());
            if (!expireResult) {
                throw new RedisException("重置过期时间失败！");
            }
            return String.valueOf(oldCode);
        }
    }


    /**
     * 生成邮件内容
     * @param captcha 验证码
     * @return {@link String } 邮件内容
     */
    private String generateEmailContent(String captcha,String type) {
        type=type.equals("login")?"登录":"注册";
        Context context = new Context();
        context.setVariable("verifyCode", Arrays.asList(captcha.split("")));
        context.setVariable("type",type);
        return templateEngine.process("EmailVerificationCode.html", context);
    }

    /**
     * 发送邮件
     * @param list 收件人列表
     * @param content 邮件内容
     */
    private void sendEmail(List<String> list, String content) {
        MailAccount account = createMailAccount();
        try {
            Mail.create(account)
                    .setTos(list.toArray(new String[0]))
                    .setTitle("验证您的电子邮箱地址")
                    .setContent(content)
                    .setHtml(true)
                    .setUseGlobalSession(false)
                    .send();
        } catch (Exception e) {
            log.error("邮件发送失败: {}", e.getMessage(), e);
            throw new BaseException(Constants.CODE_600, e.getMessage());
        }
    }

    /**
     * 创建邮件账户
     * @return {@link MailAccount } 邮件账户
     */
    private MailAccount createMailAccount() {
        MailAccount account = new MailAccount();
        account.setAuth(true);
        account.setHost(emailProperties.getHost());
        account.setPort(emailProperties.getPort());
        account.setFrom(emailProperties.getFrom());
        account.setUser(emailProperties.getUser());
        account.setPass(emailProperties.getPassword());
        account.setSslEnable(true);
        account.setStarttlsEnable(true);
        return account;
    }

}
