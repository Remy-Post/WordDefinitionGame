/*
 * File: Model.java
 * Purpose: Data model class for the Word Definition Game. Manages game state including
 *          the current word, definitions by part of speech, and provides methods to retrieve
 *          and manipulate word/definition data.
 * 
 * Global Variables:
 *   - word: String - The current word being used in the game
 *   - definitions: Map<String, ArrayList<String>> - Maps parts of speech to their definitions
 *   - wordType: String - The part of speech category for the current word
 *   - random: Random - Random number generator for selecting random definitions
 *   - dictionairyAPI: DictionairyAPI - API client for fetching definitions
 *   - wordAPI: WordAPI - API client for fetching random words
 *   - wordsFromFile: WordsFromFile - File-based word provider
 * 
 * Major Classes/Functions:
 *   - Model: Constructor that initializes the model with a valid word and definitions
 *   - newWord(): Fetches a new word from API or file
 *   - newDefinitions(): Fetches definitions for the current word
 *   - getDefinition(): Returns a random definition from available definitions
 *   - getAllDefinitionsAsList(): Converts all definitions to a flat ArrayList
 *   - Getters/Setters: Various accessor methods for model properties
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
     * Constructor that initializes the model with a valid word and its definitions.
     * 
     * What it does: Repeatedly attempts to fetch a new word and its definitions until
     *               both are successfully retrieved and the word has at least one definition.
     * 
     * Inputs: None
     * 
     * Return value: N/A (constructor)
     * 
     * Side effects: Makes API calls to fetch words and definitions, retries on failure
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
     * Fetches a new word from either the WordAPI or WordsFromFile.
     * 
     * What it does: If word is null, fetches from WordAPI; otherwise fetches from WordsFromFile.
     *               Updates the word field with the fetched word.
     * 
     * Inputs: None
     * 
     * Return value: void
     * 
     * Side effects: Makes API call or reads from file, updates the word field
     */
    private void newWord(){
        word = word == null ? wordAPI.getWord() : wordsFromFile.getWord() ;
        setWord(word);
    }

    /**
     * Fetches definitions for the current word from the dictionary API.
     * 
     * What it does: Uses the DictionairyAPI to retrieve all definitions grouped by
     *               part of speech for the current word.
     * 
     * Inputs: None
     * 
     * Return value: void
     * 
     * Side effects: Makes API call, updates the definitions map
     */
    private void newDefinitions(){
        definitions = dictionairyAPI.getDefinitions(word);
        setDefinitions(definitions);
    }

    //region #Getters and Setters
    /**
     * Gets the current word type (part of speech).
     * 
     * What it does: Returns the part of speech category for the current word.
     * 
     * Inputs: None
     * 
     * Return value: String - The word type (e.g., "noun", "verb", "adjective")
     * 
     * Side effects: None
     */
    public String getWordType() {
        return wordType;
    }

    /**
     * Sets the current word type (part of speech).
     * 
     * What it does: Updates the wordType field with the provided value.
     * 
     * Inputs:
     *   - wordType: String - The word type to set
     * 
     * Return value: void
     * 
     * Side effects: Updates the wordType field
     */
    public void setWordType(String wordType) {
        this.wordType = wordType;
    }

    /**
     * Gets the current word.
     * 
     * What it does: Returns the current word being used in the game.
     * 
     * Inputs: None
     * 
     * Return value: String - The current word
     * 
     * Side effects: None
     */
    public String getWord() {
        return word;
    }

    /**
     * Sets the current word.
     * 
     * What it does: Updates the word field with the provided value.
     * 
     * Inputs:
     *   - word: String - The word to set
     * 
     * Return value: void
     * 
     * Side effects: Updates the word field
     */
    public void setWord(String word) {
        this.word = word;
    }

    /**
     * Gets all definitions organized by part of speech.
     * 
     * What it does: Returns the complete definitions map with parts of speech as keys
     *               and lists of definitions as values.
     * 
     * Inputs: None
     * 
     * Return value: Map<String, ArrayList<String>> - Complete definitions map
     * 
     * Side effects: None
     */
    public Map<String, ArrayList<String>> getAllDefinitions() {
        return definitions;
    }

    /**
     * Gets all definitions as a flat list.
     * 
     * What it does: Converts the definitions map to a single ArrayList containing all
     *               definitions across all parts of speech.
     * 
     * Inputs: None
     * 
     * Return value: ArrayList<String> - Flat list of all definitions
     * 
     * Side effects: Prints definitions to console
     */
    public ArrayList<String> getAllDefinitionsAsList(){
        //Map to arraylist
        definitions.values().stream().flatMap(Collection::stream).toList().forEach(System.out::println);
        return new ArrayList<>(definitions.values().stream().flatMap(Collection::stream).toList());
    }

    /**
     * Gets a random definition from the available definitions.
     * 
     * What it does: Randomly selects a part of speech, then randomly selects a definition
     *               from that category. Sets the wordType to the selected part of speech.
     * 
     * Inputs: None
     * 
     * Return value: String - A random definition, or an error message if none available
     * 
     * Side effects: Updates the wordType field with the selected part of speech
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
     * What it does: Updates the definitions map with a new map of definitions
     *               organized by part of speech.
     * 
     * Inputs:
     *   - definitions: Map<String, ArrayList<String>> - New definitions map
     * 
     * Return value: void
     * 
     * Side effects: Updates the definitions field
     */
    public void setDefinitions(Map<String, ArrayList<String>> definitions) {
        this.definitions = definitions;
    }

    //endregion
}




