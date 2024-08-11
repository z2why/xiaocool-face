package entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import lombok.Data;

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

@Data
//中间传输表
@ApiModel(value = "MajorVO", description = "展示用")
@TableName("user_meeting")
public class UserMeeting implements Serializable {
    //meeting表
    private Long id;
    private String name;
    private String code;
    private String description;
    private Integer stage;
    private Integer status;
    private String address;
    private Integer show;
//    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")                    // 表示返回时间类型
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime realStartDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime realEndDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    private Long createUser;
    private Long updateUser;

    //userdetail表
    private  Long uid;
    private Integer participateStatus;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime signInDate;//加入会议时间
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime signOutDate;//退出会议时间
    private Integer checkStatus;
    private String userId;
    private Long meetingId;
    private String signAdress;
    private String supplementarySign;

}
