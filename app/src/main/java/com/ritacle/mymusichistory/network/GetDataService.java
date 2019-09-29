package com.ritacle.mymusichistory.network;

import com.ritacle.mymusichistory.model.LastListen;
import com.ritacle.mymusichistory.model.ListenAmount;

import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface GetDataService {


    @GET("listen/last/{mail}")
    Call<List<LastListen>> getSongReport(@Path("mail") String mail);

    @GET("top/songs/{mail}/{startDate}/{endDate}")
    Call<List<ListenAmount>> getUserTopListens(@Path("mail") String mail, @Path ("startDate") String startDate, @Path ("endDate") String endDate);



}