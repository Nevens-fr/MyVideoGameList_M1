package com.example.myvideogamelist;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.myvideogamelist.InterfacesAppli.Fragmentable;
import com.example.myvideogamelist.InterfacesAppli.Searchable;
import com.example.myvideogamelist.MyExceptions.EmptySearchException;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.Calendar;

/**
 * View that allow the user to browse games that he doesn't already have in his lists
 */
public class SearchCategory extends Fragment implements Fragmentable, Searchable {

    private static final SearchCategory name = new SearchCategory("");
    private static final SearchCategory devs = new SearchCategory("");
    private static final SearchCategory publishers = new SearchCategory("");
    private static final SearchCategory genre = new SearchCategory("");
    private static final SearchCategory platform = new SearchCategory("");
    private static final SearchCategory release = new SearchCategory("");

    private AppCompatActivity currentActivity;

    private View view;

    private String type, hint, search, id ="";

    private boolean isBuilt = false;

    private long time, space = 1000;

    private int cardsInserted = 0, pageNumber = 1, maxCardByApiCAll = 40, maxElemFromArray = 4;

    /**
     * private constructor for singletons
     * @param s unused
     */
    private SearchCategory(String s){}

    /**
     * Empty constructor for fragment's purpose
     */
    public SearchCategory(){}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        time = Calendar.getInstance().getTimeInMillis();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return view;
    }

    /**
     * Create the view and connect the scroll to it's action
     * @param container where the view will be contained
     */
    public void createView(ViewGroup container){
        LayoutInflater vi = (LayoutInflater)currentActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = vi.inflate(R.layout.fragment_category, container, false);

        //load more games if possible when reaching end of current scroll view
        ((ScrollView)view.findViewById(R.id.scrollView_search_id)).setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View view, int i, int i1, int i2, int i3) {
                if (!((ScrollView)view.findViewById(R.id.scrollView_search_id)).canScrollVertically(1) && cardsInserted > 0 && time + space < Calendar.getInstance().getTimeInMillis()) {
                    time = Calendar.getInstance().getTimeInMillis();
                    pageNumber++;
                    ((GameSearchActivity)currentActivity).setSearchGameAPIData(pageNumber, search, type, id);
                    ((GameSearchActivity)currentActivity).executeRequest();
                }
            }
        });
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
            return null;
        }
    }

    //**********************************************************************************************
    // Getter for singletons
    //**********************************************************************************************

    /**
     * Getter that allow you to get name category fragment
     * @return  SearchCategory name fragment
     */
    public static SearchCategory getName() {
        return name;
    }

    /**
     * Getter that allow you to get devs category fragment
     * @return  SearchCategory devs fragment
     */
    public static SearchCategory getDevs() {
        return devs;
    }

    /**
     * Getter that allow you to get publishers category fragment
     * @return  SearchCategory publishers fragment
     */
    public static SearchCategory getPublishers() {
        return publishers;
    }

    /**
     * Getter that allow you to get genre category fragment
     * @return  SearchCategory genre fragment
     */
    public static SearchCategory getGenre() {
        return genre;
    }

    /**
     * Getter that allow you to get platform category fragment
     * @return  SearchCategory platform fragment
     */
    public static SearchCategory getPlatform() {
        return platform;
    }

    /**
     * Getter that allow you to get release category fragment
     * @return  SearchCategory release fragment
     */
    public static SearchCategory getRelease() {
        return release;
    }

    //**********************************************************************************************
    // Interface's methods
    //**********************************************************************************************

    /**
     * Unused fragmentable method
     */
    @Override
    public void build() {

    }

    /**
     * Setter for parent activity
     * @param fra parent activity
     */
    @Override
    public void setFragmentActivity(AppCompatActivity fra) {
        this.currentActivity = fra;
    }

    /**
     * Setter for the fragment type of category
     * @param type category's type
     */
    @Override
    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String getType() {
        return type;
    }

    /**
     * Clear the view, remove all cards inside
     */
    @Override
    public void clearViewAndAllowBuild() {
        ((LinearLayout)view.findViewById(R.id.linear_layout_to_insert_id)).removeAllViews();
        cardsInserted = 0;
        isBuilt = false;
    }

    /**
     * Clear views and wait for new search to fill it
     */
    @Override
    public void clearViewsAndWaitSearch() {
        ((LinearLayout)view.findViewById(R.id.linear_layout_to_insert_id)).removeAllViews();
        cardsInserted = 0;
        pageNumber = 1;
    }

    /**
     * Getter for page number of the actual search
     * @return page number
     */
    @Override
    public int getPageNumber() {
        return pageNumber;
    }

    /**
     * Setter for page number
     * @param pnb new page number
     */
    @Override
    public void setPageNumber(int pnb){
        this.pageNumber = pnb;
    }

    /**
     * Allow fragment to scroll to top
     */
    @Override
    public void scrollUp() {
        ((ScrollView)view.findViewById(R.id.scrollView_search_id)).fullScroll(ScrollView.FOCUS_UP);//go to top of scroll view
    }

    /**
     * Getter for search area's hint
     * @return String hint
     */
    @Override
    public String getHint() {
        return hint;
    }

    /**
     * Setter for a new hint
     * @param hint new hint
     */
    @Override
    public void setHint(String hint) {
        this.hint = hint;
    }

    /**
     * Display an error message is network isn't working properly
     */
    @Override
    public void networkError(){
        LayoutInflater vi = (LayoutInflater)currentActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        vi.inflate(R.layout.no_internet_error, view.findViewById(R.id.linear_layout_to_insert_id), true);
    }

    /**
     * Display a loading screen
     */
    @Override
    public void setLoading() {
        LayoutInflater vi = (LayoutInflater)currentActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        vi.inflate(R.layout.loading_search, view.findViewById(R.id.linear_layout_to_insert_id), true);
    }

    /**
     * Remove network error message
     */
    @Override
    public void removeError(){
        ((LinearLayout)view.findViewById(R.id.linear_layout_to_insert_id)).removeView(view.findViewById(R.id.no_internet_to_clone_id));
    }

    /**
     * Remove loading message
     */
    @Override
    public void removeLoading(){
        ((LinearLayout)view.findViewById(R.id.linear_layout_to_insert_id)).removeView(view.findViewById(R.id.loading_search_to_clone_id));
    }

    /**
     * Create a game card with display game info
     * @param obj json object containing api data
     * @param actualElement indice of game to display
     */
    @Override
    public void createCards(JSONObject obj, int actualElement){
        JSONObject game =null;

        try {
            LayoutInflater vi = (LayoutInflater)currentActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            if(obj.getJSONArray("results").length() == 0){
                vi.inflate(R.layout.empty_search, view.findViewById(R.id.linear_layout_to_insert_id), true);
                throw new EmptySearchException();
            }
            game = obj.getJSONArray("results").getJSONObject(actualElement);

            View v = vi.inflate(R.layout.to_clone_layout, view.findViewById(R.id.linear_layout_to_insert_id), false);

            //Insert data in fields
            ((TextView)v.findViewById(R.id.game_name_to_clone_id)).setText(game.getString("name"));
            ((TextView)v.findViewById(R.id.releasedDate_to_clone_id)).setText(" "+game.getString("released"));
            ((TextView)v.findViewById(R.id.rating_to_clone_id)).setText(" "+(game.getString("metacritic") == "null" ? "no record":game.getString("metacritic")));
            ((TextView)v.findViewById(R.id.playtime_search_id)).setText(returnStringFromJSONArray("platforms", game));

            ((TextView)v.findViewById(R.id.genres_search_id)).setText(" " +returnStringFromJSONArray("genres", game));

            ((LinearLayout)view.findViewById(R.id.linear_layout_to_insert_id)).addView(v);

            //insert image
            ImageView imgV = new ImageView(currentActivity);
            Picasso.get().load(game.getJSONArray("short_screenshots").getJSONObject(0).getString("image")).resize(400,400).centerInside().into(imgV);

            ((LinearLayout)v.findViewById(R.id.search_for_image_id)).addView(imgV,0 );
            ((LinearLayout)v.findViewById(R.id.card_search_to_clone_id)).setGravity(Gravity.CENTER_VERTICAL);

            final JSONObject gameData = game;

            //start game screen activity on click on game card
            ((LinearLayout)v.findViewById(R.id.card_search_to_clone_id)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(currentActivity.getApplicationContext(), GameScreenActivity.class);
                    intent.putExtra("gameData", gameData.toString());
                    intent.putExtra("comesFrom", "search");
                    startActivity(intent);
                }
            });

            cardsInserted++;

            if(actualElement + 1 < maxCardByApiCAll)//to build next card
                createCards(obj, ++actualElement);

        }
        catch (Exception e) {}
    }

    /**
     * Getter for the text written by the user
     * @return user's text
     */
    @Override
    public String getSearch(){ return search; }

    /**
     * Setter for the text entered by the user
     * @param search new text writtent by the user
     */
    @Override
    public void setSearch(String search){ this.search = search; }

    /**
     * Getter the id for the search
     * @param id dev or publisher or platform id
     */
    @Override
    public void setId(String id){
        this.id = id;
    }

    /**
     * Getter the id for the search
     * @return dev or publisher or platform id
     */
    @Override
    public String getIds(){
        return id;
    }
}
