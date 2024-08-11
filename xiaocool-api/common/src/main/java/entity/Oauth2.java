package entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;



/**
 * 此表能表示用户与第三方平台的关联关系
 */
@TableName("oauth2")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Oauth2 {
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 平台名称
     */
    @TableField("client_name")
    private String clientName;

    /**
     * 用户id
     */
    @TableField("user_id")
    private Integer userId;

    /**
     *  平台开放的用户唯一标识id
     */
    @TableField("open_id")
    private Integer openId;


}
