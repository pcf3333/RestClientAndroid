package com.example.restclient;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import service.*;


public class MainActivity extends AppCompatActivity {

    Context context;
    List<Track> trackList;
    TrackAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        context=this;
    }

    //Function to clear teh tracks from the adapter
    public void ClearTracks(View view){
        TextView errorTV= findViewById(R.id.errorsTV);
        try {
            int size = trackList.size();
            trackList.clear();
            adapter.notifyItemRangeRemoved(0, size);
            errorTV.setText("List cleared");
        }
        catch (Exception e){
            errorTV.setText("No tracks to clear");
        }
    }

    public void getAllTracks(View view) {
        TracksService ts = TracksService.retrofit.create(TracksService.class);
        Call<List<Track>> call = ts.getAllTracks();
        final TextView errorTV= findViewById(R.id.errorsTV);

        call.enqueue(new Callback<List<Track>>() {
            @Override
            public void onResponse(Call<List<Track>> call, Response<List<Track>> response) {
                final RecyclerView RV =  findViewById(R.id.RVTracks);

                trackList=response.body();

                adapter = new TrackAdapter(trackList);

                // Attach the adapter to the recyclerview to populate items
                RV.setAdapter(adapter);
                // Set layout manager to position the items
                RV.setLayoutManager(new LinearLayoutManager(context));

                errorTV.setText("All Tracks have been displayed");
            }

            @Override
            public void onFailure(Call<List<Track>> call, Throwable t) {
                errorTV.setText("Something went wrong: " + t.getMessage());
            }
        });

    }

    //preguntar si aixo es fa aqui o al adaptador
    public static void removeTrack(String id){
        TracksService ts = TracksService.retrofit.create(TracksService.class);
        Call<ResponseBody> call = ts.deleteTrack(id);


       call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                errorsTV.setText("Track has been deleted");
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
  //              errorsTV.setText("Something went wrong: " + t.getMessage());
            }
        });
    }

    public void addTrack(View v){
        final TextInputLayout song =  findViewById(R.id.SongInput);
        final TextInputLayout singer = findViewById(R.id.SingerInput);
        final TextView errorTV= findViewById(R.id.errorsTV);

        String so = song.getEditText().getText().toString();
        String si = singer.getEditText().getText().toString();

        if (!si.equals("") &&  !so.equals("")){
            Track track = new Track(so,si);
            TracksService ts = TracksService.retrofit.create(TracksService.class);
            Call<Track> call = ts.addTrack(track);
            call.enqueue(new Callback<Track>() {
                @Override
                public void onResponse(Call<Track> call, Response<Track> response) {
                    errorTV.setText("Track has been added");
                    song.getEditText().setText("");
                    singer.getEditText().setText("");
                }

                @Override
                public void onFailure(Call<Track> call, Throwable t) {
                    errorTV.setText("Something went wrong: " + t.getMessage());
                }
            });

        }
        else{
            errorTV.setText("Fill both data items");
        }
    }

}
