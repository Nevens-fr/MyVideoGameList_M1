package com.example.myvideogamelist;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class GamesListActivity extends AppCompatActivity {

    private NavigationBar navigationBar = NavigationBar.getNavigationBar();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_games_list);

        navigationBar.init(this);
    }
}