package com.aaron.vocabulary.ws;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Set;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.aaron.vocabulary.ws.bean.ForeignLanguage.Language;
import com.aaron.vocabulary.ws.bean.Vocabulary;
import com.aaron.vocabulary.ws.model.db.VocabularyDb;
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
    private static final String DEFAULT_DATE = "2000-01-01 00:00:00";

    static
    {
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
    }

    /**
     * Test for no resource path given.
     */
    @GET
    @Produces("text/plain")
    public String getDefault()
    {
        return "Test";
    }

    /**
     * Gets all vocabularies + the total number of recently added vocabularies from the database.
     * @param authenticationHeader header for authentication
     * @param lastUpdated optional GET parameter
     * @return Response JSON formatted
     * @throws ParseException this exception will not be thrown because the String being parsed is static
     */
    @Path("get")
    @GET
    @Produces("application/json")
    public Response getVocabularies(@HeaderParam("Authorization") String authenticationHeader, @QueryParam("last_updated") String lastUpdated) throws ParseException
    {
        Status responseCode = Status.UNAUTHORIZED;
        JSONObject result = new JSONObject();

        if(AUTH_KEY.equals(authenticationHeader))
        {
            VocabularyDb vocabDb = new VocabularyDb();
            Date lastUpdatedDate;
            try
            {
                if(lastUpdated != null)
                {
                    lastUpdatedDate = simpleDateFormat.parse(lastUpdated);
                }
                else
                {
                    lastUpdatedDate = simpleDateFormat.parse(DEFAULT_DATE);
                }
            }
            catch (ParseException e)
            {
                lastUpdatedDate = simpleDateFormat.parse(DEFAULT_DATE);
            }

            int recentlyAdded = vocabDb.getRecentlyAddedCount(lastUpdatedDate);
            result.put(recently_added_count.name(), recentlyAdded);

            for(Language language: Language.values())
            {
                Set<Vocabulary> vocabList = vocabDb.getVocabularies(language);
                JSONArray jsonArray = VocabularyUtils.toJSON(vocabList);

                result.put(language.name(), jsonArray);
            }

            result.put(http_response_text.name(), "Success");
            responseCode = Status.OK;
        }
        else
        {
            result.put(http_response_text.name(), "Unauthorized access: Please provide authentication key. auth_key=" + AUTH_KEY);
        }

        return Response.status(responseCode)
                       .entity(result.toJSONString())
                       .build();
    }
}
