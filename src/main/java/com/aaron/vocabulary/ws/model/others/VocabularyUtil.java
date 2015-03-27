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
public class VocabularyUtil
{
    public static final String AUTH_KEY = getAuthKey("aaron");

    /**
     * Converts Vocabulary object into JSON.
     * @param vocabulary
     * @return JSONObject
     */
    public static final JSONObject toJSON(final Vocabulary vocabulary)
    {
        JSONObject result = new JSONObject();

        if(vocabulary == null)
        {
            return result;
        }

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

        if(set == null)
        {
            return result;
        }
        
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
     * @param plainText the readable key
     * @return String the hashed authentication key, empty string if plainText is empty
     */
    public static final String getAuthKey(final String plainText)
    {
        StringBuilder key = new StringBuilder();

        if(plainText == null || plainText.trim().length() < 1)
        {
            return key.toString();
        }

        try
        {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] msgDigest = md.digest(plainText.getBytes());
   
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