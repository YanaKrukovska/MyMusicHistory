package com.ritacle.mymusichistory.service;

import com.ritacle.mymusichistory.model.scrobbler_model.Scrobble;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface StatisticRestService {
    @POST("api/listen")
    Call<Scrobble> addListenIntoStat(@Body Scrobble listen);

    @GET("api/listen/check/{userId}/{syncId}")
    Call<Boolean> checkListenSync(@Path("userId") Long userId, @Path("syncId") Long syncId);
}