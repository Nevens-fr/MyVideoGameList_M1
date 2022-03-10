package com.example.myvideogamelist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.myvideogamelist.ApiGestion.Database;
import com.example.myvideogamelist.ApiGestion.NewsAPI;
import com.example.myvideogamelist.InterfacesAppli.MyActivityImageDisplayable;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

public class Home extends AppCompatActivity implements MyActivityImageDisplayable {

    private final NavigationBar navigationBar = NavigationBar.getNavigationBar();
    private int articleInd = 0, reviewInd = 0;
    private final int limitToLoad = 20;
    private final int limitPerRequest = 100;
    private int nextArticle = 101, nextReviews = 101, nbDisplayed = 0;
    private JSONObject articles, reviews;
    private JSONObject articlesNew = null, reviewsNew = null;
    private final NewsAPI newsAPI = NewsAPI.getNewsAPI();
    private boolean articleRequest = true, reviewRequest =true, launch = true;
    private Handler mainHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        addNavigationBar();
        navigationBar.init(this);

        fillStatsUser();

        newsAPI.setCurrentActivity(this);

        //display user name on home
        try{
            ((TextView)findViewById(R.id.textView7)).setText(getString(R.string.homeWelcome) + " " + Database.getDatabase().getCurrentUser().getString("name"));
        }
        catch(Exception e){
            e.printStackTrace();
        }

        articles = newsAPI.getArticles();
        reviews = newsAPI.getReviews();

