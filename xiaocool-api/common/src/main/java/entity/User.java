package entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;


@TableName("user")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class User implements Serializable {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String username;
    private String password;

    /**
     * 0:未认证,1:已认证
     */
    @TableField("face_authentication")
    private Integer faceAuthentication;

    /**
     * 人脸图片
     */
    @TableField("face_image")
    private String faceImage;

    @TableField("nickname")
    private String nickname;
    private String name;
    @ApiModelProperty("角色")
    private String role;
    @TableField("sex")
    private Integer sex;//0女 1男
    @TableField("email")
    private String email;
    @TableField("avatar")
    private String avatar;//头像
    @TableField("telephone")
    private  String telephone;//电话号码
    @TableField("hobby")
    private String hobby;//爱好
    @TableField("profile")
    private String profile;

    private static final long serialVersionUID = 1L;

    public User(Integer id, String nickname) {
        this.id = id;
        this.nickname = nickname;
    }
}
