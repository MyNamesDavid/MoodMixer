package com.example.moodmixer;

import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.example.moodmixer.PlaylistFragment.OnPlaylistFragmentInteractionListener;
import com.example.moodmixer.dummy.DummyContent.Songs;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Playlists} and makes a call to the
 * specified {@link PlaylistFragment.OnPlaylistFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyPlaylistRecyclerViewAdapter extends RecyclerView.Adapter<MyPlaylistRecyclerViewAdapter.ViewHolder> {

    private final List<Playlists> mValues;
    private final PlaylistFragment.OnPlaylistFragmentInteractionListener mListener;
    private Context mCtx;

    public MyPlaylistRecyclerViewAdapter(List<Playlists> items, PlaylistFragment.OnPlaylistFragmentInteractionListener listener, Context mCtx) {
        mValues = items;
        mListener = listener;
        this.mCtx = mCtx;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_playlist, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        //holder.mIdView.setText(mValues.get(position).id);
        holder.mPlaylistNameView.setText(mValues.get(position).playlistName);


        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onPlaylistFragmentInteraction(holder.mItem);
                }
            }
        });

        holder.mPopupView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //creating a popup menu
                PopupMenu popup = new PopupMenu(mCtx, holder.mPopupView);
                //inflating menu from xml resource
                popup.inflate(R.menu.playlist_option_menu);
                //adding click listener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.option_song_play_playlist:
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
        public final TextView mPlaylistNameView;
        public final ImageButton mPopupView;
        public Playlists mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mPlaylistNameView = (TextView) view.findViewById(R.id.playlist_name);
            mPopupView = (ImageButton) view.findViewById(R.id.playlist_popup);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mPlaylistNameView.getText() + "'";
        }
    }
}
