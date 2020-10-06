package com.ritacle.mymusichistory.network;

import com.ritacle.mymusichistory.model.ResponseMMH;
import com.ritacle.mymusichistory.model.scrobbler_model.Scrobble;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ListenRestService {

    @POST("api/listen")
    Call<ResponseMMH<Scrobble>> addListenIntoStat(@Body Scrobble listen);

    @GET("api/listen/check/{userId}/{syncId}")
    Call<Boolean> checkListenSync(@Path("userId") Long userId, @Path("syncId") Long syncId);

    @DELETE("api/listen/delete/{listenId}")
    Call<ResponseMMH<Scrobble>> deleteListen(@Path("listenId") Long listenId);

    @POST("api/listen/edit")
    Call<ResponseMMH<Scrobble>> editListen(@Body Scrobble listen);
}