package com.aaron.vocabularyapi.resource;

import static com.aaron.vocabularyapi.constant.ErrorCode.VOCABULARIES_NOT_FOUND;
import static reactor.core.publisher.Mono.*;

import java.util.List;
import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.aaron.vocabularyapi.constant.Language;
import com.aaron.vocabularyapi.entity.Vocabulary;
import com.aaron.vocabularyapi.response.ResponseVocabularies;
import com.aaron.vocabularyapi.service.MessageResourceService;
import com.aaron.vocabularyapi.service.VocabularyService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

// Mixed of reactive and non-reactive(because not using r2dbc due to no ORM support)
@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/vocabularies")
public class VocabularyResource
{
    private MessageResourceService msgResourceService;
    private VocabularyService vocabularyService;

    // Change to APPLICATION_NDJSON_VALUE once r2dbc and Flux is used for list result
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Get Vocabularies", notes = "Retrieves all vocabularies grouped by foreign language")
    public Mono<ResponseVocabularies> getVocabularies(
            @ApiParam @RequestParam(name = "last_updated", required = false) String lastUpdated)
    {
        log.info("getVocabularies. Start. lastUpdatedStr={}", lastUpdated);

        long recentlyAddedCount = vocabularyService.getRecentlyAddedCount(lastUpdated);
        Mono<Map<Language, List<Vocabulary>>> vocabularies = vocabularyService.getVocabulariesGroupedByForeignLanguage();

        return vocabularies.switchIfEmpty(defer(() -> msgResourceService.errorMessage(VOCABULARIES_NOT_FOUND)))
                .map(v -> ResponseVocabularies.builder()
                        .languages(v)
                        .recentlyAddedCount(recentlyAddedCount)
                        .build());
    }
}
