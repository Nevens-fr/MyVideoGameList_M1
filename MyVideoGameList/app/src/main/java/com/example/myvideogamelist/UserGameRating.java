package com.example.myvideogamelist;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class UserGameRating extends AppCompatActivity {

    private NavigationBar navigationBar = NavigationBar.getNavigationBar();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_game_rating);

        navigationBar.init(this);
    }
}