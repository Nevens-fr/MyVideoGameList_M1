package com.example.myvideogamelist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myvideogamelist.ApiGestion.GamesAPI;
import com.example.myvideogamelist.ApiGestion.NewsAPI;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Class managing the news component opened when clicking on a news from the home page
 */
public class News_activity extends AppCompatActivity {

    private NavigationBar navigationBar = NavigationBar.getNavigationBar();
    private String category;
    private JSONObject news;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        Intent intent = getIntent();
        category = intent.getStringExtra("category");
        news = GamesAPI.getGamesAPI().parseResponse(intent.getStringExtra("news"));

        addNavigationBar();
        navigationBar.init(this);

        buildActivityData();

        //on click on cross button
        findViewById(R.id.button_back_game_id).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    /**
     * Insert navigation bar into the activity
     */
    private void addNavigationBar(){
        LayoutInflater vi = (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = vi.inflate(R.layout.bottom_bar_navigation, findViewById(R.id.news_activity_id), true);
    }

    /**
     * Build activity data from api
     */
    private void buildActivityData(){
        try {
            //specific fields for articles
            if (category.compareTo("articles") == 0){
                ((TextView)findViewById(R.id.categories_id)).setText(computeDatasFromArray("", news.getJSONArray("categories"), "\t\t\t"));
                findViewById(R.id.categories_id).setVisibility(View.VISIBLE);
            }
            else{//specific fields for reviews
                ((TextView)findViewById(R.id.score_news_id)).setText(news.getString("score"));
                findViewById(R.id.score_linear_id).setVisibility(View.VISIBLE);
                buildBadAndGoodPoints(news.getString("good"), news.getString("bad"));
                findViewById(R.id.good_and_bad_points_news_id).setVisibility(View.VISIBLE);
            }

            ((TextView)findViewById(R.id.news_title_id)).setTextSize(15);

            ((TextView)findViewById(R.id.news_title_id)).setText(news.getString("title"));
            ((TextView)findViewById(R.id.publish_date_news_id)).setText(Html.fromHtml("<span>Publish date: </span><span style=\"color:#FA8002;\">" + news.getString("publish_date").substring(0, news.getString("publish_date").indexOf(' '))+"</span>"));
            ((TextView)findViewById(R.id.authors_news_id)).setText(Html.fromHtml("<span>Authors: </span><span style=\"color:#FA8002;\">" + news.getString("authors")+"</span>"));
            ((TextView)findViewById(R.id.lede_news_id)).setText(news.getString("lede"));
            ((TextView)findViewById(R.id.description_news_id)).setText(Html.fromHtml(news.getString("body")));
            ((TextView)findViewById(R.id.description_news_id)).setMovementMethod(LinkMovementMethod.getInstance());

            //insert image
            ImageView imgV = findViewById(R.id.news_image_id);
            Picasso.get().load(news.getJSONObject("image").getString("original")).resize(1000,700).centerInside().into(imgV);
        }
        catch (Exception e){
        }
    }

    /**
     * Insert good and bad points inside text view
     * @param gpts string of good points
     * @param bpts string of bad points
     */
    private void buildBadAndGoodPoints(String gpts, String bpts){
        ((TextView)findViewById(R.id.good_pts_news_id)).setText(Html.fromHtml(buildPoints(gpts)));
        ((TextView)findViewById(R.id.bad_pts_news_id)).setText(Html.fromHtml(buildPoints(bpts)));
    }

    /**
     * Build a list of good or bad points from a string
     * @param pts source string
     * @return formatted list in html
     */
    private String buildPoints(String pts){
        String res = "<ul>";

        while(pts.indexOf(('|')) != -1){
            res += "<li> " + pts.substring(0,pts.indexOf('|')) + "</li>";
            pts = pts.substring(pts.indexOf('|') + 1);
        }
        res += "<li>" + pts.substring(0) + "</li></ul>";
        return res;
    }

    /**
     * Return a string containing all data from a json array
     * @param category category name
     * @param array json array
     * @param elemToAdd element to add to the string
     * @return string containing data
     */
    private String computeDatasFromArray(String category, JSONArray array, String elemToAdd){
        try{
            String elem =category;
            for(int i = 0; i < array.length(); i++){
                elem += array.getJSONObject(i).getString("name") + elemToAdd;
                if(i+1 < array.length() && elemToAdd.isEmpty())
                    elem += ", ";
                else if(i+1 < array.length())
                    elem += "-"+elemToAdd;
            }
            return elem;
        }
        catch (Exception e){
            return null;
        }
    }
}