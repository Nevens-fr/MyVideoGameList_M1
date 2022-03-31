package com.example.myvideogamelist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.example.myvideogamelist.ApiGestion.Database;
import com.example.myvideogamelist.ApiGestion.Game;
import com.example.myvideogamelist.InterfacesAppli.ObservatorAppli;

import java.util.ArrayList;
import java.util.Random;

/**
 * Class managing the Randomatic component of the app
 */
public class RandomaticActivity extends AppCompatActivity implements ObservatorAppli {

    private NavigationBar navigationBar = NavigationBar.getNavigationBar();
    private Database database = Database.getDatabase();
    private String dataPlanned = "", dateFinished = "";
    private boolean hasFinished = true, hasPlanned = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_randomatic);

        addNavigationBar();
        navigationBar.init(this);

        connectButtons();
        createThreads();

        database.subscribe(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        createThreads();
    }

    /**
     * Insert navigation bar into the activity
     */
    private void addNavigationBar(){
        LayoutInflater vi = (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = vi.inflate(R.layout.bottom_bar_navigation, findViewById(R.id.randomatic_activity_id), true);
    }

    /**
     * Connects all the activity buttons to their dedicated action
     */
    private void connectButtons(){
        addActionToButton(findViewById(R.id.button1), "finished");
        addActionToButton(findViewById(R.id.button2), "planned");
    }

    /**
     * Add the on click listener on buttons
     * @param view view to add an onclick listener on
     * @param searchType type of list the user want to browse
     */
    public void addActionToButton(View view, String searchType){
        RandomaticActivity randomaticActivity = this;

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(searchType.compareTo("finished") == 0 && hasFinished){
                    Intent intent = new Intent(randomaticActivity, GameScreenActivity.class);
                    intent.putExtra("gameData", dateFinished);
                    intent.putExtra("comesFrom", "randomatic");
                    startActivity(intent);
                }
                else if(searchType.compareTo("planned") == 0 && hasPlanned){
                    Intent intent = new Intent(randomaticActivity, GameScreenActivity.class);
                    intent.putExtra("gameData", dataPlanned);
                    intent.putExtra("comesFrom", "randomatic");
                    startActivity(intent);
                }
                else{
                    Toast.makeText(randomaticActivity.getApplicationContext(), getString(R.string.no_game_in_list), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    /**
     * Create thread to pick random games from user lists
     */
    private void createThreads(){
        Thread finished = new Thread(new Runnable() {
            @Override
            public void run() {
                chooseAFinishedGame();
            }
        });

        Thread planned = new Thread(new Runnable() {
            @Override
            public void run() {
                chooseAPlannedGame();
            }
        });

        finished.start();
        planned.start();
    }

    /**
     * Choose a game in the user list of finished games
     */
    private void chooseAFinishedGame(){
        ArrayList<Game> games = database.getFinished();

        if(games.size() == 0){
            hasFinished = false;
        }
        else{
            Random random = new Random();
            int index = random.nextInt(games.size());
            dateFinished = games.get(index).getJSONObject().toString();
        }
    }

    /**
     * Choose a game in the user list of planned games
     */
    private void chooseAPlannedGame(){
        ArrayList<Game> games = database.getPlanned();

        if(games.size() == 0 ||games == null){
            hasPlanned = false;
        }
        else{
            Random random = new Random();
            int index = random.nextInt(games.size());
            dataPlanned = games.get(index).getJSONObject().toString();
        }
    }

    /**
     * Received a notification from observed database
     * @param status api's return code
     */
    @Override
    public void notified(int status) {
        if(status != 200)
            Toast.makeText(getApplicationContext(), getString(R.string.internet_error), Toast.LENGTH_LONG).show();
    }
}