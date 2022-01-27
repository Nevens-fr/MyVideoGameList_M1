package com.example.myvideogamelist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

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

        navigationBar.init(this);

        //get users and games data since launch
        Database.getDatabase().requestGet(0);
        Database.getDatabase().requestGet(1);
    }
}