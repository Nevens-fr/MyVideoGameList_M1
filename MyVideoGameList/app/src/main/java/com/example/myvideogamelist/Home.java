package com.example.myvideogamelist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.example.myvideogamelist.ApiGestion.Database;
import com.example.myvideogamelist.ApiGestion.Rating;
import com.example.myvideogamelist.ApiGestion.User;

import org.json.JSONException;
import org.json.JSONObject;

public class Home extends AppCompatActivity{

    private NavigationBar navigationBar = NavigationBar.getNavigationBar();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        addNavigationBar();
        navigationBar.init(this);

        getDatabases();
    }

    /**
     * Insert navigation bar into the activity
     */
    private void addNavigationBar(){
        LayoutInflater vi = (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = vi.inflate(R.layout.bottom_bar_navigation, findViewById(R.id.home_activity_id), true);
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
                Database.getDatabase().setSelectedUserID(0);
            }
        });
        thread.start();
    }
}