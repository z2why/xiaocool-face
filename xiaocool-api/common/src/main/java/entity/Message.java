package entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;

@TableName("message")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Message implements Serializable{

        @TableId(type = IdType.AUTO)
        private Integer msgId;
        private Integer userId;
        private Integer meetingId;
        private String content;
    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss:SSS")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss:SSS")
    @TableField("sendTime")
    private LocalDateTime sendTime;




}
