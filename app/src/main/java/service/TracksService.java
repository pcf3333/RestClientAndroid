package service;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.Retrofit;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface TracksService {

    //EL builder
    public static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://192.168.1.17:8080/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();


    @GET("dsaApp/tracks/")
    Call<List<Track>> getAllTracks();


    @DELETE("dsaApp/tracks/{id}/")
    Call<ResponseBody> deleteTrack(@Path("id") String TrackId);

    @POST("dsaApp/tracks/")
    Call<Track> addTrack(@Body Track track);

    @PUT("dsaApp/tracks/")
    Call<Track> updateTrack(@Body Track track);
}
