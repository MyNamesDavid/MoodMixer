package com.example.moodmixer;

import android.view.MenuItem;

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
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class UserProfileActivity extends AppCompatActivity {

    private static final String TAG = "MusicPlayerActivity";
    private ImageButton weatherImageButton;
    private ImageButton moodImageButton;

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
                            openUserProfileActivity();
                            break;
                    }

                    //getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    //      selectedFragment).commit();
                    //Want to select clicked item
                    return true;
                }
            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(TAG, "onCreate - start");

        setContentView(R.layout.activity_user_profile);

        setUpWeatherImageButton();

        setUpMoodImageButton();

    }

    private void openPastMoodActivity() {

        Intent intent = new Intent(this, PastMoodsActivity.class);
        startActivity(intent);
    }

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

    private void setUpMoodImageButton() {

        weatherImageButton = findViewById(R.id.mood_imagebutton);
        weatherImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: weatherImageButton Tapped");
                openPastMoodActivity();

            }
        });
    }



    private void openUserProfileActivity() {

        Intent intent = new Intent(this, UserProfileActivity.class);
        startActivity(intent);
    }
    private void setUpTabBarController() {

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setSelectedItemId(R.id.nav_music_viewer);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
    }

    private void toastMessage(String message) {

        Toast toast = Toast.makeText(UserProfileActivity.this, message, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.TOP, 0, 120);
        toast.show();
    }
}
