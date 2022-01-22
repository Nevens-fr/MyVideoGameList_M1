package com.example.myvideogamelist.ApiGestion;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

//String pageName = obj.getJSONObject("pageInfo").getString("pageName");
//System.out.println(obj.getJSONArray("results").getJSONObject(0).getString("name"));

/**
 * Class that fetch data from the games API
 */
public class GamesAPI {
    private static final GamesAPI gamesAPI = new GamesAPI();
    private String apiKey ="fe8335a6f6cc49b3af85f2fbea4b89c9";
    private StringBuffer content;
    private JSONObject obj;

    /**
     * Private constructor for singleton
     */
    private GamesAPI(){}

    /**
     * Getter for singleton class
     * @return unique class instance
     */
    public static GamesAPI getGamesAPI() {
        return gamesAPI;
    }

    /**
     * Make a Http reqyest withs params
     * @param searchGameAPI a class containing all params for the request
     * @throws Exception
     */
    public void requestWithParam(SearchGameAPI searchGameAPI){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL("https://api.rawg.io/api/games?key=" + apiKey + searchGameAPI.toString());
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    con.setRequestMethod("GET");

                    con.setRequestProperty("Content-Type", "application/json");

                    int status = con.getResponseCode();
                    BufferedReader in = new BufferedReader(
                            new InputStreamReader(con.getInputStream()));
                    String inputLine;
                    content = new StringBuffer();

                    while ((inputLine = in.readLine()) != null) {
                        content.append(inputLine);
                    }
                    in.close();

                    parseResponse();
                } catch (
                        Exception e) {
                    System.out.println(e.getMessage() + e.getCause() + e.getClass());
                }
            }
        });
        thread.start();
    }

    public void requestGameById(String id){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL("https://api.rawg.io/api/games/"+id+"?key=" + apiKey);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    con.setRequestMethod("GET");

                    con.setRequestProperty("Content-Type", "application/json");

                    int status = con.getResponseCode();
                    BufferedReader in = new BufferedReader(
                            new InputStreamReader(con.getInputStream()));
                    String inputLine;
                    content = new StringBuffer();

                    while ((inputLine = in.readLine()) != null) {
                        content.append(inputLine);
                    }
                    in.close();

                    System.out.println(content);
                    parseResponse();
                } catch (
                        Exception e) {
                    System.out.println(e.getMessage() + e.getCause() + e.getClass());
                }
            }
        });
        thread.start();
    }

    /**
     * Parse the JSON response from the API
     */
    private void parseResponse(){
        try {
            obj = new JSONObject(content.toString());
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    /**
     * Getter for the parsed response from API
     * @return json parsed response
     */
    public JSONObject getObj() {
        return obj;
    }
}
