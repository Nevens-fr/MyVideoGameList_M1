package com.example.myvideogamelist.ApiGestion;

import org.json.JSONArray;
import org.json.JSONObject;

public class Rating {

    String id, feedback, hours, min, status, score;

    public Rating(String id){
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
            obj.put("min", min);
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

    public String getMin() {
        return min;
    }

    public void setMin(String min) {
        this.min = min;
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
