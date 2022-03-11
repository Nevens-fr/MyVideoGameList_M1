package com.example.myvideogamelist.ApiGestion;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Class modelizing rating
 */
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

    /**
     * getter of the game id
     * @return the game id
     */
    public String getId() {
        return id;
    }

    /**
     * setter of the game id
     * @param id the game id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * getter of the game feedback
     * @return the game feedback
     */
    public String getFeedback() {
        return feedback;
    }


    /**
     * setter of the game feedback
     * @param feedback the game feedback to set
     */
    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    /**
     * getter of the game hours
     * @return the game hours
     */
    public String getHours() {
        return hours;
    }

    /**
     * setter of the game hours
     * @param hours the game hours to set
     */
    public void setHours(String hours) {
        this.hours = hours;
    }

    /**
     * getter of the game status
     * @return the game status
     */
    public String getStatus() {
        return status;
    }

    /**
     * setter of the game status
     * @param status the game status to set
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * getter of the game score
     * @return the game score
     */
    public String getScore() {
        return score;
    }


    /**
     * setter of the game score
     * @param id the game i to set
     */
    public void setScore(String score) {
        this.score = score;
    }
}
