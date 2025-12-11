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
    API() {
        urls.put("word", "https://random-word-api.herokuapp.com/word?");
        urls.put("definition", "https://api.dictionaryapi.dev/api/v2/entries/en/");
    }
    protected static final Gson gson = new Gson();
    /**
     * Reusable network helper.
     * Identical logic to DictionaryApiService.fetch()
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
