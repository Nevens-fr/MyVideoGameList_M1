package com.example.myvideogamelist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.myvideogamelist.ApiGestion.Database;
import com.example.myvideogamelist.ApiGestion.Game;
import com.example.myvideogamelist.InterfacesAppli.MyActivityImageDiplayable;
import com.example.myvideogamelist.ModifiedAndroidElements.MyLinearLayout;
import com.example.myvideogamelist.ModifiedAndroidElements.MyScrollView4Lists;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;

public class GamesListActivity extends AppCompatActivity implements MyActivityImageDiplayable{

    private final NavigationBar navigationBar = NavigationBar.getNavigationBar();
    private Button selectedButton;
    private final Database database = Database.getDatabase();
    private String listCat = "";
    private ArrayList<Game> games;
    private JSONObject user;
    private final int maxElemFromArray = 4;
    private ArrayList<Button> arraybuttons;
    private ArrayList<String> arrayString = new ArrayList<String>();
    private int currentButtonInd = 0;
    private MyLinearLayout myLinearLayout;
    private MyScrollView4Lists myScrollView4Lists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_games_list);

        user = database.getCurrentUser();

        addNavigationBar();
        navigationBar.init(this);
        connectButton();

        arrayString.add("all");
        arrayString.add("playing");
        arrayString.add("planned");
        arrayString.add("on_hold");
        arrayString.add("finished");
        arrayString.add("abandoned");
