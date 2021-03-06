package com.example.myvideogamelist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myvideogamelist.ApiGestion.Database;
import com.example.myvideogamelist.ApiGestion.GamesAPI;
import com.example.myvideogamelist.InterfacesAppli.MyActivityImageDisplayable;
import com.example.myvideogamelist.InterfacesAppli.ObservatorAppli;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Class creating a game component to show it's descriptions and all infos
 */
public class GameScreenActivity extends AppCompatActivity implements MyActivityImageDisplayable, ObservatorAppli {

    private NavigationBar navigationBar = NavigationBar.getNavigationBar();
    private GamesAPI gamesAPI = GamesAPI.getGamesAPI();
    private JSONObject game, gameFromSearchData, user;
    private boolean isHiddenDesc= true;
    private int predHeight;
    private Database database = Database.getDatabase();
    private String comesFrom;
    private GameScreenActivity gameScreenActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_screen);

        gameScreenActivity = this;
        database.subscribe(this);

        //Get game data from previous activity
        Intent intent = getIntent();
        comesFrom = intent.getStringExtra("comesFrom");
        game = gamesAPI.parseResponse(intent.getStringExtra("gameData"));
        gameFromSearchData = game;
        user = database.getCurrentUser();

        if(comesFrom.compareTo("search") == 0) {//need a request we might not have seen this game before
            try {//new request for all game data
                gamesAPI.requestGameById(game.getString("id"), this);
            } catch (JSONException e) {
            }
        }
        else{
            //display game data, we already have everything
            fillGameData();
        }

        addNavigationBar();
        navigationBar.init(this);
        connectButtons();
    }

    /**
     * Internet connexion impossible, displaying placeholders
     */
    private void connectionError(){
        ((ViewGroup)findViewById(R.id.scroll_view_game_screen_id)).removeAllViews();
        LayoutInflater vi = (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = vi.inflate(R.layout.no_internet_error, findViewById(R.id.scroll_view_game_screen_id), true);
        findViewById(R.id.button_to_user_rating_game_screen_id).setVisibility(View.GONE);
    }

    /**
     * Insert navigation bar into the activity
     */
    private void addNavigationBar(){
        LayoutInflater vi = (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = vi.inflate(R.layout.bottom_bar_navigation, findViewById(R.id.game_screen_activity_id), true);
    }

    /**
     * If user has data on this game, we use it, else default
     * @param gameID the id of the game to look at
     */
    private void look4GameInUserData(String gameID){
        Boolean over = false;
        try{
            for(int i = 0; i < database.getCurrentUser().getJSONArray("games").length() && !over; i++){
                if (gameID.compareTo(database.getCurrentUser().getJSONArray("games").getJSONObject(i).getString("id")) == 0){
                    String time = "<span>Your time<br/></span><span style=\"color:#FA8002;\">"+ user.getJSONArray("games").getJSONObject(i).getString("hours") + " hours</span>";
                    ((TextView)findViewById(R.id.user_time_game_screen_id)).setText(Html.fromHtml(time));
                    ((TextView)findViewById(R.id.user_score_game_screen_id)).setText(Html.fromHtml("<span>Your score<br/></span><span style=\"color:#FA8002;\">" +user.getJSONArray("games").getJSONObject(i).getString("score") + "/10</span>"));
                    over = true;
                }
            }

            if(!over){//user has no data for this game
                ((TextView)findViewById(R.id.user_time_game_screen_id)).setText(Html.fromHtml("<span>Your time<br/></span><span style=\"color:#FA8002;\">no record</span>"));
                ((TextView)findViewById(R.id.user_score_game_screen_id)).setText(Html.fromHtml("<span>Your score<br/></span><span style=\"color:#FA8002;\">no record</span>"));
            }
        }
        catch (Exception e){
        }
    }

    /**
     * Return the average number from users rating
     * @param field field we are building a mean for
     * @param textAfter text to display after users mean
     * @return string mean of users rating field
     */
    private String getReviewForGame(String field, String textAfter){
        int found = 0;
        int nb = 0;

        try{
            JSONArray users = database.getUsers().getJSONArray("users");
            if(users.length() == 0)
                throw new Exception();
            else{
                for(int i = 0; i < users.length(); i++){
                    JSONArray games = users.getJSONObject(i).getJSONArray("games");
                    for(int j = 0; j < games.length(); j++){
                        if(games.getJSONObject(j).getString("id").compareTo(game.getString("id")) == 0){
                            found += Integer.parseInt(games.getJSONObject(j).getString(field));
                            nb++;
                        }
                    }
                }
            }
        }
        catch(Exception e){
        }
        if(nb == 0)
            return "no record";
        return String.valueOf(found / nb) + textAfter;
    }

    /**
     * Set all fields with game datas
     */
    private void fillGameData(){
        try {
            ((TextView)findViewById(R.id.text_game_name_game_screen_id)).setText(game.getString("name"));
            ((TextView)findViewById(R.id.released_date_game_screen_id)).setText(Html.fromHtml("<span>Released\n</span><span style=\"color:#FA8002;\">"+ game.getString("released")+"</span>"));
            ((TextView)findViewById(R.id.description_game_screen_id)).setText(Html.fromHtml(game.getString("description")));
            ((TextView)findViewById(R.id.dev_game_screen_id)).setText(Html.fromHtml(computeDatasFromArray("Developers\n", game.getJSONArray("developers"),"", true)));
            ((TextView)findViewById(R.id.publisher_game_screen_id)).setText(Html.fromHtml(computeDatasFromArray("Publishers\n", game.getJSONArray("publishers"),"", true)));
            ((TextView)findViewById(R.id.average_time_game_screen_id)).setText(Html.fromHtml("<span>Average playtime\n</span><span style=\"color:#FA8002;\">"+getReviewForGame("hours", " hours")+"</span>"));
            ((TextView)findViewById(R.id.average_rating_game_screen_id)).setText(Html.fromHtml("<span>Average score\n</span><span style=\"color:#FA8002;\">"+getReviewForGame("score", "/10")+"</span>"));
            ((TextView)findViewById(R.id.metacritic_game_screen_id)).setText(Html.fromHtml("<span>Metacritic<br/></span><span style=\"color:#FA8002;\">" +(game.getString("metacritic").compareTo("null") ==0 ? "no record":game.getString("metacritic"))+"</span>"));
            ((TextView)findViewById(R.id.genres_game_screen_id)).setText(computeDatasFromArray("", game.getJSONArray("genres"), "\t\t\t", false));
            look4GameInUserData(game.getString("id"));
            addScreen();

            //If game name is too large for title, reduce size
            if(game.getString("name").length() > 18){
                ((TextView)findViewById(R.id.text_game_name_game_screen_id)).setTextSize(15);
            }

            //Making description bigger or shorter on click
            findViewById(R.id.description_game_screen_id).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(isHiddenDesc) {
                        isHiddenDesc = false;
                        predHeight = findViewById(R.id.description_game_screen_id).getLayoutParams().height;
                        ((TextView) findViewById(R.id.description_show_game_screen_id)).setText(getString(R.string.click_to_less_desc));
                        findViewById(R.id.description_game_screen_id).getLayoutParams().height = LinearLayout.LayoutParams.WRAP_CONTENT;
                        findViewById(R.id.description_game_screen_id).requestLayout();
                    }
                    else{
                        ((TextView) findViewById(R.id.description_show_game_screen_id)).setText(getString(R.string.click_to_show_desc));
                        findViewById(R.id.description_game_screen_id).getLayoutParams().height = predHeight;
                        findViewById(R.id.description_game_screen_id).requestLayout();
                        isHiddenDesc = true;
                    }
                }
            });
        } catch (JSONException e) {
        }
    }

    /**
     * Add images to scroll section
     */
    private void addScreen(){
        try {
            for(int i = 0; i < gameFromSearchData.getJSONArray("short_screenshots").length();i++){
                ImageView imgV = new ImageView(this);
                Picasso.get().load(gameFromSearchData.getJSONArray("short_screenshots").getJSONObject(i).getString("image")).resize(1000, 600).centerInside().into(imgV);

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
     * @param addHtml boolean, true if html is needed
     * @param elemToAdd separator
     * @return string containing data
     */
    private String computeDatasFromArray(String category, JSONArray array, String elemToAdd, boolean addHtml){
        try{
            String elem;
            if(addHtml)
                elem ="<span>"+category+"<br/></span></span><span style=\"color:#FA8002;\">";
            else
                elem = category;
            for(int i = 0; i < array.length(); i++){
                elem += array.getJSONObject(i).getString("name") + elemToAdd;
                if(i+1 < array.length() && elemToAdd.isEmpty())
                    elem += ", ";
                else if(i+1 < array.length())
                    elem += "-"+elemToAdd;
            }
            if(addHtml)
                return elem+"</span>";
            else
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
        GameScreenActivity gsa = this;
        //on click on cross button
        findViewById(R.id.button_back_game_id).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                database.unsubscribe(gsa);
                finish();
            }
        });

        //on click on edit button on game info
        findViewById(R.id.button_to_user_rating_game_screen_id).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), UserGameRating.class);
                try {
                    intent.putExtra("gameID", game.getString("id"));
                    intent.putExtra("gameData", game.toString());
                    intent.putExtra("gameDataSearch", gameFromSearchData.toString());
                    intent.putExtra("comesFrom", comesFrom);
                }
                catch (Exception e){System.out.println(e.getMessage());}
                startActivity(intent);
            }
        });
    }

    /**
     * Receive data from game API
     * @param obj JSON data from game API
     */
    @Override
    public void getApiInfo(JSONObject obj) {
        game = obj;
        if(game == null)
            connectionError();
        else
            fillGameData();
    }

    /**
     * When user come back to this activity, we need refresh data
     */
    @Override
    protected void onResume() {
        super.onResume();
        try{
            look4GameInUserData(game.getString("id"));
            ((TextView)findViewById(R.id.average_time_game_screen_id)).setText(Html.fromHtml("<span>Average playtime\n<br/></span><span style=\"color:#FA8002;\">"+getReviewForGame("hours", " hours")+"</span>"));
            ((TextView)findViewById(R.id.average_rating_game_screen_id)).setText(Html.fromHtml("<span>Average score\n<br/></span><span style=\"color:#FA8002;\">"+getReviewForGame("score", "/10")+"</span>"));
            findViewById(R.id.button_to_user_rating_game_screen_id).setVisibility(View.VISIBLE);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Received a notification from observed database
     */
    @Override
    public void notified(int status) {
        if(status != 200) {
            gameScreenActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    String txt = gameScreenActivity.getResources().getString(R.string.internet_error);
                    Toast.makeText(gameScreenActivity.getApplicationContext(), txt, Toast.LENGTH_LONG).show();
                }
            });
        }
    }
}