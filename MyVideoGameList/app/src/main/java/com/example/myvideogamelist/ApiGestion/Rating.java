package com.example.myvideogamelist.ApiGestion;

public class Rating {

    String id, feedback, hours, min, status, score;

    public Rating(String id){
        this.id = id;
    }

    @Override
    public String toString(){
        String elem = "{\"id\":\""+id+"\",";
        elem += "\"hours\":\""+hours+"\",";
        elem += "\"min\":\""+min+"\",";
        elem += "\"status\":\""+status+"\",";
        elem += "\"score\":\""+score+"\"";

        return elem +"}";
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
