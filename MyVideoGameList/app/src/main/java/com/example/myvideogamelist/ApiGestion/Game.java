package com.example.myvideogamelist.ApiGestion;

import org.json.JSONArray;
import org.json.JSONObject;

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
            obj.put("released", releasedDate);
            obj.put("description", description);
            obj.put("metacritic", metacritic);
            obj.put("playtime", playtime);

            obj.put("genres", createArray(genres));
            obj.put("short_screenshots", createArray(images));
            obj.put("publishers", createArray(publishers));
            obj.put("developers", createArray(devs));

            return obj;
        }
        catch (Exception e){
            return null;
        }
    }

    private JSONArray createArray(String [] strings) throws  Exception{
        JSONArray jsonArray = new JSONArray();
        for(String s : strings){
            JSONObject elem = new JSONObject();
            elem.put("name", s);
            jsonArray.put(name);
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

    public String getIdGame() {
        return idGame;
    }

    public void setIdGame(String idGame) {
        this.idGame = idGame;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getReleasedDate() {
        return releasedDate;
    }

    public void setReleasedDate(String releasedDate) {
        this.releasedDate = releasedDate;
    }

    public String getPlaytime() {
        return playtime;
    }

    public void setPlaytime(String playtime) {
        this.playtime = playtime;
    }

    public String getMetacritic() {
        return metacritic;
    }

    public void setMetacritic(String metacritic) {
        this.metacritic = metacritic;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String[] getGenres() {
        return genres;
    }

    public void setGenres(JSONArray genres) {
        completeStringArrayFromJsonArray(genres, this.genres);
    }

    public String[] getDevs() {
        return devs;
    }

    public void setDevs(JSONArray devs) {
        completeStringArrayFromJsonArray(devs, this.devs);
    }

    public String[] getImages() {
        return images;
    }

    public void setImages(JSONArray images) {
        completeStringArrayFromJsonArray(images, this.images);
    }

    public String[] getPublishers() {
        return publishers;
    }

    public void setPublishers(JSONArray publishers) {
        completeStringArrayFromJsonArray(publishers, this.publishers);
    }

    /**
     * Take a json array and parse it into a string array with only name field
     * @param data json array to turn into string array
     * @param strings string array results from json array
     */
    private void completeStringArrayFromJsonArray(JSONArray data, String[] strings){
        for(int i = 0; i < data.length(); i++){
            try{
                strings[i] = data.getJSONObject(i).getString("name");
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
