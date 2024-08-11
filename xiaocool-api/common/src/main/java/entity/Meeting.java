package entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

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
@Accessors(chain = true)
@TableName("meeting")
public class Meeting implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("`name`")
    private String name;

    @TableField("`code`")
    private String code;

    @TableField("description")
    private String description;

    /**
     *  0.未开始 1.进行中 2已结束，3暂缓中
     */
    @TableField("stage")
    private Integer stage;

    /**
     * 0:禁，1可
     */
    @TableField("`status`")
    private Integer status;

    @TableField("address")
    private String address;

    /**
     * 0私有1公开
     */
    @TableField("`show`")
    private Integer show;


    /**
     * 预计开始时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    @TableField("start_date")
    private LocalDateTime startDate;

    /**
     * 预计结束时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    @TableField("end_date")
    private LocalDateTime endDate;

    /**
     * 实际开始时间
     */
    @TableField("real_start_date")
    private LocalDateTime realStartDate;

    /**
     * 实际结束时间
     */
    @TableField("real_end_date")
    private LocalDateTime realEndDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    @TableField(value = "create_time" ,fill = FieldFill.INSERT )
    private LocalDateTime createTime;

    @TableField(value = "update_time",fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableField(value = "create_user" ,fill = FieldFill.INSERT)
    private Long createUser;




}