package entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;
@Data
public class MsgUser {
    //msg表
    @TableId(type = IdType.AUTO)
    private Long msgId;
    private Integer userId;
    private Integer meetingId;
    private String content;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss:SSS")
    private LocalDateTime sendTime;
    private String avatar;//头像
    private String nickname;

}
