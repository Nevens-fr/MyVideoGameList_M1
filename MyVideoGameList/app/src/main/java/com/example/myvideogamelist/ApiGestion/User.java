package com.example.myvideogamelist.ApiGestion;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class User {
    String id, pwd, name, mail;
    ArrayList<Rating> games = new ArrayList<>();

    /**
     * Constructor
     * @param id user id
     * @param pwd user password
     * @param name user name
     * @param mail user mail
     */
    public User(String id, String pwd, String name, String mail){
        this.id = id;
        this.pwd = pwd;
        this.name = name;
        this.mail = mail;
    }

    /**
     * Create and return an json object
     * @return json object from the class
     */
    public JSONObject getJSONObject(){
        try{
            JSONObject obj = new JSONObject();
            JSONArray rating = new JSONArray();
            obj.put("id", id);
            obj.put("name", name);
            obj.put("pwd", pwd);
            obj.put("mail", mail);

            for(Rating r : games)
                rating.put(r.getJSONObject());
            obj.put("games", rating);

            return obj;
        }
        catch (Exception e){
            return null;
        }
    }

    /**
     * Add a game in the user array
     * @param id game id
     * @return the rating to be complete
     */
    public Rating addGame(String id){
        games.add(new Rating(id));
        return games.get(games.size() - 1);
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Rating> getGames() {
        return games;
    }

    public void setGames(ArrayList<Rating> games) {
        this.games = games;
    }
}
