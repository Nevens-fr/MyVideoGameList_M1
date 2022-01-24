package com.example.myvideogamelist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.myvideogamelist.ApiGestion.GamesAPI;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GameScreenActivity extends AppCompatActivity implements MyActivityImageDiplayable {

    private NavigationBar navigationBar = NavigationBar.getNavigationBar();
    private GamesAPI gamesAPI = GamesAPI.getGamesAPI();
    private JSONObject game, gameFromSearchData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_screen);

        //Get game data from previous activity
        Intent intent = getIntent();
        game = gamesAPI.parseResponse(intent.getStringExtra("gameData"));
        gameFromSearchData = game;

        try {//new request for all game datas
            gamesAPI.requestGameById(game.getString("id"), this);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        navigationBar.init(this);
        connectButtons();
    }

    /**
     * Set all fields with game datas
     */
    private void fillGameData(){
        try {
            Picasso.get().load(game.getString("background_image")).resize(800,400).centerInside().into((ImageView)findViewById(R.id.image_top_game_screen_id));
            ((TextView)findViewById(R.id.text_game_name_game_screen_id)).setText(game.getString("name"));
            ((TextView)findViewById(R.id.released_date_game_screen_id)).setText("Released date: "+ game.getString("released"));
            ((TextView)findViewById(R.id.description_game_screen_id)).setText(Html.fromHtml(game.getString("description")));
            ((TextView)findViewById(R.id.dev_game_screen_id)).setText(computeDatasFromArray("Devs", game.getJSONArray("developers")));
            ((TextView)findViewById(R.id.publisher_game_screen_id)).setText(computeDatasFromArray("Publishers", game.getJSONArray("publishers")));
            ((TextView)findViewById(R.id.average_time_game_screen_id)).setText("Average playtime: "+game.getString("playtime")+" hours");
            ((TextView)findViewById(R.id.metacritic_game_screen_id)).setText("Metacritic: " +(game.getString("metacritic") == "null" ? "no record":game.getString("metacritic")+" score"));
            ((TextView)findViewById(R.id.genres_game_screen_id)).setText(computeDatasFromArray("Genres", game.getJSONArray("genres")));
            addScreen();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Add image to scroll section
     */
    private void addScreen(){
        try {
            for(int i = 1; i < gameFromSearchData.getJSONArray("short_screenshots").length();i++){
                ImageView imgV = new ImageView(this);
                Picasso.get().load(gameFromSearchData.getJSONArray("short_screenshots").getJSONObject(i).getString("image")).resize(800, 400).centerInside().into(imgV);

                ((LinearLayout)findViewById(R.id.linear_layout_inside_scroll_view_game_screen_id)).addView(imgV);
            }
        }
        catch (Exception e){

        }
    }

    /**
     * Return a string containing all data from a json array
     * @param category category name
     * @param array json array
     * @return string containing data
     */
    private String computeDatasFromArray(String category, JSONArray array){
        try{
            String elem =category+": ";
            for(int i = 0; i < array.length(); i++){
                elem += array.getJSONObject(i).getString("name");
                if(i+1 < array.length())
                    elem += ", ";
            }
            return elem;
        }
        catch (Exception e){
            return null;
        }
    }

    /**
     * Add action on buttons
     */
    private void connectButtons(){
        findViewById(R.id.button_back_game_id).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    /**
     * Get data from game API
     * @param obj JSON data from game API
     */
    @Override
    public void getApiInfo(JSONObject obj) {
        game = obj;
        fillGameData();
    }
}