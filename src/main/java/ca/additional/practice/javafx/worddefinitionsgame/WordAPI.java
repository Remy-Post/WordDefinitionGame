/**
 * File: WordAPI.java
 * Purpose: API service class for fetching random words from an external word generation API
 * 
 * This class extends the base API class to provide functionality for retrieving random English
 * words. It makes requests to the random-word-api service and parses the JSON response to
 * extract a single word for use in the game.
 * 
 * Global Variables:
 * - jsonResponse: String - Stores the raw JSON response from the API
 * 
 * Key Classes/Methods:
 * - WordAPI: Constructor that initializes the parent API class
 * - getWord(): Fetches a random word from the API and returns it as a string
 */
package ca.additional.practice.javafx.worddefinitionsgame;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;

import java.io.IOException;

public class WordAPI extends API{
    private String jsonResponse = null;
    /**
     * Constructs a WordAPI instance.
     * 
     * Calls the parent API constructor to initialize base API functionality including
     * endpoint URLs.
     * 
     * Inputs: None
     * Return value: N/A (constructor)
     * Side effects: Initializes parent API class
     */
    WordAPI() {
        super(); //initiate super class
    }
    
    /**
     * Fetches a random English word from the random-word-api service.
     * 
     * This method makes an HTTP GET request to the word API, parses the JSON array response,
     * and extracts the first word from the array. Handles errors gracefully by printing error
     * messages and returning null if the API call or parsing fails.
     * 
     * Inputs: None
     * 
     * Return value: String - A random English word, or null if fetching/parsing fails
     * 
     * Side effects:
     * - Makes an HTTP request to random-word-api.herokuapp.com
     * - Prints error messages to System.err if failures occur
     * - Updates jsonResponse field with the fetched word
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

