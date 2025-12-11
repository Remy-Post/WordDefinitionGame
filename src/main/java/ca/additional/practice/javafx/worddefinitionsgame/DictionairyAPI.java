/*
 * File: DictionairyAPI.java
 * Purpose: API client for fetching word definitions from the Dictionary API.
 *          Parses JSON responses to extract definitions organized by part of speech.
 * 
 * Global Variables:
 *   - jsonResponse: String - Stores the JSON response from the API
 *   - wordsFromFile: WordsFromFile - Fallback word provider
 *   - currentWord: String - The word being looked up
 * 
 * Major Classes/Functions:
 *   - DictionairyAPI: Constructor that initializes the API client
 *   - getDefinitions(String): Fetches and parses definitions for a given word
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
     * Constructor that initializes the DictionairyAPI.
     * 
     * What it does: Calls the parent API constructor to set up base API functionality.
     * 
     * Inputs: None
     * 
     * Return value: N/A (constructor)
     * 
     * Side effects: Initializes parent class with API URLs
     */
    DictionairyAPI() {
        super(); //initiate super class
    }

    /**
     * Fetches and parses definitions for a given word.
     * 
     * What it does: Makes an API request to get definitions, parses the JSON response,
     *               and organizes definitions by part of speech. Requires at least 3 definitions
     *               to be valid.
     * 
     * Inputs:
     *   - word: String - The word to look up definitions for
     * 
     * Return value: Map<String, ArrayList<String>> - Map of parts of speech to definition lists
     * 
     * Side effects: Makes HTTP API call, prints definitions and errors to console
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
