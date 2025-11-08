package com.mulberry.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

@Data
public class ArticleSimpleDTO {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Integer id;
    @NotBlank
    @Size(max = 64)
    private String title;
    @URL
    private String coverImg;
    @NotBlank
    @Size(max = 32)
    private String state;
    @NotNull
    private Integer categoryId;
    @JsonIgnore
    private Integer createUser;
}
