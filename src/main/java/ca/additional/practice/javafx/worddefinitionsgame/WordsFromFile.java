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


    public JsonArray ToGson() {
        return gson.fromJson(getJson(), JsonArray.class);
    }

    public String getWord(){
        return ToGson().get(rand.nextInt(ToGson().size())).getAsString();
    }

    private String getJson() {
        StringBuilder sb = new StringBuilder();
        try{
            //https://www.youtube.com/watch?v=ScUJx4aWRi0
            InputStream is = getClass().getResourceAsStream("/ca/additional/practice/javafx/worddefinitionsgame/words.json");
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            br.close();
        }
        catch (Exception e){
            System.err.println("Error reading words.json");
        }
        return sb.toString();
    }


}
