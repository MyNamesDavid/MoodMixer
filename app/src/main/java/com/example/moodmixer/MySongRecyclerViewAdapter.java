package com.example.moodmixer;

import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.moodmixer.SongFragment.OnSongListFragmentInteractionListener;


import java.util.ArrayList;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Songs} and makes a call to the
 * specified {@link OnSongListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MySongRecyclerViewAdapter extends RecyclerView.Adapter<MySongRecyclerViewAdapter.ViewHolder> {

    private Context mCtx;

    private final List<Songs> mValues;
    private final OnSongListFragmentInteractionListener mSongListener;


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

                //creating a popup menu
                PopupMenu popup = new PopupMenu(mCtx, holder.mAlbumView);
                //inflating menu from xml resource
                popup.inflate(R.menu.song_option_menu);
                //adding click listener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.option_add_to_playlist:
                                return true;
                            case R.id.option_play_next:
                                return true;
                            case R.id.option_play_previous:
                                return true;
                            case R.id.option_song_play:
                                return true;
                            default:
                                return false;
                        }
                    }
                });
                //displaying the popup
                popup.show();

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
        public Songs mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mSongNameView = (TextView) view.findViewById(R.id.song_name);
            mArtistNameView = (TextView) view.findViewById(R.id.artist_name);
            mAlbumView = (ImageView) view.findViewById(R.id.song_icon);
            mPopupView = (ImageButton) view.findViewById(R.id.song_popup);

        }

        @Override
        public String toString() {
            return super.toString() + " '" + mSongNameView.getText() + "'";
        }
    }


}
