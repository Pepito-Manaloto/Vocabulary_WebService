package com.aaron.vocabulary.ws.model.others;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Utility class for vocabulary service
 */
public class VocabularyUtils
{
    public static final String AUTH_KEY = getAuthKey();

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
