package com.aaron.vocabulary.ws;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.aaron.vocabulary.ws.bean.Vocabulary;
import com.aaron.vocabulary.ws.model.db.VocabularyDb;
import com.aaron.vocabulary.ws.model.others.ForeignLanguageEnum;
import com.aaron.vocabulary.ws.model.others.VocabularyUtils;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import static com.aaron.vocabulary.ws.model.others.VocabularyUtils.*;
import static com.aaron.vocabulary.ws.model.others.VocabularyJSONKey.*;

/**
 * Web service for vocabularies.
 */
@Path("/")
public class VocabularyService
{
    private static final SimpleDateFormat simpleDateFormat;
    static
    {
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
    }

    /**
     * Gets all vocabularies + the total number of recently added vocabularies from the database.
     * @param authenticationHeader header for authentication
     * @param lastUpdated optional GET parameter
     * @return Response JSON formatted
     */
    @Path("get")
    @GET
    @Produces("application/json")
    public Response getVocabularies(@HeaderParam("Authorization") String authenticationHeader, @QueryParam("last_updated") String lastUpdated)
    {
        Status responseCode = Status.UNAUTHORIZED;
        JSONObject result = new JSONObject();

        if(AUTH_KEY.equals(authenticationHeader))
        {
            VocabularyDb vocabDb = new VocabularyDb();

            try
            {
                simpleDateFormat.parse(lastUpdated);
            }
            catch (ParseException e)
            {
                lastUpdated = "2000-01-01 00:00:00";
            }

            int recentlyAdded = vocabDb.getRecentlyAddedCount(lastUpdated);
            result.put(recently_added_count.name(), recentlyAdded);

            for(ForeignLanguageEnum language: ForeignLanguageEnum.values())
            {
                List<Vocabulary> vocabList = vocabDb.getVocabularies(language);
                JSONArray jsonArray = VocabularyUtils.toJSON(vocabList);

                result.put(language.name(), jsonArray);
            }

            result.put(http_response_text.name(), "Success");
            responseCode = Status.OK;
        }
        else
        {
            result.put(http_response_text.name(), "Unauthorized access: Please provide authentication key.");
        }

        return Response.status(responseCode)
                       .entity(result)
                       .build();
    }
}
