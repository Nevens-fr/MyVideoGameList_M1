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

/**
 * Class managing the research component
 */
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
        }
    }

}