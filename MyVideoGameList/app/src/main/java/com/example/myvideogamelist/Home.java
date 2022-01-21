package com.example.myvideogamelist;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class Home extends AppCompatActivity {

    private NavigationBar navigationBar = NavigationBar.getNavigationBar();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        navigationBar.init(this);
    }
}