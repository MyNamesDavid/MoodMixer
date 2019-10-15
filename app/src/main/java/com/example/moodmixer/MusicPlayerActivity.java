package com.example.moodmixer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;
import java.util.ArrayList;

public class MusicPlayerActivity extends AppCompatActivity {

    private ImageButton playImageButton;
    private ImageButton nextSongImageButton;
    private ImageButton previousSongImageButton;
    private ImageButton chartsImageButton;
    private List<ImageView> albumCoverImages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_music_player);

        // Setup Buttons
        playImageButton = findViewById(R.id.play_imagebutton);
        nextSongImageButton = findViewById(R.id.next_song_imagebutton);
        previousSongImageButton = findViewById(R.id.previous_song_imagebutton);
        chartsImageButton = findViewById(R.id.charts_imagebutton);

        // Create A collection of album covers to cycle through
        albumCoverImages = new ArrayList<ImageView>();

        // Setup Tab Bar
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setSelectedItemId(R.id.nav_music_viewer);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
    }

    public void playSongButtonTapped() {
        // increment a progress bar
    }

    public void nextSongButtonTapped() {
        // cycle through albumCoverImages
    }

    public void previousSongButtonTapped() {
        // cycle through albumCoverImages

        // reset current song on single tap
        // play previous song on double tap
    }

    public void chartsButtonTapped() {

    }

    // MARK: Tab Bar

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
                    }

                    //getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    //      selectedFragment).commit();
                    //Want to select clicked item
                    return true;
                }
            };

    public void openPlaylistActivity() {
        Intent intent = new Intent(this, PlaylistActivity.class);
        startActivity(intent);
    }

    public void openMusicPlayerActivity() {
        Intent intent = new Intent(this, MusicPlayerActivity.class);
        startActivity(intent);
    }

    public void openSongsListActivity() {
        Intent intent = new Intent(this, SongsListActivity.class);
        startActivity(intent);
    }
}
