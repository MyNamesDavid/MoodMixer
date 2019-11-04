package com.example.moodmixer;

import android.content.Intent;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class PastMoodsActivity extends AppCompatActivity {
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

                    //getSupportFragmentManager;().beginTransaction().replace(R.id.fragment_container,
                    //      selectedFragment).commit();
                    //Want to select clicked item
                    return true;
                }
            };
    private void openPlaylistActivity() {

        Intent intent = new Intent(this, PlaylistActivity.class);
        startActivity(intent);
    }

    private void openMusicPlayerActivity() {

//        Intent intent = new Intent(this, MusicPlayerActivity.class);
//        startActivity(intent);
    }

    private void openSongsListActivity() {

        Intent intent = new Intent(this, SongsListActivity.class);
        startActivity(intent);
    }
}
