package com.example.myvideogamelist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.myvideogamelist.ApiGestion.GamesAPI;
import com.example.myvideogamelist.ApiGestion.SearchGameAPI;

public class SearchActivity extends AppCompatActivity {

    private NavigationBar navigationBar = NavigationBar.getNavigationBar();

    private GamesAPI gamesAPI = GamesAPI.getGamesAPI();
    private Button selectedButton;
    private String searchType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_search);

        navigationBar.init(this);
        connectButtonSearch();

        selectedButton = findViewById(R.id.search_name_button_id);
        searchType = "search";
    }

    /**
     * Create buttons interactions for user search options
     */
    private void connectButtonSearch(){
        findViewById(R.id.search_name_button_id).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selectedButton != findViewById(R.id.search_name_button_id)){
                    selectedButton.setTextColor(ResourcesCompat.getColor(getResources(), R.color.grey, null));
                    selectedButton = findViewById(R.id.search_name_button_id);
                    selectedButton.setTextColor(ResourcesCompat.getColor(getResources(), R.color.white, null));
                    ((EditText)findViewById(R.id.user_search_text_id)).setHint(getString(R.string.searchNameEditText));
                    searchType = "search";
                }
            }
        });
        findViewById(R.id.search_devs_button_id).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selectedButton != findViewById(R.id.search_devs_button_id)){
                    selectedButton.setTextColor(ResourcesCompat.getColor(getResources(), R.color.grey, null));
                    selectedButton = findViewById(R.id.search_devs_button_id);
                    selectedButton.setTextColor(ResourcesCompat.getColor(getResources(), R.color.white, null));
                    ((EditText)findViewById(R.id.user_search_text_id)).setHint(getString(R.string.searchDevsEditText));
                    searchType = "developers";
                }
            }
        });
        findViewById(R.id.search_publisher_button_id).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selectedButton != findViewById(R.id.search_publisher_button_id)){
                    selectedButton.setTextColor(ResourcesCompat.getColor(getResources(), R.color.grey, null));
                    selectedButton = findViewById(R.id.search_publisher_button_id);
                    selectedButton.setTextColor(ResourcesCompat.getColor(getResources(), R.color.white, null));
                    ((EditText)findViewById(R.id.user_search_text_id)).setHint(getString(R.string.searchPublisherEditText));
                    searchType = "publishers";
                }
            }
        });
        findViewById(R.id.search_releasedDate_button_id).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selectedButton != findViewById(R.id.search_releasedDate_button_id)){
                    selectedButton.setTextColor(ResourcesCompat.getColor(getResources(), R.color.grey, null));
                    selectedButton = findViewById(R.id.search_releasedDate_button_id);
                    selectedButton.setTextColor(ResourcesCompat.getColor(getResources(), R.color.white, null));
                    ((EditText)findViewById(R.id.user_search_text_id)).setHint(getString(R.string.searchReleasedDateEditText));
                    searchType = "dates";
                }
            }
        });
        findViewById(R.id.search_platforms_button_id).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selectedButton != findViewById(R.id.search_platforms_button_id)){
                    selectedButton.setTextColor(ResourcesCompat.getColor(getResources(), R.color.grey, null));
                    selectedButton = findViewById(R.id.search_platforms_button_id);
                    selectedButton.setTextColor(ResourcesCompat.getColor(getResources(), R.color.white, null));
                    ((EditText)findViewById(R.id.user_search_text_id)).setHint(getString(R.string.searchPlatformsEditText));
                    searchType = "platforms";
                }
            }
        });
        findViewById(R.id.search_genres_button_id).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selectedButton != findViewById(R.id.search_genres_button_id)){
                    selectedButton.setTextColor(ResourcesCompat.getColor(getResources(), R.color.grey, null));
                    selectedButton = findViewById(R.id.search_genres_button_id);
                    selectedButton.setTextColor(ResourcesCompat.getColor(getResources(), R.color.white, null));
                    ((EditText)findViewById(R.id.user_search_text_id)).setHint(getString(R.string.searchGenresEditText));
                    searchType = "genres";
                }
            }
        });
        findViewById(R.id.launch_search_button_id).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SearchGameAPI searchGameAPI = new SearchGameAPI();
                switch (searchType){
                    case "genres" : searchGameAPI.setGenres( ((EditText)findViewById(R.id.user_search_text_id)).getText().toString());
                    case "platforms" : searchGameAPI.setPlatforms( ((EditText)findViewById(R.id.user_search_text_id)).getText().toString());
                    case "dates" : searchGameAPI.setDates( ((EditText)findViewById(R.id.user_search_text_id)).getText().toString());
                    case "publishers" : searchGameAPI.setPublishers( ((EditText)findViewById(R.id.user_search_text_id)).getText().toString());
                    case "developers" : searchGameAPI.setDevelopers( ((EditText)findViewById(R.id.user_search_text_id)).getText().toString());
                    case "search" : searchGameAPI.setSearch( ((EditText)findViewById(R.id.user_search_text_id)).getText().toString());
                }
                searchGameAPI.setPage_size("10");
                gamesAPI.requestWithParam(searchGameAPI);
            }
        });
    }
}