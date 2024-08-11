package dto;

import lombok.Data;

@Data
public class UserRoleDTO {

    private Integer userId;
    private Integer roleId;
    private String username;
    private String role;


}
