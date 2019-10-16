package com.example.moodmixer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class SongsListActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_songs_list);

        //Code for bottom navigation view
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setSelectedItemId(R.id.nav_songlist);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        //listview
        populateSongsList();
    }



    private  BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    //Fragment selectedFragment = null;

                    switch (menuItem.getItemId()){
                        case R.id.nav_music_viewer:
                            //selectedFragment = new MusicPlayerActivity();
                            openMusicPlayerActivity();
                            break;
                        case R.id.nav_playlist:
                            openPlaylistActivity();
                            //selectedFragment = new PlaylistActivity();
                            break;
                        case R.id.nav_songlist:

                            //selectedFragment = new SongsListActivity();
                            break;
                        case R.id.nav_mood:
                            openUserProfileActivity();
                            break;
                    }

                    //getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    //      selectedFragment).commit();
                    //Want to select clicked item
                    return true;
                }
            };

    private void openUserProfileActivity() {

        Intent intent = new Intent(this, UserProfileActivity.class);
        startActivity(intent);
    }
    public void openPlaylistActivity(){
        Intent intent = new Intent(this, PlaylistActivity.class);
        startActivity(intent);
    }
    public void openMusicPlayerActivity(){
        Intent intent = new Intent(this, MusicPlayerActivity.class);
        startActivity(intent);
    }

    private void populateSongsList(){
        //Construct data source
        ArrayList<Songs> songsArray = Songs.getSongs();
        //Create adapter
        SongListAdapter songAdapter = new SongListAdapter(this,songsArray);
        //attatch Data to listView
        ListView songListView = (ListView) findViewById(R.id.list_of_songs);
        songListView.setAdapter(songAdapter);

    }

}
