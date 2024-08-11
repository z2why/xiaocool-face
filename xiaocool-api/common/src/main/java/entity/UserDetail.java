package entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 *
 * </p>
 *
 * @author
 * @since
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("user_detail")
public class UserDetail implements Serializable {
    private static final long serialVersionUID = 1L;
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 0未签到 1 已签到 2 迟到 3早退
     */
    @TableField("participate_status")
    private Integer participateStatus;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    @TableField("sign_in_date")
    private LocalDateTime signInDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    @TableField("sign_out_date")
    private LocalDateTime signOutDate;

    @TableField("user_id")
    private Long userId;

    @TableField("meeting_id")
    private Long meetingId;

    @TableField("check_status")//报名情况
    private Long checkStatus;

    @TableField("sign_adress")//签到地址
    private String signAdress;

    @TableField("supplementary_sign")//是否补签
    private String supplementarySign;

}
