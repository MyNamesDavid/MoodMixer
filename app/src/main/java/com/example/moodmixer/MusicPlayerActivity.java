package com.example.moodmixer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

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
    private SpotifyAppRemote musicPlayer; // mSpotifyAppRemove

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

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    Fragment selectedFragment = null;

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
                            //tabBarController.openSongsListActivity();
                            //selectedFragment = new SongListFragment();

                            break;

                        case R.id.nav_library:
                            tabBarController.openLibraryActivity();
                            break;

                    }

                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            selectedFragment).commit();
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

                        // Now you can start interacting with App Remote
                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        Log.e(TAG, throwable.getMessage(), throwable);

                        // Something went wrong when attempting to connect! Handle errors here
                    }
                });
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
                presentNextSong();
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
                presentPreviousSong();
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

        musicPlayer.getPlayerApi().getPlayerState().setResultCallback(playerState -> {

            if (playerState.isPaused) {
//                musicPlayer.getPlayerApi().resume();
                musicPlayer.getPlayerApi().play("spotify:playlist:37i9dQZF1DX2sUQwD7tbmL");

            } else {
                musicPlayer.getPlayerApi().pause();
            }
        });

        // Subscribe to PlayerState
        musicPlayer.getPlayerApi()
                .subscribeToPlayerState()
                .setEventCallback(playerState -> {
                    final Track track = playerState.track;
                    if (track != null) {
                        Log.d("MainActivity", track.name + " by " + track.artist.name);
                    }
                });
    }

    private void pauseSong() {

        musicPlayer.getPlayerApi().pause();
    }

    private void presentNextSong() {
        // cycle through albumCoverImages

        songIndex = (songIndex < albumCoverImages.length - 1) ? (songIndex + 1) : (0);

        albumCoverImageView.setBackgroundResource(albumCoverImages[songIndex]);
    }

    private void presentPreviousSong() {
        // cycle through albumCoverImages

        // reset current song on single tap
        // play previous song on double tap

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
