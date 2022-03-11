package com.example.myvideogamelist.ApiGestion;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Class that modelize a game
 */
public class Game {

    private String idGame, name, releasedDate, playtime, metacritic, description;
    private String []genres;
    private String []devs;
    private String []images;
    private String []publishers;

    /**
     * Create and return an json object
     * @return json object from the class
     */
    public JSONObject getJSONObject(){
        try{
            JSONObject obj = new JSONObject();
            JSONArray rating = new JSONArray();
            obj.put("id", idGame);
            obj.put("name", name);
            obj.put("released", releasedDate);
            obj.put("description", description);
            obj.put("metacritic", metacritic);
            obj.put("playtime", playtime);

            obj.put("genres", createArray(genres, "name"));
            obj.put("short_screenshots", createArray(images, "image"));
            obj.put("publishers", createArray(publishers, "name"));
            obj.put("developers", createArray(devs, "name"));

            return obj;
        }
        catch (Exception e){
            return null;
        }
    }

    /**
     * Create an json array from string array
     * @param strings string array
     * @return json array containing data
     * @throws Exception
     */
    private JSONArray createArray(String [] strings, String key) throws  Exception{
        JSONArray jsonArray = new JSONArray();
        for(String s : strings){
            JSONObject elem = new JSONObject();
            elem.put(key, s);
            jsonArray.put(elem);
        }
        return jsonArray;
    }

    /**
     * Constructor to build arrays
     * @param nbGenres array size
     * @param nbDev array size
     * @param nbImages array size
     * @param nbPublishers array size
     */
    public Game(int nbGenres, int nbDev, int nbImages, int nbPublishers){
        genres = new String[nbGenres];
        images = new String[nbImages];
        devs = new String[nbDev];
        publishers = new String[nbPublishers];

    }

    /**
     * getter of the id of the game
     * @return the id of the game
     */
    public String getIdGame() {
        return idGame;
    }

    /**
     * setter of the id of the game
     * @param idGame the id of the game
     */
    public void setIdGame(String idGame) {
        this.idGame = idGame;
    }

    /**
     * getter of the name of the game
     * @return the name of the game
     */
    public String getName() {
        return name;
    }

    /**
     * setter of the name of the game
     * @param name the name of the game
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * getter of the release date of the game
     * @return the release date of the game
     */
    public String getReleasedDate() {
        return releasedDate;
    }

    /**
     * setter of the release date of the game
     * @param releasedDate the release date of the game
     */
    public void setReleasedDate(String releasedDate) {
        this.releasedDate = releasedDate;
    }

    /**
     * getter of the playtime of the game
     * @return the playtime of the game
     */
    public String getPlaytime() {
        return playtime;
    }

    /**
     * setter of the playt of the game
     * @param idGame the id of the game
     */
    public void setPlaytime(String playtime) {
        this.playtime = playtime;
    }

    /**
     * getter of the metacritic score of the game
     * @return the metacritic score of the game
     */
    public String getMetacritic() {
        return metacritic;
    }

    public void setMetacritic(String metacritic) {
        this.metacritic = metacritic;
    }

    /**
     * getter of the description of the game
     * @return the description of the game
     */
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * getter of the genres of the game
     * @return the genres of the game
     */
    public String[] getGenres() {
        return genres;
    }

    public void setGenres(JSONArray genres) {
        completeStringArrayFromJsonArray(genres, this.genres,"name");
    }

    /**
     * getter of the devs of the game
     * @return the devs of the game
     */
    public String[] getDevs() {
        return devs;
    }

    public void setDevs(JSONArray devs) {
        completeStringArrayFromJsonArray(devs, this.devs,"name");
    }

    /**
     * getter of the images of the game
     * @return the images of the game
     */
    public String[] getImages() {
        return images;
    }

    public void setImages(JSONArray images) {
        completeStringArrayFromJsonArray(images, this.images, "image");
    }

    /**
     * getter of the publishers of the game
     * @return the publishers of the game
     */
    public String[] getPublishers() {
        return publishers;
    }

    public void setPublishers(JSONArray publishers) {
        completeStringArrayFromJsonArray(publishers, this.publishers, "name");
    }

    /**
     * Take a json array and parse it into a string array with only name field
     * @param data json array to turn into string array
     * @param strings string array results from json array
     * @param key to access data
     */
    private void completeStringArrayFromJsonArray(JSONArray data, String[] strings, String key){
        for(int i = 0; i < data.length(); i++){
            try{
                strings[i] = data.getJSONObject(i).getString(key);
            }
            catch (Exception e){
            }
        }
    }
}
