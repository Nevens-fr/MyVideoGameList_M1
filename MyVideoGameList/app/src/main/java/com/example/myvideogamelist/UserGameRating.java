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

    private Button statusButtons[] = new Button[5];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_game_rating);

        navigationBar.init(this);

        statusButtons[0] = findViewById(R.id.abandonned_button_id);
        statusButtons[1] = findViewById(R.id.finished_button_id);
        statusButtons[2] = findViewById(R.id.planned_button_id);
        statusButtons[3] = findViewById(R.id.on_hold_button_id);
        statusButtons[4] = findViewById(R.id.playing_button_id);

        connectRatingButtons();
        connectTopToolbarButtons();
        connectStatusButtons();
    }

    /**
     * Create buttons interactions for status buttons
     */
    private void connectStatusButtons(){
        findViewById(R.id.abandonned_button_id).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setWhiteStrokeStatusButtons();
                selectedStatus = 0;
                findViewById(R.id.abandonned_button_id).setBackgroundResource(R.drawable.button_border_orange);
            }
        });
        findViewById(R.id.finished_button_id).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setWhiteStrokeStatusButtons();
                selectedStatus = 1;
                findViewById(R.id.finished_button_id).setBackgroundResource(R.drawable.button_border_orange);
            }
        });
        findViewById(R.id.planned_button_id).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setWhiteStrokeStatusButtons();
                selectedStatus = 2;
                findViewById(R.id.planned_button_id).setBackgroundResource(R.drawable.button_border_orange);
            }
        });
        findViewById(R.id.on_hold_button_id).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setWhiteStrokeStatusButtons();
                selectedStatus = 3;
                findViewById(R.id.on_hold_button_id).setBackgroundResource(R.drawable.button_border_orange);
            }
        });
        findViewById(R.id.playing_button_id).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setWhiteStrokeStatusButtons();
                selectedStatus = 4;
                findViewById(R.id.playing_button_id).setBackgroundResource(R.drawable.button_border_orange);
            }
        });
    }

    /**
     * Create buttons interactions for top toolbar buttons
     */
    private void connectTopToolbarButtons(){
        findViewById(R.id.button_back_game_id).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        findViewById(R.id.button_game_rating_cancel_id).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO get rid of data from user
                finish();
            }
        });

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
                System.out.println(selectedHours);
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
                removeRating_Border();
                selectedRating = -1;
                findViewById(R.id.empty_rating_id).setBackgroundResource(R.drawable.button_border_orange);
            }
        });
        findViewById(R.id.ten_rating_id).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeRating_Border();
                selectedRating = 10;
                findViewById(R.id.ten_rating_id).setBackgroundResource(R.drawable.button_border_orange);
            }
        });
        findViewById(R.id.nine_rating_id).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeRating_Border();
                selectedRating = 9;
                findViewById(R.id.nine_rating_id).setBackgroundResource(R.drawable.button_border_orange);
            }
        });
        findViewById(R.id.height_rating_id).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeRating_Border();
                selectedRating = 8;
                findViewById(R.id.height_rating_id).setBackgroundResource(R.drawable.button_border_orange);
            }
        });
        findViewById(R.id.seven_rating_id).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeRating_Border();
                selectedRating = 7;
                findViewById(R.id.seven_rating_id).setBackgroundResource(R.drawable.button_border_orange);
            }
        });
        findViewById(R.id.six_rating_id).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeRating_Border();
                selectedRating = 6;
                findViewById(R.id.six_rating_id).setBackgroundResource(R.drawable.button_border_orange);
            }
        });
        findViewById(R.id.five_rating_id).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeRating_Border();
                selectedRating = 5;
                findViewById(R.id.five_rating_id).setBackgroundResource(R.drawable.button_border_orange);
            }
        });
        findViewById(R.id.four_rating_id).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeRating_Border();
                selectedRating = 4;
                findViewById(R.id.four_rating_id).setBackgroundResource(R.drawable.button_border_orange);
            }
        });
        findViewById(R.id.three_rating_id).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeRating_Border();
                selectedRating = 3;
                findViewById(R.id.three_rating_id).setBackgroundResource(R.drawable.button_border_orange);
            }
        });
        findViewById(R.id.two_rating_id).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeRating_Border();
                selectedRating = 2;
                findViewById(R.id.two_rating_id).setBackgroundResource(R.drawable.button_border_orange);
            }
        });
        findViewById(R.id.one_rating_id).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeRating_Border();
                selectedRating = 1;
                findViewById(R.id.one_rating_id).setBackgroundResource(R.drawable.button_border_orange);
            }
        });
        findViewById(R.id.zero_rating_id).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeRating_Border();
                selectedRating = 0;
                findViewById(R.id.zero_rating_id).setBackgroundResource(R.drawable.button_border_orange);
            }
        });
    }

    /**
     * Put all ratings unselected
     */
    private void removeRating_Border(){
        findViewById(R.id.empty_rating_id).setBackgroundResource(R.drawable.button_black_border);
        findViewById(R.id.ten_rating_id).setBackgroundResource(R.drawable.button_black_border);
        findViewById(R.id.nine_rating_id).setBackgroundResource(R.drawable.button_black_border);
        findViewById(R.id.height_rating_id).setBackgroundResource(R.drawable.button_black_border);
        findViewById(R.id.seven_rating_id).setBackgroundResource(R.drawable.button_black_border);
        findViewById(R.id.six_rating_id).setBackgroundResource(R.drawable.button_black_border);
        findViewById(R.id.five_rating_id).setBackgroundResource(R.drawable.button_black_border);
        findViewById(R.id.four_rating_id).setBackgroundResource(R.drawable.button_black_border);
        findViewById(R.id.three_rating_id).setBackgroundResource(R.drawable.button_black_border);
        findViewById(R.id.two_rating_id).setBackgroundResource(R.drawable.button_black_border);
        findViewById(R.id.one_rating_id).setBackgroundResource(R.drawable.button_black_border);
        findViewById(R.id.zero_rating_id).setBackgroundResource(R.drawable.button_black_border);
    }


    /**
     * Put all status with a white border
     */
    private void setWhiteStrokeStatusButtons(){
        for(Button b : statusButtons){
            b.setBackgroundResource(R.drawable.button_border_white);
        }
    }
}