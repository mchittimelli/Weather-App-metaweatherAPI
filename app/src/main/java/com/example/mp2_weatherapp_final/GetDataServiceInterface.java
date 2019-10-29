package com.example.mp2_weatherapp_final;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface GetDataServiceInterface {

    @GET("{geoID}")
    Call<Weather> getWeather(@Path("geoID") int geoId);
}
