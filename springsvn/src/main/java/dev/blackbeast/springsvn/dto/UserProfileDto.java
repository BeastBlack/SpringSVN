package dev.blackbeast.springsvn.dto;

import dev.blackbeast.springsvn.domain.User;
import lombok.Data;

@Data
public class UserProfileDto {
    private Long id;
    private String username;
    private String password;
    private String name;

    public UserProfileDto(User user) {
        if(user != null) {
            this.id = user.getId();
            this.username = user.getUsername();
            this.password = "";
            this.name = user.getName();
        }
    }
}
