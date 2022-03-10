package com.example.myvideogamelist;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.widget.Toast;

import com.example.myvideogamelist.ApiGestion.Database;
import com.example.myvideogamelist.ApiGestion.NewsAPI;
import com.example.myvideogamelist.Notification.NotificationReceiver;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.Date;

public class LoadingActivity extends AppCompatActivity {

    private int call = 0;
    private Object o = new Object();
    private String user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        if(isNetworkConnected()){
            readConfig();
            getDatabases();
            myAlarm();
        }
        else{
            Toast.makeText(getApplicationContext(), getString(R.string.no_internet_access), Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Check network connexion
     * @return true if connected
     */
    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

    /**
     * Read user config to get his index in database
     */
    private void readConfig(){
        try {
            InputStream inputStream = getApplicationContext().openFileInput("config.txt");

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append("\n").append(receiveString);
                }

                inputStream.close();
                user = stringBuilder.toString().replace("\n", "");
            }
        }
        catch (FileNotFoundException e) {
            user = null;
            e.printStackTrace();
        }
        catch (IOException e) {
            user = null;
            e.printStackTrace();
        }
    }

    /**
     * Starts a thread to fetch database data
     */
    private void getDatabases(){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                //get users and games data since launch
                Database.getDatabase().requestGet(0);
                Database.getDatabase().requestGet(1);

                //if user doesn't exist, necessity to call again these methods after login or register
                System.out.println(user);
                if(user != null)
                    Database.getDatabase().setSelectedUserID(Integer.parseInt(user));
                Database.getDatabase().createListsGames();
                launchApp();
            }
        });
        thread.start();

        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                NewsAPI.getNewsAPI().requestWithParam("articles", "");
                try{
                    //to wait and get articles first
                    Thread.sleep(500);
                }
                catch (Exception e){}
                NewsAPI.getNewsAPI().requestWithParam("reviews", "");
                launchApp();
            }
        });
        thread2.start();
    }

    /**
     * Launch the app when all data are retrieved from APIs
     */
    private void launchApp(){
        //Allow app to wait for all data before launching
        synchronized (o){
            if(call + 1 < 2 ){
                call++;
            }
            else{
                if(user == null){
                    Intent intent = new Intent(this, FirstScreenActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    this.startActivity(intent);
                    this.finish();
                }
                else{
                    Intent intent = new Intent(this, Home.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    this.startActivity(intent);
                    this.finish();
                }
            }
        }
    }


    /**
     * Create a notification each day
     */
    public void myAlarm() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 16);
        calendar.set(Calendar.MINUTE, 1);
        calendar.set(Calendar.SECOND, 0);

        if (calendar.getTime().compareTo(new Date()) < 0)
            calendar.add(Calendar.DAY_OF_MONTH, 1);

        Intent intent = new Intent(getApplicationContext(), NotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        if (alarmManager != null) {
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
        }
    }
}