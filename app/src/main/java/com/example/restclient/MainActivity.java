package com.example.restclient;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
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
    static LayoutInflater inflater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        this.context=this;
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
                //errorsTV.setText("Track has been deleted");
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                //errorsTV.setText("Something went wrong: " + t.getMessage());
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

    public void showEditForm(View view, final Track track){
        // inflate the layout of the popup window
        View popupView = inflater.inflate(R.layout.editform, null);

        // create the popup window
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        final TextInputLayout name=popupView.findViewById(R.id.TVname);
        final TextInputLayout singer=popupView.findViewById(R.id.TVauthor);

        name.getEditText().setText(track.getTitle());
        singer.getEditText().setText(track.getSinger());

        // show the popup window
        // which view you pass in doesn't matter, it is only used for the window tolken
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        //boton para cerrar
        Button discBTN = popupView.findViewById(R.id.discardButton);
        discBTN.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }

        });

        //boton para guardar
        Button saveBTN = popupView.findViewById(R.id.saveButton);
        saveBTN.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(final View v) {
                if (!name.getEditText().getText().toString().equals("") && !singer.getEditText().getText().toString().equals("")){
                    TracksService ts = TracksService.retrofit.create(TracksService.class);
                    track.setTitle(name.getEditText().getText().toString());
                    track.setSinger(singer.getEditText().getText().toString());
                    Call<Track> call = ts.updateTrack(track);
                    call.enqueue(new Callback<Track>() {
                        @Override
                        public void onResponse(Call<Track> call, Response<Track> response) {
                            //errorTV.setText("Track has been updated");
                        }

                        @Override
                        public void onFailure(Call<Track> call, Throwable t) {
                            //errorTV.setText("Something went wrong: " + t.getMessage());
                        }
                    });

                    popupWindow.dismiss();
                }
                else{
                    AlertDialog alertDialog = new AlertDialog.Builder(v.getContext()).create();
                    alertDialog.setTitle("Alert");
                    alertDialog.setMessage("Make Sure all the fields are correct");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();

                }
            }

        });
    }


}
