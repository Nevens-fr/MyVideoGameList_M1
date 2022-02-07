package com.example.myvideogamelist.ApiGestion;

import com.example.myvideogamelist.InterfacesAppli.MyActivityImageDiplayable;

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
     * @param currentActivity activity running
     * @param category category the user is searching in
     */
    public void requestWithParam(SearchGameAPI searchGameAPI, MyActivityImageDiplayable currentActivity, String category) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL("https://api.rawg.io/api/"+category+"?key=" + apiKey + searchGameAPI.toString());
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
        try {
            thread.join();
            currentActivity.getApiInfo(obj);
        } catch (InterruptedException e) {
            e.printStackTrace();
            currentActivity.getApiInfo(null);
        }
    }

    /**
     * Request game data from a game id
     * @param id the game id
     */
    public void requestGameById(String id, MyActivityImageDiplayable currentActivity) {
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

                    parseResponse();
                } catch (
                        Exception e) {
                    System.out.println(e.getMessage() + e.getCause() + e.getClass());
                }
            }
        });
        thread.start();
        try {
            thread.join();
            currentActivity.getApiInfo(obj);
        } catch (InterruptedException e) {
            e.printStackTrace();
            currentActivity.getApiInfo(null);
        }
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
            obj = null;
        }
    }

    /**
     * Parse the JSON response from a string
     * @param response string json to parse
     * @return parsed string
     */
    public JSONObject parseResponse(String response){
        try {
            return new JSONObject(response);
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            return null;
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