/*
        myScrollView4Lists = (MyScrollView4Lists)findViewById(R.id.scrollView_list_id);
        myLinearLayout = (MyLinearLayout)findViewById(R.id.linearLayout_to_insert_clones_list_id);
        myLinearLayout.setActivity(this);*/
    }

    /**
     * On resume, restart all lists to apply user changes
     */
    @Override
    protected void onResume(){
        super.onResume();
        user = database.getCurrentUser();
        selectedButton.setTextColor(ResourcesCompat.getColor(getResources(), R.color.grey, null));
        selectedButton = findViewById(R.id.list_all_button_id);
        selectedButton.setTextColor(ResourcesCompat.getColor(getResources(), R.color.white, null));
        currentButtonInd = 0;
        fillArrayButton();
        connectButton();
    }

    /**
     * Insert navigation bar into the activity
     */
    private void addNavigationBar(){
        LayoutInflater vi = (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        vi.inflate(R.layout.bottom_bar_navigation, findViewById(R.id.game_lists_activity_id), true);
    }

    /**
     * Create the button array for swipe between categories
     */
    private void fillArrayButton(){
        arraybuttons = new ArrayList<Button>();
        arraybuttons.add(findViewById(R.id.list_all_button_id));
        arraybuttons.add(findViewById(R.id.list_playing_button_id));
        arraybuttons.add(findViewById(R.id.list_planned_button_id));
        arraybuttons.add(findViewById(R.id.list_on_hold_button_id));
        arraybuttons.add(findViewById(R.id.list_finished_button_id));
        arraybuttons.add(findViewById(R.id.list_abandoned_button_id));
    }

    /**
     * Create buttons interactions for user search options
     */
    @SuppressLint("ClickableViewAccessibility")
    private void connectButton(){
        selectedButton = findViewById(R.id.list_all_button_id);
        addOnClickListener(findViewById(R.id.list_all_button_id), "all");
        addOnClickListener(findViewById(R.id.list_planned_button_id), "planned");
        addOnClickListener(findViewById(R.id.list_playing_button_id), "playing");
        addOnClickListener(findViewById(R.id.list_on_hold_button_id), "on_hold");
        addOnClickListener(findViewById(R.id.list_abandoned_button_id), "abandoned");
        addOnClickListener(findViewById(R.id.list_finished_button_id), "finished");

        fillArrayButton();
/*
        ((ScrollView)findViewById(R.id.scrollView_list_id)).setOnTouchListener(new View.OnTouchListener(){
            private float x1 = 0, x2 = 0, y1, y2;
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent){
                int MIN_DISTANCE = 150;
                switch(motionEvent.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        x1 = motionEvent.getX();
                        y1 = motionEvent.getY();
                        return false;
                    case MotionEvent.ACTION_UP:
                        x2 = motionEvent.getX();
                        y2 = motionEvent.getY();
                        float deltaX = x2 - x1;
                        float deltaY = y2 - y1;
                        if (Math.abs(deltaY) > MIN_DISTANCE){//swipe vertical
                            return false;
                        }
                        else if (Math.abs(deltaX) > MIN_DISTANCE){
                            if (x2 > x1){// Left to right swipe action
                                System.out.println("left2right swipe");
                                if(currentButtonInd > 0){
                                    arraybuttons.get(currentButtonInd - 1).performClick();
                                    ((HorizontalScrollView)findViewById(R.id.linearLayout1)).requestChildFocus( arraybuttons.get(currentButtonInd),  arraybuttons.get(currentButtonInd));
                                }
                            }
                            else{// Right to left swipe action
                                System.out.println("right2left swipe");
                                if(currentButtonInd < arraybuttons.size() - 1){
                                    arraybuttons.get(currentButtonInd + 1).performClick();
                                    ((HorizontalScrollView)findViewById(R.id.linearLayout1)).requestChildFocus( arraybuttons.get(currentButtonInd),  arraybuttons.get(currentButtonInd));
                                }
                            }
                        }
                        else{
                            // consider as something else - a screen tap for example
                        }
                        break;
                }
                return true;
            }
        });
*/
        listCat = "all";
        if(user != null)
            getGamesData();
        insertData();
    }

    /**
     * Add an on click event listener on a button
     * @param currentButton button to add an event listener on
     * @param cat button's category
     */
    private void addOnClickListener(View currentButton, String cat){
        currentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selectedButton != currentButton){
                    selectedButton.setTextColor(ResourcesCompat.getColor(getResources(), R.color.grey, null));
                    selectedButton = (Button)currentButton;
                    selectedButton.setTextColor(ResourcesCompat.getColor(getResources(), R.color.white, null));
                    listCat = cat;
                    getGamesData();
                    insertData();

                    for(int i = 0; i < arraybuttons.size(); i++){
                        if(arrayString.get(i).compareTo(listCat) == 0)
                            currentButtonInd = i;
                    }
                }
            }
        });
    }

    /**
     * Get games data depending on current category
     */
    private void getGamesData(){
        switch (listCat){
            case "all" : games = database.getAll(); break;
            case "finished" : games = database.getFinished(); break;
            case "planned" : games = database.getPlanned(); break;
            case "on_hold" : games = database.getOn_hold(); break;
            case "playing" : games = database.getPlaying(); break;
            case "abandoned" : games = database.getAbandoned();
        }
    }

    /**
     * start a thread that get rid of precedents cards and create new ones
     */
    private void insertData(){
        //remove actual cards
       ((LinearLayout)findViewById(R.id.linearLayout_to_insert_clones_list_id)).removeAllViews();

       if(user == null){
           LayoutInflater vi = (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
           View v = vi.inflate(R.layout.no_internet_error, findViewById(R.id.linearLayout_to_insert_clones_list_id), true);
       }
       else{

            try{
                boolean found = false;
                for(int i = 0; i < user.getJSONArray("games").length(); i++){
                    String id = user.getJSONArray("games").getJSONObject(i).getString("id");
                    if(createGameCard(id))
                        found = true;
                }
                if(found == false){
                    LayoutInflater vi = (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View v = vi.inflate(R.layout.no_element_found, findViewById(R.id.linearLayout_to_insert_clones_list_id), true);
                }
            }
            catch (Exception e){e.printStackTrace();}
       }
    }

    /**
     * Look for game with the same id then build game card
     * @param id game id
     */
    private boolean createGameCard(String id){
        boolean found = false;

        for(int i = 0; i < games.size() && !found; i++){
            if(id.compareTo(games.get(i).getIdGame()) == 0){
                found = true;
                LayoutInflater vi = (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View v = vi.inflate(R.layout.to_clone_layout, findViewById(R.id.linearLayout_to_insert_clones_list_id), false);
                //Insert data in fields
                //todo change metacritic by user score, idem playtime by user playtime
                ((TextView)v.findViewById(R.id.game_name_to_clone_id)).setText(games.get(i).getName());
                ((TextView)v.findViewById(R.id.releasedDate_to_clone_id)).setText(" "+games.get(i).getReleasedDate());
                ((TextView)v.findViewById(R.id.rating_to_clone_id)).setText(" "+games.get(i).getMetacritic());
                ((TextView)v.findViewById(R.id.playtime_search_id)).setText(returnStringFromStringArray(games.get(i).getDevs()));

                ((TextView)v.findViewById(R.id.genres_search_id)).setText(" " +returnStringFromStringArray(games.get(i).getGenres()));

                //insert image
                ImageView imgV = new ImageView(this);
                Picasso.get().load(games.get(i).getImages()[0]).resize(400,400).centerInside().into(imgV);

                ((LinearLayout)v.findViewById(R.id.search_for_image_id)).addView(imgV,0 );
                ((LinearLayout)v.findViewById(R.id.card_search_to_clone_id)).setGravity(Gravity.CENTER_VERTICAL);
                ((LinearLayout)findViewById(R.id.linearLayout_to_insert_clones_list_id)).addView(v);

                Game g = games.get(i);

                //start game screen activity on click on game card
                ((LinearLayout)v.findViewById(R.id.card_search_to_clone_id)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getApplicationContext(), GameScreenActivity.class);
                        intent.putExtra("gameData", g.getJSONObject().toString());
                        intent.putExtra("comesFrom", "gameList");
                        startActivity(intent);
                    }
                });
            }
        }
        return found;
    }

    /**
     * Return a string made from a string array
     * @param array string array
     * @return a string
     */
    private String returnStringFromStringArray(String[] array){
        String elem = "";
        for(int i = 0; i < array.length && i < maxElemFromArray; i++)
            elem += array[i] + ", ";
        if(maxElemFromArray < array.length)
            elem = elem.substring(0, elem.length() - 2) + "...";
        return elem;
    }

    /**
     * Useless here
     * @param obj none used param
     */
    @Override
    public void getApiInfo(JSONObject obj) {
    }

    public ArrayList<Button> getArraybuttons() {
        return arraybuttons;
    }

    public int getCurrentButtonInd() {
        return currentButtonInd;
    }

    public HorizontalScrollView getHorizontalScrollView() {
        return ((HorizontalScrollView) findViewById(R.id.linearLayout1));
    }
}