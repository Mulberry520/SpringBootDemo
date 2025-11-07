package com.mulberry.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@Data
public class UserDTO {
    @NotBlank
    @Size(min = 4, max = 16)
    @Pattern(regexp = "^[a-zA-Z0-9_]+$")
    private String username;

    @NotBlank
    @Size(min = 6, max = 64)
//    @JsonIgnore
    private String password;

    @Size(max = 64)
    private String nickname;
    @Email
    @Size(max = 64)
    private String email;
    @Size(max = 128)
    private String userPic;
}
