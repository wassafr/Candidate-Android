package com.wassa.candidate.model;

import android.support.annotation.NonNull;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * @author khadir
 * @since 10/06/2017
 */
public interface PlaceCall {

    // get request for list of places
    @GET("/ws/places")
    Call<List<Place>> getPlaces();

    // get request for image's place with it's imageId
    @GET("/images/{imageId}")
    Call<String> getPlaceImage(@Path("imageId") @NonNull String imageId);

    // get request for a specific place with it's id
    @GET("/ws/places/{placeId}")
    Call<Place> getPlaceInfo(@Path("placeId") @NonNull String id);
}
