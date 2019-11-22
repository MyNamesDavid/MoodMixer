package com.example.moodmixer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.MotionEvent;
import java.beans.*;
import java.util.prefs.Preferences;

public class MusicPlayerFragment extends Fragment {

    public static final String TAG = "MusicPlayerFragment";
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private MusicPlayerFragment.OnFragmentInteractionListener mListener;

    private ImageButton playImageButton;
    private ImageButton nextSongImageButton;
    private ImageButton previousSongImageButton;
    private ImageButton weatherImageButton;
    private ImageView albumCoverImageView;
    MessageModel message;

    private SpotifyModel spotify;
    private TextView songNameTextView;
    private TextView songArtistTextView;
    private String songName;
    private String songArtist;

    private Preferences preferences;
    public PropertyChangeListener listener;
    Animation fadeOutAnimation;
    Animation fadeInAnimation;

    // MARK: Lifecycle

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public MusicPlayerFragment() {
        // Required empty public constructor
    }

    public static MusicPlayerFragment newInstance(String param1, String param2) {
        MusicPlayerFragment fragment = new MusicPlayerFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MusicPlayerFragment.OnFragmentInteractionListener) {
            mListener = (MusicPlayerFragment.OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Log.d(TAG, "onCreate - start");
        preferences = Preferences.userNodeForPackage(this.getClass());
        spotify = new SpotifyModel(MusicPlayerFragment.TAG, getContext());
        message = new MessageModel(getTag(), getContext());
        fadeOutAnimation = AnimationUtils.loadAnimation(this.getContext(), R.anim.fadeout);
        fadeInAnimation = AnimationUtils.loadAnimation(this.getContext(), R.anim.fadein);

        View rootView = inflater.inflate(R.layout.activity_music_player, null);
        setUpPlayImageButton(rootView);
        setUpNextSongImageButton(rootView);
        setUpPreviousSongImageButton(rootView);
        setUpWeatherImageButton(rootView);
        setUpAlbumCoverCollection(rootView);
        setUpSongNameTextView(rootView);
        setUpSongArtistTextView(rootView);

        loadLastPlayedMusicResources();
        handleSpotifyPropertyChanges();

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();

        String appPackageName = (getContext().getPackageName() == null) ? ("package is null") : (getContext().getPackageName());
        String message = String.format("Package Name: %s\n ", appPackageName);
        Log.d(TAG, message);

        spotify.setUpConnectionToSpotify();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onStop() {
        super.onStop();

        spotify.disconnect();
    }

    // MARK: Actions

    private void onPreviousSongButtonTapped() {

        if (spotify.isConnected()) {
            albumCoverImageView.startAnimation(fadeOutAnimation);
            spotify.previousSong();
            albumCoverImageView.startAnimation(fadeInAnimation);
        }
    }

    private void onNextSongButtonTapped() {

        if (spotify.isConnected()) {
            albumCoverImageView.startAnimation(fadeOutAnimation);
            spotify.nextSong();
            albumCoverImageView.startAnimation(fadeInAnimation);
        }
    }

    private void onPlayPauseButtonTapped() {

        if (spotify.isConnected()) {

            if (spotify.isPaused)
                spotify.resume();

            else
                spotify.pause();
        }
    }

    // MARK: Setup

    private void setUpPlayImageButton(View rootView) {

        playImageButton = rootView.findViewById(R.id.play_imagebutton);
        playImageButton.setOnClickListener((View v) -> {
            Log.d(TAG, "onClick: playImageButton Tapped");
            buttonEffect(playImageButton);
            onPlayPauseButtonTapped();
        });
    }

    private void setUpNextSongImageButton(View rootView) {

        nextSongImageButton = rootView.findViewById(R.id.next_song_imagebutton);
        nextSongImageButton.setOnClickListener((View v) -> {
            Log.d(TAG, "onClick: nextSongImageButton Tapped");
            onNextSongButtonTapped();
            buttonEffect(nextSongImageButton);
        });
    }

    private void setUpPreviousSongImageButton(View rootView) {

        previousSongImageButton = rootView.findViewById(R.id.previous_song_imagebutton);
        previousSongImageButton.setOnClickListener((View v) -> {
            Log.d(TAG, "onClick: previousSongImageButton Tapped");
            onPreviousSongButtonTapped();
            buttonEffect(previousSongImageButton);
        });
    }

    private void setUpWeatherImageButton(View rootView) {

        weatherImageButton = rootView.findViewById(R.id.weather_imagebutton);
        weatherImageButton.setOnClickListener((View v) -> {

            Log.d(TAG, "onClick: weatherImageButton Tapped");
            message.toast("☀️Warm Sunny Day Mood Recommendation - Joyful");
            buttonEffect(weatherImageButton);
        });
    }

    private void setUpAlbumCoverCollection(View rootView) {
        albumCoverImageView = rootView.findViewById(R.id.album_cover_imageview);
    }

    private void setUpSongNameTextView(View rootView) {
        songNameTextView = rootView.findViewById(R.id.song_name_textview);
        songNameTextView.setVisibility(View.VISIBLE);
    }

    private void setUpSongArtistTextView(View rootView) {
        songArtistTextView = rootView.findViewById(R.id.song_artist_textview);
        songArtistTextView.setVisibility(View.VISIBLE);
    }

    // MARK: Helpers

    private void handleSpotifyPropertyChanges() {

        listener = new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent event) {
                if (event.getPropertyName().equals(SpotifyProps.SongArtist.toString())) {
                    songArtist = event.getNewValue().toString();
                    songArtistTextView.setText(songArtist);
                    preferences.put("LastPlayedSongArtistName", songArtist);
                }

                if (event.getPropertyName().equals(SpotifyProps.SongName.toString())) {
                    songName = event.getNewValue().toString();
                    songNameTextView.setText(songName);
                    preferences.put("LastPlayedSongName", songName);
                }

                if (event.getPropertyName().equals(SpotifyProps.AlbumCover.toString())) {
                    albumCoverImageView.setImageBitmap((Bitmap) event.getNewValue());
                }

                if (event.getPropertyName().equals(SpotifyProps.IsPaused.toString())) {
                    Boolean isPaused = (Boolean) event.getNewValue();
                    if (isPaused) {
                        playImageButton.setBackgroundResource(R.drawable.play_button_blue);
                    } else {
                        playImageButton.setBackgroundResource(R.drawable.pause_button_blue);
                    }
                }
            }
        };

        spotify.propertyChange.addPropertyChangeListener(listener); //The Support class binds the property change listener to our Object
    }

    private void loadLastPlayedMusicResources() {
        songArtist = preferences.get("LastPlayedSongArtistName", ""); // "a string"
        songName = preferences.get("LastPlayedSongName", ""); // "a string"
    }

    // MARK: Static Functions

    public static void buttonEffect(View button) {
        button.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        v.getBackground().setColorFilter(0xe0f47521, PorterDuff.Mode.SRC_ATOP);
                        v.invalidate();
                        break;
                    }
                    case MotionEvent.ACTION_UP: {
                        v.getBackground().clearColorFilter();
                        v.invalidate();
                        break;
                    }
                }
                return false;
            }
        });
    }
}