        if(articles != null && reviews != null)
            launchCreation();
        else
            ((LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.loading_news, findViewById(R.id.home_to_clone_id), true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        newsAPI.setCurrentActivity(this);
        fillStatsUser();
    }

    /**
     * Fill user stats from database
     */
    private void fillStatsUser(){
        ((TextView)findViewById(R.id.textView10)).setText(String.valueOf(Database.getDatabase().allGamesNumber()));
        ((TextView)findViewById(R.id.textView11)).setText(String.valueOf(Database.getDatabase().finishedGamesNumber()));
        Home tmp = this;
        new Thread(new Runnable() {
            @Override
            public void run() {
                String time = Database.getDatabase().totalPlayTime();
                tmp.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ((TextView)findViewById(R.id.textView13)).setText(time);
                    }
                });
            }
        }).start();
    }

    /**
     * Insert navigation bar into the activity
     */
    private void addNavigationBar(){
        LayoutInflater vi = (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        vi.inflate(R.layout.bottom_bar_navigation, findViewById(R.id.home_activity_id), true);

        //load more games if possible when reaching end of current scroll view
        ((ScrollView)findViewById(R.id.news_scroll_id)).setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View view, int i, int i1, int i2, int i3) {
                if (!((ScrollView)findViewById(R.id.news_scroll_id)).canScrollVertically(1)) {
                    if((articles != null && reviews != null) || (newsAPI.getReviews() == null && newsAPI.getArticles() == null))
                        launchCreation();
                    else{
                        newsAPI.requestWithParam("articles", "");
                        newsAPI.requestWithParam("reviews", "");
                    }
                }
                if(articleInd + limitToLoad >= limitPerRequest){
                    if(!articleRequest && articlesNew == null){
                        newsAPI.requestWithParam("articles", "&offset="+nextArticle);
                        nextArticle+= limitPerRequest;
                        articleRequest = true;
                    }
                }
                else if(reviewInd + limitToLoad >= limitPerRequest){
                    if(!reviewRequest && reviewsNew == null){
                        newsAPI.requestWithParam("reviews", "&offset="+nextReviews);
                        nextReviews+= limitPerRequest;
                        reviewRequest = true;
                    }
                }
            }
        });

        Home tmp = this;

        findViewById(R.id.button_sign_out).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteFile("config.txt");//disable auto connect
                newsAPI.setCurrentActivity(null);
                Intent intent = new Intent(tmp,LoadingActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                tmp.startActivity(intent);
                //finish();
            }
        });
    }

    /**
     * Start creating cards
     */
    private void launchCreation(){
        while(nbDisplayed < limitToLoad){
            try{
                if(articleInd == limitPerRequest){
                    if(articlesNew !=  null){//previous request all displayed, getting the new one
                        articles = articlesNew;
                        articlesNew = null;
                        articleInd = 0;
                        launchCreation();
                    }
                    else {
                        makeCard(reviews.getJSONArray("results").getJSONObject(reviewInd), "reviews");
                        reviewInd++;
                        nbDisplayed++;
                    }
                }
                else if(reviewInd == limitPerRequest){
                    if(reviewsNew !=  null){
                        reviews = reviewsNew;
                        reviewsNew = null;
                        reviewInd = 0;
                        launchCreation();
                    }
                    else {
                        makeCard(articles.getJSONArray("results").getJSONObject(articleInd), "articles");
                        articleInd++;
                        nbDisplayed++;
                    }
                }
                else{
                    String d1 = articles.getJSONArray("results").getJSONObject(articleInd).getString("publish_date");
                    d1 = d1.substring(0, d1.indexOf(' '));
                    String d2 = reviews.getJSONArray("results").getJSONObject(reviewInd).getString("publish_date");
                    d2 = d2.substring(0, d2.indexOf(' '));
                    compareArticleAndReview(d1, d2);
                    nbDisplayed++;
                }
            }
            catch (Exception e){
                e.printStackTrace();
                System.exit(0);
            }
        }
        nbDisplayed = 0;
    }

    /**
     * Compare two dates from two differents types of news
     * @param articleDate article publish date
     * @param reviewDate review publish date
     */
    private void compareArticleAndReview(String articleDate, String reviewDate){
        try {
            int result = compareDate(articleDate, reviewDate);

            if(result > 0){
                //d2 more recent
                makeCard(reviews.getJSONArray("results").getJSONObject(reviewInd), "reviews");
                reviewInd++;
            }
            else if(result < 0){
                //d1 more recent
                makeCard(articles.getJSONArray("results").getJSONObject(articleInd), "articles");
                articleInd++;
            }
            else{
                //same date
                makeCard(reviews.getJSONArray("results").getJSONObject(reviewInd), "reviews");
                reviewInd++;
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }


    /**
     * Create a card and display it
     * @param news the news object
     * @param category whether it is an article or an review
     */
    private void makeCard(JSONObject news, String category){
        LayoutInflater vi = (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = vi.inflate(R.layout.card_news_to_clone, findViewById(R.id.home_to_clone_id), false);

        try{
            //Insert data in fields
            ((TextView)v.findViewById(R.id.new_title_to_clone_id)).setText(news.getString("title"));
            ((TextView)v.findViewById(R.id.type_to_clone_id)).setText(category.toUpperCase().substring(0, category.length() - 1));
            ((TextView)v.findViewById(R.id.releasedDate_to_clone_id)).setText(' ' + news.getString("publish_date").substring(0, news.getString("publish_date").indexOf(' ')));
            ((TextView)v.findViewById(R.id.desc_news_card_id)).setText(news.getString("deck"));

            ((LinearLayout)findViewById(R.id.home_to_clone_id)).addView(v);

            //insert image
            ImageView imgV = new ImageView(this);
            Picasso.get().load(news.getJSONObject("image").getString("square_tiny")).resize(400,400).centerInside().into(imgV);

            ((LinearLayout)v.findViewById(R.id.news_for_image_id)).addView(imgV,0 );
            ((LinearLayout)v.findViewById(R.id.card_news_to_clone_id)).setGravity(Gravity.CENTER_VERTICAL);

            //start news activity on click on game card
            ((LinearLayout)v.findViewById(R.id.card_news_to_clone_id)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    newsAPI.setCurrentActivity(null);
                    Intent intent = new Intent(getApplicationContext(), News_activity.class);
                    intent.putExtra("category", category);
                    intent.putExtra("news", news.toString());
                    startActivity(intent);
                }
            });
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Compare two dates
     * @param d1 article's publish date
     * @param d2 review's publish date
     * @return 1 if d2 is much recent, -1 if d1 is more recent, 0 if equals
     * @throws Exception from parse integer
     */
    private int compareDate(String d1, String d2) throws Exception{
        int y1 = Integer.parseInt(d1.substring(0, d1.indexOf('-')));
        int y2 = Integer.parseInt(d2.substring(0, d1.indexOf('-')));
        int m1 = Integer.parseInt(d1.substring(d1.indexOf('-') + 1, d1.indexOf('-') + 3));
        int m2 = Integer.parseInt(d2.substring(d1.indexOf('-') + 1, d1.indexOf('-') + 3));
        int da1 = Integer.parseInt(d1.substring(d1.length() - 2));
        int da2 = Integer.parseInt(d2.substring(d2.length() - 2));

        if(y1 > y2)
            return -1;
        else if(y1 < y2)
            return 1;
        else{
            if(m1 > m2)
                return -1;
            else if(m1 < m2)
                return 1;
            else{
                if(da1 > da2)
                    return -1;
                else if(da1 < da2)
                    return 1;
            }
        }
        return 0;
    }

    /**
     * Get data from api
     * @param obj json object response from api
     */
    @Override
    public void getApiInfo(JSONObject obj) {
        if(launch == true){
            mainHandler.post(new Runnable() {
                @Override
                public void run() {
                    if(articleRequest){
                        articleRequest = false;
                        articles = newsAPI.getArticles();
                    }
                    else if (reviewRequest){
                        reviewRequest = false;
                        reviews = newsAPI.getReviews();
                        launch = false;
                        ((LinearLayout)findViewById(R.id.home_to_clone_id)).removeView(findViewById(R.id.no_internet_to_clone_id));
                        launchCreation();
                    }
                }
            });
        }
        else{
            if(articleRequest){
                articleRequest = false;
                articlesNew = newsAPI.getArticles();
            }
            else if (reviewRequest){
                reviewRequest = false;
                reviewsNew = newsAPI.getReviews();
            }
            ((LinearLayout)findViewById(R.id.home_to_clone_id)).removeView(findViewById(R.id.no_internet_to_clone_id));
        }
    }
}