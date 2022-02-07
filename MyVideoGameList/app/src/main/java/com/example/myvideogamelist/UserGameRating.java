package com.example.myvideogamelist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myvideogamelist.ApiGestion.Database;
import com.example.myvideogamelist.ApiGestion.Game;
import com.example.myvideogamelist.ApiGestion.GamesAPI;
import com.example.myvideogamelist.ApiGestion.Rating;

import org.json.JSONArray;
import org.json.JSONObject;

public class UserGameRating extends AppCompatActivity {

    private final NavigationBar navigationBar = NavigationBar.getNavigationBar();
    private final Database database = Database.getDatabase();

    private int selectedRating = -1;
    private int selectedStatus = -1;
    private int selectedHours = -1;
    private int userGameID =-1;
    private final int duration = Toast.LENGTH_LONG;
    private String gameID;
    private String feedback;
    private View selectedStatusButton, selectedRatingButton;
    private boolean isEmptyStatus, dataChanged = false;
    private JSONObject gameData, gameDataFromSearch;
    private final GamesAPI gamesAPI = GamesAPI.getGamesAPI();
    private Game gameToSave;
    private String comesFrom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_game_rating);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        addNavigationBar();
        navigationBar.init(this);

        Intent intent = getIntent();
        gameID = intent.getStringExtra("gameID");
        gameData = gamesAPI.parseResponse(intent.getStringExtra("gameData"));
        gameDataFromSearch = gamesAPI.parseResponse(intent.getStringExtra("gameDataSearch"));
        comesFrom = intent.getStringExtra("comesFrom");

        if(comesFrom.compareTo("search") == 0)//coming from search activity, need to build game data json to save it
            createGameData();
        look4GameInUserData();
    }

    /**
     * Prepare game data to be save to limit GameAPI usage for those the user already possess in his lists
     */
    private void createGameData(){
        int nbGenres, nbDev, nbImages, nbPublishers;
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

            gameToSave.setGenres(getRelevantJSONARRAY("genres", 0));
            gameToSave.setImages(getRelevantJSONARRAY("short_screenshots", 0));
            gameToSave.setDevs(getRelevantJSONARRAY("developers", 1));
            gameToSave.setPublishers(getRelevantJSONARRAY("publishers", 1));
        }
        catch (Exception e){
            e.printStackTrace();
        }
        addOptionsToNumberPicker(((NumberPicker)findViewById(R.id.thousands_rating_id)), 0);
        addOptionsToNumberPicker(((NumberPicker)findViewById(R.id.hundreds_rating_id)), 0);
        addOptionsToNumberPicker(((NumberPicker)findViewById(R.id.dozens_rating_id)), 0);
        addOptionsToNumberPicker(((NumberPicker)findViewById(R.id.units_rating_id)), 0);
    }

    /**
     * Return the json array corresponding to the given key
     * @param key the key in the json object for access to the array
     * @param dbToUse define which jsonarray to use
     * @return a JSON array containing only relevant data
     */
    private JSONArray getRelevantJSONARRAY(String key, int dbToUse){
        try{
            return (dbToUse == 0) ? gameDataFromSearch.getJSONArray(key) : gameData.getJSONArray(key);
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Insert navigation bar into the activity
     */
    private void addNavigationBar(){
        LayoutInflater vi = (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        vi.inflate(R.layout.bottom_bar_navigation, findViewById(R.id.user_game_rating_activity_id), true);
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
            ((TextView)findViewById(R.id.text_game_rating_game_name)).setText(gameData.getString("name"));
            //If game name is too large for title, reduce size
            if(gameData.getString("name").length() > 15){
                System.out.println(((TextView)findViewById(R.id.text_game_rating_game_name)).getTextSize());
                ((TextView)findViewById(R.id.text_game_rating_game_name)).setTextSize(15);
            }
        }
        catch (Exception e){
            selectedRatingButton = findViewById(R.id.empty_rating_id);
            selectedStatusButton = null;
            isEmptyStatus = true;
            addOptionsToNumberPicker(((NumberPicker)findViewById(R.id.thousands_rating_id)), 0);
            addOptionsToNumberPicker(((NumberPicker)findViewById(R.id.hundreds_rating_id)), 0);
            addOptionsToNumberPicker(((NumberPicker)findViewById(R.id.dozens_rating_id)), 0);
            addOptionsToNumberPicker(((NumberPicker)findViewById(R.id.units_rating_id)), 0);
            e.printStackTrace();
        }
        finally {
            if(!over){
                selectedRatingButton = findViewById(R.id.empty_rating_id);
                selectedStatusButton = null;
                isEmptyStatus = true;
            }
            connectRatingButtons();
            connectTopToolbarButtons();
            connectStatusButtons();
            ((HorizontalScrollView)findViewById(R.id.scroll_user_rating_id)).requestChildFocus(selectedRatingButton, selectedRatingButton);
        }
    }

    /**
     * Add all the necessary options and selected options to the numberPicker
     * @param npick the number picker to add options to
     * @param value selected value to focus on
     */
    private void addOptionsToNumberPicker(NumberPicker npick, int value){
        npick.setMinValue(0);
        npick.setMaxValue(9);
        npick.setDisplayedValues(new String[]{"0","1","2","3","4","5","6","7","8","9"});
        npick.setValue(value);
    }

    /**
     * Create all data for number picker from user's game data
     * @param data json object containing user data
     * @throws Exception
     */
    private void createNumberPickerWithData(JSONObject data)throws Exception{
        String hours = data.getString("hours");
        int val, hoursInt = Integer.parseInt(hours);

        //from 1000 to 9999
        val = (hoursInt % 10000)/1000;
        addOptionsToNumberPicker(((NumberPicker)findViewById(R.id.thousands_rating_id)), val);

        //from 100 to 999
        val = (hoursInt % 1000)/100;
        addOptionsToNumberPicker(((NumberPicker)findViewById(R.id.hundreds_rating_id)), val);

        //from 10 to 99
        val = (hoursInt % 100)/10;
        addOptionsToNumberPicker(((NumberPicker)findViewById(R.id.dozens_rating_id)), val);

        //from 0 to 9
        val = hoursInt % 10;
        addOptionsToNumberPicker(((NumberPicker)findViewById(R.id.units_rating_id)), val);
    }

    /**
     * Apply user data on game
     * @param data jsonobject with user data on this game
     * @throws Exception data not found
     */
    private void useUserData(JSONObject data) throws Exception{

        ((TextView)findViewById(R.id.feedback_game_rating_id)).setText(data.getString("feedback"));
        createNumberPickerWithData(data);
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
        applyOnClickListenerStatusButton(findViewById(R.id.abandonned_button_id), 0);
        applyOnClickListenerStatusButton(findViewById(R.id.finished_button_id), 1);
        applyOnClickListenerStatusButton(findViewById(R.id.planned_button_id), 2);
        applyOnClickListenerStatusButton(findViewById(R.id.on_hold_button_id), 3);
        applyOnClickListenerStatusButton(findViewById(R.id.playing_button_id), 4);
    }

    /**
     * Apply on cclick listener on status button
     * @param v button itself
     * @param value associated value
     */
    private void applyOnClickListenerStatusButton(View v, int value){
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setWhiteStrokeStatusButtons(v);
                selectedStatus = value;
                dataChanged = true;
                v.setBackgroundResource(R.drawable.button_border_orange);
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
                    selectedHours = Integer.parseInt(String.valueOf(((NumberPicker)findViewById(R.id.thousands_rating_id)).getValue())) * 1000;
                    selectedHours += Integer.parseInt(String.valueOf(((NumberPicker)findViewById(R.id.hundreds_rating_id)).getValue())) * 100;
                    selectedHours += Integer.parseInt(String.valueOf(((NumberPicker)findViewById(R.id.dozens_rating_id)).getValue())) * 10;
                    selectedHours += Integer.parseInt(String.valueOf(((NumberPicker)findViewById(R.id.units_rating_id)).getValue()));
                }
                catch(Exception e){
                    selectedHours = 0;
                    e.printStackTrace();
                }

                //Looking for any change from user already saved dataException
                try{
                    if(feedback.compareTo(database.getCurrentUser().getJSONArray("games").getJSONObject(userGameID).getString("feedback")) != 0)
                        dataChanged = true;
                    if(String.valueOf(selectedHours).compareTo(database.getCurrentUser().getJSONArray("games").getJSONObject(userGameID).getString("hours")) != 0)
                        dataChanged = true;

                    if(dataChanged){//save change
                        Rating r = new Rating(gameID, feedback, String.valueOf(selectedHours), returnStatus(), returnRating());
                        database.getCurrentUser().getJSONArray("games").remove(userGameID);
                        database.getCurrentUser().getJSONArray("games").put(r.getJSONObject());
                        database.requestPost(1, null, database.getUsers());
                    }
                }
                catch (Exception e){//user does not possess the game already, save change
                    Rating rating = new Rating(gameID, feedback,String.valueOf(selectedHours), returnStatus(), returnRating());

                    try{
                        database.getCurrentUser().getJSONArray("games").put(rating.getJSONObject());//add game feedback to user data
                        if(comesFrom.compareTo("search") == 0)//coming from search activity, need to save game data
                            database.getGames().getJSONArray("games").put(gameToSave.getJSONObject());//adding game to our DB
                    }
                    catch(Exception e4){ e4.printStackTrace(); }
                    database.requestPost(1, null, database.getUsers());
                    if(comesFrom.compareTo("search") == 0){//coming from search activity, need to load newly saved game data
                        database.requestPost(0, null, database.getGames());
                    }
                }

                boolean canLeave = true;

                //get clean data from our DB
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Database.getDatabase().requestGet(0);
                        Database.getDatabase().requestGet(1);
                        if(Database.getDatabase().getCurrentUser() != null || Database.getDatabase().getGames() != null)
                            Database.getDatabase().createListsGames();
                    }
                });
                thread.start();

                try {
                    thread.join();
                    if(Database.getDatabase().getCurrentUser() == null || Database.getDatabase().getGames() == null){
                        Toast toast = Toast.makeText(getApplicationContext(), "No internet connection, your changes will not be saved, please check your Wifi or data are turned on", duration);
                        toast.show();
                        canLeave = false;
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if(canLeave)
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
     * Return a string equivalent to selected rating
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
        applyOnClickListenerRatingButton(findViewById(R.id.empty_rating_id), -1);
        applyOnClickListenerRatingButton(findViewById(R.id.ten_rating_id), 10);
        applyOnClickListenerRatingButton(findViewById(R.id.nine_rating_id), 9);
        applyOnClickListenerRatingButton(findViewById(R.id.height_rating_id), 8);
        applyOnClickListenerRatingButton(findViewById(R.id.seven_rating_id), 7);
        applyOnClickListenerRatingButton(findViewById(R.id.six_rating_id), 6);
        applyOnClickListenerRatingButton(findViewById(R.id.five_rating_id), 5);
        applyOnClickListenerRatingButton(findViewById(R.id.four_rating_id), 4);
        applyOnClickListenerRatingButton(findViewById(R.id.three_rating_id), 3);
        applyOnClickListenerRatingButton(findViewById(R.id.two_rating_id), 2);
        applyOnClickListenerRatingButton(findViewById(R.id.one_rating_id), 1);
        applyOnClickListenerRatingButton(findViewById(R.id.zero_rating_id), 0);
    }

    /**
     * Apply button on click listener for rating button
     * @param v button itself
     * @param val value associated
     */
    private void applyOnClickListenerRatingButton(View v, int val){
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeRating_Border(v);
                selectedRating = val;
                dataChanged = true;
                v.setBackgroundResource(R.drawable.button_border_orange);
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
        if(!isEmptyStatus){
            selectedStatusButton.setBackgroundResource(R.drawable.button_border_white);
        }
        else
            isEmptyStatus = false;
        selectedStatusButton = currentView;
    }
}