package domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * description:
 *
 * Date: 2020/9/16 11:59
 **/
@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum SmsLengthEnum {

    /** 4位短信验证码 */
    SMS_LENGTH_4(4),
    /** 6位短信验证码 */
    SMS_LENGTH_6(6),

    ;

    private int length;
}
