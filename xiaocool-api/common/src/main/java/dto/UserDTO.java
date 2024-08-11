package dto;

import lombok.Data;

import java.util.List;

@Data
public class UserDTO {
    private String username;
    private String password;
    private String code;
    private List<String> role;
    private String email;
    private String avatar;
    private String telephone;
    private String LoginType;
    private Integer openId;
    private String token;

    private Integer userId;
    private String nickname;
    private String name;

}
