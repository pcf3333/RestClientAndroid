package com.example.restclient;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import service.Track;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class TrackAdapter extends RecyclerView.Adapter<TrackAdapter.ViewHolder> {

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public TextView RVtextView;
        public Button ModButton;
        public Button DelButton;
        public View view;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            RVtextView = itemView.findViewById(R.id.RVtextView);
            ModButton = itemView.findViewById(R.id.ModifyButton);
            DelButton = itemView.findViewById(R.id.DeleteButton);
            view=itemView;
        }

    }

    //Per guardar la llista
    private List<Track> mTracks;

    // Pass in the track array into the constructor
    public TrackAdapter(List<Track> tracks) {
        mTracks = tracks;
    }


    // Usually involves inflating a layout from XML and returning the holder
    @Override
    public TrackAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Aqui pones el layaut que has creado
        View trackView = inflater.inflate(R.layout.track_item, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(trackView);
        return viewHolder;
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(final TrackAdapter.ViewHolder viewHolder, final int position) {
        // Get the data model based on position
        final Track track = mTracks.get(position);

        // Set item views based on your views and data model
        TextView textView = viewHolder.RVtextView;
        textView.setText(track.getTitle()+" | "+track.getSinger());
        Button mbutton = viewHolder.ModButton;
        mbutton.setText("Edit");
        Button delbutton = viewHolder.DelButton;
        delbutton.setText("Delete");

        //To edit tracks
        mbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MainActivity().showEditForm(v,track);
            }
        });

        //To delete tracks
        delbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteTrack(position);
                MainActivity.removeTrack(track.getId());
            }
        });
    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return mTracks.size();
    }

    private void deleteTrack(int position){
        mTracks.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position,mTracks.size());
    }





}
