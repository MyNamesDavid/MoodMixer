package com.example.moodmixer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.view.MotionEvent;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import com.spotify.android.appremote.api.ConnectionParams;
import com.spotify.android.appremote.api.Connector;
import com.spotify.android.appremote.api.SpotifyAppRemote;

import com.spotify.protocol.types.Image;
import com.spotify.protocol.types.ImageUri;
import com.spotify.protocol.types.Track;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.spotify.android.appremote.api.ConnectionParams;
import com.spotify.android.appremote.api.Connector;
import com.spotify.android.appremote.api.ContentApi;
import com.spotify.android.appremote.api.SpotifyAppRemote;
import com.spotify.android.appremote.api.error.AuthenticationFailedException;
import com.spotify.android.appremote.api.error.CouldNotFindSpotifyApp;
import com.spotify.android.appremote.api.error.LoggedOutException;
import com.spotify.android.appremote.api.error.NotLoggedInException;
import com.spotify.android.appremote.api.error.OfflineModeException;
import com.spotify.android.appremote.api.error.SpotifyConnectionTerminatedException;
import com.spotify.android.appremote.api.error.SpotifyDisconnectedException;
import com.spotify.android.appremote.api.error.SpotifyRemoteServiceException;
import com.spotify.android.appremote.api.error.UnsupportedFeatureVersionException;
import com.spotify.android.appremote.api.error.UserNotAuthorizedException;
import com.spotify.protocol.client.ErrorCallback;
import com.spotify.protocol.client.Subscription;
import com.spotify.protocol.types.Capabilities;
import com.spotify.protocol.types.Image;
import com.spotify.protocol.types.ListItem;
import com.spotify.protocol.types.PlaybackSpeed;
import com.spotify.protocol.types.PlayerContext;
import com.spotify.protocol.types.PlayerState;
import com.spotify.protocol.types.Repeat;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;


