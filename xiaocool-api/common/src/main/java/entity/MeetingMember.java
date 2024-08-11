package entity;


import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;

@ApiModel(value = "MajorVO", description = "展示用")
@Data
public class MeetingMember implements Serializable {
    //user表
    private Long uid;
    private String username;
    private String password;
    private Integer faceAuthentication;
    private String faceImage;
    private String uname;
    private String role;
    private Integer sex;//0女 1男
    private String email;
    private String avatar;//头像
    private  String telephone;//电话号码
    private String hobby;//爱好
    private String profile;
    //userdetail
    private Long udid;
    private Integer participateStatus;
    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime signInDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime signOutDate;
    private Long userId;
    private Long meetingId;
    private Long checkStatus;
    private String signAdress;
    private String supplementarySign;

    //meeting
    private Long id;
    private String mname;
    private String code;
    private String description;
    private Integer stage;
    private Integer status;
    private String address;
    private Integer show;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Long createUser;

    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

}
