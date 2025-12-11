/**
 * File: DictionairyAPI.java
 * Purpose: API service class for fetching word definitions from an external dictionary API
 * 
 * This class extends the base API class to provide functionality for retrieving word definitions
 * organized by part of speech. It queries the dictionaryapi.dev service, parses the complex JSON
 * response structure, and organizes definitions into a map keyed by part of speech. Ensures
 * words have at least 3 definitions to be suitable for gameplay.
 * 
 * Global Variables:
 * - jsonResponse: String - Stores the raw JSON response from the dictionary API
 * - wordsFromFile: WordsFromFile - Fallback service for getting words from local file
 * - currentWord: String - The word currently being looked up
 * 
 * Key Classes/Methods:
 * - DictionairyAPI: Constructor that initializes the parent API class
 * - getDefinitions(): Fetches all definitions for a given word, organized by part of speech
 */
package ca.additional.practice.javafx.worddefinitionsgame;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.*;

public class DictionairyAPI extends API {
    private String jsonResponse = null;
    private WordsFromFile wordsFromFile = new WordsFromFile();
    private String currentWord;

    /**
     * Constructs a DictionairyAPI instance.
     * 
     * Calls the parent API constructor to initialize base API functionality and creates
     * a WordsFromFile instance for potential fallback word sources.
     * 
     * Inputs: None
     * Return value: N/A (constructor)
     * Side effects:
     * - Initializes parent API class
     * - Creates WordsFromFile instance
     */
    DictionairyAPI() {
        super(); //initiate super class
    }

    /**
     * Fetches all definitions for a given word from the dictionary API.
     * 
     * This method makes an HTTP request to the dictionary API with the specified word,
     * parses the nested JSON structure to extract definitions and their parts of speech,
     * and organizes them into a map. It requires at least 3 total definitions for the word
     * to be considered valid for gameplay. Returns an empty map if the API fails, parsing
     * fails, or fewer than 3 definitions are found.
     * 
     * Inputs:
     * - word: String - The word to look up definitions for
     * 
     * Return value: Map<String, ArrayList<String>> - Map where keys are parts of speech
     *               (e.g., "noun", "verb") and values are lists of definition strings.
     *               Returns empty map on failure.
     * 
     * Side effects:
     * - Makes an HTTP request to api.dictionaryapi.dev
     * - Updates currentWord field
     * - Prints definition count and debug messages to console
     * - Prints error messages to System.err or System.out for various failure cases
     * - Prints the full JSON response for debugging
     */
    public Map<String, ArrayList<String>> getDefinitions(String word) {
        System.out.println("searching for definitions");
        Map<String, ArrayList<String>> definitions = new HashMap<>();
        currentWord = word;

        //Use the resuable private method to fetch the data
        try {
            jsonResponse = super.fetch(
                    super.urls.get("definition") + word //Gets the api and fetch from Parent class
            );
        } catch (Exception e) {
            System.err.println("Error fetching data from API");

            return definitions; //AKA an empty arraylist
        }


        try {
            int definitionsCount = 0;
            JsonArray jsonObj = JsonParser.parseString(jsonResponse).getAsJsonArray();

            //Drilling concept, of URLs
            JsonObject entry = jsonObj.get(0).getAsJsonObject();
            JsonArray meanings = entry.get("meanings").getAsJsonArray();

            //As a word can have multiple meanings, we need to loop through them
            for (int j = 0; j < meanings.size(); j++) {
                //For Each Meaning
                JsonObject meaning = meanings.get(j).getAsJsonObject();
                JsonArray defs = meaning.get("definitions").getAsJsonArray();

                //Get partOfSpeech
                String partOfSpeech = meaning.get("partOfSpeech").getAsString();
                definitions.putIfAbsent(partOfSpeech, new ArrayList<>());

                //loop throw each meaning
                for (int k = 0; k < defs.size(); k++) {
                    JsonObject def = defs.get(k).getAsJsonObject();

                    String definition = def.get("definition").getAsString();
                    System.out.println(definition);
                    definitionsCount++;
                    definitions.get(partOfSpeech).add(definition);
                }
            }
            if (definitionsCount < 3) {
                throw new IllegalArgumentException("Not enough definitions");
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Not enough definitions found for '" + word + "', trying another word...");
            return definitions;
        }
        catch (Exception e) {
            System.out.println("Error parsing data from API");
            return definitions; //AKA an empty arraylist;
        }

        System.out.println(jsonResponse);

        return definitions;
    }
}
