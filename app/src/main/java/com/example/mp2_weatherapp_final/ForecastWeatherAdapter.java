package com.example.mp2_weatherapp_final;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ForecastWeatherAdapter extends RecyclerView.Adapter<ForecastWeatherAdapter.ViewHolder>{

    private ArrayList<ConsolidatedWeather> consolidatedWeathers;
    private Context context;

    public ForecastWeatherAdapter(ArrayList<ConsolidatedWeather> consolidatedWeathers, Context context) {
        this.consolidatedWeathers = consolidatedWeathers;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.forecast_layout,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Date date = new Date();

        try {
            date = new SimpleDateFormat("yyyy-MM-d").parse(consolidatedWeathers.get(position).getApplicableDate());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("E, dd MMM");
        String today = simpleDateFormat.format(date);

        Picasso.get().load(getWeatherImage(consolidatedWeathers.get(position).getWeatherStateAbbr())).into(holder.for_weather_img);
        holder.for_day.setText(today);
        //holder.for_temp.setText(consolidatedWeathers.get(position).getWeatherStateName() + ", " + String.format("%.2f",consolidatedWeathers.get(position).getTheTemp()) + "°");
        holder.for_temp.setText(String.format("%.2f",consolidatedWeathers.get(position).getTheTemp()) + "°");
        holder.for_day_state.setText(consolidatedWeathers.get(position).getWeatherStateName());

        //holder.cardView.setBackgroundResource(Color.TRANSPARENT);

    }

    @Override
    public int getItemCount() {
        return consolidatedWeathers.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView for_weather_img;
        TextView for_day, for_temp, for_day_state;
        CardView cardView;

        public ViewHolder(View view) {
            super(view);

            for_weather_img = view.findViewById(R.id.for_weather_img);

            for_day = view.findViewById(R.id.for_day);
            for_temp = view.findViewById(R.id.for_temp);
            for_day_state = view.findViewById(R.id.for_day_state);

            cardView = view.findViewById(R.id.cardView);

            itemView.setTag(this);

        }
    }

    public String getWeatherImage(String weatherStateAbbr)
    {
        return "https://www.metaweather.com/static/img/weather/png/" + weatherStateAbbr + ".png";
    }

}
