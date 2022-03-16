package com.example.myvideogamelist;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.myvideogamelist.ApiGestion.GamesAPI;
import com.example.myvideogamelist.ApiGestion.SearchGameAPI;
import com.example.myvideogamelist.InterfacesAppli.Fragmentable;
import com.example.myvideogamelist.InterfacesAppli.MyActivityImageDisplayable;
import com.example.myvideogamelist.InterfacesAppli.Searchable;

import org.json.JSONObject;

public class GameSearchActivity extends AppCompatActivity implements MyActivityImageDisplayable{

    private static final int NUM_PAGES = 6;
    private ViewPager2 mPager;
    private FragmentStateAdapter pagerAdapter;
    private GameSearchActivity gameSearchActivity = this;
    private final NavigationBar navigationBar = NavigationBar.getNavigationBar();
    private GamesAPI gamesAPI = GamesAPI.getGamesAPI();
    private Button selectedButton;
    private String searchType;
    private SearchGameAPI searchGameAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_search);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        mPager = (ViewPager2) findViewById(R.id.insert_fragments_id);
        pagerAdapter = new GameSearchActivity.GameListPagerAdapter(this);
        mPager.setAdapter(pagerAdapter);

        addNavigationBar();
        navigationBar.init(this);

        selectedButton = findViewById(R.id.search_name_button_id);
        searchType = "games";

        connectButtonSearch();
        createFragments();

        //Allow scroll for category to switch when the user swipe
        mPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                switch(mPager.getCurrentItem()){
                    case 0:changeSelectedButton(findViewById(R.id.search_name_button_id), SearchCategory.getName().getType(), SearchCategory.getName().getHint()); break;
                    case 1:changeSelectedButton(findViewById(R.id.search_devs_button_id), SearchCategory.getDevs().getType(), SearchCategory.getDevs().getHint()); break;
                    case 2:changeSelectedButton(findViewById(R.id.search_publisher_button_id), SearchCategory.getPublishers().getType(), SearchCategory.getPublishers().getHint());break;
                    case 3:changeSelectedButton(findViewById(R.id.search_genres_button_id), SearchCategory.getGenre().getType(), SearchCategory.getGenre().getHint());break;
                    case 4:changeSelectedButton(findViewById(R.id.search_platforms_button_id), SearchCategory.getPlatform().getType(), SearchCategory.getPlatform().getHint());break;
                    case 5:changeSelectedButton(findViewById(R.id.search_releasedDate_button_id), SearchCategory.getRelease().getType(), SearchCategory.getRelease().getHint());
                }
            }
        });
    }

    /**
     * Create data in fragments
     */
    private void createFragments(){
        SearchCategory.getName().setType("games");
        SearchCategory.getName().setHint(getString(R.string.searchNameEditText));
        SearchCategory.getName().setFragmentActivity(this);
        SearchCategory.getName().createView(mPager);

        SearchCategory.getDevs().setType("developers");
        SearchCategory.getDevs().setHint(getString(R.string.searchDevsEditText));
        SearchCategory.getDevs().setFragmentActivity(this);
        SearchCategory.getDevs().createView(mPager);

        SearchCategory.getPublishers().setType("publishers");
        SearchCategory.getPublishers().setHint(getString(R.string.searchPublisherEditText));
        SearchCategory.getPublishers().setFragmentActivity(this);
        SearchCategory.getPublishers().createView(mPager);

        SearchCategory.getGenre().setType("genres");
        SearchCategory.getGenre().setHint(getString(R.string.searchGenresEditText));
        SearchCategory.getGenre().setFragmentActivity(this);
        SearchCategory.getGenre().createView(mPager);

        SearchCategory.getPlatform().setType("platforms");
        SearchCategory.getPlatform().setHint(getString(R.string.searchPlatformsEditText));
        SearchCategory.getPlatform().setFragmentActivity(this);
        SearchCategory.getPlatform().createView(mPager);

        SearchCategory.getRelease().setType("dates");
        SearchCategory.getRelease().setHint(getString(R.string.searchReleasedDateEditText));
        SearchCategory.getRelease().setFragmentActivity(this);
        SearchCategory.getRelease().createView(mPager);
    }

    /**
     * A simple pager adapter
     */
    private class GameListPagerAdapter extends FragmentStateAdapter {
        public GameListPagerAdapter(FragmentActivity fm) {
            super(fm);
        }

        @Override
        public Fragment createFragment(int position) {
            Fragmentable c = null;
            switch (position){
                case 0 : c = SearchCategory.getName(); break;
                case 1 : c = SearchCategory.getDevs(); break;
                case 2 : c = SearchCategory.getPublishers(); break;
                case 3 : c = SearchCategory.getGenre(); break;
                case 4 : c = SearchCategory.getPlatform(); break;
                case 5 : c = SearchCategory.getRelease(); break;
            }
            return (Fragment) c;
        }

        @Override
        public int getItemCount() {
            return NUM_PAGES;
        }
    }

    /**
     * Insert navigation bar into the activity
     */
    private void addNavigationBar(){
        LayoutInflater vi = (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        vi.inflate(R.layout.bottom_bar_navigation, findViewById(R.id.search_activity_id), true);
    }

    /**
     * Change selected button
     * @param v view to scroll to
     * @param category view's category
     * @param text text to insert as hint
     */
    private void changeSelectedButton(View v, String category, String text){
        selectedButton.setTextColor(ResourcesCompat.getColor(getResources(), R.color.grey, null));
        selectedButton = (Button) v;
        selectedButton.setTextColor(ResourcesCompat.getColor(getResources(), R.color.white, null));
        ((EditText)findViewById(R.id.user_search_text_id)).setHint(text);
        ((EditText)findViewById(R.id.user_search_text_id)).setText("");
        searchType = category;
        ((HorizontalScrollView) findViewById(R.id.linearLayout1)).requestChildFocus(selectedButton, selectedButton);
    }

    /**
     * Create buttons interactions for user search options
     */
    private void connectButtonSearch(){
        MyActivityImageDisplayable currentActivity = this;

        addListener(findViewById(R.id.search_name_button_id), "games", getString(R.string.searchNameEditText),0);
        addListener(findViewById(R.id.search_devs_button_id), "developers", getString(R.string.searchDevsEditText),1);
        addListener(findViewById(R.id.search_publisher_button_id), "publishers", getString(R.string.searchPublisherEditText),2);
        addListener(findViewById(R.id.search_releasedDate_button_id), "dates", getString(R.string.searchReleasedDateEditText),5);
        addListener(findViewById(R.id.search_platforms_button_id), "platforms", getString(R.string.searchPlatformsEditText),4);
        addListener(findViewById(R.id.search_genres_button_id), "genres", getString(R.string.searchGenresEditText),3);

        //on click on search button to launch an API call
        findViewById(R.id.launch_search_button_id).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchGameAPI = new SearchGameAPI();
                switch (((Fragmentable)getCurrentFragment()).getType()){
                    case "genres" :     searchGameAPI.setGenres( ((EditText)findViewById(R.id.user_search_text_id)).getText().toString()); break;
                    case "platforms" :  searchGameAPI.setPlatforms( ((EditText)findViewById(R.id.user_search_text_id)).getText().toString()); break;
                    case "dates" :      searchGameAPI.setDates( ((EditText)findViewById(R.id.user_search_text_id)).getText().toString());
                                        searchType = "games";
                                        searchGameAPI.setOrdering("released");
                                        break;
                    //same effects for all 3 followings
                    case "publishers" :
                    case "games" :
                    case "developers" :
                        searchGameAPI.setSearch( ((EditText)findViewById(R.id.user_search_text_id)).getText().toString()); break;
                }

                //Hide keyboard
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(((EditText)findViewById(R.id.user_search_text_id)).getWindowToken(), 0);

                //prepare request
                searchGameAPI.setPage_size("40");

                //clear view and scroll up, get page number
                ((Searchable)getCurrentFragment()).clearViewsAndWaitSearch();
                searchGameAPI.setPage(String.valueOf(((Searchable)getCurrentFragment()).getPageNumber()));
                ((Searchable)getCurrentFragment()).scrollUp();

                gamesAPI.requestWithParam(searchGameAPI, currentActivity, ((Fragmentable)getCurrentFragment()).getType());
            }
        });
    }

    /**
     * Add a listener on a view
     * @param v the view to add a listener on
     * @param category search category
     * @param text hint for search text box
     * @param newCurrent new category to display
     */
    private void addListener(View v, String category, String text, int newCurrent){
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
                    mPager.setCurrentItem(newCurrent);
                }
            }
        });
    }

    /**
     * Set the page number for next request
     * @param nb
     */
    public void setSearchGameAPIPageNumber(int nb){
        searchGameAPI.setPage(String.valueOf(nb));
    }

    /**
     * Execute a request based on parameters built
     */
    public void executeRequest(){
        gamesAPI.requestWithParam(searchGameAPI, gameSearchActivity, searchType);
    }

    /**
     * return actual fragment displayed
     * @return actually displayed fragment
     */
    private Fragment getCurrentFragment(){
        switch (mPager.getCurrentItem()){
            case 0: return SearchCategory.getName();
            case 1: return SearchCategory.getDevs();
            case 2: return SearchCategory.getPublishers();
            case 3: return SearchCategory.getGenre();
            case 4: return SearchCategory.getPlatform();
            default: return SearchCategory.getRelease();
        }
    }

    /**
     * Get the info from the api to render it
     * @param obj json object containing api data
     */
    @Override
    public void getApiInfo(JSONObject obj) {
        if(obj == null)//error when getting data
            ((Searchable)getCurrentFragment()).networkError();
        else{
            ((Searchable)getCurrentFragment()).removeError();
            ((Searchable)getCurrentFragment()).removeLoading();
            if(searchType.compareTo("games") == 0 || searchType.compareTo("dates") == 0){//display data
                ((Searchable)getCurrentFragment()).createCards(obj, 0);
            }
            else{//new request
                ((Searchable)getCurrentFragment()).setLoading();
                searchGameAPI = new SearchGameAPI();
                searchGameAPI.setPage_size("40");
                ((Searchable)getCurrentFragment()).setPageNumber(1);
                searchGameAPI.setPage(String.valueOf(((Searchable)getCurrentFragment()).getPageNumber()));

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
                }

                searchType = "games";
                gamesAPI.requestWithParam(searchGameAPI, this, searchType);
            }
        }
    }
}
