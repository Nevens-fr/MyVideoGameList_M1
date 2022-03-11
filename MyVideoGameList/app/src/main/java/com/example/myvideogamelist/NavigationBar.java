package com.example.myvideogamelist;


import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.Button;

import com.example.myvideogamelist.ApiGestion.NewsAPI;

/**
 * Class that allow user to navigate in the app with a bottom bar
 */
public class NavigationBar {
    private static final NavigationBar navigationBar = new NavigationBar();
    private static Button buttonHome, buttonSearch, buttonRandomatic, buttonMyLists;

    /**
     * Private constructor for singleton
     */
    private NavigationBar(){    }

    /**
     * Getting buttons from the current activity and applying comportment, prevent activity to restart itself
     * @param currentActivity the activity displayed to the user
     */
    public static void init(Activity currentActivity){
        buttonHome = currentActivity.findViewById(R.id.home_button_id);
        buttonSearch = currentActivity.findViewById(R.id.search_button_id);
        buttonRandomatic = currentActivity.findViewById(R.id.randomatic_button_id);
        buttonMyLists = currentActivity.findViewById(R.id.my_lists_button_id);

        if(currentActivity.getClass() != Home.class)
            buttonHome.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    NewsAPI.getNewsAPI().setCurrentActivity(null);
                    Intent intent = new Intent(currentActivity, Home.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    currentActivity.startActivity(intent);
                }
            });
        if(currentActivity.getClass() != SearchActivity.class)
            buttonSearch.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    NewsAPI.getNewsAPI().setCurrentActivity(null);
                    Intent intent = new Intent(currentActivity, SearchActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    currentActivity.startActivity(intent);
                }
            });
        if(currentActivity.getClass() != RandomaticActivity.class)
            buttonRandomatic.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    NewsAPI.getNewsAPI().setCurrentActivity(null);
                    Intent intent = new Intent(currentActivity, RandomaticActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    currentActivity.startActivity(intent);
                }
            });
        if(currentActivity.getClass() != GameListFragmentActivity.class)
            buttonMyLists.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    NewsAPI.getNewsAPI().setCurrentActivity(null);
                    Intent intent = new Intent(currentActivity, GameListFragmentActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    currentActivity.startActivity(intent);
                }
            });
    }

    /**
     * Navigation bar's getter
     * @return the navigation bar
     */
    public static NavigationBar getNavigationBar() {
        return navigationBar;
    }
}
