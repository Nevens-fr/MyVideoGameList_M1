package com.example.myvideogamelist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.myvideogamelist.ApiGestion.Database;

import org.json.JSONObject;

public class UserGameRating extends AppCompatActivity {

    private NavigationBar navigationBar = NavigationBar.getNavigationBar();
    private Database database = Database.getDatabase();

    private int tab[] = {10,9,8,7,6};

    private int selectedRating = -1, selectedStatus = -1, selectedHours = -1, selectedMin = -1;

    private String feedback;

    private View selectedStatusButton, selectedRatingButton;

    private boolean isEmptyStatus, dataChanged = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_game_rating);

        navigationBar.init(this);

        Intent intent = getIntent();
        String gameID = intent.getStringExtra("gameID");

        look4GameInUserData(gameID);
    }

    /**
     * If user has data on this game, we use it, else default
     * @param gameId the game ID
     */
    private void look4GameInUserData(String gameId){
        try{
            Boolean over = false;
            for(int i = 0; i < database.getCurrentUser().getJSONArray("games").length() && !over; i++){
                if (gameId.compareTo(database.getCurrentUser().getJSONArray("games").getJSONObject(i).getString("id")) == 0){
                    useUserData(database.getCurrentUser().getJSONArray("games").getJSONObject(i));
                    over = true;
                }
            }
        }
        catch (Exception e){
            selectedRatingButton = findViewById(R.id.empty_rating_id);
            selectedStatusButton = null;
            isEmptyStatus = true;
        }
        finally {
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
                                break;
            case "Abandoned" :  findViewById(R.id.abandonned_button_id).setBackgroundResource(R.drawable.button_border_orange);
                                selectedStatus = 0;
                                break;
            case "Playing"   :  findViewById(R.id.playing_button_id).setBackgroundResource(R.drawable.button_border_orange);
                                selectedStatus = 4;
                                break;
            case "On-hold"   :  findViewById(R.id.on_hold_button_id).setBackgroundResource(R.drawable.button_border_orange);
                                selectedStatus = 3;
                                break;
            case "Planned"   :  findViewById(R.id.planned_button_id).setBackgroundResource(R.drawable.button_border_orange);
                                selectedStatus = 2;
        }

        switch (data.getString("score")){
            case "--" :findViewById(R.id.empty_rating_id).setBackgroundResource(R.drawable.button_border_orange); selectedRating = -1;break;
            case "10" :findViewById(R.id.ten_rating_id).setBackgroundResource(R.drawable.button_border_orange); selectedRating = 10;break;
            case "9" :findViewById(R.id.nine_rating_id).setBackgroundResource(R.drawable.button_border_orange); selectedRating = 9;break;
            case "8" :findViewById(R.id.height_rating_id).setBackgroundResource(R.drawable.button_border_orange); selectedRating = 8;break;
            case "7" :findViewById(R.id.seven_rating_id).setBackgroundResource(R.drawable.button_border_orange); selectedRating = 7;break;
            case "6" :findViewById(R.id.six_rating_id).setBackgroundResource(R.drawable.button_border_orange); selectedRating = 6;break;
            case "5" :findViewById(R.id.five_rating_id).setBackgroundResource(R.drawable.button_border_orange); selectedRating = 5;break;
            case "4" :findViewById(R.id.four_rating_id).setBackgroundResource(R.drawable.button_border_orange); selectedRating = 4;break;
            case "3" :findViewById(R.id.three_rating_id).setBackgroundResource(R.drawable.button_border_orange); selectedRating = 3;break;
            case "2" :findViewById(R.id.two_rating_id).setBackgroundResource(R.drawable.button_border_orange); selectedRating = 2;break;
            case "1" :findViewById(R.id.one_rating_id).setBackgroundResource(R.drawable.button_border_orange); selectedRating = 1;break;
            case "0" :findViewById(R.id.zero_rating_id).setBackgroundResource(R.drawable.button_border_orange); selectedRating = 0;
        }
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
                //TODO save data from user
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
                if(dataChanged){
                    //TODO save data from user
                }
                finish();
            }
        });
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