package entity;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Set;

@Data
@ApiModel(description = "websocket消息内容")
public class MsgVo {
    @ApiModelProperty(value = "用户id")
    private String  nickName;

    @ApiModelProperty(value = "消息")
    private String msg;

    @ApiModelProperty(value = "在线人数")
    private int count;

    @ApiModelProperty(value = "在线用户")
    private Set<String> onlineList;

    @ApiModelProperty(value = "发送者")
    private String fromUser;

    @ApiModelProperty(value = "发送者房间号")
    private String fromSid;

    @ApiModelProperty(value = "接受者")
    private String toUser;

    @ApiModelProperty(value = "发送者房间号")
    private String ToSid;

    @ApiModelProperty(value = "消息类型")
    private Integer messageType;


}
