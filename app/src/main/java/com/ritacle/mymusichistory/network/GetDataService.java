package com.ritacle.mymusichistory.network;

import com.ritacle.mymusichistory.model.LastListen;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface GetDataService {


    @GET("listen/last/{mail}")
    Call<List<LastListen>> getSongReport(@Path("mail") String mail);
}