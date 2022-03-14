package com.example.myvideogamelist;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.myvideogamelist.ApiGestion.Database;
import com.example.myvideogamelist.ApiGestion.Game;
import com.example.myvideogamelist.InterfacesAppli.Fragmentable;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Category#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Category extends Fragment implements Fragmentable {

    private AppCompatActivity currentActivity = null;

    private static Category catAll = new Category("");
    private static Category catPlanned = new Category("");
    private static Category catFinished = new Category("");
    private static Category catOnHold = new Category("");
    private static Category catAbandoned = new Category("");
    private static Category catPlaying = new Category("");

    private View view;

    private String category = "";

    private boolean isBuilt = false;

    private ArrayList<Game> games;
    private JSONObject user;
    private final int maxElemFromArray = 4;

    private final Database database = Database.getDatabase();


    //***********************************************************************************************
    // Constructors area
    //***********************************************************************************************

    private Category(String useless){}

    public Category() {
        // Required empty public constructor
    }

    //***********************************************************************************************
    // Fragment specific code area
    //***********************************************************************************************

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment Category.
     */
    public static Category newInstance() {
        Category fragment = new Category();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return view;
    }

    /**
     * Create the view
     * @param container where the view will be contained
     */
    public void createView(ViewGroup container){
        LayoutInflater vi = (LayoutInflater)currentActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = vi.inflate(R.layout.fragment_category, container, false);
        build();
    }

    /**
     * Start card building if not already done
     */
    @Override
    public void build(){
        if(!isBuilt && database.getCurrentUser() != null){
            isBuilt = true;

            user = database.getCurrentUser();

            switch (category){
                case "all" : games = database.getAll(); break;
                case "finished" : games = database.getFinished(); break;
                case "planned" : games = database.getPlanned(); break;
                case "on_hold" : games = database.getOn_hold(); break;
                case "playing" : games = database.getPlaying(); break;
                case "abandoned" : games = database.getAbandoned();
            }

            insertData();
        }
    }

    /**
     * start a thread that get rid of precedents cards and create new ones
     */
    private void insertData(){
        //remove actual cards
        ((LinearLayout)view.findViewById(R.id.linear_layout_to_insert_id)).removeAllViews();

        if(user == null){
            LayoutInflater vi = (LayoutInflater)currentActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            vi.inflate(R.layout.no_internet_error, view.findViewById(R.id.linear_layout_to_insert_id), true);
        }
        else{

            try{
                boolean found = false;

                for(int i = 0; i < user.getJSONArray("games").length(); i++){
                    String id = user.getJSONArray("games").getJSONObject(i).getString("id");
                    String score = user.getJSONArray("games").getJSONObject(i).getString("score");
                    String playtime = user.getJSONArray("games").getJSONObject(i).getString("hours");
                    if(createGameCard(id, score, playtime, i)){
                        found = true;
                    }
                }
                if(!found){
                    LayoutInflater vi = (LayoutInflater)currentActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    vi.inflate(R.layout.no_element_found, view.findViewById(R.id.linear_layout_to_insert_id), true);
                }
            }
            catch (Exception e){e.printStackTrace();}
        }
    }

    /**
     * Look for game with the same id then build game card
     * @param id game id
     * @param score user score for the game
     * @param playtime user playtime on the game
     * @param userInd indice of game in user array
     * @return boolean, if the card has been created
     */
    @SuppressLint("ClickableViewAccessibility")
    private boolean createGameCard(String id, String score, String playtime, int userInd){
        boolean found = false;

        for(int i = 0; i < games.size() && !found; i++){
            if(id.compareTo(games.get(i).getIdGame()) == 0){
                found = true;
                LayoutInflater vi = (LayoutInflater)currentActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View v = vi.inflate(R.layout.to_clone_layout, view.findViewById(R.id.linear_layout_to_insert_id), false);
                //Insert data in fields
                ((TextView)v.findViewById(R.id.game_name_to_clone_id)).setText(games.get(i).getName());
                ((TextView)v.findViewById(R.id.releasedDate_to_clone_id)).setText(" "+games.get(i).getReleasedDate());
                ((TextView)v.findViewById(R.id.rating_to_clone_id)).setText(" "+score + "/10");
                ((TextView)v.findViewById(R.id.playtime_search_id)).setText(" "+playtime +" h");
                ((TextView)v.findViewById(R.id.playtime_search_id)).setTextColor(ContextCompat.getColor(currentActivity.getApplicationContext(), R.color.white));
                ((TextView)v.findViewById(R.id.metacritic_list_id)).setText("Your score:");
                ((TextView)v.findViewById(R.id.playtime_list_id)).setVisibility(View.VISIBLE);
                ((TextView)v.findViewById(R.id.genres_search_id)).setText(" " +returnStringFromStringArray(games.get(i).getGenres()));

                //insert image
                ImageView imgV = new ImageView(currentActivity);
                Picasso.get().load(games.get(i).getImages()[0]).resize(400,400).centerInside().into(imgV);

                ((LinearLayout)v.findViewById(R.id.search_for_image_id)).addView(imgV,0 );
                ((LinearLayout)v.findViewById(R.id.card_search_to_clone_id)).setGravity(Gravity.CENTER_VERTICAL);
                ((LinearLayout)view.findViewById(R.id.linear_layout_to_insert_id)).addView(v);

                Game g = games.get(i);

                //start game screen activity on click on game card
                ((LinearLayout)v.findViewById(R.id.card_search_to_clone_id)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(currentActivity.getApplicationContext(), GameScreenActivity.class);
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

    //***********************************************************************************************
    // Getter and setter area
    //***********************************************************************************************

    @Override
    public void setFragmentActivity(AppCompatActivity fra) {
        if(currentActivity == null)
            currentActivity = fra;
    }

    public static Category getCatAll() {
        return catAll;
    }

    public static Category getCatPlanned() {
        return catPlanned;
    }

    public static Category getCatFinished() {
        return catFinished;
    }

    public static Category getCatOnHold() {
        return catOnHold;
    }

    public static Category getCatAbandoned() {
        return catAbandoned;
    }

    public static Category getCatPlaying() {
        return catPlaying;
    }

    @Override
    public void setType(String category) {
        this.category = category;
    }

    @Override
    public String getType() {
        return category;
    }

    public boolean isBuilt() {
        return isBuilt;
    }

    public void setBuilt(boolean built) {
        isBuilt = built;
        view = null;
        currentActivity = null;
    }

    public void clearViewAndAllowBuild(){
        if(isBuilt)
            ((LinearLayout)view.findViewById(R.id.linear_layout_to_insert_id)).removeAllViews();
        isBuilt = false;
    }
}