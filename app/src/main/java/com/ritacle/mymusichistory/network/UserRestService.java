package com.ritacle.mymusichistory.network;

import com.ritacle.mymusichistory.model.ResponseMMH;
import com.ritacle.mymusichistory.model.scrobbler_model.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface UserRestService {

    @POST("user/add")
    Call<ResponseMMH<User>> addUser(@Body User user);

    @GET("user/{mail}")
    Call<User> getUser(@Path("mail") String mail);
}
