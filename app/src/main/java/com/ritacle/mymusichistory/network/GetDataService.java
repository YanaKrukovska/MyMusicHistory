package com.ritacle.mymusichistory.network;

import com.ritacle.mymusichistory.model.discogs_model.DiscogsResponse;
import com.ritacle.mymusichistory.model.LastListen;
import com.ritacle.mymusichistory.model.ListenAmount;
import com.ritacle.mymusichistory.model.TopAlbum;
import com.ritacle.mymusichistory.model.TopArtist;
import com.ritacle.mymusichistory.model.scrobbler_model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface GetDataService {

    @GET("report/listen/last/{mail}")
    Call<List<LastListen>> getSongReport(@Path("mail") String mail);

    @GET("report/top/songs/{mail}/{startDate}/{endDate}")
    Call<List<ListenAmount>> getUserTopListens(@Path("mail") String mail, @Path("startDate") String startDate, @Path("endDate") String endDate);

    @GET("report/top/artists/{mail}/{startDate}/{endDate}")
    Call<List<TopArtist>> getUserTopArtists(@Path("mail") String mail, @Path("startDate") String startDate, @Path("endDate") String endDate);

    @GET("report/top/albums/{mail}/{startDate}/{endDate}")
    Call<List<TopAlbum>> getUserTopAlbums(@Path("mail") String mail, @Path("startDate") String startDate, @Path("endDate") String endDate);

    @GET("user/{mail}")
    Call<User> getUser(@Path("mail") String mail);

    @GET("search?token=KBZpGABXomzDFAyQiRlIgFULMluPFdQpmpOHthIO&release_title=folklore&artist=taylor%20swift&per_page=1&page=1")
    Call<DiscogsResponse> getSongArtwork();

}