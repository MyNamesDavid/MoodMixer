package com.example.moodmixer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class PlaylistAdapter extends ArrayAdapter<Playlists> {

    public PlaylistAdapter(Context context, ArrayList<Playlists> playlists){
        super(context, 0, playlists);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        //Get data item for this position
        Playlists playlist = getItem(position);
        //Check if an existing view is being reused else inflate view
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_playlist,
                    parent, false);
        }

        //Lookup view for data population
        TextView playlistName = (TextView) convertView.findViewById(R.id.list_item_playlist_name);
        //Populate the data
        playlistName.setText(playlist.playlistName);
        //Return view
        return  convertView;
    }
}
