package com.example.myvideogamelist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;

import com.example.myvideogamelist.InterfacesAppli.Fragmentable;

public class GameListFragmentActivity extends AppCompatActivity {
    private GameListFragmentActivity current = this;
    private static final int NUM_PAGES = 6;
    private ViewPager2 mPager;
    private FragmentStateAdapter pagerAdapter;

    private final NavigationBar navigationBar = NavigationBar.getNavigationBar();
    private Button selectedButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_list_fragment);
        // Instantiate a ViewPager and a PagerAdapter.
        mPager = (ViewPager2) findViewById(R.id.insert_fragments_id);
        pagerAdapter = new GameListPagerAdapter(this);
        mPager.setAdapter(pagerAdapter);

        addNavigationBar();
        navigationBar.init(this);
        connectButton();
        createFragments();
/*
        //for a fragment activity, there is the need to fix navigation bar padding manually
        float density = getApplicationContext().getResources().getDisplayMetrics().density;
        findViewById(R.id.home_button_id).setPadding(0,(int)(15 * density),(int)(25 * density),0);
        findViewById(R.id.search_button_id).setPadding(0,(int)(15 * density),(int)(40 * density),0);
        findViewById(R.id.randomatic_button_id).setPadding(0,(int)(7 * density),(int)(20 * density),0);
        findViewById(R.id.my_lists_button_id).setPadding(0,(int)(7 * density),0,0);*/

        //Allow scroll for category to switch when the user swipe
        mPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                switch(mPager.getCurrentItem()){
                    case 0:changeSelectedButton(findViewById(R.id.list_all_button_id)); break;
                    case 1:changeSelectedButton(findViewById(R.id.list_playing_button_id)); break;
                    case 2:changeSelectedButton(findViewById(R.id.list_planned_button_id));break;
                    case 3:changeSelectedButton(findViewById(R.id.list_on_hold_button_id));break;
                    case 4:changeSelectedButton(findViewById(R.id.list_finished_button_id));break;
                    case 5:changeSelectedButton(findViewById(R.id.list_abandoned_button_id));
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        setBuiltFalse();
    }

    /**
     * Allow rebuilding cards if changes appears
     */
    private void setBuiltFalse(){
        Category.getCatAll().setBuilt(false);
        Category.getCatPlanned().setBuilt(false);
        Category.getCatPlaying().setBuilt(false);
        Category.getCatOnHold().setBuilt(false);
        Category.getCatFinished().setBuilt(false);
        Category.getCatAbandoned().setBuilt(false);
    }

    /**
     * Change selected button
     * @param v view to scroll to
     */
    private void changeSelectedButton(View v){
        selectedButton.setTextColor(ResourcesCompat.getColor(getResources(), R.color.grey, null));
        selectedButton = (Button) v;
        selectedButton.setTextColor(ResourcesCompat.getColor(getResources(), R.color.white, null));
        ((HorizontalScrollView) findViewById(R.id.linearLayout1)).requestChildFocus(selectedButton, selectedButton);
    }

    /**
     * Create and give to fragments all informations to build themselves
     */
    private void createFragments(){
        Category.getCatAll().setType("all");
        Category.getCatAll().setFragmentActivity(current);
        Category.getCatAll().createView(mPager);

        Category.getCatPlanned().setType("planned");
        Category.getCatPlanned().setFragmentActivity(current);
        Category.getCatPlanned().createView(mPager);

        Category.getCatPlaying().setType("playing");
        Category.getCatPlaying().setFragmentActivity(current);
        Category.getCatPlaying().createView(mPager);

        Category.getCatOnHold().setType("on_hold");
        Category.getCatOnHold().setFragmentActivity(current);
        Category.getCatOnHold().createView(mPager);

        Category.getCatFinished().setType("finished");
        Category.getCatFinished().setFragmentActivity(current);
        Category.getCatFinished().createView(mPager);

        Category.getCatAbandoned().setType("abandoned");
        Category.getCatAbandoned().setFragmentActivity(current);
        Category.getCatAbandoned().createView(mPager);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(Category.getCatAll().isBuilt()){
            setBuiltFalse();
            createFragments();
            Category.getCatAbandoned().build();
            Category.getCatAll().build();
            Category.getCatPlanned().build();
            Category.getCatOnHold().build();
            Category.getCatFinished().build();
            Category.getCatPlaying().build();
        }
    }

    @Override
    public void onBackPressed() {
        if (mPager.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } else {
            // Otherwise, select the previous step.
            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
        }
    }

    /**
     * A simple pager adapter that represents 5 ScreenSlidePageFragment objects, in
     * sequence.
     */
    private class GameListPagerAdapter extends FragmentStateAdapter {
        public GameListPagerAdapter(FragmentActivity fm) {
            super(fm);
        }

        @Override
        public Fragment createFragment(int position) {
            Fragmentable c = null;
            switch (position){
                case 0 : c = Category.getCatAll(); break;
                case 2 : c = Category.getCatPlanned(); break;
                case 1 : c = Category.getCatPlaying(); break;
                case 3 : c = Category.getCatOnHold(); break;
                case 4 : c = Category.getCatFinished(); break;
                case 5 : c = Category.getCatAbandoned(); break;
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
        vi.inflate(R.layout.bottom_bar_navigation, findViewById(R.id.game_lists_activity_id), true);
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
                    switch (cat){
                        case "all" : mPager.setCurrentItem(0); break;
                        case "finished" : mPager.setCurrentItem(4); break;
                        case "planned" : mPager.setCurrentItem(2); break;
                        case "on_hold" : mPager.setCurrentItem(3); break;
                        case "playing" : mPager.setCurrentItem(1); break;
                        case "abandoned" : mPager.setCurrentItem(5);
                    }
                }
            }
        });
    }
}