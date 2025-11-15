package com.mulberry.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

@Data
public class ArticleUpdateDTO {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Integer id;
    @Size(max = 64)
    private String title;
    @Size(max = 10000)
    private String content;
    @Size(max = 32)
    private String state;
    private Integer categoryId;
    @JsonIgnore
    private Integer createUser;
}
