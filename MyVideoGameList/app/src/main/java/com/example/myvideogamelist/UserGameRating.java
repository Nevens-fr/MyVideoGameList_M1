package com.example.myvideogamelist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.myvideogamelist.ApiGestion.Database;
import com.example.myvideogamelist.ApiGestion.Game;
import com.example.myvideogamelist.ApiGestion.GamesAPI;
import com.example.myvideogamelist.ApiGestion.Rating;
import com.example.myvideogamelist.ApiGestion.User;

import org.json.JSONObject;

public class UserGameRating extends AppCompatActivity {

    private NavigationBar navigationBar = NavigationBar.getNavigationBar();
    private Database database = Database.getDatabase();

    private int tab[] = {10,9,8,7,6};

    private int selectedRating = -1;
    private int selectedStatus = -1;
    private int selectedHours = -1;
    private int selectedMin = -1;
    private int userGameID =-1;
    private String gameID;
    private String feedback;
    private View selectedStatusButton, selectedRatingButton;
    private boolean isEmptyStatus, dataChanged = false;
    private JSONObject gameData, gameDataFromSearch;
    private GamesAPI gamesAPI = GamesAPI.getGamesAPI();
    private Game gameToSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_game_rating);

        addNavigationBar();
        navigationBar.init(this);

        Intent intent = getIntent();
        gameID = intent.getStringExtra("gameID");
        gameData = gamesAPI.parseResponse(intent.getStringExtra("gameData"));
        gameDataFromSearch = gamesAPI.parseResponse(intent.getStringExtra("gameDataSearch"));

        look4GameInUserData();
    }

    /**
     *
     */
    private void createGameData(){
        int nbGenres =0, nbDev =0, nbImages =0, nbPublishers=0;
        try{
            nbGenres = gameDataFromSearch.getJSONArray("genres").length();
            nbImages = gameDataFromSearch.getJSONArray("short_screenshots").length();
            nbDev = gameData.getJSONArray("developers").length();
            nbPublishers = gameData.getJSONArray("publishers").length();

            gameToSave = new Game(nbGenres, nbDev, nbImages, nbPublishers);

            gameToSave.setIdGame(gameData.getString("id"));
            gameToSave.setDescription(gameData.getString("description"));
            gameToSave.setMetacritic(gameData.getString("metacritic"));
            gameToSave.setReleasedDate(gameData.getString("released"));
            gameToSave.setPlaytime(gameData.getString("playtime"));
            gameToSave.setName(gameData.getString("name"));

            gameToSave.setGenres(buildArrayFromJSonArray("genres", nbGenres));
            gameToSave.setImages(buildArrayFromJSonArray("short_screenshots", nbGenres));
            gameToSave.setDevs(buildArrayFromJSonArray("developers", nbGenres));
            gameToSave.setPublishers(buildArrayFromJSonArray("publishers", nbGenres));
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     *
     */
    private String[] buildArrayFromJSonArray(String key, int size){

        String[] array = new String[size];

    //todo rentrer les array dev genre publisher et images pour save tout ça
        return array;
    }

    /**
     * Insert navigation bar into the activity
     */
    private void addNavigationBar(){
        LayoutInflater vi = (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = vi.inflate(R.layout.bottom_bar_navigation, findViewById(R.id.user_game_rating_activity_id), true);
    }

    /**
     * If user has data on this game, we use it, else default
     */
    private void look4GameInUserData(){
        Boolean over = false;
        try{
            for(int i = 0; i < database.getCurrentUser().getJSONArray("games").length() && !over; i++){
                if (gameID.compareTo(database.getCurrentUser().getJSONArray("games").getJSONObject(i).getString("id")) == 0){
                    useUserData(database.getCurrentUser().getJSONArray("games").getJSONObject(i));
                    userGameID = i;
                    isEmptyStatus = false;
                    over = true;
                }
            }
        }
        catch (Exception e){
            selectedRatingButton = findViewById(R.id.empty_rating_id);
            selectedStatusButton = null;
            isEmptyStatus = true;
            e.printStackTrace();
        }
        finally {
            if(over == false){
                selectedRatingButton = findViewById(R.id.empty_rating_id);
                selectedStatusButton = null;
                isEmptyStatus = true;
            }
            connectRatingButtons();
            connectTopToolbarButtons();
            connectStatusButtons();
        }
    }

    /**
     * Apply user data on game
     * @param data jsonobject with user data on this game
     * @throws Exception data not found
     */
    private void useUserData(JSONObject data) throws Exception{

        ((TextView)findViewById(R.id.feedback_game_rating_id)).setText(data.getString("feedback"));
        ((TextView)findViewById(R.id.edit_text_minutes_game_rating_id)).setText(data.getString("min"));
        ((TextView)findViewById(R.id.edit_text_hours_game_rating_id)).setText(data.getString("hours"));

        //Applying user's status on game
        switch (data.getString("status")){
            case "Finished"  :  findViewById(R.id.finished_button_id).setBackgroundResource(R.drawable.button_border_orange);
                                selectedStatus = 1;
                                selectedStatusButton = findViewById(R.id.finished_button_id);
                                break;
            case "Abandoned" :  findViewById(R.id.abandonned_button_id).setBackgroundResource(R.drawable.button_border_orange);
                                selectedStatus = 0;
                                selectedStatusButton =findViewById(R.id.abandonned_button_id);
                                break;
            case "Playing"   :  findViewById(R.id.playing_button_id).setBackgroundResource(R.drawable.button_border_orange);
                                selectedStatus = 4;
                                selectedStatusButton =findViewById(R.id.playing_button_id);
                                break;
            case "On-hold"   :  findViewById(R.id.on_hold_button_id).setBackgroundResource(R.drawable.button_border_orange);
                                selectedStatus = 3;
                                selectedStatusButton =findViewById(R.id.on_hold_button_id);
                                break;
            case "Planned"   :  findViewById(R.id.planned_button_id).setBackgroundResource(R.drawable.button_border_orange);
                                selectedStatus = 2;
                                selectedStatusButton =findViewById(R.id.planned_button_id);
        }

        switch (data.getString("score")){
            case "--" :findViewById(R.id.empty_rating_id).setBackgroundResource(R.drawable.button_border_orange); selectedRating = -1;
                    selectedRatingButton = findViewById(R.id.empty_rating_id);
                    break;
            case "10" :findViewById(R.id.ten_rating_id).setBackgroundResource(R.drawable.button_border_orange); selectedRating = 10;
                    selectedRatingButton = findViewById(R.id.ten_rating_id);
                    break;
            case "9" :findViewById(R.id.nine_rating_id).setBackgroundResource(R.drawable.button_border_orange); selectedRating = 9;
                    selectedRatingButton = findViewById(R.id.nine_rating_id);
                    break;
            case "8" :findViewById(R.id.height_rating_id).setBackgroundResource(R.drawable.button_border_orange); selectedRating = 8;
                    selectedRatingButton = findViewById(R.id.height_rating_id);
                    break;
            case "7" :findViewById(R.id.seven_rating_id).setBackgroundResource(R.drawable.button_border_orange); selectedRating = 7;
                    selectedRatingButton = findViewById(R.id.seven_rating_id);
                    break;
            case "6" :findViewById(R.id.six_rating_id).setBackgroundResource(R.drawable.button_border_orange); selectedRating = 6;
                    selectedRatingButton = findViewById(R.id.six_rating_id);
                    break;
            case "5" :findViewById(R.id.five_rating_id).setBackgroundResource(R.drawable.button_border_orange); selectedRating = 5;
                    selectedRatingButton = findViewById(R.id.five_rating_id);
                    break;
            case "4" :findViewById(R.id.four_rating_id).setBackgroundResource(R.drawable.button_border_orange); selectedRating = 4;
                    selectedRatingButton = findViewById(R.id.four_rating_id);
                    break;
            case "3" :findViewById(R.id.three_rating_id).setBackgroundResource(R.drawable.button_border_orange); selectedRating = 3;
                    selectedRatingButton = findViewById(R.id.three_rating_id);
                    break;
            case "2" :findViewById(R.id.two_rating_id).setBackgroundResource(R.drawable.button_border_orange); selectedRating = 2;
                    selectedRatingButton = findViewById(R.id.two_rating_id);
                    break;
            case "1" :findViewById(R.id.one_rating_id).setBackgroundResource(R.drawable.button_border_orange); selectedRating = 1;
                    selectedRatingButton = findViewById(R.id.one_rating_id);
                    break;
            case "0" :findViewById(R.id.zero_rating_id).setBackgroundResource(R.drawable.button_border_orange); selectedRating = 0;
                    selectedRatingButton = findViewById(R.id.zero_rating_id);
                    break;
        }
        findViewById(R.id.empty_rating_id).setBackgroundResource(R.drawable.button_black_border);
    }

    /**
     * Create buttons interactions for status buttons
     */
    private void connectStatusButtons(){
        //on click on abandoned button
        findViewById(R.id.abandonned_button_id).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setWhiteStrokeStatusButtons(findViewById(R.id.abandonned_button_id));
                selectedStatus = 0;
                dataChanged = true;
                findViewById(R.id.abandonned_button_id).setBackgroundResource(R.drawable.button_border_orange);
            }
        });

        //on click on finished button
        findViewById(R.id.finished_button_id).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setWhiteStrokeStatusButtons(findViewById(R.id.finished_button_id));
                selectedStatus = 1;
                dataChanged = true;
                findViewById(R.id.finished_button_id).setBackgroundResource(R.drawable.button_border_orange);
            }
        });

        //on click on planned button
        findViewById(R.id.planned_button_id).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setWhiteStrokeStatusButtons(findViewById(R.id.planned_button_id));
                selectedStatus = 2;
                dataChanged = true;
                findViewById(R.id.planned_button_id).setBackgroundResource(R.drawable.button_border_orange);
            }
        });

        //on click on on-hold button
        findViewById(R.id.on_hold_button_id).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setWhiteStrokeStatusButtons(findViewById(R.id.on_hold_button_id));
                selectedStatus = 3;
                dataChanged = true;
                findViewById(R.id.on_hold_button_id).setBackgroundResource(R.drawable.button_border_orange);
            }
        });

        //on click on playing button
        findViewById(R.id.playing_button_id).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setWhiteStrokeStatusButtons(findViewById(R.id.playing_button_id));
                selectedStatus = 4;
                dataChanged = true;
                findViewById(R.id.playing_button_id).setBackgroundResource(R.drawable.button_border_orange);
            }
        });
    }

    /**
     * Create buttons interactions for top toolbar buttons
     */
    private void connectTopToolbarButtons(){
        //on click on cross button
        findViewById(R.id.button_back_game_id).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //on click on trash button
        findViewById(R.id.button_game_rating_cancel_id).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //on click on save button
        findViewById(R.id.button_game_rating_save_id).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                feedback = ((EditText)findViewById(R.id.feedback_game_rating_id)).getText().toString();
                try{
                    selectedHours = Integer.parseInt(((EditText)findViewById(R.id.edit_text_hours_game_rating_id)).getText().toString());
                }
                catch(Exception e){
                    selectedHours = 0;
                }
                try{
                    selectedMin = Integer.parseInt(((EditText)findViewById(R.id.edit_text_minutes_game_rating_id)).getText().toString());
                }
                catch(Exception e){
                    selectedMin = 0;
                }
                //Looking for any change from user already saved data
                try{
                    if(feedback.compareTo(database.getCurrentUser().getJSONArray("games").getJSONObject(userGameID).getString("feedback")) != 0)
                        dataChanged = true;
                    if(String.valueOf(selectedHours).compareTo(database.getCurrentUser().getJSONArray("games").getJSONObject(userGameID).getString("hours")) != 0)
                        dataChanged = true;
                    if(String.valueOf(selectedMin).compareTo(database.getCurrentUser().getJSONArray("games").getJSONObject(userGameID).getString("min")) != 0)
                        dataChanged = true;

                    if(dataChanged){//save change
                        database.getCurrentUser().getJSONArray("games").getJSONObject(userGameID).remove("feedback");
                        database.getCurrentUser().getJSONArray("games").getJSONObject(userGameID).put("feedback", feedback);

                        database.getCurrentUser().getJSONArray("games").getJSONObject(userGameID).remove("hours");
                        database.getCurrentUser().getJSONArray("games").getJSONObject(userGameID).put("hours", String.valueOf(selectedHours));

                        database.getCurrentUser().getJSONArray("games").getJSONObject(userGameID).remove("min");
                        database.getCurrentUser().getJSONArray("games").getJSONObject(userGameID).put("min", String.valueOf(selectedMin));

                        database.getCurrentUser().getJSONArray("games").getJSONObject(userGameID).remove("status");
                        database.getCurrentUser().getJSONArray("games").getJSONObject(userGameID).put("status", returnStatus());

                        database.getCurrentUser().getJSONArray("games").getJSONObject(userGameID).remove("score");
                        database.getCurrentUser().getJSONArray("games").getJSONObject(userGameID).put("score", returnRating());
                        database.requestPost(1, null, database.getUsers());
                    }
                }
                catch (Exception e){//user does not possess the game already, save change
                    Rating rating = new Rating(gameID);
                    rating.setStatus(returnStatus());
                    rating.setScore(returnRating());
                    rating.setMin(String.valueOf(selectedMin));
                    rating.setHours(String.valueOf(selectedHours));
                    rating.setFeedback(feedback);
                    try{
                        database.getCurrentUser().getJSONArray("games").put(rating.getJSONObject());//add game feedback to user data
                        //database.getGames().getJSONArray("games").put(gameToSave);//adding game to our DB
                        //todo décommenter quand données prêtes
                    }
                    catch(Exception e4){ }
                    database.requestPost(1, null, database.getUsers());
                    //database.requestPost(0, null, database.getGames());
                    //todo décommenter quand données prêtes
                }

                finish();
            }
        });
    }

    /**
     * Return a string equivalent to selected status
     * @return status in string
     */
    private String returnStatus(){
        switch(selectedStatus){
            case 0 : return "Abandoned";
            case 1 : return "Finished";
            case 2 : return "Planned";
            case 3 : return "On-hold";
            default: return "Playing";
        }
    }


    /**
     * Return a string quivalent to selected rating
     * @return rating in string
     */
    private String returnRating(){
        switch (selectedRating){
            case -1:return "--";
            case 10:return "10";
            case 9 :return "9";
            case 8 :return "8";
            case 7 :return "7";
            case 6 :return "6";
            case 5 :return "5";
            case 4 :return "4";
            case 3 :return "3";
            case 2 :return "2";
            case 1 :return "1";
            default:return "0";
        }
    }

    /**
     * Create buttons interactions for user rating
     */
    private void connectRatingButtons(){
        findViewById(R.id.empty_rating_id).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeRating_Border(findViewById(R.id.empty_rating_id));
                selectedRating = -1;
                dataChanged = true;
                findViewById(R.id.empty_rating_id).setBackgroundResource(R.drawable.button_border_orange);
            }
        });
        findViewById(R.id.ten_rating_id).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeRating_Border(findViewById(R.id.ten_rating_id));
                selectedRating = 10;
                dataChanged = true;
                findViewById(R.id.ten_rating_id).setBackgroundResource(R.drawable.button_border_orange);
            }
        });
        findViewById(R.id.nine_rating_id).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeRating_Border(findViewById(R.id.nine_rating_id));
                selectedRating = 9;
                dataChanged = true;
                findViewById(R.id.nine_rating_id).setBackgroundResource(R.drawable.button_border_orange);
            }
        });
        findViewById(R.id.height_rating_id).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeRating_Border(findViewById(R.id.height_rating_id));
                selectedRating = 8;
                dataChanged = true;
                findViewById(R.id.height_rating_id).setBackgroundResource(R.drawable.button_border_orange);
            }
        });
        findViewById(R.id.seven_rating_id).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeRating_Border(findViewById(R.id.seven_rating_id));
                selectedRating = 7;
                dataChanged = true;
                findViewById(R.id.seven_rating_id).setBackgroundResource(R.drawable.button_border_orange);
            }
        });
        findViewById(R.id.six_rating_id).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeRating_Border(findViewById(R.id.six_rating_id));
                selectedRating = 6;
                dataChanged = true;
                findViewById(R.id.six_rating_id).setBackgroundResource(R.drawable.button_border_orange);
            }
        });
        findViewById(R.id.five_rating_id).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeRating_Border(findViewById(R.id.five_rating_id));
                selectedRating = 5;
                dataChanged = true;
                findViewById(R.id.five_rating_id).setBackgroundResource(R.drawable.button_border_orange);
            }
        });
        findViewById(R.id.four_rating_id).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeRating_Border(findViewById(R.id.four_rating_id));
                selectedRating = 4;
                dataChanged = true;
                findViewById(R.id.four_rating_id).setBackgroundResource(R.drawable.button_border_orange);
            }
        });
        findViewById(R.id.three_rating_id).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeRating_Border(findViewById(R.id.three_rating_id));
                selectedRating = 3;
                dataChanged = true;
                findViewById(R.id.three_rating_id).setBackgroundResource(R.drawable.button_border_orange);
            }
        });
        findViewById(R.id.two_rating_id).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeRating_Border(findViewById(R.id.two_rating_id));
                selectedRating = 2;
                dataChanged = true;
                findViewById(R.id.two_rating_id).setBackgroundResource(R.drawable.button_border_orange);
            }
        });
        findViewById(R.id.one_rating_id).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeRating_Border(findViewById(R.id.one_rating_id));
                selectedRating = 1;
                dataChanged = true;
                findViewById(R.id.one_rating_id).setBackgroundResource(R.drawable.button_border_orange);
            }
        });
        findViewById(R.id.zero_rating_id).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeRating_Border(findViewById(R.id.zero_rating_id));
                selectedRating = 0;
                dataChanged = true;
                findViewById(R.id.zero_rating_id).setBackgroundResource(R.drawable.button_border_orange);
            }
        });
    }

    /**
     * Put all ratings unselected
     * @param currentView new selected rating
     */
    private void removeRating_Border(View currentView){
        selectedRatingButton.setBackgroundResource(R.drawable.button_black_border);
        selectedRatingButton = currentView;
    }


    /**
     * Change selected button for status
     * @param currentView new selected status
     */
    private void setWhiteStrokeStatusButtons(View currentView){
        if(isEmptyStatus != true){
            selectedStatusButton.setBackgroundResource(R.drawable.button_border_white);
        }
        else
            isEmptyStatus = false;
        selectedStatusButton = currentView;
    }
}