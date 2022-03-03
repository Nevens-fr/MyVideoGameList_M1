package com.example.myvideogamelist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.widget.Toast;

import com.example.myvideogamelist.ApiGestion.Database;
import com.example.myvideogamelist.ApiGestion.NewsAPI;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

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
                user = stringBuilder.toString();
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
                Database.getDatabase().setSelectedUserID(0);
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
}