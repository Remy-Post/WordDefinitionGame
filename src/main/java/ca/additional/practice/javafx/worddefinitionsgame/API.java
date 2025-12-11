/**
 * File: API.java
 * Purpose: Base class for all API service classes providing common HTTP request functionality
 * 
 * This abstract base class provides shared functionality for making HTTP requests to external APIs.
 * It defines common URL endpoints and a reusable fetch method for GET requests with timeout handling.
 * Subclasses (WordAPI, DictionairyAPI) extend this to access specific API services.
 * 
 * Global Variables:
 * - urls: Map<String, String> - Map of API names to their base URLs
 * - gson: Gson (static final) - Shared Gson instance for JSON parsing
 * 
 * Key Classes/Methods:
 * - API: Constructor that initializes API endpoint URLs
 * - fetch(): Protected method to make HTTP GET requests with timeout handling
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
     * Constructs the API base class and initializes endpoint URLs.
     * 
     * Sets up the base URLs for the word generation API and dictionary definition API.
     * These URLs are inherited by subclasses for making specific API requests.
     * 
     * Inputs: None
     * Return value: N/A (constructor)
     * Side effects:
     * - Populates urls map with "word" and "definition" endpoint base URLs
     */
    API() {
        urls.put("word", "https://random-word-api.herokuapp.com/word?");
        urls.put("definition", "https://api.dictionaryapi.dev/api/v2/entries/en/");
    }
    protected static final Gson gson = new Gson();
    /**
     * Makes an HTTP GET request to the specified URL and returns the response as a string.
     * 
     * This reusable method handles HTTP connections, sets timeouts, reads the response stream,
     * and gracefully handles both successful responses and error responses. It's used by
     * subclasses to fetch data from external APIs.
     * 
     * Inputs:
     * - urlStr: String - The complete URL to fetch from
     * 
     * Return value: String - The response body from the API (JSON or error message)
     * 
     * Side effects:
     * - Opens an HTTP connection to the specified URL
     * - Sets connection and read timeouts to 2000ms
     * - Reads from either input stream (success) or error stream (failure)
     * - Closes the BufferedReader after reading
     * 
     * @throws IOException if network connection fails or times out
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
