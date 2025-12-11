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

        try {
            return ToGson().getAsJsonArray()
                    .asList().toArray()
                    [rand.nextInt(1000)].toString();
        } catch (Exception e) {
            System.err.println("Error getting word from file");
            return null;
        }
    }

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
