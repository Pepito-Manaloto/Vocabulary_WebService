package com.aaron.vocabularyapi.response;

import java.util.List;
import java.util.Map;

import com.aaron.vocabularyapi.constant.Language;
import com.aaron.vocabularyapi.entity.Vocabulary;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseVocabularies
{
    private Map<Language, List<Vocabulary>> languages;

    @ApiModelProperty(value = "recently_added_count", example = "88", required = true)
    @JsonProperty("recently_added_count")
    private long recentlyAddedCount;
}
