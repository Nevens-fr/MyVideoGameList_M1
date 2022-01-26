package com.example.myvideogamelist.ApiGestion;

import java.util.ArrayList;

public class User {
    String id, pwd, name;
    ArrayList<Rating> games = new ArrayList<>();

    /**
     * Constructor
     * @param id user id
     * @param pwd user password
     * @param name user name
     */
    public User(String id, String pwd, String name){
        this.id = id;
        this.pwd = pwd;
        this.name = name;
    }

    @Override
    public String toString(){
        String elem ="{";
        elem += "\"id\":\"" + id +"\",";
        elem += "\"name\":\"" + name +"\",";
        elem += "\"pwd\":\"" + pwd +"\",";
        elem += "\"games\":[" ;

        for(Rating r : games){
            elem += r.toString() + ",";
        }

        elem = elem.substring(0, elem.length() - 1);

        return "]" + elem+"}";
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
