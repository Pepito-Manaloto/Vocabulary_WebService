package com.aaron.vocabulary.ws.model.others;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Iterator;
import java.util.Set;

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
     * Converts Vocabulary set into JSONArray.
     * @param set
     * @return JSONArray
     */
    public static final JSONArray toJSON(final Set<Vocabulary> set)
    {
        JSONArray result = new JSONArray();

        int index=0;
        Iterator<Vocabulary> iterator = set.iterator();

        while(iterator.hasNext())
        {
            result.add(index, toJSON(iterator.next()));
            index++;
        }

        return result;
    }

    /**
     * Obtains the hashed authentication key.
     * @return String the hashed authentication key
     */
    private static final String getAuthKey()
    {
        StringBuilder key = new StringBuilder();

        try
        {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] msgDigest = md.digest("aaron".getBytes());
   
            for(byte b: msgDigest)
            {
                key.append(String.format("%02x", b & 0xff));
            }
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }

        return key.toString();
    }
}
