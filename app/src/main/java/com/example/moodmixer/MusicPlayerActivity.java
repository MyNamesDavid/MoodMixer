package com.example.moodmixer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.sql.Array;
import java.util.List;
import java.util.ArrayList;

public class MusicPlayerActivity extends AppCompatActivity {

    private static final String TAG = "MusicPlayerActivity";

    private TextView moodOneImageView;
    private TextView moodTwoImageView;
    private TextView moodThreeImageView;
    private TextView moodFourImageView;
    private TextView moodFiveImageView;
    private TextView moodSixImageView;

    private ImageButton playImageButton;
    private ImageButton nextSongImageButton;
    private ImageButton previousSongImageButton;
    private ImageButton chartsImageButton;
    private ImageButton weatherImageButton;
    private ImageView albumCoverImageView;
    private int[] albumCoverImages;
    private int songIndex = 0;

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

                        case R.id.nav_mood:
                            toggleMoodViews();
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
        setUpAlbumCoverCollection();
        setUpTabBarController();
        setUpMoodViews();
    }

    private void setUpMoodViews() {
        moodOneImageView = findViewById(R.id.moodone_imageview);
        moodTwoImageView = findViewById(R.id.moodtwo_imageview);
        moodThreeImageView = findViewById(R.id.moodthree_imageview);
        moodFourImageView = findViewById(R.id.moodfour_imageview);
        moodFiveImageView = findViewById(R.id.moodfive_imageview);
        moodSixImageView = findViewById(R.id.moodsix_imageview);

        moodOneImageView.setVisibility(View.INVISIBLE);
        moodTwoImageView.setVisibility(View.INVISIBLE);
        moodThreeImageView.setVisibility(View.INVISIBLE);
        moodFourImageView.setVisibility(View.INVISIBLE);
        moodFiveImageView.setVisibility(View.INVISIBLE);
        moodSixImageView.setVisibility(View.INVISIBLE);
    }

    private void toggleMoodViews() {

        switch (moodOneImageView.getVisibility()) {
            case View.VISIBLE:
                moodOneImageView.setVisibility(View.INVISIBLE);
                moodTwoImageView.setVisibility(View.INVISIBLE);
                moodThreeImageView.setVisibility(View.INVISIBLE);
                moodFourImageView.setVisibility(View.INVISIBLE);
                moodFiveImageView.setVisibility(View.INVISIBLE);
                moodSixImageView.setVisibility(View.INVISIBLE);
                break;
            case View.INVISIBLE:
                moodOneImageView.setVisibility(View.VISIBLE);
                moodTwoImageView.setVisibility(View.VISIBLE);
                moodThreeImageView.setVisibility(View.VISIBLE);
                moodFourImageView.setVisibility(View.VISIBLE);
                moodFiveImageView.setVisibility(View.VISIBLE);
                moodSixImageView.setVisibility(View.VISIBLE);
                break;
        }
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
                presentNextSong();
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

    private void setUpAlbumCoverCollection() {

        albumCoverImages = new int[]{
                R.drawable.album_cover_image,
                R.drawable.zeppelin_albumcover,
                R.drawable.pinkfloyd_albumcover,
                R.drawable.beatles_albumcover
        };

        albumCoverImageView = findViewById(R.id.album_cover_imageview);
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

    private void openSongsListActivity() {

        Intent intent = new Intent(this, SongsListActivity.class);
        startActivity(intent);
    }

    // MARK: Actions

    private void playSong() {
        // increment a progress bar
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

    // MARK: Messages

    private void toastMessage(String message) {

        Toast toast = Toast.makeText(MusicPlayerActivity.this, message, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.TOP, 0, 120);
        toast.show();
    }
}
