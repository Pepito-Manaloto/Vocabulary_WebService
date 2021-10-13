package com.aaron.vocabularyapi.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseToken
{
    @ApiModelProperty(value = "token", example = "abc.xyz.123-qwerty", required = true)
    private String token;
}