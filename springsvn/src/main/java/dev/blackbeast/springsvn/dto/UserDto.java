package dev.blackbeast.springsvn.dto;

import dev.blackbeast.springsvn.domain.User;
import lombok.Data;

@Data
public class UserDto {
    private final String name;
    private final String username;
    private final Boolean isAdmin;

    public UserDto(User user) {
        if(user != null) {
            this.name = user.getName();
            this.username = user.getUsername();
            this.isAdmin = user.getIsAdmin();
        } else {
            this.name = null;
            this.username = null;
            this.isAdmin = true;
        }
    }

}
