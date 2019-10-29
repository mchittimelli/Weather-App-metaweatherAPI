package com.example.mp2_weatherapp_final;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public DrawerLayout drawerLayout;
    NavigationView navigationView;

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    String title;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {

            addFragment(3534, 6077243, "Montréal");
        }

        setupNavigation();

    }

    public void setupNavigation() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigationView);
        getSupportActionBar().show();
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }/*
        else  if(getSupportFragmentManager().getBackStackEntryCount() > 0){
            getSupportFragmentManager().getBackStackEntryAt(1);
            getSupportActionBar().setTitle(title.toUpperCase());
            System.out.println("Count2: " + getSupportFragmentManager().getBackStackEntryCount());
        }*/ else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            default:
                title = item.getTitle().toString();
                addFragment(3534, 6077243, title);
                Toast.makeText(getApplicationContext(), navigationView.getMenu().getItem(0) + "'s Weather", Toast.LENGTH_LONG).show();
                break;
            case R.id.w_toronto:
                title = item.getTitle().toString();
                addFragment(4118, 6167865, title);
                Toast.makeText(getApplicationContext(), navigationView.getMenu().getItem(1) + "'s Weather", Toast.LENGTH_LONG).show();
                break;
            case R.id.w_vancouver:
                title = item.getTitle().toString();
                addFragment(9807, 6173331, title);
                Toast.makeText(getApplicationContext(), navigationView.getMenu().getItem(2) + "'s Weather", Toast.LENGTH_LONG).show();
                break;
            case R.id.w_hyderabad:
                title = item.getTitle().toString();
                addFragment(2295414, 1269843, title);
                Toast.makeText(getApplicationContext(), navigationView.getMenu().getItem(3) + "'s Weather", Toast.LENGTH_LONG).show();
                break;
            case R.id.w_delhi:
                title = item.getTitle().toString();
                addFragment(28743736, 1261481, title);
                Toast.makeText(getApplicationContext(), navigationView.getMenu().getItem(4) + "'s Weather", Toast.LENGTH_LONG).show();
                break;
        }

        drawerLayout.closeDrawer(GravityCompat.START);

        System.out.println("Title: " + item.getTitle());


        return true;
    }

    public void addFragment(int geoId, int bbcId, final String title) {

        Bundle bundle = new Bundle();
        bundle.putInt("geoid", geoId);
        bundle.putInt("bbcid", bbcId);
        bundle.putString("title", title);
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        WeatherFragment weatherFragment = new WeatherFragment();
        fragmentTransaction.replace(R.id.fragmentHolder, weatherFragment);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        fragmentTransaction.addToBackStack(null);
        weatherFragment.setArguments(bundle);
        fragmentTransaction.commit();
        //addWebViewFragment(bbcId);


        System.out.println("Count1: " + getSupportFragmentManager().getBackStackEntryCount());

        getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            public void onBackStackChanged() {
                getSupportActionBar().setTitle(title.toUpperCase());
                if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
                    finish();
                }
                System.out.println("Count2: " + getSupportFragmentManager().getBackStackEntryCount());
            }
        });
    }
    /*public void addWebViewFragment(int bbcId){

        Bundle bundle = new Bundle();
        bundle.putInt("bbcid", bbcId);
        fragmentTransaction = fragmentManager.beginTransaction();
        WebViewFragment webViewFragment = new WebViewFragment();
        fragmentTransaction.add(R.id.fragmentHolder, webViewFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

    }*/

    /*public void getWeather(int geoId)
    {
        GetDataServiceInterface service = RetrofitClientInstance.getRetrofitInstance().create(GetDataServiceInterface.class);

        Call<Weather> call = service.getWeather(geoId);

        call.enqueue(new Callback<Weather>() {
            @Override
            public void onResponse(Call<Weather> call, Response<Weather> response) {

                Bundle bundle = new Bundle();

                Weather weather = response.body();

                consolidatedWeather = new ArrayList<>(weather.getConsolidatedWeather());

                bundle.putString("city", weather.getTitle());

                bundle.putString("the_temp", String.format("%.2f",consolidatedWeather.get(0).getTheTemp()));
                System.out.println("Temp: " + String.format("%.2f",consolidatedWeather.get(0).getTheTemp()));

                bundle.putString("min_temp", String.format("%.2f",consolidatedWeather.get(0).getMinTemp()));
                bundle.putString("max_temp", String.format("%.2f",consolidatedWeather.get(0).getMaxTemp()));
                bundle.putString("weather_state", String.format(consolidatedWeather.get(0).getWeatherStateName()));
                bundle.putString("3", consolidatedWeather.get(0).getHumidity().toString());
                bundle.putString("4", consolidatedWeather.get(0).getPredictability().toString());
                bundle.putString("weather_state_abbr", consolidatedWeather.get(0).getWeatherStateAbbr());
                bundle.putString("date", consolidatedWeather.get(0).getApplicableDate());

                initForecastWeather(consolidatedWeather);

                addFragment(bundle);

                //setData(consolidatedWeather);
            }

            @Override
            public void onFailure(Call<Weather> call, Throwable t) {

                System.out.println("Failure Called! :" +t.getMessage());
            }
        });
    }*/

}