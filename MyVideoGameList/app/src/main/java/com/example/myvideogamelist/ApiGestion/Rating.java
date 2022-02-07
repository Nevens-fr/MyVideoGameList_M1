package com.example.myvideogamelist.ApiGestion;

import org.json.JSONArray;
import org.json.JSONObject;

public class Rating {

    String id;
    String feedback;
    String hours;
    String status;
    String score;

    public Rating(String id){
        this.id = id;
    }

    /**
     * Contructor with all fields
     * @param id game id
     * @param feedback user feedback on game
     * @param hours number of hours user spend on the game
     * @param status user status on game (finished, planned...=
     * @param score rate on ten from the user
     */
    public Rating(String id, String feedback, String hours, String status, String score){
        this.feedback = feedback;
        this.hours = hours;
        this.score = score;
        this.status = status;
        this.id = id;
    }

    /**
     * Create and return an json object
     * @return json object from the class
     */
    public JSONObject getJSONObject(){
        try{
            JSONObject obj = new JSONObject();
            obj.put("id", id);
            obj.put("feedback", feedback);
            obj.put("hours", hours);
            obj.put("status", status);
            obj.put("score", score);

            return obj;
        }
        catch (Exception e){
            return null;
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public String getHours() {
        return hours;
    }

    public void setHours(String hours) {
        this.hours = hours;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }
}
