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
    private ImageButton chartsImageButton;
    private ImageButton weatherImageButton;
    private ImageButton userProfileImageButton;
    private ImageView albumCoverImageView;
    private ImageView backgroundView;
    private int[] albumCoverImages;
    private int songIndex = 0;
    private TabBarController tabBarController;

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

        View rootView = inflater.inflate(R.layout.activity_music_player, null);

        setUpPlayImageButton(rootView);
        setUpNextSongImageButton(rootView);
        setUpPreviousSongImageButton(rootView);
        setUpChartsImageButton(rootView);
        setUpWeatherImageButton(rootView);
        setUpAlbumCoverCollection(rootView);
        setUpMoodViews(rootView);
        setUserProfileImageButton(rootView);
        setUpSongNameTextView(rootView);
        setUpSongArtistTextView(rootView);

        backgroundView = rootView.findViewById(R.id.background_blueblackgradient);


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
                            if (error instanceof SpotifyRemoteServiceException) {
                                if (error.getCause() instanceof SecurityException) {
                                    logError(error, "SecurityException");
                                } else if (error.getCause() instanceof IllegalStateException) {
                                    logError(error, "IllegalStateException");
                                }
                            } else if (error instanceof NotLoggedInException) {
                                logError(error, "NotLoggedInException");
                            } else if (error instanceof AuthenticationFailedException) {
                                logError(error, "AuthenticationFailedException");
                            } else if (error instanceof CouldNotFindSpotifyApp) {
                                logError(error, "CouldNotFindSpotifyApp");
                            } else if (error instanceof LoggedOutException) {
                                logError(error, "LoggedOutException");
                            } else if (error instanceof OfflineModeException) {
                                logError(error, "OfflineModeException");
                            } else if (error instanceof UserNotAuthorizedException) {
                                logError(error, "UserNotAuthorizedException");
                            } else if (error instanceof UnsupportedFeatureVersionException) {
                                logError(error, "UnsupportedFeatureVersionException");
                            } else if (error instanceof SpotifyDisconnectedException) {
                                logError(error, "SpotifyDisconnectedException");
                            } else if (error instanceof SpotifyConnectionTerminatedException) {
                                logError(error, "SpotifyConnectionTerminatedException");
                            } else {
                                logError(error, String.format("Connection failed: %s", error));
                            }
                        }
                    });
        } else {
            Log.d(TAG, "Requirement: Spotify must be installed on device with a premium membership");
        }
    }

    private void setUpPlayImageButton(View rootView) {


        playImageButton = rootView.findViewById(R.id.play_imagebutton);
        playImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: playImageButton Tapped");
                buttonEffect(playImageButton);
                onPlayPauseButtonTapped();
            }
        });
    }

    private void setUpNextSongImageButton(View rootView) {

        nextSongImageButton = rootView.findViewById(R.id.next_song_imagebutton);
        nextSongImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: nextSongImageButton Tapped");
                onNextSongButtonTapped();
                buttonEffect(nextSongImageButton);
            }
        });
    }

    private void setUpPreviousSongImageButton(View rootView) {

        previousSongImageButton = rootView.findViewById(R.id.previous_song_imagebutton);
        previousSongImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: previousSongImageButton Tapped");
                onPreviousSongButtonTapped();
                buttonEffect(previousSongImageButton);
            }
        });
    }

    private void setUpChartsImageButton(View rootView) {

        chartsImageButton = rootView.findViewById(R.id.charts_imagebutton);
        chartsImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: chartsImageButton Tapped");
                toastMessage("📊 \nBPM: 150\n Fun Fact - fast-tempo songs are directly associated with more energy, movement, and dancing, typically linked to being in a joyful state.");
                buttonEffect(chartsImageButton);
            }
        });
    }

    private void setUpWeatherImageButton(View rootView) {

        weatherImageButton = rootView.findViewById(R.id.weather_imagebutton);
        weatherImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: weatherImageButton Tapped");
                toastMessage("☀️Warm Sunny Day Mood Recommendation - Joyful");
                buttonEffect(weatherImageButton);
            }
        });
    }

    private void setUpAlbumCoverCollection(View rootView) {

        albumCoverImages = new int[]{
                R.drawable.album_cover_image,
                R.drawable.zeppelin_albumcover,
                R.drawable.pinkfloyd_albumcover,
                R.drawable.beatles_albumcover
        };

        albumCoverImageView = rootView.findViewById(R.id.album_cover_imageview);
    }

    private void setUserProfileImageButton(View rootView) {

        userProfileImageButton = rootView.findViewById(R.id.userprofile_imagebutton);
        userProfileImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: userProfileImageButton Tapped");
                tabBarController.openUserProfileActivity();
            }
        });
    }

    private void setUpMoodViews(View rootView) {

        moodView = rootView.findViewById(R.id.moodmixer_playlist_options);
        moodView.setVisibility(View.INVISIBLE);
    }

    private void setUpSongNameTextView(View rootView) {
        songNameTextView = rootView.findViewById(R.id.songname_textview);
        songNameTextView.setVisibility(View.VISIBLE);
    }

    private void setUpSongArtistTextView(View rootView) {
        songArtistTextView = rootView.findViewById(R.id.songartist_textview);
        songArtistTextView.setVisibility(View.VISIBLE);
    }

    // MARK: Actions

    private void onPlayPauseButtonTapped() {
        // increment a progress bar

        // GUARD
        if (!SpotifyAppRemote.isSpotifyInstalled(getContext()) || musicPlayer == null || !musicPlayer.isConnected()) {
            return;
        }

//        musicPlayer.getPlayerApi().play("spotify:playlist:37i9dQZF1DX2sUQwD7tbmL");

        musicPlayer.getPlayerApi().getPlayerState().setResultCallback(playerState -> {

            if (playerState.isPaused) {
                musicPlayer.getPlayerApi().resume();
                playImageButton.setBackgroundResource(R.drawable.pause_button_clouds);
                subscribeToPlayerState();

            } else {
                musicPlayer.getPlayerApi().pause();
                playImageButton.setBackgroundResource(R.drawable.play_button_clouds);
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

    private void presentChartsActivity() {

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
