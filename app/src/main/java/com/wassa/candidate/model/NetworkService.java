package com.wassa.candidate.model;


import android.content.Context;
import android.util.Log;

import java.io.File;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author khadir
 * @since 10/06/2017
 */
public class NetworkService {
    public static final int HTTP_200 = 200;
    private static final String TAG = NetworkService.class.getSimpleName();
    private String BASE_URL = "http://localhost:8080";
    private Retrofit mRetrofit;
    protected PlaceCall mPlaceCall;
    private static Context mContext;
    private static final NetworkService mInstance = new NetworkService();

    private void initRetrofitClient() {
        // clean old mRetrofit client if exists
        if (this.mRetrofit != null) {
            this.mRetrofit = null;
        } else {
            // create new mRetrofit client
            Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create());

            this.mRetrofit = retrofitBuilder.build();
            this.mPlaceCall = mRetrofit.create(PlaceCall.class);
        }
    }

    protected NetworkService() {
        this.initRetrofitClient();
    }

    public interface ApiPlacesResult<T> {
        void success(List<Place> places);

        void error(int code, String message);
    }

    public interface ApiPlaceInfoResult<T> {
        void success(Place place);

        void error(int code, String message);
    }

    public interface ApiPlaceImageResult<T> {
        void success(File image);

        void error(int code, String message);
    }

    /*
    * retorfit method to get public repositories list
    * the return is transmitted to the ui with the callback
    */
    public void getPlacesList(final ApiPlacesResult<List<Place>> callback) {
        Call<List<Place>> call = this.mPlaceCall.getPlaces();
        call.enqueue(new Callback<List<Place>>() {
            @Override
            public void onResponse(Call<List<Place>> call, Response<List<Place>> response) {
                int statusCode = response.code();
                if (statusCode == HTTP_200) {
                    initRetrofitClient();
                    List<Place> places = response.body();
                    callback.success(places);
                } else {
                    callback.error(statusCode, response.message());
                }
            }

            @Override
            public void onFailure(Call<List<Place>> call, Throwable t) {
                Log.e(TAG, "Error while calling the 'getPlacesList' method!", t);
                callback.error(-1, t.getLocalizedMessage());
            }
        });
    }

    /*
    * retorfit method to get image of specific place
    * the return is transmitted to the ui with the callback
    */
    public String getPlaceImage(String imageId) {
        Call<String> call = this.mPlaceCall.getPlaceImage(imageId);
        return call.request().url().toString();
    }

    /*
    * retorfit method to get infos of specific place
    * the return is transmitted to the ui with the callback
    */
    public void getPlaceInfo(String id, final ApiPlaceInfoResult callback) {
        Call<Place> call = this.mPlaceCall.getPlaceInfo(id);
        call.enqueue(new Callback<Place>() {
            @Override
            public void onResponse(Call<Place> call, Response<Place> response) {
                int statusCode = response.code();
                if (statusCode == HTTP_200) {
                    initRetrofitClient();
                    Place repository = response.body();
                    Log.d(TAG, repository.toString());
                    callback.success(repository);
                } else {
                    callback.error(statusCode, response.message());
                }
            }

            @Override
            public void onFailure(Call<Place> call, Throwable t) {
                Log.e(TAG, "Error while calling the 'getPlaceInfo' method!", t);
                callback.error(-1, t.getLocalizedMessage());
            }
        });
    }

    public static NetworkService getInstance(Context context) {
        mContext = context;
        return mInstance;
    }

}
