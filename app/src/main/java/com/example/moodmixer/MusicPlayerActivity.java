package com.example.moodmixer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;
import java.util.ArrayList;

public class MusicPlayerActivity extends AppCompatActivity {

    private static final String TAG = "MusicPlayerActivity";

    private ImageButton playImageButton;
    private ImageButton nextSongImageButton;
    private ImageButton previousSongImageButton;
    private ImageButton chartsImageButton;
    private ImageButton weatherImageButton;
    private List<ImageView> albumCoverImages;

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    //Fragment selectedFragment = null;

                    switch (menuItem.getItemId()) {
                        case R.id.nav_music_viewer:
                            //selectedFragment = new MusicPlayerActivity();
                            break;
                        case R.id.nav_playlist:
                            openPlaylistActivity();
                            //selectedFragment = new PlaylistActivity();
                            break;
                        case R.id.nav_songlist:
                            openSongsListActivity();
                            //selectedFragment = new SongsListActivity();
                            break;
                        case R.id.nav_library:
                            openLibraryActivity();
                            break;
                    }

                    //getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    //      selectedFragment).commit();
                    //Want to select clicked item
                    return true;
                }
            };

    // MARK: Lifecycle

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(TAG, "onCreate - start");

        setContentView(R.layout.activity_music_player);

        setUpPlayImageButton();
        setUpNextSongImageButton();
        setUpPreviousSongImageButton();
        setUpChartsImageButton();
        setUpWeatherImageButton();
        setUpAlbumCoverImagesCollection();
        setUpTabBarController();
    }

    // MARK: Setup

    /**
     * Set Up Bottom Tab Bar Navigation Item
     */
    private void setUpTabBarController() {
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setSelectedItemId(R.id.nav_music_viewer);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
    }

    private void setUpPlayImageButton() {

        playImageButton = findViewById(R.id.play_imagebutton);
        playImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: playImageButton Tapped");
            }
        });
    }

    private void setUpNextSongImageButton() {

        nextSongImageButton = findViewById(R.id.next_song_imagebutton);
        nextSongImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: nextSongImageButton Tapped");
            }
        });
    }

    private void setUpPreviousSongImageButton() {

        previousSongImageButton = findViewById(R.id.previous_song_imagebutton);
        previousSongImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: previousSongImageButton Tapped");
            }
        });
    }

    private void setUpChartsImageButton() {

        chartsImageButton = findViewById(R.id.charts_imagebutton);
        chartsImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: chartsImageButton Tapped");
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
            }
        });
    }

    /**
     * Create A collection of album covers to cycle through
     */
    private void setUpAlbumCoverImagesCollection() {

        albumCoverImages = new ArrayList<ImageView>();

    }

    // MARK: Navigation

    private void openPlaylistActivity() {
        Intent intent = new Intent(this, PlaylistActivity.class);
        startActivity(intent);
    }

    private void openMusicPlayerActivity() {
        Intent intent = new Intent(this, MusicPlayerActivity.class);
        startActivity(intent);
    }
    public void openLibraryActivity() {
        Intent intent = new Intent(this, LibraryActivity.class);
        startActivity(intent);
    }

    private void openSongsListActivity() {
        Intent intent = new Intent(this, SongsListActivity.class);
        startActivity(intent);
    }

    // MARK: Actions

    private void playSongButtonTapped() {
        // increment a progress bar
    }

    private void nextSongButtonTapped() {
        // cycle through albumCoverImages
    }

    private void previousSongButtonTapped() {
        // cycle through albumCoverImages

        // reset current song on single tap
        // play previous song on double tap
    }

    private void chartsButtonTapped() {

    }

    // MARK: Messages

    private void toastMessage(String message) {

        Toast toast = Toast.makeText(MusicPlayerActivity.this, message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.TOP,0,120);
        toast.show();
    }
}
