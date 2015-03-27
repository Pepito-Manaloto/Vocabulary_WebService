package com.aaron.vocabulary.ws.model.others;

import com.aaron.vocabulary.ws.bean.ForeignLanguage;
import com.aaron.vocabulary.ws.bean.ForeignLanguage.Language;
import com.aaron.vocabulary.ws.bean.Vocabulary;

import java.util.HashSet;
import java.util.Set;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

import org.junit.BeforeClass;
import org.junit.Test;

import static com.aaron.vocabulary.ws.model.others.VocabularyJSONKey.english_word;
import static com.aaron.vocabulary.ws.model.others.VocabularyJSONKey.foreign_word;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for VocabularyUtil class.
 */
public class VocabularyUtilTest
{
    // Objects containing method-to-test results.
    private static Vocabulary vocabularyTestObj;
    private static Set<Vocabulary> vocabulariesTestObj;

    // Objects containing expected results.
    private static JSONObject expectedJsonObj;
    private static JSONArray expectedJsonObjArr;

    @BeforeClass
    public static void initialize()
    {
        vocabularyTestObj = new Vocabulary("Hi", "こんにちは", new ForeignLanguage(Language.Japanese));

        vocabulariesTestObj = new HashSet<>();

        vocabulariesTestObj.add(new Vocabulary("I", "goá", new ForeignLanguage(Language.Hokkien)));
        vocabulariesTestObj.add(new Vocabulary("Goodbye", "さようなら", new ForeignLanguage(Language.Hokkien)));
        vocabulariesTestObj.add(new Vocabulary("Goodbye", "再見", new ForeignLanguage(Language.Hokkien)));

        expectedJsonObj = new JSONObject();
        expectedJsonObj.put(english_word.name(), "Hi");
        expectedJsonObj.put(foreign_word.name(), "こんにちは");

        JSONObject jsonObj1 = new JSONObject();
        jsonObj1.put(english_word.name(), "I");
        jsonObj1.put(foreign_word.name(), "goá");

        JSONObject jsonObj2 = new JSONObject();
        jsonObj2.put(english_word.name(), "Goodbye");
        jsonObj2.put(foreign_word.name(), "さようなら");

        JSONObject jsonObj3 = new JSONObject();
        jsonObj3.put(english_word.name(), "Goodbye");
        jsonObj3.put(foreign_word.name(), "再見");

        expectedJsonObjArr = new JSONArray();
        expectedJsonObjArr.add(0, jsonObj1);
        expectedJsonObjArr.add(1, jsonObj2);
        expectedJsonObjArr.add(2, jsonObj3);
    }

    @Test
    public void testToJSONValid()
    {
        assertEquals(expectedJsonObj, VocabularyUtil.toJSON(vocabularyTestObj));

        assertEquals(expectedJsonObjArr.size(), VocabularyUtil.toJSON(vocabulariesTestObj).size());
        for(Object json: VocabularyUtil.toJSON(vocabulariesTestObj))
        {
            assertTrue(expectedJsonObjArr.contains(json));
        }

        assertNotEquals(expectedJsonObj, VocabularyUtil.toJSON(new Vocabulary("", "", null)));

        assertNotEquals(expectedJsonObjArr.size(), VocabularyUtil.toJSON(new HashSet<Vocabulary>()).size());
        for(Object json: VocabularyUtil.toJSON(new HashSet<Vocabulary>()))
        {
            assertFalse(expectedJsonObjArr.contains(json));
        }
    }

    @Test
    public void testToJSONInvalid()
    {
        assertEquals(new JSONObject(), VocabularyUtil.toJSON((Vocabulary) null));
        assertEquals(new JSONArray(), VocabularyUtil.toJSON((Set<Vocabulary>) null));
    }

    @Test
    public void testGetAuthKeyValid()
    {
        assertEquals("449a36b6689d841d7d27f31b4b7cc73a", VocabularyUtil.getAuthKey("aaron"));
        assertNotEquals("449a36b6689d841d7d27f31b4b7cc73a", VocabularyUtil.getAuthKey("Aaron"));
    }
    
    @Test
    public void testGetAuthKeyInvalid()
    {
        assertEquals("", VocabularyUtil.getAuthKey(null));
        assertEquals("", VocabularyUtil.getAuthKey(""));
        assertEquals("", VocabularyUtil.getAuthKey(" "));
    }
}
