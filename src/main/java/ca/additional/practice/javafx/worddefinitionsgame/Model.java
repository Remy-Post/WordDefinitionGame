package ca.additional.practice.javafx.worddefinitionsgame;

import java.util.*;

public class Model {
    private String word;
    private Map<String, ArrayList<String>> definitions;
    // Word Tpye : Difitionss
    private String wordType;

    public Random random = new Random();


    //region #Getters and Setters
    public String getWordType() {
        return wordType;
    }

    public void setWordType(String wordType) {
        this.wordType = wordType;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public Map<String, ArrayList<String>> getAllDefinitions() {
        return definitions;
    }

    public String getDefinition() {
        if (definitions == null || definitions.isEmpty()) {
            return "No definitions found.";
        }
        
        // 1. Get all keys (Word Types) into a list
        List<String> keys = new ArrayList<>(definitions.keySet());
        
        // 2. Pick a random Key
        String randomKey = keys.get(random.nextInt(keys.size()));
        
        // 3. Get the list of definitions for that key
        ArrayList<String> definitionList = definitions.get(randomKey);
        
        // 4. Pick a random definition from that list
        if (definitionList != null && !definitionList.isEmpty()) {
            return definitionList.get(random.nextInt(definitionList.size()));
        }
        
        return "No definition available.";
    }

    public void setDefinitions(Map<String, ArrayList<String>> definitions) {
        this.definitions = definitions;
    }
    //endregion



    //endregion

}
