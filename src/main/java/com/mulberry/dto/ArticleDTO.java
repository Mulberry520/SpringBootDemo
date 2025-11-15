package com.mulberry.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ArticleDTO {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Integer id;
    @NotBlank
    @Size(max = 64)
    private String title;
    @NotBlank
    @Size(max = 10000)
    private String content;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String coverImg;
    @NotBlank
    @Size(max = 32)
    private String state;
    @NotNull
    private Integer categoryId;
    @JsonIgnore
    private Integer createUser;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String createUsername;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
}
