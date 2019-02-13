package com.example.lux.popmovieskv.activities;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.lux.popmovieskv.R;
import com.example.lux.popmovieskv.fragments.Fav_Movies_Fragment;
import com.example.lux.popmovieskv.fragments.Pop_Movies_Fragment;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView nav = findViewById(R.id.navigationView);
        nav.setOnNavigationItemSelectedListener(this);
        loadFragment(new Pop_Movies_Fragment());
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Fragment fragment = null;
        switch(menuItem.getItemId()){
            case R.id.navPopular:
                fragment = new Pop_Movies_Fragment();
                break;
            case R.id.navFavorites:
                fragment = new Fav_Movies_Fragment();
                break;
        }
        return loadFragment(fragment);
    }

    private boolean loadFragment(Fragment fragment){
        if(fragment != null){
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container,fragment)
                    .commit();
            return true;
        }
        return false;
    }
}
