package ca.additional.practice.javafx.worddefinitionsgame;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;

import java.io.IOException;

public class WordAPI extends API{
    private String jsonResponse = null;
    //controller
    WordAPI() {
        super(); //initiate super class
    }
    
    public String getWord(){
        try {
            jsonResponse = super.fetch(urls.get("word"));
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
