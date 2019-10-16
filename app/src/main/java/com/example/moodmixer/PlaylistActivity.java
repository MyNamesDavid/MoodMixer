package com.example.moodmixer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class PlaylistActivity extends AppCompatActivity {

    FloatingActionButton floatingActionButtonPlaylist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist);

        //Code for bottom navigation view
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setSelectedItemId(R.id.nav_playlist);
        bottomNav.setOnNavigationItemSelectedListener(navListener);


        //Code for populating the playlist
        populatePlaylist();

        //Code for Floating action button
        floatingActionButtonPlaylist =
                (FloatingActionButton) findViewById(R.id.floating_action_button_playlist);
        floatingActionButtonPlaylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Handles the click

            }
        });

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
                            //selectedFragment = new PlaylistActivity();
                            break;
                        case R.id.nav_songlist:
                            openSongsListActivity();
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

    public void openMusicPlayerActivity(){
        Intent intent = new Intent(this, MusicPlayerActivity.class);
        startActivity(intent);
    }
    public void openSongsListActivity(){
        Intent intent = new Intent(this, SongsListActivity.class);
        startActivity(intent);
    }

    private void populatePlaylist(){
        //Construct data source
        ArrayList<Playlists> playlistsArray = Playlists.getPlaylists();
        //Create adapter
        PlaylistAdapter playlistAdapter = new PlaylistAdapter(this,playlistsArray);
        //attatch Data to listView
        ListView playlistListView = (ListView) findViewById(R.id.list_of_playlists);
        playlistListView.setAdapter(playlistAdapter);

    }
}
