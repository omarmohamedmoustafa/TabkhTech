package com.example.tabkhtech;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.tabkhtech.ui.favourites.view.FavouritesFragment;
import com.example.tabkhtech.ui.home.view.HomeViewImpl;
import com.example.tabkhtech.ui.search.view.SearchViewImpl;
import com.example.tabkhtech.ui.single_meal.view.mealFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.color.DynamicColors;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        DynamicColors.applyToActivitiesIfAvailable(getApplication());
        // Initialize views
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        // Set smaller icon size programmatically

        // Set default fragment
        if (savedInstanceState == null) {
            loadFragment(new HomeViewImpl(), "home");
            bottomNavigationView.setSelectedItemId(R.id.home);
        }

        // Set navigation item selected listener
        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            String tag = null;

            int itemId = item.getItemId();
            if (itemId == R.id.home) {
                selectedFragment = new HomeViewImpl();
                tag = "home";
            } else if (itemId == R.id.search) {
                selectedFragment = new SearchViewImpl();
                tag = "search";
            } else if (itemId == R.id.favourites) {
                 selectedFragment = new FavouritesFragment();
                 tag = "favourites";
            } else if (itemId == R.id.calendar) {
//                 selectedFragment = new CalendarFragment();
                // tag = "calendar";
            } else if (itemId == R.id.profile) {
                 selectedFragment = new ProfileFragment();
                // tag = "profile";
            }

            if (selectedFragment != null) {
                loadFragment(selectedFragment, tag);
                return true;
            }
            return false;
        });
    }

    private void loadFragment(Fragment fragment, String tag) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment existingFragment = fragmentManager.findFragmentByTag(tag);
        if (existingFragment == null || !existingFragment.isVisible()) {
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.frame_layout, fragment, tag)
                    .addToBackStack(tag)
                    .commit();
        }
    }
}