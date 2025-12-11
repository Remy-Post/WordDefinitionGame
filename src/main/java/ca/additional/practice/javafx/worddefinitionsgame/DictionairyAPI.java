package ca.additional.practice.javafx.worddefinitionsgame;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.*;

public class DictionairyAPI extends API {
    private String jsonResponse = null;
    private WordsFromFile wordsFromFile = new WordsFromFile();
    private String currentWord;

    //controller
    DictionairyAPI() {
        super(); //initiate super class
    }

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
            return null;
        }
        catch (Exception e) {
            System.out.println("Error parsing data from API");
            return definitions; //AKA an empty arraylist;
        }

        System.out.println(jsonResponse);

        return definitions;
    }
}
