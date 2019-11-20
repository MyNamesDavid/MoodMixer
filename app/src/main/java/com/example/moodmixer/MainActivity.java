package com.example.moodmixer;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import android.view.Menu;
import android.view.MenuItem;

import android.widget.ImageView;


import com.example.moodmixer.dummy.DummyContent;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import com.spotify.android.appremote.api.ConnectionParams;
import com.spotify.android.appremote.api.Connector;
import com.spotify.android.appremote.api.SpotifyAppRemote;
import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;


import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.widget.Toolbar;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import java.util.ArrayList;
import java.util.List;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyCallback;
import kaaes.spotify.webapi.android.SpotifyError;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Pager;
import kaaes.spotify.webapi.android.models.SavedTrack;
import kaaes.spotify.webapi.android.models.TrackSimple;
import retrofit.client.Response;

public class MainActivity
        extends AppCompatActivity
        implements MusicPlayerFragment.OnFragmentInteractionListener,
        SongFragment.OnSongListFragmentInteractionListener,
        UserProfileFragment.onProfileFragmentInteractionListener,
        PlaylistFragment.OnPlaylistFragmentInteractionListener {

    private static final String TAG = "MainActivity";
    private static final String CLIENT_ID = "a6d6003f62b54f1c9a3ea665f4ded656";
    private static final String REDIRECT_URI = "com.example.moodmixer://callback/";
    private static final int REQUEST_CODE = 1337;
    public static String token;
    private MyPlaylistRecyclerViewAdapter mPlaylistAdapter;
    private MySongRecyclerViewAdapter mSongRecyclerViewAdapter;
    private SpotifyAppRemote musicPlayer; // mSpotifyAppRemove
    private Songs tracks;
    private String trackName;
    private String trackArtist;
    private ImageView trackAlbumCover;
    SpotifyApi api = new SpotifyApi();
    SpotifyService spotify = api.getService();
    List<TrackSimple> tracksList = new ArrayList<>();
    Toolbar toolbar;
    private MessageModel message;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(TAG, "onCreate - start");

        this.message = new MessageModel(MainActivity.TAG, this);

        setTitle("Mood Mixer");
        BottomNavigationView navView = findViewById(R.id.bottom_nav_view);
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(navView, navController);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Mood Mixer");

        setUpLogin();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        // Check if result comes from the correct activity
        if (requestCode == REQUEST_CODE) {
            AuthenticationResponse response = AuthenticationClient.getResponse(resultCode, intent);

            switch (response.getType()) {
                // Response was successful and contains auth token
                case TOKEN:
                    token = response.getAccessToken();
                    startSpotifyService();
                    // Handle successful response
                    break;

                // Auth flow returned an error
                case ERROR:
                    message.toast("ERROR: AuthenticationResponse - protected void onActivityResult");
                    // Handle error response
                    break;

                // Most likely auth flow was cancelled
                default:
                    // Handle other cases
            }
        }
    }

    private void startSpotifyService(){
        api.setAccessToken(token);
        spotify = api.getService();
        initSpotifyInfo(spotify);

    }

    private void initSpotifyInfo(final SpotifyService spotify){

        spotify.getMySavedTracks(new SpotifyCallback<Pager<SavedTrack>>() {
            @Override
            public void success(Pager<SavedTrack> savedTrackPager, Response response) {
                // handle successful response
            }

            @Override
            public void failure(SpotifyError error) {
                // handle error
            }
        });
    }

    public void setUpLogin() {
        AuthenticationRequest.Builder builder =
                new AuthenticationRequest.Builder(CLIENT_ID, AuthenticationResponse.Type.TOKEN, REDIRECT_URI);

        builder.setScopes(new String[]{"streaming", "user-read-private", "playlist-read-private", "user-library-modify",
        "playlist-read-collaborative", "playlist-modify-private", "user-follow-modify", "user-read-currently-playing",
        "user-read-recently-played", "user-read-email", "user-library-read", "user-top-read", "playlist-modify-public"
        ,"user-follow-read", "user-read-playback-state", "user-modify-playback-state"});
        builder.setShowDialog(true);
        AuthenticationRequest request = builder.build();
        AuthenticationClient.openLoginActivity(this, REQUEST_CODE, request);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.userProfileFragment) {
            return super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    protected void onStart() {
        super.onStart();
        setUpConnectionToSpotify();
    }

    private void setUpConnectionToSpotify() {

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
                            message.logDebug("Connected! Yay!");
                        }

                        public void onFailure(Throwable error) {
                            message.handleSpotifyOnFailureError(error);
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

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onSongListFragmentInteraction(Songs item) {

    }

    @Override
    public void onPlaylistFragmentInteraction(DummyContent.Songs item) {

    }

    @Override
    public void onProfileFragmentInteraction(Profile button) {
    }
}