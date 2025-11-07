package com.mulberry.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateDTO {
    @NotBlank
    @Size(min = 6, max = 64)
    private String password;

    @Size(min = 6, max = 64)
    private String newPassword;

    @Size(min = 6, max = 64)
    private String repeatPassword;

    @Size(max = 64)
    private String nickname;

    @Email
    @Size(max = 64)
    private String email;
}
