package com.example.moodmixer;

import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.moodmixer.SongFragment.OnSongListFragmentInteractionListener;
import com.example.moodmixer.dummy.DummyContent;
import com.spotify.android.appremote.api.SpotifyAppRemote;
import com.spotify.protocol.types.Image;
import com.spotify.protocol.types.Item;
import com.spotify.protocol.types.Track;


import java.util.ArrayList;
import java.util.List;

import kotlin.Metadata;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Songs} and makes a call to the
 * specified {@link OnSongListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MySongRecyclerViewAdapter extends RecyclerView.Adapter<MySongRecyclerViewAdapter.ViewHolder> {

    private Context mCtx;

    private final List<Songs> mValues;
    private final OnSongListFragmentInteractionListener mSongListener;

    SharedPreferences sharedpreferences;
    private static final String CLIENT_ID = "a6d6003f62b54f1c9a3ea665f4ded656";
    private static final String REDIRECT_URI = "https://elliottdiaz1.wixsite.com/moodmixer";
    private SpotifyAppRemote musicPlayer; // mSpotifyAppRemove
    private Songs tracks;
    private String trackName;
    private String trackArtist;
    private ImageView trackAlbumCover;
    String songId;



    public MySongRecyclerViewAdapter(List<Songs> items, OnSongListFragmentInteractionListener listener, Context mCtx) {
        mValues = items;
        mSongListener = listener;
        this.mCtx = mCtx;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_song, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        holder.mItem = mValues.get(position);
        holder.mSongNameView.setText(mValues.get(position).songName);
        holder.mArtistNameView.setText(mValues.get(position).songArtistName);
        holder.mSongLengthView.setText(mValues.get(position).songLength);
        //holder.mAlbumView.setId(mValues.get(position).songAlbumCover);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mSongListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mSongListener.onSongListFragmentInteraction(holder.mItem);
                }
            }
        });

        holder.mPopupView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSongListener.OnSongListFragmentAddToPlaylistInteractionListener(holder.mItem);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mSongNameView;
        public final TextView mArtistNameView;
        public final ImageView mAlbumView;
        public final ImageButton mPopupView;
        public final TextView mSongLengthView;
        public Songs mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mSongNameView = (TextView) view.findViewById(R.id.song_name);
            mArtistNameView = (TextView) view.findViewById(R.id.artist_name);
            mAlbumView = (ImageView) view.findViewById(R.id.song_icon);
            mPopupView = (ImageButton) view.findViewById(R.id.song_popup);
            mSongLengthView = (TextView) view.findViewById(R.id.song_length);

        }
    }

}
