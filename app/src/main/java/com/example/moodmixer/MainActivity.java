package com.example.moodmixer;

import android.net.Uri;
import android.os.Bundle;
import android.telecom.Call;
import android.util.Log;
import android.view.MenuInflater;
import android.view.View;
import android.widget.PopupMenu;


import com.example.moodmixer.dummy.DummyContent;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.spotify.android.appremote.api.ConnectionParams;
import com.spotify.android.appremote.api.Connector;
import com.spotify.android.appremote.api.SpotifyAppRemote;
import com.spotify.android.appremote.api.error.AuthenticationFailedException;
import com.spotify.android.appremote.api.error.CouldNotFindSpotifyApp;
import com.spotify.android.appremote.api.error.LoggedOutException;
import com.spotify.android.appremote.api.error.NotLoggedInException;
import com.spotify.android.appremote.api.error.OfflineModeException;
import com.spotify.android.appremote.api.error.SpotifyConnectionTerminatedException;
import com.spotify.android.appremote.api.error.SpotifyDisconnectedException;
import com.spotify.android.appremote.api.error.SpotifyRemoteServiceException;
import com.spotify.android.appremote.api.error.UnsupportedFeatureVersionException;
import com.spotify.android.appremote.api.error.UserNotAuthorizedException;
import com.spotify.protocol.types.Track;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavArgument;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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

import com.spotify.protocol.types.Image;
import com.spotify.protocol.types.ImageUri;
import com.spotify.protocol.types.Track;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.spotify.android.appremote.api.ConnectionParams;
import com.spotify.android.appremote.api.Connector;
import com.spotify.android.appremote.api.ContentApi;
import com.spotify.android.appremote.api.SpotifyAppRemote;
import com.spotify.android.appremote.api.error.AuthenticationFailedException;
import com.spotify.android.appremote.api.error.CouldNotFindSpotifyApp;
import com.spotify.android.appremote.api.error.LoggedOutException;
import com.spotify.android.appremote.api.error.NotLoggedInException;
import com.spotify.android.appremote.api.error.OfflineModeException;
import com.spotify.android.appremote.api.error.SpotifyConnectionTerminatedException;
import com.spotify.android.appremote.api.error.SpotifyDisconnectedException;
import com.spotify.android.appremote.api.error.SpotifyRemoteServiceException;
import com.spotify.android.appremote.api.error.UnsupportedFeatureVersionException;
import com.spotify.android.appremote.api.error.UserNotAuthorizedException;
import com.spotify.protocol.client.ErrorCallback;
import com.spotify.protocol.client.Subscription;
import com.spotify.protocol.types.Capabilities;
import com.spotify.protocol.types.Image;
import com.spotify.protocol.types.ListItem;
import com.spotify.protocol.types.PlaybackSpeed;
import com.spotify.protocol.types.PlayerContext;
import com.spotify.protocol.types.PlayerState;
import com.spotify.protocol.types.Repeat;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainActivity extends AppCompatActivity implements MusicPlayerFragment.OnFragmentInteractionListener, SongFragment.OnSongListFragmentInteractionListener
        , PlaylistFragment.OnPlaylistFragmentInteractionListener
{


    private static final String CLIENT_ID = "a6d6003f62b54f1c9a3ea665f4ded656";
    private static final String REDIRECT_URI = "com.example.moodmixer://callback/";
    private SpotifyAppRemote musicPlayer; // mSpotifyAppRemote

    ArrayList<Songs> country;

    public static final List<Songs> SONGS = new ArrayList<Songs>();
    public static final Map<String, Songs> SONGS_MAP = new HashMap<String, Songs>();

    Songs testSongNow;




    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(TAG, "onCreate - start");

        setTitle("Mood Mixer");
        BottomNavigationView navView = findViewById(R.id.bottom_nav_view);
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(navView, navController);

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    protected void onStart() {
        super.onStart();

        String message = String.format("Package Name: %s\n ", this.getPackageName());
        Log.d(TAG, message );

        setUpConnectionToSpotify();
    }

    private void logError(Throwable throwable, String msg) {
        Toast.makeText(this, "Error: " + msg, Toast.LENGTH_SHORT).show();
        Log.e(TAG, msg, throwable);
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
                            addItem(createSongs("hello", "there", "fuck"));
                            Log.d(TAG, "Connected! Yay!");
                        }

                        public void onFailure(Throwable error) {
                            if (error instanceof SpotifyRemoteServiceException) {
                                if (error.getCause() instanceof SecurityException) {
                                    logError(error, "SecurityException");
                                } else if (error.getCause() instanceof IllegalStateException) {
                                    logError(error, "IllegalStateException");
                                }
                            } else if (error instanceof NotLoggedInException) {
                                logError(error, "NotLoggedInException");
                            } else if (error instanceof AuthenticationFailedException) {
                                logError(error, "AuthenticationFailedException");
                            } else if (error instanceof CouldNotFindSpotifyApp) {
                                logError(error, "CouldNotFindSpotifyApp");
                            } else if (error instanceof LoggedOutException) {
                                logError(error, "LoggedOutException");
                            } else if (error instanceof OfflineModeException) {
                                logError(error, "OfflineModeException");
                            } else if (error instanceof UserNotAuthorizedException) {
                                logError(error, "UserNotAuthorizedException");
                            } else if (error instanceof UnsupportedFeatureVersionException) {
                                logError(error, "UnsupportedFeatureVersionException");
                            } else if (error instanceof SpotifyDisconnectedException) {
                                logError(error, "SpotifyDisconnectedException");
                            } else if (error instanceof SpotifyConnectionTerminatedException) {
                                logError(error, "SpotifyConnectionTerminatedException");
                            } else {
                                logError(error, String.format("Connection failed: %s", error));
                            }
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

    private static Songs createSongs(String trackName, String artistName, String url) {
        Songs songs = new Songs("Song:" + trackName, "Artist" + artistName, "Fuck");
        return songs;
    }

    private static void addItem(Songs item) {
        SONGS.add(item);
        SONGS_MAP.put(item.songName, item);
        SONGS_MAP.put(item.songArtistName, item);
    }
}
