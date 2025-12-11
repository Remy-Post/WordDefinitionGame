/**
 * File: WordsFromFile.java
 * Purpose: Service class for loading and retrieving words from a local JSON file
 * 
 * This class provides a fallback mechanism for word generation when the external word API
 * is unavailable. It reads a JSON array of common English words from a bundled resource file
 * (words.json) and provides random word selection from that list.
 * 
 * Global Variables:
 * - gson: Gson (static final) - Gson instance for parsing JSON
 * - rand: Random - Random number generator for selecting random words
 * 
 * Key Classes/Methods:
 * - WordsFromFile: Implicit default constructor
 * - ToGson(): Converts the JSON file content to a JsonArray
 * - getWord(): Returns a random word from the local word list
 * - getJson(): Reads the words.json file and returns its content as a string
 */
package ca.additional.practice.javafx.worddefinitionsgame;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Random;

public class WordsFromFile {
    private static final Gson gson = new Gson();
    private Random rand = new Random();


    /**
     * Converts the JSON file content to a Gson JsonArray.
     * 
     * This method reads the JSON string from getJson() and parses it into a JsonArray
     * for easier access to individual words.
     * 
     * Inputs: None
     * 
     * Return value: JsonArray - The parsed JSON array of words
     * 
     * Side effects: Calls getJson() to read the file
     */
    public JsonArray ToGson() {
        return gson.fromJson(getJson(), JsonArray.class);
    }

    /**
     * Returns a random word from the local word list.
     * 
     * This method converts the JSON file to a JsonArray, selects a random index (0-999),
     * and returns the word at that position. Handles exceptions by printing error messages
     * and returning null.
     * 
     * Inputs: None
     * 
     * Return value: String - A random word from words.json, or null if reading fails
     * 
     * Side effects:
     * - Calls ToGson() which reads the JSON file
     * - Prints error message to System.err if exception occurs
     */
    public String getWord(){

        try {
            return ToGson().getAsJsonArray()
                    .asList().toArray()
                    [rand.nextInt(1000)].toString();
        } catch (Exception e) {
            System.err.println("Error getting word from file");
            return null;
        }
    }

    /**
     * Reads the words.json file from resources and returns its content as a string.
     * 
     * This private method loads the bundled words.json file using the class resource loader,
     * reads it line by line, and concatenates all lines into a single JSON string. Uses an
     * absolute resource path to ensure the file is found in the classpath.
     * 
     * Inputs: None
     * 
     * Return value: String - The complete JSON content of words.json, or empty string if read fails
     * 
     * Side effects:
     * - Opens an InputStream to read the resource file
     * - Prints stack trace and error message to System.err if exception occurs
     * - Closes the BufferedReader after reading
     */
    private String getJson() {
        StringBuilder sb = new StringBuilder();
        try {
            // UPDATED: Use absolute path with leading slash
            InputStream is = getClass().getResourceAsStream("/ca/additional/practice/javafx/worddefinitionsgame/words.json");

            if (is == null) {
                throw new NullPointerException("Cannot find words.json file");
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace(); // This will help you see the specific error if it fails
            System.err.println("Error reading words.json");
        }
        return sb.toString();
    }


}
