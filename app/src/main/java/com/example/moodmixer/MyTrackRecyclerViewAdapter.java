package com.example.moodmixer;

import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.moodmixer.TrackFragment.OnTrackListFragmentInteractionListener;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Songs} and makes a call to the
 * specified {@link OnTrackListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyTrackRecyclerViewAdapter extends RecyclerView.Adapter<MyTrackRecyclerViewAdapter.ViewHolder> {

    private Context mCtx;

    private final List<Songs> mValues;
    private final OnTrackListFragmentInteractionListener mListener;

    public MyTrackRecyclerViewAdapter(List<Songs> items, OnTrackListFragmentInteractionListener listener, Context mCtx) {
        mValues = items;
        mListener = listener;
        this.mCtx = mCtx;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_tracks, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mSongNameView.setText(mValues.get(position).songName);
        holder.mArtistNameView.setText(mValues.get(position).songArtistName);
        holder.mSongLengthView.setText(mValues.get(position).songLength);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onTrackListFragmentInteraction(holder.mItem);
                }
            }
        });

        holder.mPopupView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.OnTrackListFragmentAddToPlaylistInteractionListener(holder.mItem);
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
