package com.aaron.vocabulary.ws;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import net.minidev.json.JSONObject;
import static com.aaron.vocabulary.ws.model.others.VocabularyUtils.*;
import static com.aaron.vocabulary.ws.model.others.VocabularyJSONKey.*;

/**
 * Web service for vocabularies.
 */
@Path("/")
public class VocabularyService
{
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
