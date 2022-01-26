package com.example.myvideogamelist.ApiGestion;

import com.example.myvideogamelist.MyActivityImageDiplayable;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Class allowing access to databases containing users or games datas
 */
public class Database {
    private static final Database database = new Database();
    private String usersDB = "https://api.jsonstorage.net/v1/json/42be9027-73a8-4666-bb6e-c0902e8b074b";
    private String gamesDB = "https://api.jsonstorage.net/v1/json/9f313232-8220-4c82-b107-b6529a389678";
    private StringBuffer content;
    private JSONObject games, users;

    /**
     * Empty private constructor for
     */
    private Database(){}

    /**
     * Send a request to get user data or game data from our DB
     * @param type 0 for games DB or 1 for users DB
     */
    public void requestGet(int type) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(type == 0 ? gamesDB : usersDB);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    con.setRequestMethod("GET");

                    con.setRequestProperty("Content-Type", "application/json; utf-8");

                    int status = con.getResponseCode();
                    BufferedReader in = new BufferedReader(
                            new InputStreamReader(con.getInputStream()));
                    String inputLine;
                    content = new StringBuffer();

                    while ((inputLine = in.readLine()) != null) {
                        content.append(inputLine);
                    }
                    in.close();

                    parseResponse(type);
                    System.out.println(content);
                } catch (
                        Exception e) {
                    System.out.println(e.getMessage() + e.getCause() + e.getClass());
                }
            }
        });
        thread.start();
    }

    /**
     * Send a requets to get user data or game data from our DB
     * @param type 0 for games DB or 1 for users DB
     * @param currentActivity the activity asking for data
     * @param newDatas le nouveau json à sauvegarder
     */
    public void requestPost(int type, MyActivityImageDiplayable currentActivity, JSONObject newDatas) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(type == 0 ? gamesDB : usersDB);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    con.setRequestMethod("POST");

                    con.setRequestProperty("Content-Type", "application/json; UTF-8");

                    int status = con.getResponseCode();
                    //BufferedReader in = new BufferedReader(
                      //      new InputStreamReader(con.getInputStream()));

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
     * @param type 0 for games DB or 1 for users DB
     */
    private void parseResponse(int type){
        try {
            if(type == 1)
                games = new JSONObject(content.toString());
            else
                users = new JSONObject(content.toString());
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            if(type == 1)
                games = null;
            else
                users = null;
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
    public JSONObject getGames() {
        return games;
    }

    public JSONObject getUsers() {
        return users;
    }

    public static Database getDatabase() {
        return database;
    }
}
