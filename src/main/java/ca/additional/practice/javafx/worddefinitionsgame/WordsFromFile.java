/*
 * File: WordsFromFile.java
 * Purpose: Provides words from a local JSON file as a fallback or alternative to API calls.
 *          Reads from words.json resource file and returns random words.
 * 
 * Global Variables:
 *   - gson: Gson - Static Gson instance for JSON parsing
 *   - rand: Random - Random number generator for selecting words
 * 
 * Major Classes/Functions:
 *   - ToGson(): Converts JSON string to JsonArray
 *   - getWord(): Returns a random word from the file
 *   - getJson(): Reads and returns the JSON file contents as a string
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
     * Converts JSON string to JsonArray.
     * 
     * What it does: Parses the JSON string from getJson() into a JsonArray object.
     * 
     * Inputs: None
     * 
     * Return value: JsonArray - The parsed JSON array of words
     * 
     * Side effects: Reads from file via getJson()
     */
    public JsonArray ToGson() {
        return gson.fromJson(getJson(), JsonArray.class);
    }

    /**
     * Gets a random word from the words.json file.
     * 
     * What it does: Converts JSON to array, selects a random word from up to 1000 entries.
     * 
     * Inputs: None
     * 
     * Return value: String - A random word from the file, or null if an error occurs
     * 
     * Side effects: Reads from file, prints errors to console
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
     * Reads the words.json file and returns its contents as a string.
     * 
     * What it does: Locates the words.json resource file, reads all lines,
     *               and concatenates them into a single string.
     * 
     * Inputs: None
     * 
     * Return value: String - The complete JSON file contents
     * 
     * Side effects: Opens and reads from file, prints errors to console
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
