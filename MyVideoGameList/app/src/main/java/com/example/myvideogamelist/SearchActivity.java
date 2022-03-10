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
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.myvideogamelist.ApiGestion.GamesAPI;
import com.example.myvideogamelist.ApiGestion.SearchGameAPI;
import com.example.myvideogamelist.InterfacesAppli.MyActivityImageDisplayable;
import com.example.myvideogamelist.InterfacesAppli.Scrollable_horizontally;
import com.example.myvideogamelist.ModifiedAndroidElements.MyLinearLayout;
import com.example.myvideogamelist.MyExceptions.EmptySearchException;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity implements MyActivityImageDisplayable, Scrollable_horizontally {

    private NavigationBar navigationBar = NavigationBar.getNavigationBar();
    private MyLinearLayout myLinearLayout;
    private GamesAPI gamesAPI = GamesAPI.getGamesAPI();
    private Button selectedButton;
    private String searchType;
    private int cardsInserted = 0, pageNumber = 1, maxCardByApiCAll = 40, maxElemFromArray = 4;
    private SearchGameAPI searchGameAPI;
    private ArrayList<Button> arraybuttons;
    private final ArrayList<String> arrayString = new ArrayList<String>();
    private int currentButtonInd = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_search);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        addNavigationBar();
        navigationBar.init(this);
        connectButtonSearch();

        selectedButton = findViewById(R.id.search_name_button_id);
        searchType = "games";

        myLinearLayout = (MyLinearLayout)findViewById(R.id.linearLayout_to_insert_clones_search_id);
        myLinearLayout.setActivity(this);
        fillArrayButton();

        MyActivityImageDisplayable currentActivity = this;

        //load more games if possible when reaching end of current scroll view
        ((ScrollView)findViewById(R.id.scrollView_search_id)).setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View view, int i, int i1, int i2, int i3) {
                if (!((ScrollView)findViewById(R.id.scrollView_search_id)).canScrollVertically(1) && cardsInserted > 0) {
                    pageNumber++;
                    searchGameAPI.setPage(String.valueOf(pageNumber));
                    gamesAPI.requestWithParam(searchGameAPI, currentActivity, searchType);
                }
            }
        });
    }

    /**
     * Insert navigation bar into the activity
     */
    private void addNavigationBar(){
        LayoutInflater vi = (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = vi.inflate(R.layout.bottom_bar_navigation, findViewById(R.id.search_activity_id), true);
    }

    /**
     * Create the button array for swipe between categories
     */
    private void fillArrayButton(){
        arraybuttons = new ArrayList<Button>();
        arraybuttons.add(findViewById(R.id.search_name_button_id));
        arraybuttons.add(findViewById(R.id.search_devs_button_id));
        arraybuttons.add(findViewById(R.id.search_publisher_button_id));
        arraybuttons.add(findViewById(R.id.search_genres_button_id));
        arraybuttons.add(findViewById(R.id.search_platforms_button_id));
        arraybuttons.add(findViewById(R.id.search_releasedDate_button_id));
    }

    /**
     * Add a listener on a view
     * @param v the view to add a listener on
     * @param category view's category
     * @param text text to insert as hint
     */
    private void addListener(View v, String category, String text){
        v.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View view) {
                if(selectedButton != v){
                    selectedButton.setTextColor(ResourcesCompat.getColor(getResources(), R.color.grey, null));
                    selectedButton = (Button)v;
                    selectedButton.setTextColor(ResourcesCompat.getColor(getResources(), R.color.white, null));
                    ((EditText)findViewById(R.id.user_search_text_id)).setHint(text);
                    ((EditText)findViewById(R.id.user_search_text_id)).setText("");
                    searchType = category;
                }
            }
        });
    }

    /**
     * Create buttons interactions for user search options
     */
    private void connectButtonSearch(){
        MyActivityImageDisplayable currentActivity = this;

        addListener(findViewById(R.id.search_name_button_id), "games", getString(R.string.searchNameEditText));
        addListener(findViewById(R.id.search_devs_button_id), "developers", getString(R.string.searchDevsEditText));
        addListener(findViewById(R.id.search_publisher_button_id), "publishers", getString(R.string.searchPublisherEditText));
        addListener(findViewById(R.id.search_releasedDate_button_id), "dates", getString(R.string.searchReleasedDateEditText));
        addListener(findViewById(R.id.search_platforms_button_id), "platforms", getString(R.string.searchPlatformsEditText));
        addListener(findViewById(R.id.search_genres_button_id), "genres", getString(R.string.searchGenresEditText));

        //on click on search button to launch an API call
        findViewById(R.id.launch_search_button_id).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchGameAPI = new SearchGameAPI();
                switch (searchType){
                    case "genres" : searchGameAPI.setGenres( ((EditText)findViewById(R.id.user_search_text_id)).getText().toString()); break;
                    case "platforms" : searchGameAPI.setPlatforms( ((EditText)findViewById(R.id.user_search_text_id)).getText().toString()); break;
                    case "dates" : searchGameAPI.setDates( ((EditText)findViewById(R.id.user_search_text_id)).getText().toString());
                                   searchType = "games";
                                   searchGameAPI.setOrdering("released");
                                   break;
                    //same effects for all 3 followings
                    case "publishers" :
                    case "games" :
                    case "developers" :
                        searchGameAPI.setSearch( ((EditText)findViewById(R.id.user_search_text_id)).getText().toString()); break;
                }

                //remove actual cards
                for(; ((LinearLayout)findViewById(R.id.linearLayout_to_insert_clones_search_id)).getChildCount() > 2; )//removing all cards from previous search
                    ((LinearLayout)findViewById(R.id.linearLayout_to_insert_clones_search_id)).removeView(findViewById(R.id.card_search_to_clone_id));
                ((LinearLayout)findViewById(R.id.linearLayout_to_insert_clones_search_id)).removeView(findViewById(R.id.emptys_search_to_clone_id));
                cardsInserted = 0;

                //display loading screen
                findViewById(R.id.loading_search_text_id).setVisibility(View.VISIBLE);
                findViewById(R.id.error_fetch_data_text_search_id).setVisibility(View.GONE);

                //Hide keyboard
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(((EditText)findViewById(R.id.user_search_text_id)).getWindowToken(), 0);

                //prepare request
                searchGameAPI.setPage_size("40");
                pageNumber = 1;
                searchGameAPI.setPage(String.valueOf(pageNumber));
                ((ScrollView)findViewById(R.id.scrollView_search_id)).fullScroll(ScrollView.FOCUS_UP);//go to top of scroll view
                gamesAPI.requestWithParam(searchGameAPI, currentActivity, searchType);
            }
        });
    }

    /**
     * Get the info from the api to render it
     * @param obj json object containing api data
     */
    @Override
    public void getApiInfo(JSONObject obj){

        if(obj == null){
            findViewById(R.id.loading_search_text_id).setVisibility(View.GONE);
            findViewById(R.id.error_fetch_data_text_search_id).setVisibility(View.VISIBLE);
        }
        else{
            if(searchType.compareTo("games") == 0 || searchType.compareTo("dates") == 0){
                findViewById(R.id.loading_search_text_id).setVisibility(View.GONE);
                createCard(obj, 0, maxCardByApiCAll);
            }
            else{
                searchGameAPI = new SearchGameAPI();
                searchGameAPI.setPage_size("40");
                pageNumber = 1;
                searchGameAPI.setPage(String.valueOf(pageNumber));

                try{//get result from previous request to make a new one
                    if(searchType.compareTo("developers") == 0)
                        searchGameAPI.setDevelopers(obj.getJSONArray("results").getJSONObject(0).getString("id"));
                    if(searchType.compareTo("publishers") == 0)
                        searchGameAPI.setPublishers(obj.getJSONArray("results").getJSONObject(0).getString("id"));
                    if(searchType.compareTo("platforms") == 0)
                        searchGameAPI.setPlatforms(obj.getJSONArray("results").getJSONObject(0).getString("id"));
                    if(searchType.compareTo("genres") == 0)
                        searchGameAPI.setGenres(obj.getJSONArray("results").getJSONObject(0).getString("id"));
                }
                catch (Exception e){
                    e.printStackTrace();
                }

                searchType = "games";
                gamesAPI.requestWithParam(searchGameAPI, this, searchType);
            }
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
            LayoutInflater vi = (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            if(obj.getJSONArray("results").length() == 0){
                vi.inflate(R.layout.empty_search, findViewById(R.id.linearLayout_to_insert_clones_search_id), true);
                throw new EmptySearchException();
            }
            game = obj.getJSONArray("results").getJSONObject(actualElem);

            View v = vi.inflate(R.layout.to_clone_layout_my_linear_layout, findViewById(R.id.linearLayout_to_insert_clones_search_id), false);

            //Insert data in fields
            ((TextView)v.findViewById(R.id.game_name_to_clone_id)).setText(game.getString("name"));
            ((TextView)v.findViewById(R.id.releasedDate_to_clone_id)).setText(" "+game.getString("released"));
            ((TextView)v.findViewById(R.id.rating_to_clone_id)).setText(" "+(game.getString("metacritic") == "null" ? "no record":game.getString("metacritic")));
            ((TextView)v.findViewById(R.id.playtime_search_id)).setText(returnStringFromJSONArray("platforms", game));

            ((TextView)v.findViewById(R.id.genres_search_id)).setText(" " +returnStringFromJSONArray("genres", game));

            ((LinearLayout)findViewById(R.id.linearLayout_to_insert_clones_search_id)).addView(v);

            //insert image
            ImageView imgV = new ImageView(this);
            Picasso.get().load(game.getJSONArray("short_screenshots").getJSONObject(0).getString("image")).resize(400,400).centerInside().into(imgV);

            ((MyLinearLayout)v.findViewById(R.id.card_search_to_clone_id)).setActivity(this);
            ((MyLinearLayout)v.findViewById(R.id.card_search_to_clone_id)).setCard(true);

            ((LinearLayout)v.findViewById(R.id.card_search_to_clone_id)).setOnTouchListener(new View.OnTouchListener() {
                @SuppressLint("ClickableViewAccessibility")
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    return false;
                }
            });

            ((LinearLayout)v.findViewById(R.id.search_for_image_id)).addView(imgV,0 );
            ((LinearLayout)v.findViewById(R.id.card_search_to_clone_id)).setGravity(Gravity.CENTER_VERTICAL);

            final JSONObject gameData = game;

            //start game screen activity on click on game card
            ((LinearLayout)v.findViewById(R.id.card_search_to_clone_id)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(), GameScreenActivity.class);
                    intent.putExtra("gameData", gameData.toString());
                    intent.putExtra("comesFrom", "search");
                    startActivity(intent);
                }
            });

            cardsInserted++;

            if(actualElem + 1 < maxElem)//to build next card
                createCard(obj, ++actualElem, maxElem);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Return a string formed from a JSON Array
     * @param key key to access the array
     * @param game JSON object containing the json array
     * @return json array content in string
     */
    private String returnStringFromJSONArray(String key, JSONObject game){
        try {
            String elem = "";
            for (int i = 0; i < maxElemFromArray && i < game.getJSONArray(key).length(); i++) {
                elem += getName(key, game, i).getString("name");
                if (i + 1 < game.getJSONArray(key).length())
                    elem += ", ";
            }
            if(maxElemFromArray < game.getJSONArray(key).length())
                elem = elem.substring(0, elem.length() - 2) + "...";
            return elem;
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Return the json object containing the data inside the array
     * @param key type of key, String
     * @param game json object containing the array
     * @param i indice in array of the desired object
     * @return JSONOBJECT the desired object
     */
    private JSONObject getName(String key, JSONObject game, int i){
        try{
            switch (key){
                case "genres" : return game.getJSONArray(key).getJSONObject(i);
                case "platforms": return game.getJSONArray(key).getJSONObject(i).getJSONObject("platform");
                default: return null;
            }
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public HorizontalScrollView getHorizontalScrollView() {
        return ((HorizontalScrollView) findViewById(R.id.linearLayout1));
    }

    /**
     * An horizontal scroll is received to switch category
     * @param direction 1 to right to left and 0 for left to right swipe
     */
    @Override
    public void scrollReceived(int direction) {
        if(direction == 0){
            //left2right swipe
            if(currentButtonInd > 0){
                arraybuttons.get(currentButtonInd - 1).performClick();
                getHorizontalScrollView().requestChildFocus(arraybuttons.get(currentButtonInd),  arraybuttons.get(currentButtonInd));
            }
        }
        else{
            //right2left swipe
            if(currentButtonInd < arraybuttons.size() - 1){
                arraybuttons.get(currentButtonInd + 1).performClick();
                getHorizontalScrollView().requestChildFocus(arraybuttons.get(currentButtonInd),  arraybuttons.get(currentButtonInd));
            }
        }
    }
}