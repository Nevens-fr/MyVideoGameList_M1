package com.example.myvideogamelist.Notification;

import android.content.Context;
import android.net.ConnectivityManager;

import com.example.myvideogamelist.ApiGestion.Database;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Class managing the creation of a notification
 */
public class Notification {
    private String title = "";
    private int nbReleaseToday = 0;
    private String body = "";
    private Database database;
    private Context context;
    private String user;

    /**
     * Constructor
     * @param context application context
     */
    public Notification(Context context){
        this.context = context;
        database = Database.getDatabase();
        if(database.getUsers() == null || database.getGames() == null){
            database.requestGet(0);
            database.requestGet(1);
            user = readConfig();
            database.setSelectedUserID(Integer.parseInt(user));
        }
    }

    /**
     * Read user config to get his index in database
     * @return user if user exist
     */
    private String readConfig(){
        try {
            InputStream inputStream = context.openFileInput("config.txt");

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append("\n").append(receiveString);
                }

                inputStream.close();
                return stringBuilder.toString().replace("\n", "");
            }
        }
        catch (Exception e) {
            return null;
        }
        return null;
    }


    /**
     * Create the notification's content
     * @param date date of day to check released date of games
     * @return true if no problem found, false if problem or if no game released today
     */
    public boolean createNotificationData(String date){
        DateNotif dateN = parseDate(date);
        //error on parsing or not connected to network or user not log, no notification
        if(dateN == null || !isNetworkConnected() || database.getSelectedUserID() == -1)
            return false;

        try{
            JSONArray games = database.getCurrentUser().getJSONArray("games");
            JSONArray gamesDB = database.getGames().getJSONArray("games");
            for(int i = 0; i < games.length(); i++){
                String id = games.getJSONObject(i).getString("id");
                for(int j = 0; j < gamesDB.length(); j++){
                    if(gamesDB.getJSONObject(j).getString("id").compareTo(id) == 0){
                        DateNotif dateGame = parseDate(gamesDB.getJSONObject(j).getString("released"));

                        if(dateGame != null && dateN.compareTo(dateGame) == 0){
                            if(nbReleaseToday >= 1)
                                body +=", ";
                            nbReleaseToday++;
                            body += gamesDB.getJSONObject(j).getString("name");
                        }
                    }
                }
            }

            if(nbReleaseToday == 1){
                title = "A game is released today.";
                body += " in your list is now available.";
            }
            else{
                title = "Multiple games are released today.";
                body += " from your list are now available.";
            }
        }
        catch(Exception e){
            return false;
        }
        return nbReleaseToday >0;
    }

    /**
     * Check network connexion
     * @return true if connected
     */
    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

    /**
     * Parse a date in string to a dateNotification format
     * @param date date in string format
     * @return date in usable format
     */
    public DateNotif parseDate(String date){
        DateNotif dateN = null;

        try{
            int y = Integer.parseInt(date.substring(0, date.indexOf('-')));
            date = date.substring(date.indexOf('-') + 1);
            int m = Integer.parseInt(date.substring(0, date.indexOf('-')));
            date = date.substring(date.indexOf('-') + 1);
            int d = Integer.parseInt(date);

            dateN = new DateNotif(y, m, d);
        }
        catch(Exception e){
        }
        return dateN;
    }

    /**
    * Getter of the title
    * @return the title
    */
    public String getTitle() {
        return title;
    }

    /**
     * Getter of the body
     * @return the body
     */
    public String getBody() {
        return body;
    }
}
