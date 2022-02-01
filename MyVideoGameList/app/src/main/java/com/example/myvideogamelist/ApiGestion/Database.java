package com.example.myvideogamelist.ApiGestion;

import com.example.myvideogamelist.InterfacesAppli.MyActivityImageDiplayable;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Class allowing access to databases containing users or games datas
 */
public class Database {
    private static final Database database = new Database();
    private final String usersDB = "https://api.jsonstorage.net/v1/json/42be9027-73a8-4666-bb6e-c0902e8b074b";
    private final String gamesDB = "https://api.jsonstorage.net/v1/json/9f313232-8220-4c82-b107-b6529a389678";
    private StringBuffer content;
    private JSONObject games, users;
    private int currentUser;
    private ArrayList<Game> finished, planned, abandoned, on_hold, playing;

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
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
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
                    con.setDoOutput(true);
                    con.setRequestMethod("PUT");
                    con.setRequestProperty("Content-Type", "application/json; utf-8");
                    con.setRequestProperty("Accept", "application/json");

                    OutputStream output = con.getOutputStream();
                        //output.write(newDatas);
                    byte[] input = newDatas.toString().getBytes("utf-8");
                    output.write(input, 0, input.length);

                    int status = con.getResponseCode();
                    System.out.println(status);
                } catch (Exception e) {
                    System.out.println(e.getMessage() + e.getCause() + e.getClass());
                }
            }
        });
        thread.start();
        try{
        thread.join();
        }
        catch (Exception e){System.out.println(e.getMessage() + e.getCause() + e.getClass());}
    }

    /**
     * Parse the JSON response from the API
     * @param type 0 for games DB or 1 for users DB
     */
    private void parseResponse(int type){
        try {
            if(type == 0)
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

    /**
     * Getter for the current user
     * @return
     */
    public JSONObject getCurrentUser() {
        try {
            return users.getJSONArray("users").getJSONObject(currentUser);
        }
        catch (Exception e){
            return null;
        }
    }

    public void setSelectedUserID(int id){
        this.currentUser = id;
    }

    /**
     * Set the user by removing his old data
     * @param user
     */
    public void setCurrentUser(JSONObject user) {
        try {
            users.getJSONArray("users").remove(currentUser);
            users.getJSONArray("users").put(currentUser, user);
        }
        catch (Exception e){  }
    }

    /**
     * Create arraylist of game by categories (finished, playing...)
     */
    public void createListsGames(){
        putGameDataInList(finished,"Finished");
        putGameDataInList(abandoned,"Abandoned");
        putGameDataInList(planned,"Planned");
        putGameDataInList(playing,"Playing");
        putGameDataInList(on_hold,"On-hold");
    }

    /**
     * Fill category list with game from our DB
     * @param cat array list of Game containing all games for a list
     * @param category category to look for
     */
    private void putGameDataInList(ArrayList<Game> cat, String category){
        cat = new ArrayList<>();
        try{
            JSONArray gamesArray = games.getJSONArray("games");
            for(int i = 0; i < gamesArray.length(); i++){
                if(gamesArray.getJSONObject(i).getString("status").compareTo(category) == 0){
                    Game newGame = new Game(gamesArray.getJSONObject(i).getJSONArray("genres").length(), gamesArray.getJSONObject(i).getJSONArray("developers").length(),gamesArray.getJSONObject(i).getJSONArray("short_screenshots").length(),gamesArray.getJSONObject(i).getJSONArray("publishers").length());
                    newGame.setIdGame(gamesArray.getJSONObject(i).getString("id"));
                    newGame.setDescription(gamesArray.getJSONObject(i).getString("description"));
                    newGame.setMetacritic(gamesArray.getJSONObject(i).getString("metacritic"));
                    newGame.setName(gamesArray.getJSONObject(i).getString("name"));
                    newGame.setPlaytime(gamesArray.getJSONObject(i).getString("playtime"));
                    newGame.setReleasedDate(gamesArray.getJSONObject(i).getString("released"));
                    newGame.setDevs(games.getJSONArray("developers"));
                    newGame.setGenres(games.getJSONArray("genres"));
                    newGame.setImages(games.getJSONArray("short_screenshots"));
                    newGame.setPublishers(games.getJSONArray("publishers"));
                    cat.add(newGame);
                }
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Get games from all categories
     * @return array list from all user games
     */
    public ArrayList<Game> getAll(){
        ArrayList<Game> allGames = new ArrayList<>();
        getGamesFromList(allGames, playing);
        getGamesFromList(allGames, planned);
        getGamesFromList(allGames, on_hold);
        getGamesFromList(allGames, finished);
        getGamesFromList(allGames, abandoned);
        return allGames;
    }

    /**
     * Add games from a list to another
     * @param newList list where to add games
     * @param oldList list having games needed
     */
    private void getGamesFromList(ArrayList<Game> newList, ArrayList<Game> oldList){
        for(Game g : oldList)
            newList.add(g);
    }

    public ArrayList<Game> getFinished() {
        return finished;
    }

    public ArrayList<Game> getPlanned() {
        return planned;
    }

    public ArrayList<Game> getAbandoned() {
        return abandoned;
    }

    public ArrayList<Game> getOn_hold() {
        return on_hold;
    }

    public ArrayList<Game> getPlaying() {
        return playing;
    }
}
//example
/*
        User user = new User("1", "toto", "axel");
        Rating r = user.addGame("10");
        r.setFeedback("Like it soo much");
        r.setHours("150");
        r.setMin("00");
        r.setScore("5");
        r.setStatus("Finished");
        try {
            int cpt = Integer.parseInt(String.valueOf(Database.getDatabase().getUsers().getString("nbUsers")));
            cpt++;
            for(int i = 0; i < Database.getDatabase().getUsers().getJSONArray("users").length();i++)
                Database.getDatabase().getUsers().getJSONArray("users").remove(i);

            //Database.getDatabase().getUsers().getJSONArray("users").put(user.getJSONObject());
            Database.getDatabase().getUsers().remove("nbUsers");
            Database.getDatabase().getUsers().put("nbUsers", String.valueOf(0));
            System.out.println( Database.getDatabase().getUsers());
            Database.getDatabase().requestPost(1, null, Database.getDatabase().getUsers());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        */
