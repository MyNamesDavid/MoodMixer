package com.example.moodmixer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.view.MotionEvent;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import com.spotify.android.appremote.api.ConnectionParams;
import com.spotify.android.appremote.api.Connector;
import com.spotify.android.appremote.api.SpotifyAppRemote;

import com.spotify.protocol.types.Track;

public class MusicPlayerActivity extends AppCompatActivity {

    private static final String CLIENT_ID = "a6d6003f62b54f1c9a3ea665f4ded656";
    private static final String REDIRECT_URI = "https://elliottdiaz1.wixsite.com/moodmixer";
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
    private int[] albumCoverImages;
    private int songIndex = 0;
    private TabBarController tabBarController;
    private String trackName;
    private String trackArtist;

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    //Fragment selectedFragment = null;

                    switch (menuItem.getItemId()) {

                        case R.id.nav_mood:
                            toggleMoodViews();
                            break;

                        case R.id.nav_playlist:
                            tabBarController.openPlaylistActivity();
                            break;

                        case R.id.nav_music_viewer:
                            break; // Current View - do nothing

                        case R.id.nav_songlist:
                            tabBarController.openSongsListActivity();
                            break;

                        case R.id.nav_library:
                            tabBarController.openLibraryActivity();
                            break;

                    }
                    return true;
                }
            };

    // MARK: Lifecycle

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(TAG, "onCreate - start");

        setContentView(R.layout.activity_music_player);
        setUpTabBarController();
        setUpPlayImageButton();
        setUpNextSongImageButton();
        setUpPreviousSongImageButton();
        setUpChartsImageButton();
        setUpWeatherImageButton();
        setUpAlbumCoverCollection();
        setUpMoodViews();
        setUserProfileImageButton();
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (SpotifyAppRemote.isSpotifyInstalled(this)) {

            ConnectionParams connectionParams =
                    new ConnectionParams.Builder(CLIENT_ID)
                            .setRedirectUri(REDIRECT_URI)
                            .showAuthView(true)
                            .build();

            SpotifyAppRemote.connect(this, connectionParams,
                    new Connector.ConnectionListener() {

                        @Override
                        public void onConnected(SpotifyAppRemote spotifyAppRemote) {
                            musicPlayer = spotifyAppRemote;
                            Log.d(TAG, "Connected! Yay!");

                        }

                        @Override
                        public void onFailure(Throwable throwable) {
                            Log.e(TAG, throwable.getMessage(), throwable);
                            // Default to Local Library If app cannot connect to spotify
                        }
                    });
        } else {
            Log.d(TAG, "Requirement: Spotify must be installed on device with a premium membership");
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        SpotifyAppRemote.disconnect(musicPlayer);
    }

    // MARK: Setup

    /**
     * Set Up Bottom Tab Bar Navigation Item
     */

    private void setUpTabBarController() {

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setSelectedItemId(R.id.nav_music_viewer);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        tabBarController = new TabBarController(this);
    }

    private void setUpPlayImageButton() {

        playImageButton = findViewById(R.id.play_imagebutton);
        playImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: playImageButton Tapped");
                buttonEffect(playImageButton);
                onPlayPauseButtonTapped();
            }
        });
    }

    private void setUpNextSongImageButton() {

        nextSongImageButton = findViewById(R.id.next_song_imagebutton);
        nextSongImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: nextSongImageButton Tapped");
                onNextSongButtonTapped();
                buttonEffect(nextSongImageButton);
            }
        });
    }

    private void setUpPreviousSongImageButton() {

        previousSongImageButton = findViewById(R.id.previous_song_imagebutton);
        previousSongImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: previousSongImageButton Tapped");
                onPreviousSongButtonTapped();
                buttonEffect(previousSongImageButton);
            }
        });
    }

    private void setUpChartsImageButton() {

        chartsImageButton = findViewById(R.id.charts_imagebutton);
        chartsImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: chartsImageButton Tapped");
                toastMessage("📊 \nBPM: 150\n Fun Fact - fast-tempo songs are directly associated with more energy, movement, and dancing, typically linked to being in a joyful state.");
                buttonEffect(chartsImageButton);
            }
        });
    }

    private void setUpWeatherImageButton() {

        weatherImageButton = findViewById(R.id.weather_imagebutton);
        weatherImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: weatherImageButton Tapped");
                toastMessage("☀️Warm Sunny Day Mood Recommendation - Joyful");
                buttonEffect(weatherImageButton);
            }
        });
    }

    private void setUpAlbumCoverCollection() {

        albumCoverImages = new int[]{
                R.drawable.album_cover_image,
                R.drawable.zeppelin_albumcover,
                R.drawable.pinkfloyd_albumcover,
                R.drawable.beatles_albumcover
        };

        albumCoverImageView = findViewById(R.id.album_cover_imageview);
    }

    private void setUserProfileImageButton() {

        userProfileImageButton = findViewById(R.id.userprofile_imagebutton);
        userProfileImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: userProfileImageButton Tapped");
                tabBarController.openUserProfileActivity();
            }
        });
    }

    private void setUpMoodViews() {

        moodView = findViewById(R.id.moodmixer_playlist_options);
        moodView.setVisibility(View.INVISIBLE);
    }

    // MARK: Actions

    private void onPlayPauseButtonTapped() {
        // increment a progress bar

        // GUARD
        if (!SpotifyAppRemote.isSpotifyInstalled(this) || musicPlayer == null || !musicPlayer.isConnected()) {
            return;
        }


        musicPlayer.getPlayerApi().getPlayerState().setResultCallback(playerState -> {
            if (playerState.isPaused) {
//                musicPlayer.getPlayerApi().resume();
                musicPlayer.getPlayerApi().play("spotify:playlist:37i9dQZF1DX2sUQwD7tbmL");
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
                        trackName = track.name;
                        trackArtist = track.artist.name;
                    }
                });
    }

    private void pauseSong() {

        musicPlayer.getPlayerApi().pause();
    }

    private void onNextSongButtonTapped() {
        // cycle through albumCoverImages

        // GUARD
        if (!SpotifyAppRemote.isSpotifyInstalled(this) || musicPlayer == null || !musicPlayer.isConnected()) {
            return;
        }

        musicPlayer.getPlayerApi().skipNext();

        songIndex = (songIndex < albumCoverImages.length - 1) ? (songIndex + 1) : (0);

        albumCoverImageView.setBackgroundResource(albumCoverImages[songIndex]);
    }

    private void onPreviousSongButtonTapped() {

        // reset current song on single tap
        // play previous song on double tap

        // GUARD
        if (!SpotifyAppRemote.isSpotifyInstalled(this) || musicPlayer == null || !musicPlayer.isConnected()) {
            return;
        }

        musicPlayer.getPlayerApi().skipPrevious();

        songIndex = (songIndex > 0) ? (songIndex - 1) : (albumCoverImages.length - 1);

        albumCoverImageView.setBackgroundResource(albumCoverImages[songIndex]);
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

        Toast toast = Toast.makeText(MusicPlayerActivity.this, message, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.TOP, 0, 120);
        toast.show();
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
