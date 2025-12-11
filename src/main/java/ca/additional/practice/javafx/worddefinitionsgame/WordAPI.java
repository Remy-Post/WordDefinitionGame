/*
 * File: WordAPI.java
 * Purpose: API client for fetching random words from the Random Word API.
 *          Parses JSON responses to extract a single random word.
 * 
 * Global Variables:
 *   - jsonResponse: String - Stores the JSON response from the API
 * 
 * Major Classes/Functions:
 *   - WordAPI: Constructor that initializes the API client
 *   - getWord(): Fetches and returns a random word from the API
 */
package ca.additional.practice.javafx.worddefinitionsgame;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;

import java.io.IOException;

public class WordAPI extends API{
    private String jsonResponse = null;
    
    /**
     * Constructor that initializes the WordAPI.
     * 
     * What it does: Calls the parent API constructor to set up base API functionality.
     * 
     * Inputs: None
     * 
     * Return value: N/A (constructor)
     * 
     * Side effects: Initializes parent class with API URLs
     */
    WordAPI() {
        super(); //initiate super class
    }
    
    /**
     * Fetches a random word from the Random Word API.
     * 
     * What it does: Makes an API request, parses the JSON response, and extracts
     *               the first word from the returned array.
     * 
     * Inputs: None
     * 
     * Return value: String - A random word, or null if the API call fails
     * 
     * Side effects: Makes HTTP API call, prints errors to console
     */
    public String getWord(){
        try {
            jsonResponse = super.fetch(super.urls.get("word"));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        if (jsonResponse == null){
            System.err.println("Error fetching data from API");
            return null;
        };

        try {
            //Gets root element
            JsonArray jsonArray = JsonParser.parseString(jsonResponse).getAsJsonArray();

            jsonResponse = jsonArray.get(0).getAsString();
        }
        catch (Exception e){
            System.err.println("Error parsing data from API");
            return null;
        }

        return jsonResponse;
    }
}

