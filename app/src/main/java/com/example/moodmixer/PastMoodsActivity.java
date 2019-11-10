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

                            //selectedFragment = new PlaylistActivity();
                            break;
                        case R.id.nav_songlist:

                            //selectedFragment = new SongsListActivity();
                            break;
                    }

                    //getSupportFragmentManager;().beginTransaction().replace(R.id.fragment_container,
                    //      selectedFragment).commit();
                    //Want to select clicked item
                    return true;
                }
            };

    private void openMusicPlayerActivity() {

//        Intent intent = new Intent(this, MusicPlayerActivity.class);
//        startActivity(intent);
    }


}
