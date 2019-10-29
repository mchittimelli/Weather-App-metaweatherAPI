package com.example.mp2_weatherapp_final;

import android.content.Context;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class WeatherFragment extends Fragment {

    ArrayList<ConsolidatedWeather> consolidatedWeather;
    ArrayList<Source> sources;

    TextView f_today, f_city, f_today_temp, f_min_temp, f_max_temp, f_weather_state, f_humidity, f_predictability, f_more_info;
    ImageView f_weather_icon;

    ProgressBar f_min_prog, f_max_prog, f_hum_prog, f_pred_prog;

    LinearLayout linearLayout, linearLayout1, linearLayout2, linearLayout3, linearLayout4, linearLayout5;

    View rootView;
    Bundle bundle;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView =  inflater.inflate(R.layout.fragment_weather,container,false);
        return rootView;

    }

    @Override
    public void onResume() {
        super.onResume();

        //((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(getArguments().getString("city").toUpperCase());
        //((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(weather.getTitle().toUpperCase());

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        f_city = rootView.findViewById(R.id.f_city);
        f_today_temp = rootView.findViewById(R.id.f_today_temp);
        //f_min_max_temp = rootView.findViewById(R.id.f_min_max_temp);
        f_min_temp = rootView.findViewById(R.id.f_min_temp);
        f_max_temp = rootView.findViewById(R.id.f_max_temp);
        f_weather_state = rootView.findViewById(R.id.f_weather_state);
        f_humidity = rootView.findViewById(R.id.f_humidity);
        f_predictability = rootView.findViewById(R.id.f_predictability);
        f_weather_icon = rootView.findViewById(R.id.f_weather_icon);
        f_today = rootView.findViewById(R.id.f_today);

        f_more_info = rootView.findViewById(R.id.f_more_info);

        f_more_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                final WebViewFragment webViewFragment = new WebViewFragment();
                fragmentTransaction.add(R.id.fragmentHolder, webViewFragment, "web");
                fragmentTransaction.addToBackStack(null);
                webViewFragment.setArguments(bundle);
                fragmentTransaction.commit();

                System.out.println("Count3: " + getFragmentManager().getBackStackEntryCount());

                /*getFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
                    public void onBackStackChanged() {
                        if(getFragmentManager().findFragmentByTag("web") instanceof WebViewFragment){
                            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("BBC WEATHER, " + getArguments().getString("title").toUpperCase());
                        /*if(getFragmentManager().getBackStackEntryCount() == 0){
                            this.finish();*//*
                        }
                        else {
                            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(getArguments().getString("title").toUpperCase());

                            FragmentTransaction f = getFragmentManager().beginTransaction();
                            f.remove(webViewFragment);
                            f.commit();
                        }
                        System.out.println("Count4: " + getFragmentManager().getBackStackEntryCount());
                    }
                });*/
            }
        });

        f_min_prog = rootView.findViewById(R.id.f_min_prog);
        f_max_prog = rootView.findViewById(R.id.f_max_prog);
        f_hum_prog = rootView.findViewById(R.id.f_hum_prog);
        f_pred_prog = rootView.findViewById(R.id.f_pred_prog);

        linearLayout = rootView.findViewById(R.id.linearLayout);
        linearLayout1 = rootView.findViewById(R.id.linearLayout1);
        linearLayout2 = rootView.findViewById(R.id.linearLayout2);
        linearLayout3 = rootView.findViewById(R.id.linearLayout3);
        linearLayout4 = rootView.findViewById(R.id.linearLayout4);
        linearLayout5 = rootView.findViewById(R.id.linearLayout5);

        getWeather();
    }

    public void getWeather()
    {
        GetDataServiceInterface service = RetrofitClientInstance.getRetrofitInstance().create(GetDataServiceInterface.class);

        Call<Weather> call = service.getWeather(getArguments().getInt("geoid"));

        call.enqueue(new Callback<Weather>() {
            @Override
            public void onResponse(Call<Weather> call, Response<Weather> response) {


                Weather weather = response.body();

                sources = new ArrayList<>(weather.getSources());
                Parent parent = weather.getParent();

                consolidatedWeather = new ArrayList<>(weather.getConsolidatedWeather());

                bundle = new Bundle();

                bundle.putString("bbc_url", sources.get(0).getUrl() + getArguments().getInt("bbcid"));
                bundle.putString("city", weather.getTitle());

                Date date = new Date();
                String date1 = consolidatedWeather.get(0).getApplicableDate();
                try {
                    date = new SimpleDateFormat("yyyy-MM-dd").parse(date1);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                System.out.println("Date: " + date);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("E, dd MMM");
                String today = simpleDateFormat.format(date);
                System.out.println("Date: " + today);

                f_city.setText(weather.getTitle() + ",\n" + parent.getTitle());
                f_today_temp.setText(String.format("%.2f",consolidatedWeather.get(0).getTheTemp()) + "°C");
                //f_min_max_temp.setText(String.format("%.2f",consolidatedWeather.get(0).getMaxTemp()) + "°" + "/" + String.format("%.2f",consolidatedWeather.get(0).getMinTemp()) + "°");
                f_min_temp.setText(String.format("%.2f",consolidatedWeather.get(0).getMinTemp()) + "°");
                f_max_temp.setText(String.format("%.2f",consolidatedWeather.get(0).getMaxTemp()) + "°");
                f_weather_state.setText(consolidatedWeather.get(0).getWeatherStateName());
                f_humidity.setText(consolidatedWeather.get(0).getHumidity().toString() + "%");
                f_predictability.setText(consolidatedWeather.get(0).getPredictability().toString() + "%");
                Picasso.get().load(getWeatherImage(consolidatedWeather.get(0).getWeatherStateAbbr())).into(f_weather_icon);
                f_today.setText(today);

                initForecastWeather(consolidatedWeather);

                ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(weather.getTitle().toUpperCase());

                f_min_prog.setProgress(Integer.parseInt(String.format("%.0f",consolidatedWeather.get(0).getMinTemp())));
                f_max_prog.setProgress(Integer.parseInt(String.format("%.0f",consolidatedWeather.get(0).getMaxTemp())));
                f_hum_prog.setProgress(consolidatedWeather.get(0).getHumidity());
                f_pred_prog.setProgress(consolidatedWeather.get(0).getPredictability());
                f_min_prog.getProgressDrawable().setColorFilter(Color.parseColor("#C03A77"), android.graphics.PorterDuff.Mode.SRC_IN);
                f_max_prog.getProgressDrawable().setColorFilter(Color.parseColor("#1abf08"), android.graphics.PorterDuff.Mode.SRC_IN);
                f_hum_prog.getProgressDrawable().setColorFilter(Color.parseColor("#FFCD00"), android.graphics.PorterDuff.Mode.SRC_IN);
                f_pred_prog.getProgressDrawable().setColorFilter(Color.parseColor("#FC4800"), android.graphics.PorterDuff.Mode.SRC_IN);

                String city = weather.getTitle();

                switch (city){
                    case "Toronto":
                        linearLayout.setBackgroundResource(R.drawable.toronto);
                        linearLayout1.setBackgroundResource(R.drawable.four);
                        linearLayout2.setBackgroundResource(R.drawable.three);
                        linearLayout3.setBackgroundResource(R.drawable.two);
                        linearLayout4.setBackgroundResource(R.drawable.one);

                        f_pred_prog.getProgressDrawable().setColorFilter(Color.parseColor("#C03A77"), android.graphics.PorterDuff.Mode.SRC_IN);
                        f_hum_prog.getProgressDrawable().setColorFilter(Color.parseColor("#1abf08"), android.graphics.PorterDuff.Mode.SRC_IN);
                        f_max_prog.getProgressDrawable().setColorFilter(Color.parseColor("#FFCD00"), android.graphics.PorterDuff.Mode.SRC_IN);
                        f_min_prog.getProgressDrawable().setColorFilter(Color.parseColor("#FC4800"), android.graphics.PorterDuff.Mode.SRC_IN);
                        break;
                    case "Hyderabad":
                        linearLayout.setBackgroundResource(R.drawable.hyderabad);
                        linearLayout1.setBackgroundResource(R.drawable.two);
                        linearLayout2.setBackgroundResource(R.drawable.three);
                        linearLayout3.setBackgroundResource(R.drawable.four);
                        linearLayout4.setBackgroundResource(R.drawable.one);

                        f_pred_prog.getProgressDrawable().setColorFilter(Color.parseColor("#C03A77"), android.graphics.PorterDuff.Mode.SRC_IN);
                        f_min_prog.getProgressDrawable().setColorFilter(Color.parseColor("#1abf08"), android.graphics.PorterDuff.Mode.SRC_IN);
                        f_max_prog.getProgressDrawable().setColorFilter(Color.parseColor("#FFCD00"), android.graphics.PorterDuff.Mode.SRC_IN);
                        f_hum_prog.getProgressDrawable().setColorFilter(Color.parseColor("#FC4800"), android.graphics.PorterDuff.Mode.SRC_IN);
                        break;
                    case "New Delhi":
                        linearLayout.setBackgroundResource(R.drawable.delhi);
                        linearLayout1.setBackgroundResource(R.drawable.one);
                        linearLayout2.setBackgroundResource(R.drawable.four);
                        linearLayout3.setBackgroundResource(R.drawable.three);
                        linearLayout4.setBackgroundResource(R.drawable.two);

                        f_min_prog.getProgressDrawable().setColorFilter(Color.parseColor("#C03A77"), android.graphics.PorterDuff.Mode.SRC_IN);
                        f_pred_prog.getProgressDrawable().setColorFilter(Color.parseColor("#1abf08"), android.graphics.PorterDuff.Mode.SRC_IN);
                        f_hum_prog.getProgressDrawable().setColorFilter(Color.parseColor("#FFCD00"), android.graphics.PorterDuff.Mode.SRC_IN);
                        f_max_prog.getProgressDrawable().setColorFilter(Color.parseColor("#FC4800"), android.graphics.PorterDuff.Mode.SRC_IN);
                        break;
                    case "Vancouver":
                        linearLayout.setBackgroundResource(R.drawable.vancouver);
                        linearLayout1.setBackgroundResource(R.drawable.three);
                        linearLayout2.setBackgroundResource(R.drawable.two);
                        linearLayout3.setBackgroundResource(R.drawable.one);
                        linearLayout4.setBackgroundResource(R.drawable.four);

                        f_hum_prog.getProgressDrawable().setColorFilter(Color.parseColor("#C03A77"), android.graphics.PorterDuff.Mode.SRC_IN);
                        f_max_prog.getProgressDrawable().setColorFilter(Color.parseColor("#1abf08"), android.graphics.PorterDuff.Mode.SRC_IN);
                        f_min_prog.getProgressDrawable().setColorFilter(Color.parseColor("#FFCD00"), android.graphics.PorterDuff.Mode.SRC_IN);
                        f_pred_prog.getProgressDrawable().setColorFilter(Color.parseColor("#FC4800"), android.graphics.PorterDuff.Mode.SRC_IN);
                        break;
                    case "Montréal":
                        linearLayout.setBackgroundResource(R.drawable.montreal);
                        f_min_prog.getProgressDrawable().setColorFilter(Color.parseColor("#C03A77"), android.graphics.PorterDuff.Mode.SRC_IN);
                        f_max_prog.getProgressDrawable().setColorFilter(Color.parseColor("#1abf08"), android.graphics.PorterDuff.Mode.SRC_IN);
                        f_hum_prog.getProgressDrawable().setColorFilter(Color.parseColor("#FFCD00"), android.graphics.PorterDuff.Mode.SRC_IN);
                        f_pred_prog.getProgressDrawable().setColorFilter(Color.parseColor("#FC4800"), android.graphics.PorterDuff.Mode.SRC_IN);
                        break;

                }

            }

            @Override
            public void onFailure(Call<Weather> call, Throwable t) {

                System.out.println("Failure Called! :" +t.getMessage());
            }
        });
    }

    public String getWeatherImage(String weatherStateAbbr)
    {
        return "https://www.metaweather.com/static/img/weather/png/" + weatherStateAbbr + ".png";
    }

    public void initForecastWeather(ArrayList<ConsolidatedWeather> consolidatedWeather)
    {
        RecyclerView recyclerView = rootView.findViewById(R.id.f_recycle);
        consolidatedWeather.remove(0);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.HORIZONTAL,false);
        recyclerView.setLayoutManager(layoutManager);
        ForecastWeatherAdapter adapter = new ForecastWeatherAdapter(consolidatedWeather, getActivity().getApplicationContext());
        recyclerView.setAdapter(adapter);
    }

    
}
