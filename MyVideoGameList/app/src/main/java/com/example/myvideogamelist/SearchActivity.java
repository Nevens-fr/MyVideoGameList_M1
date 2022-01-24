package com.example.myvideogamelist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.myvideogamelist.ApiGestion.GamesAPI;
import com.example.myvideogamelist.ApiGestion.SearchGameAPI;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;
//todo implement search for other categories
public class SearchActivity extends AppCompatActivity implements MyActivityImageDiplayable{

    private NavigationBar navigationBar = NavigationBar.getNavigationBar();

    private GamesAPI gamesAPI = GamesAPI.getGamesAPI();
    private Button selectedButton;
    private String searchType;
    private int cardsInserted = 0, pageNumber = 1, maxCardByApiCAll = 40;
    private SearchGameAPI searchGameAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_search);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        navigationBar.init(this);
        connectButtonSearch();

        selectedButton = findViewById(R.id.search_name_button_id);
        searchType = "search";

        MyActivityImageDiplayable currentActivity = this;
        //load more games if possible when reaching end of current scroll view
        ((ScrollView)findViewById(R.id.scrollView_search_id)).setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View view, int i, int i1, int i2, int i3) {
                if (!((ScrollView)findViewById(R.id.scrollView_search_id)).canScrollVertically(1) && cardsInserted > 0) {
                    pageNumber++;
                    searchGameAPI.setPage(String.valueOf(pageNumber));
                    gamesAPI.requestWithParam(searchGameAPI, currentActivity);
                }
            }
        });
    }

    /**
     * Create buttons interactions for user search options
     */
    private void connectButtonSearch(){
        MyActivityImageDiplayable currentActivity = this;
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
                searchGameAPI = new SearchGameAPI();
                switch (searchType){
                    case "genres" : searchGameAPI.setGenres( ((EditText)findViewById(R.id.user_search_text_id)).getText().toString());
                    case "platforms" : searchGameAPI.setPlatforms( ((EditText)findViewById(R.id.user_search_text_id)).getText().toString());
                    case "dates" : searchGameAPI.setDates( ((EditText)findViewById(R.id.user_search_text_id)).getText().toString());
                    //case "publishers" : searchGameAPI.setPublishers( ((EditText)findViewById(R.id.user_search_text_id)).getText().toString());
                    //case "developers" : searchGameAPI.setDevelopers( ((EditText)findViewById(R.id.user_search_text_id)).getText().toString());
                    case "search" : searchGameAPI.setSearch( ((EditText)findViewById(R.id.user_search_text_id)).getText().toString());
                }
                for(; ((LinearLayout)findViewById(R.id.linearLayout_to_insert_clones_search_id)).getChildCount() > 2; )//removing all cards from previous search
                    ((LinearLayout)findViewById(R.id.linearLayout_to_insert_clones_search_id)).removeView(findViewById(R.id.card_search_to_clone_id));
                cardsInserted = 0;
                findViewById(R.id.loading_search_text_id).setVisibility(View.VISIBLE);
                findViewById(R.id.error_fetch_data_text_search_id).setVisibility(View.GONE);
                searchGameAPI.setPage_size("40");
                searchGameAPI.setPage("1");
                pageNumber = 1;
                searchGameAPI.setPage(String.valueOf(pageNumber));
                ((ScrollView)findViewById(R.id.scrollView_search_id)).fullScroll(ScrollView.FOCUS_UP);
                gamesAPI.requestWithParam(searchGameAPI, currentActivity);
            }
        });
    }

    /**
     * Get the info from the api to render it
     * @param obj json object containing api data
     */
    @Override
    public void getApiInfo(JSONObject obj){
        findViewById(R.id.loading_search_text_id).setVisibility(View.GONE);

        if(obj == null){
            findViewById(R.id.error_fetch_data_text_search_id).setVisibility(View.VISIBLE);
        }
        else{
            System.out.println(obj);
            createCard(obj, 0, maxCardByApiCAll);
        }
    }


    /**
     * Create a game card with display game info
     * @param obj json object containing api data
     * @param actualElem indice of game to display
     * @param maxElem max number of games
     */
    private void createCard(JSONObject obj,int actualElem, int maxElem){

        JSONObject game =null;

        try {
            game = obj.getJSONArray("results").getJSONObject(actualElem);

            LayoutInflater vi = (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = vi.inflate(R.layout.to_clone_layout, findViewById(R.id.linearLayout_to_insert_clones_search_id), false);

            //Insert data in fields
            ((TextView)v.findViewById(R.id.game_name_to_clone_id)).setText(game.getString("name"));
            ((TextView)v.findViewById(R.id.releasedDate_to_clone_id)).setText("Released: "+game.getString("released"));
            ((TextView)v.findViewById(R.id.rating_to_clone_id)).setText("Metacritic: " +(game.getString("metacritic") == "null" ? "no record":game.getString("metacritic")+" score"));
            ((TextView)v.findViewById(R.id.playtime_search_id)).setText("Average playtime: " +game.getString("playtime") +" hours");

            //Adding genres
            String elem ="Genres: ";
            for(int i = 0; i < game.getJSONArray("genres").length(); i++){
                elem += game.getJSONArray("genres").getJSONObject(i).getString("name");
                if(i+1 < game.getJSONArray("genres").length())
                    elem += ", ";
            }

            ((TextView)v.findViewById(R.id.genres_search_id)).setText(elem);

            ((LinearLayout)findViewById(R.id.linearLayout_to_insert_clones_search_id)).addView(v);

            //insert image
            ImageView imgV = new ImageView(this);
            Picasso.get().load(game.getJSONArray("short_screenshots").getJSONObject(0).getString("image")).resize(400,400).centerInside().into(imgV);

            ((LinearLayout)v.findViewById(R.id.card_search_to_clone_id)).addView(imgV,0 );
            ((LinearLayout)v.findViewById(R.id.card_search_to_clone_id)).setGravity(Gravity.CENTER_VERTICAL);

            final JSONObject gameData = game;

            //start game screen activity
            ((LinearLayout)v.findViewById(R.id.card_search_to_clone_id)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(), GameScreenActivity.class);
                    intent.putExtra("gameData", gameData.toString());
                    startActivity(intent);
                }
            });

            cardsInserted++;

            if(actualElem + 1 < maxElem)//to build next card
                createCard(obj, ++actualElem, maxElem);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}