public class MusicPlayerFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private MusicPlayerFragment.OnFragmentInteractionListener mListener;

    private static final String CLIENT_ID = "a6d6003f62b54f1c9a3ea665f4ded656";
    private static final String REDIRECT_URI = "com.example.moodmixer://callback/";
    private SpotifyAppRemote musicPlayer; // mSpotifyAppRemote

    private static final String TAG = "MusicPlayerActivity";
    private RelativeLayout moodView;
    private ImageButton currentMoodOneImageButton;
    private ImageButton currentMoodTwoImageButton;
    private ImageButton currentMoodThreeImageButton;
    private ImageButton desiredMoodOneImageButton;
    private ImageButton desiredMoodTwoImageButton;
    private ImageButton desiredMoodThreeImageButton;
    private ImageButton playImageButton;
    private ImageButton nextSongImageButton;
    private ImageButton previousSongImageButton;
    private ImageButton weatherImageButton;
    private ImageButton userProfileImageButton;
    private ImageView albumCoverImageView;
    private ImageView backgroundView;
    private int[] albumCoverImages;
    private int songIndex = 0;
    private TabBarController tabBarController;
    MessageModel message;

    private TextView songNameTextView;
    private TextView songArtistTextView;
    private String songName;
    private String songArtist;

    // MARK: Lifecycle

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Log.d(TAG, "onCreate - start");
        message = new MessageModel(getTag(), getContext());

        View rootView = inflater.inflate(R.layout.activity_music_player, null);

        setUpPlayImageButton(rootView);
        setUpNextSongImageButton(rootView);
        setUpPreviousSongImageButton(rootView);
        setUpWeatherImageButton(rootView);
        setUpAlbumCoverCollection(rootView);
        setUpSongNameTextView(rootView);
        setUpSongArtistTextView(rootView);

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();

        String appPackageName = (getContext().getPackageName() == null) ? ("package is null") : (getContext().getPackageName());
        String message = String.format("Package Name: %s\n ", appPackageName);
        Log.d(TAG, message);

        setUpConnectionToSpotify();
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
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onStop() {
        super.onStop();

        SpotifyAppRemote.disconnect(musicPlayer);
    }

    // MARK: Setup

    /**
     * Set Up Bottom Tab Bar Navigation Item
     */

    private void setUpConnectionToSpotify() {

        if (SpotifyAppRemote.isSpotifyInstalled(getContext())) {

            ConnectionParams connectionParams =
                    new ConnectionParams.Builder(CLIENT_ID)
                            .setRedirectUri(REDIRECT_URI)
                            .showAuthView(true)
                            .build();

            SpotifyAppRemote.connect(getContext(), connectionParams,
                    new Connector.ConnectionListener() {
                        @Override
                        public void onConnected(SpotifyAppRemote spotifyAppRemote) {
                            musicPlayer = spotifyAppRemote;
                            Log.d(TAG, "Connected! Yay!");
                        }

                        public void onFailure(Throwable error) {
                            message.handleSpotifyOnFailureError(error);
                        }
                    });
        } else {
            Log.d(TAG, "Requirement: Spotify must be installed on device with a premium membership");
        }
    }

    private void setUpPlayImageButton(View rootView) {


        playImageButton = rootView.findViewById(R.id.play_imagebutton);
        playImageButton.setOnClickListener( (View v) -> {
                Log.d(TAG, "onClick: playImageButton Tapped");
                buttonEffect(playImageButton);
                onPlayPauseButtonTapped();
        });
    }

    private void setUpNextSongImageButton(View rootView) {

        nextSongImageButton = rootView.findViewById(R.id.next_song_imagebutton);
        nextSongImageButton.setOnClickListener((View v) ->  {
                Log.d(TAG, "onClick: nextSongImageButton Tapped");
                onNextSongButtonTapped();
                buttonEffect(nextSongImageButton);
        });
    }

    private void setUpPreviousSongImageButton(View rootView) {

        previousSongImageButton = rootView.findViewById(R.id.previous_song_imagebutton);
        previousSongImageButton.setOnClickListener((View v) ->  {
                Log.d(TAG, "onClick: previousSongImageButton Tapped");
                onPreviousSongButtonTapped();
                buttonEffect(previousSongImageButton);
        });
    }

    private void setUpWeatherImageButton(View rootView) {

        weatherImageButton = rootView.findViewById(R.id.weather_imagebutton);
        weatherImageButton.setOnClickListener((View v) -> {

                Log.d(TAG, "onClick: weatherImageButton Tapped");
                toastMessage("☀️Warm Sunny Day Mood Recommendation - Joyful");
                buttonEffect(weatherImageButton);
        });
    }

    private void setUpAlbumCoverCollection(View rootView) {

        albumCoverImages = new int[] {
                R.drawable.album_cover_image,
                R.drawable.zeppelin_albumcover,
                R.drawable.pinkfloyd_albumcover,
                R.drawable.beatles_albumcover
        };

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

    // MARK: Actions

    private void onPlayPauseButtonTapped() {
        // increment a progress bar

        // GUARD
        if (!SpotifyAppRemote.isSpotifyInstalled(getContext()) || musicPlayer == null || !musicPlayer.isConnected()) {
            return;
        }

        musicPlayer.getPlayerApi().getPlayerState().setResultCallback(playerState -> {

            if (playerState.isPaused) {
                musicPlayer.getPlayerApi().resume();
                playImageButton.setBackgroundResource(R.drawable.pause_button_blue);
                subscribeToPlayerState();

            } else {
                musicPlayer.getPlayerApi().pause();
                playImageButton.setBackgroundResource(R.drawable.play_button_blue);
            }
        });
    }

    private void subscribeToPlayerState() {
        // Subscribe to PlayerState
        musicPlayer.getPlayerApi()
                .subscribeToPlayerState()
                .setEventCallback(playerState -> {
                    final Track track = playerState.track;
                    if (track != null) {
                        Log.d("MainActivity", track.name + " by " + track.artist.name);
                        songName = track.name;
                        songArtist = track.artist.name;
                        songNameTextView.setText(songName);
                        songArtistTextView.setText(songArtist);

                        // Get image from track
                        musicPlayer.getImagesApi()
                                .getImage(playerState.track.imageUri, Image.Dimension.LARGE)
                                .setResultCallback(bitmap -> {
                                    albumCoverImageView.setImageBitmap(bitmap);
                                });
                    }
                });
    }

    private void onNextSongButtonTapped() {
        // cycle through albumCoverImages

        // GUARD
        if (!SpotifyAppRemote.isSpotifyInstalled(getContext()) || musicPlayer == null || !musicPlayer.isConnected()) {
            songIndex = (songIndex < albumCoverImages.length - 1) ? (songIndex + 1) : (0);
            albumCoverImageView.setBackgroundResource(albumCoverImages[songIndex]);
//            toastMessage("error - Spotify Not Connected");
            return;
        }

        musicPlayer.getPlayerApi().skipNext();
    }

    private void onPreviousSongButtonTapped() {

        // GUARD
        if (!SpotifyAppRemote.isSpotifyInstalled(getContext()) || musicPlayer == null || !musicPlayer.isConnected()) {
            songIndex = (songIndex > 0) ? (songIndex - 1) : (albumCoverImages.length - 1);
            albumCoverImageView.setBackgroundResource(albumCoverImages[songIndex]);
            //            toastMessage("error - Spotify Not Connected");
            return;
        }

        musicPlayer.getPlayerApi().skipPrevious();
    }

    private void toggleMoodViews() {

        switch (moodView.getVisibility()) {
            case View.VISIBLE:
                moodView.setVisibility(View.INVISIBLE);
                break;

            case View.INVISIBLE:
                moodView.setVisibility(View.VISIBLE);
                break;
        }
    }

    // MARK: Messages

    private void toastMessage(String message) {

        Toast toast = Toast.makeText(getContext(), message, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.TOP, 0, 120);
        toast.show();
    }

    private void logError(Throwable throwable, String msg) {
        Toast.makeText(getContext(), "Error: " + msg, Toast.LENGTH_SHORT).show();
        Log.e(TAG, msg, throwable);
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
