/**
 * File: Model.java
 * Purpose: MVC Model class that manages game state and data for the Word Definition Game
 * 
 * This class represents the data model in the MVC pattern. It handles fetching random words,
 * retrieving their definitions from APIs, organizing definitions by part of speech, and
 * providing access to this data for the controller. The model automatically retries if a
 * word has insufficient definitions (less than 3).
 * 
 * Global Variables:
 * - word: String - The current word being defined
 * - definitions: Map<String, ArrayList<String>> - Map of part-of-speech to list of definitions
 * - wordType: String - The current part of speech being used
 * - random: Random - Random number generator for selecting random definitions
 * - dictionairyAPI: DictionairyAPI - Service for fetching word definitions
 * - wordAPI: WordAPI - Service for fetching random words from API
 * - wordsFromFile: WordsFromFile - Service for fetching words from local file
 * 
 * Key Classes/Methods:
 * - Model: Constructor that initializes the model with a valid word and definitions
 * - newWord(): Fetches a new random word from API or file
 * - newDefinitions(): Retrieves definitions for the current word
 * - getWord(), setWord(): Access the current word
 * - getAllDefinitions(): Returns all definitions organized by part of speech
 * - getAllDefinitionsAsList(): Converts all definitions to a flat list
 * - getDefinition(): Returns a random definition from a random part of speech
 * - getWordType(), setWordType(): Access the current part of speech
 */
package ca.additional.practice.javafx.worddefinitionsgame;

import java.util.*;

public class Model {
    private String word = null;
    private Map<String, ArrayList<String>> definitions = new HashMap<>();

    // Word Tpye : Difitionss
    private String wordType;

    public Random random = new Random();

    private DictionairyAPI dictionairyAPI = new DictionairyAPI();
    private WordAPI wordAPI = new WordAPI();
    private WordsFromFile wordsFromFile = new WordsFromFile();


    /**
     * Constructs a new Model and initializes it with a valid word and definitions.
     * 
     * This constructor repeatedly attempts to fetch a word and its definitions until it finds
     * one with at least 3 definitions. If fetching fails, it sets word to null and retries.
     * This ensures the model always starts with playable data.
     * 
     * Inputs: None
     * 
     * Return value: N/A (constructor)
     * 
     * Side effects:
     * - Calls newWord() to fetch a random word
     * - Calls newDefinitions() to fetch definitions for the word
     * - Retries if definitions are empty or word is null
     * - May make multiple API calls until successful
     */
    Model() {
        do{
            try {
                newWord();
                newDefinitions();
                if (definitions.isEmpty()) throw new Exception();
            } catch (Exception e) {
                word = null;
            }
        }while (definitions.isEmpty() || word == null);
    }

    /**
     * Fetches a new random word from either API or local file.
     * 
     * This method attempts to get a word from WordAPI if word is null (first time),
     * otherwise it falls back to WordsFromFile. Sets the retrieved word as the current word.
     * 
     * Inputs: None
     * 
     * Return value: void
     * 
     * Side effects:
     * - Updates the word field with a new random word
     * - Calls setWord() to store the word
     * - May make an API call or read from local file
     */
    private void newWord(){
        word = word == null ? wordAPI.getWord() : wordsFromFile.getWord() ;
        setWord(word);
    }

    /**
     * Fetches definitions for the current word from the dictionary API.
     * 
     * This method uses DictionairyAPI to retrieve all definitions for the current word,
     * organized by part of speech (noun, verb, adjective, etc.), and stores them in
     * the definitions map.
     * 
     * Inputs: None
     * 
     * Return value: void
     * 
     * Side effects:
     * - Updates the definitions field with a map of part-of-speech to definition lists
     * - Makes an API call to dictionary service
     * - Calls setDefinitions() to store the results
     */
    private void newDefinitions(){
        definitions = dictionairyAPI.getDefinitions(word);
        setDefinitions(definitions);
    }

    //region #Getters and Setters
    /**
     * Gets the current word type (part of speech).
     * 
     * Inputs: None
     * Return value: String - The current part of speech (e.g., "noun", "verb")
     * Side effects: None
     */
    public String getWordType() {
        return wordType;
    }

    /**
     * Sets the current word type (part of speech).
     * 
     * Inputs:
     * - wordType: String - The part of speech to set
     * Return value: void
     * Side effects: Updates the wordType field
     */
    public void setWordType(String wordType) {
        this.wordType = wordType;
    }

    /**
     * Gets the current word.
     * 
     * Inputs: None
     * Return value: String - The current word
     * Side effects: None
     */
    public String getWord() {
        return word;
    }

    /**
     * Sets the current word.
     * 
     * Inputs:
     * - word: String - The word to set
     * Return value: void
     * Side effects: Updates the word field
     */
    public void setWord(String word) {
        this.word = word;
    }

    /**
     * Gets all definitions organized by part of speech.
     * 
     * Inputs: None
     * Return value: Map<String, ArrayList<String>> - Map where keys are parts of speech
     *               and values are lists of definitions
     * Side effects: None
     */
    public Map<String, ArrayList<String>> getAllDefinitions() {
        return definitions;
    }

    /**
     * Converts all definitions to a flat ArrayList.
     * 
     * This method flattens the definitions map into a single list by streaming all values
     * and collecting them. It also prints each definition to the console for debugging.
     * 
     * Inputs: None
     * Return value: ArrayList<String> - Flat list of all definitions across all parts of speech
     * Side effects: Prints each definition to System.out
     */
    public ArrayList<String> getAllDefinitionsAsList(){
        //Map to arraylist
        definitions.values().stream().flatMap(Collection::stream).toList().forEach(System.out::println);
        return new ArrayList<>(definitions.values().stream().flatMap(Collection::stream).toList());
    }

    /**
     * Gets a random definition from a random part of speech.
     * 
     * This method randomly selects a part of speech from the available keys, then randomly
     * selects one definition from that part of speech's list. Updates wordType to reflect
     * the selected part of speech.
     * 
     * Inputs: None
     * Return value: String - A randomly selected definition, or error message if none available
     * Side effects:
     * - Sets wordType to the randomly selected part of speech
     * - Returns "No definitions found." if definitions map is empty
     * - Returns "No definition available." if selected list is empty
     */
    public String getDefinition() {
        if (definitions == null || definitions.isEmpty()) {
            return "No definitions found.";
        }

        // 1. Get all keys (Word Types) into a list
        List<String> keys = new ArrayList<>(definitions.keySet());

        // 2. Pick a random Key
        String randomKey = keys.get(random.nextInt(keys.size()));
        setWordType(randomKey);

        // 3. Get the list of definitions for that key
        ArrayList<String> definitionList = definitions.get(randomKey);

        // 4. Pick a random definition from that list
        if (definitionList != null && !definitionList.isEmpty()) {
            return definitionList.get(random.nextInt(definitionList.size()));
        }

        return "No definition available.";
    }

    /**
     * Sets the definitions map.
     * 
     * Inputs:
     * - definitions: Map<String, ArrayList<String>> - Map of part-of-speech to definition lists
     * Return value: void
     * Side effects: Updates the definitions field
     */
    public void setDefinitions(Map<String, ArrayList<String>> definitions) {
        this.definitions = definitions;
    }

    //endregion
}




