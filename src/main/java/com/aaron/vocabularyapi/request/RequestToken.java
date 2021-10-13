package com.aaron.vocabularyapi.request;

import javax.validation.constraints.NotBlank;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestToken
{
    // Username omitted
    @ApiModelProperty(value = "password", example = "some secret password", required = true)
    @NotBlank(message = "{error.token.bad_request}")
    private String password;

}