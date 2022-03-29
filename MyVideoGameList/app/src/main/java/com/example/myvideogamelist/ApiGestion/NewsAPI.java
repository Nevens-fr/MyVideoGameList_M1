package com.example.myvideogamelist.ApiGestion;

import com.example.myvideogamelist.InterfacesAppli.MyActivityImageDisplayable;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
  * Class that fetch data from the news API
 */ 
public class NewsAPI {

    private final static NewsAPI newsAPI = new NewsAPI();
    private String apiKey ="1248c3af4075abbcda5f4e975962c97bf5fb78e9";
    private StringBuffer content;
    private JSONObject articles, reviews;
    private MyActivityImageDisplayable currentActivity = null;
    private int status;

    /**
     * Private constructor for singleton
     */
    private NewsAPI(){}

    /**
     * Singleton's getter
     * @return class variable singleton
     */
    public static NewsAPI getNewsAPI() {
        return newsAPI;
    }

    /**
     * Make a Http request withs params
     * @param category category the user is searching in
     * @param otherParams others parameters to give to the request
     */
    public void requestWithParam(String category, String otherParams) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL("https://www.gamespot.com/api/"+category+"/?api_key=" + apiKey + "&format=json&sort=publish_date:desc" + otherParams);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    con.setRequestMethod("GET");

                    con.setRequestProperty("Content-Type", "application/json");

                    status = con.getResponseCode();
                    BufferedReader in = new BufferedReader(
                            new InputStreamReader(con.getInputStream()));
                    String inputLine;
                    content = new StringBuffer();

                    while ((inputLine = in.readLine()) != null) {
                        content.append(inputLine);
                    }
                    in.close();

                    parseResponse(category);
                    if(currentActivity != null)
                        currentActivity.getApiInfo(category.compareTo("articles") == 0? articles : reviews);
                } catch (Exception e) {
                    System.out.println(e.getMessage() + " "+ e.getCause()+ " " + e.getClass() + " " + status);
                    e.printStackTrace();
                    if(currentActivity != null)
                        currentActivity.getApiInfo(null);
                }
            }
        });
        thread.start();
    }

    /**
     * Parse the JSON response from the API
     */
    private void parseResponse(String category){
        try {
            if(category.compareTo("articles") == 0)
                articles = new JSONObject(content.toString());
            else
                reviews = new JSONObject(content.toString());
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            if(category.compareTo("articles") == 0)
                articles = null;
            else
                reviews = null;
        }
    }

    /**
     * getter of the articles
     * @return the articles
     */
    public JSONObject getArticles() {
        return articles;
    }

    /**
     * getter of the reviews
     * @return the reviews
     */
    public JSONObject getReviews() {
        return reviews;
    }

    /**
     * setter of the current activity
     * @param currentActivity
     */
    public void setCurrentActivity(MyActivityImageDisplayable currentActivity) {
        this.currentActivity = currentActivity;
    }
}
