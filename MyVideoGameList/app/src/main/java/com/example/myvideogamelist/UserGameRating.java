package com.example.myvideogamelist;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class UserGameRating extends AppCompatActivity {

    private NavigationBar navigationBar = NavigationBar.getNavigationBar();

    private int tab[] = {10,9,8,7,6};

    private int selectedRating = -1, selectedStatus = -1, selectedHours = -1, selectedMin = -1;

    private String feedback;

    private View selectedStatusButton, selectedRatingButton;

    private boolean isEmptyStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_game_rating);

        navigationBar.init(this);

        selectedRatingButton = findViewById(R.id.empty_rating_id);
        selectedStatusButton = null;
        isEmptyStatus = true;

        connectRatingButtons();
        connectTopToolbarButtons();
        connectStatusButtons();
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
                findViewById(R.id.abandonned_button_id).setBackgroundResource(R.drawable.button_border_orange);
            }
        });

        //on click on finished button
        findViewById(R.id.finished_button_id).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setWhiteStrokeStatusButtons(findViewById(R.id.finished_button_id));
                selectedStatus = 1;
                findViewById(R.id.finished_button_id).setBackgroundResource(R.drawable.button_border_orange);
            }
        });

        //on click on planned button
        findViewById(R.id.planned_button_id).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setWhiteStrokeStatusButtons(findViewById(R.id.planned_button_id));
                selectedStatus = 2;
                findViewById(R.id.planned_button_id).setBackgroundResource(R.drawable.button_border_orange);
            }
        });

        //on click on on-hold button
        findViewById(R.id.on_hold_button_id).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setWhiteStrokeStatusButtons(findViewById(R.id.on_hold_button_id));
                selectedStatus = 3;
                findViewById(R.id.on_hold_button_id).setBackgroundResource(R.drawable.button_border_orange);
            }
        });

        //on click on playing button
        findViewById(R.id.playing_button_id).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setWhiteStrokeStatusButtons(findViewById(R.id.playing_button_id));
                selectedStatus = 4;
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
                //TODO get rid of data from user
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
                findViewById(R.id.empty_rating_id).setBackgroundResource(R.drawable.button_border_orange);
            }
        });
        findViewById(R.id.ten_rating_id).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeRating_Border(findViewById(R.id.ten_rating_id));
                selectedRating = 10;
                findViewById(R.id.ten_rating_id).setBackgroundResource(R.drawable.button_border_orange);
            }
        });
        findViewById(R.id.nine_rating_id).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeRating_Border(findViewById(R.id.nine_rating_id));
                selectedRating = 9;
                findViewById(R.id.nine_rating_id).setBackgroundResource(R.drawable.button_border_orange);
            }
        });
        findViewById(R.id.height_rating_id).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeRating_Border(findViewById(R.id.height_rating_id));
                selectedRating = 8;
                findViewById(R.id.height_rating_id).setBackgroundResource(R.drawable.button_border_orange);
            }
        });
        findViewById(R.id.seven_rating_id).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeRating_Border(findViewById(R.id.seven_rating_id));
                selectedRating = 7;
                findViewById(R.id.seven_rating_id).setBackgroundResource(R.drawable.button_border_orange);
            }
        });
        findViewById(R.id.six_rating_id).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeRating_Border(findViewById(R.id.six_rating_id));
                selectedRating = 6;
                findViewById(R.id.six_rating_id).setBackgroundResource(R.drawable.button_border_orange);
            }
        });
        findViewById(R.id.five_rating_id).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeRating_Border(findViewById(R.id.five_rating_id));
                selectedRating = 5;
                findViewById(R.id.five_rating_id).setBackgroundResource(R.drawable.button_border_orange);
            }
        });
        findViewById(R.id.four_rating_id).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeRating_Border(findViewById(R.id.four_rating_id));
                selectedRating = 4;
                findViewById(R.id.four_rating_id).setBackgroundResource(R.drawable.button_border_orange);
            }
        });
        findViewById(R.id.three_rating_id).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeRating_Border(findViewById(R.id.three_rating_id));
                selectedRating = 3;
                findViewById(R.id.three_rating_id).setBackgroundResource(R.drawable.button_border_orange);
            }
        });
        findViewById(R.id.two_rating_id).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeRating_Border(findViewById(R.id.two_rating_id));
                selectedRating = 2;
                findViewById(R.id.two_rating_id).setBackgroundResource(R.drawable.button_border_orange);
            }
        });
        findViewById(R.id.one_rating_id).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeRating_Border(findViewById(R.id.one_rating_id));
                selectedRating = 1;
                findViewById(R.id.one_rating_id).setBackgroundResource(R.drawable.button_border_orange);
            }
        });
        findViewById(R.id.zero_rating_id).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeRating_Border(findViewById(R.id.zero_rating_id));
                selectedRating = 0;
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