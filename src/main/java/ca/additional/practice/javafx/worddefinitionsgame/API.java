/*
 * File: API.java
 * Purpose: Base API class that provides common functionality for making HTTP requests to external APIs.
 *          Contains URL mappings and a reusable fetch method for network operations.
 * 
 * Global Variables:
 *   - urls: Map<String, String> - Stores API endpoint URLs (key: API name, value: URL)
 *   - gson: Gson - Static Gson instance for JSON parsing operations
 * 
 * Major Classes/Functions:
 *   - API: Base class for API clients
 *   - fetch(String): Makes HTTP GET requests to specified URLs
 */
package ca.additional.practice.javafx.worddefinitionsgame;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class API {
    protected final Map<String, String> urls = new HashMap<>();
    // API name : URL
    // [definition] : https://api.dictionaryapi.dev/api/v2/entries/en_US/[word]
    // [word] : https://api.dictionaryapi.dev/api/v2/entries/en_US/[word]
    
    /**
     * Constructor that initializes API endpoint URLs.
     * 
     * What it does: Sets up the mapping of API names to their corresponding URLs
     *               for word generation and definition lookup.
     * 
     * Inputs: None
     * 
     * Return value: N/A (constructor)
     * 
     * Side effects: Populates the urls HashMap with API endpoint mappings
     */
    API() {
        urls.put("word", "https://random-word-api.herokuapp.com/word?");
        urls.put("definition", "https://api.dictionaryapi.dev/api/v2/entries/en/");
    }
    protected static final Gson gson = new Gson();
    
    /**
     * Reusable network helper method for making HTTP GET requests.
     * 
     * What it does: Creates an HTTP connection to the specified URL, sends a GET request,
     *               and reads the response into a String. Handles both successful responses
     *               and error streams.
     * 
     * Inputs:
     *   - urlStr: The URL string to send the GET request to
     * 
     * Return value: String - The response body from the HTTP request
     * 
     * Side effects: Opens network connection, makes HTTP request with 2-second timeout
     */
    protected String fetch(String urlStr) throws IOException {
        URL url = new URL(urlStr);

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setConnectTimeout(2000);
        conn.setReadTimeout(2000);


        int code = conn.getResponseCode();

        // Handle error streams gracefully
        try (BufferedReader br = new BufferedReader(new InputStreamReader(
                code >= 400 ? conn.getErrorStream() : conn.getInputStream(), StandardCharsets.UTF_8))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) sb.append(line);
            return sb.toString();
        }
    }
}
