package com.example.teacheronlinecourse.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.teacheronlinecourse.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class Home extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home,R.id.navigation_search,R.id.navigation_courses,R.id.navigation_favourite, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);


//        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
//        BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
//                new BottomNavigationView.OnNavigationItemSelectedListener() {
//                    @Override public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                        switch (item.getItemId()) {
//                            case R.id.navigation_courses:
//                                startActivity(new Intent(Home.this, UserCourses.class));
//
//                                return true;
//                        }
//                        return false;
//                    }
//                };
    }

//    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
//            = new BottomNavigationView.OnNavigationItemSelectedListener() {
//
//        @Override
//        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//            Fragment fragment;
//            switch (item.getItemId()) {
//                case R.id.navigation_home:
//                    fragment = new HomeTab();
//                    loadFragment(fragment);
//                    return true;
//                case R.id.navigation_search:
//                    fragment = new Search();
//                    loadFragment(fragment);
//                    return true;
//                case R.id.navigation_courses:
//                    startActivity(new Intent(Home.this, UserCourses.class));
//
//                    return true;
//                case R.id.navigation_favourite:
//                    fragment = new FavouriteCourses();
//                    loadFragment(fragment);
//                    return true;
//                case R.id.navigation_notifications:
//                    fragment = new ProfileTab();
//                    loadFragment(fragment);
//                    return true;
//            }
//            return false;
//        }
//    };
//    private void loadFragment(Fragment fragment) {
//        // load fragment
//        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//        transaction.replace(R.id.nav_host_fragment, fragment);
//        transaction.addToBackStack(null);
//        transaction.commit();
//    }
}
