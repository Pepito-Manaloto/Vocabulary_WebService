package com.aaron.vocabulary.ws.model.others;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

import com.aaron.vocabulary.ws.bean.Vocabulary;

import static com.aaron.vocabulary.ws.model.others.VocabularyJSONKey.*;

/**
 * Utility class for vocabulary service
 */
public class VocabularyUtils
{
    public static final String AUTH_KEY = getAuthKey();

    /**
     * Converts Vocabulary object into JSON.
     * @param vocabulary
     * @return JSONObject
     */
    public static final JSONObject toJSON(final Vocabulary vocabulary)
    {
        JSONObject result = new JSONObject();

        result.put(english_word.name(), vocabulary.getEnglishWord());
        result.put(foreign_word.name(), vocabulary.getForeignWord());

        return result;
    }

    /**
     * Converts Vocabulary List into JSONArray.
     * @param list
     * @return JSONArray
     */
    public static final JSONArray toJSON(final List<Vocabulary> list)
    {
        JSONArray result = new JSONArray();
        int size = list.size();

        for(int index=0; index < size; index++)
        {
            result.add(index, toJSON(list.get(index)));
        }
        
        return result;
    }

    /**
     * Obtains the hashed authentication key.
     * @return String the hashed authentication key
     */
    private static final String getAuthKey()
    {
        String key = "";

        try
        {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] msgDigest = md.digest("aaron".getBytes("UTF-8"));
            key = new String(msgDigest, "UTF-8");
        }
        catch (NoSuchAlgorithmException | UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }

        return key;
    }
}
