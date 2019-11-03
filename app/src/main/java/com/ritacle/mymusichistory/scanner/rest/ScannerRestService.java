package com.ritacle.mymusichistory.scanner.rest;


import com.ritacle.mymusichistory.scanner.model.Listen;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ScannerRestService {
    @POST("api/listen")
    Call<Listen> addListenIntoStat(@Body Listen listen);

    @GET("api/listen/check/{userId}/{syncId}")
    Call<Boolean> checkListenSync(@Path("userId") Long userId, @Path("syncId") Long syncId);
}
