package com.example.myvideogamelist;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class SearchActivity extends AppCompatActivity {

    private NavigationBar navigationBar = NavigationBar.getNavigationBar();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_search);

        navigationBar.init(this);
    }
}