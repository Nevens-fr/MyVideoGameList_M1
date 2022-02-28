package com.example.myvideogamelist.ApiGestion;

import com.example.myvideogamelist.InterfacesAppli.MyActivityImageDisplayable;
import com.example.myvideogamelist.InterfacesAppli.ObservableAppli;
import com.example.myvideogamelist.InterfacesAppli.ObservatorAppli;

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
public class Database implements ObservableAppli {
    private static final Database database = new Database();
    private int status;
    private final String usersDB = "https://api.jsonstorage.net/v1/json/ace6b1ed-31d8-412f-a913-f7daea4e91ed";
    private final String gamesDB = "https://api.jsonstorage.net/v1/json/711efbe0-1ed7-4ae8-811b-77e998db7085";
    private StringBuffer content;
    private JSONObject games = null, users = null;
    private int currentUser;
    private ArrayList<Game> finished, planned, abandoned, on_hold, playing;
    private ArrayList<ObservatorAppli> observators = new ArrayList<>();

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

                    status = con.getResponseCode();
                    BufferedReader in = new BufferedReader(
                            new InputStreamReader(con.getInputStream()));
                    String inputLine;
                    content = new StringBuffer();

                    while ((inputLine = in.readLine()) != null) {
                        content.append(inputLine);
                    }
                    in.close();

                    parseResponse(type);
                } catch (
                        Exception e) {
                    System.out.println(e.getMessage() + e.getCause() + e.getClass());
                    if(type == 0)
                        games = null;
                    else
                        users = null;
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
    public void requestPost(int type, MyActivityImageDisplayable currentActivity, JSONObject newDatas) {
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
                    byte[] input = newDatas.toString().getBytes("utf-8");
                    output.write(input, 0, input.length);

                    status = con.getResponseCode();
                    notifyObs(status);
                } catch (Exception e) {
                    System.out.println(e.getMessage() + e.getCause() + e.getClass());
                    notifyObs(400);
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
        finished = putGameDataInList(finished,"Finished");
        abandoned = putGameDataInList(abandoned,"Abandoned");
        planned = putGameDataInList(planned,"Planned");
        playing = putGameDataInList(playing,"Playing");
        on_hold = putGameDataInList(on_hold,"On-hold");
    }

    /**
     * Fill category list with game from our DB
     * @param cat array list of Game containing all games for a list
     * @param category category to look for
     */
    private ArrayList<Game> putGameDataInList(ArrayList<Game> cat, String category){
        cat = new ArrayList<>();
        try{
            JSONArray gamesArray = games.getJSONArray("games");

            for(int i = 0; i < getCurrentUser().getJSONArray("games").length(); i++){
                if(getCurrentUser().getJSONArray("games").getJSONObject(i).getString("status").compareTo(category) == 0){
                    String id = getCurrentUser().getJSONArray("games").getJSONObject(i).getString("id");

                    for(int j = 0; j < gamesArray.length(); j++){
                        if(gamesArray.getJSONObject(j).getString("id").compareTo(id) == 0){
                            Game newGame = new Game(gamesArray.getJSONObject(j).getJSONArray("genres").length(), gamesArray.getJSONObject(j).getJSONArray("developers").length(),gamesArray.getJSONObject(j).getJSONArray("short_screenshots").length(),gamesArray.getJSONObject(j).getJSONArray("publishers").length());
                            newGame.setIdGame(gamesArray.getJSONObject(j).getString("id"));
                            newGame.setDescription(gamesArray.getJSONObject(j).getString("description"));
                            newGame.setMetacritic(gamesArray.getJSONObject(j).getString("metacritic"));
                            newGame.setName(gamesArray.getJSONObject(j).getString("name"));
                            newGame.setPlaytime(gamesArray.getJSONObject(j).getString("playtime"));
                            newGame.setReleasedDate(gamesArray.getJSONObject(j).getString("released"));
                            newGame.setDevs(gamesArray.getJSONObject(j).getJSONArray("developers"));
                            newGame.setGenres(gamesArray.getJSONObject(j).getJSONArray("genres"));
                            newGame.setImages(gamesArray.getJSONObject(j).getJSONArray("short_screenshots"));
                            newGame.setPublishers(gamesArray.getJSONObject(j).getJSONArray("publishers"));
                            cat.add(newGame);
                        }
                    }
                }
            }
            return cat;
        }
        catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Get games from all categories
     * @return array list from all user games
     */
    public ArrayList<Game> getAll(){
        ArrayList<Game> allGames = new ArrayList<>();
        if(playing == null)//internet error cause array to become null
            return new ArrayList<>();
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

    /**
     * Add a new observator
     * @param obs the new observator
     */
    @Override
    public void subscribe(ObservatorAppli obs) {
        observators.add(obs);
    }

    /**
     * Notify observators that datas are available
     */
    @Override
    public void notifyObs(int status) {
        for(ObservatorAppli obs : observators){
            if(obs != null)
                obs.notified(status);
        }
    }

    @Override
    public void unsubscribe(ObservatorAppli obs) {
        observators.remove(obs);
    }
}
