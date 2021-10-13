package com.aaron.vocabularyapi.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseError
{
    @ApiModelProperty(value = "message", example = "Error message", required = true)
    private String message;
}
