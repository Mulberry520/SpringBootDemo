package com.mulberry.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CategorySimpleDTO {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Integer id;
    @NotBlank
    @Size(max = 32)
    private String categoryName;
    @NotBlank
    @Size(max = 32)
    private String categoryAlias;
    @JsonIgnore
    private Integer createUserid;
}
