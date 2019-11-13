
package com.example.moodmixer;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;

import android.widget.ImageView;
import android.widget.Toast;


import com.example.moodmixer.dummy.DummyContent;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import com.google.android.material.internal.NavigationMenuView;
import com.google.android.material.navigation.NavigationView;
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
import com.spotify.protocol.client.CallResult;
import com.spotify.protocol.types.ListItems;
import com.spotify.protocol.types.Track;
import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;


import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.SortedList;

import java.util.ArrayList;
import java.util.List;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyCallback;
import kaaes.spotify.webapi.android.SpotifyError;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Album;
import kaaes.spotify.webapi.android.models.Pager;
import kaaes.spotify.webapi.android.models.PlaylistTrack;
import kaaes.spotify.webapi.android.models.SavedTrack;
import kaaes.spotify.webapi.android.models.TrackSimple;
import kaaes.spotify.webapi.android.models.Tracks;
import retrofit.Callback;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static androidx.test.InstrumentationRegistry.getContext;


public class MainActivity
        extends AppCompatActivity
        implements MusicPlayerFragment.OnFragmentInteractionListener,
        SongFragment.OnSongListFragmentInteractionListener,
        UserProfileFragment.onProfileFragmentInteractionListener,
        PlaylistFragment.OnPlaylistFragmentInteractionListener {

    private static final String TAG = "MainActivity";
    private MyPlaylistRecyclerViewAdapter mPlaylistAdapter;
    private MySongRecyclerViewAdapter mSongRecyclerViewAdapter;
    private static final String CLIENT_ID = "a6d6003f62b54f1c9a3ea665f4ded656";
    private static final String REDIRECT_URI = "com.example.moodmixer://callback/";
    private static final int REQUEST_CODE = 1337;
    public static String AUTH_TOKEN;
    private SpotifyAppRemote musicPlayer; // mSpotifyAppRemove
    private Songs tracks;
    private String trackName;
    private String trackArtist;
    private ImageView trackAlbumCover;
    SpotifyApi api = new SpotifyApi();
    SpotifyService spotify = api.getService();
    Toolbar toolbar;
    List<TrackSimple> tracksList = new ArrayList<>();

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        // Check if result comes from the correct activity
        if (requestCode == REQUEST_CODE) {
            AuthenticationResponse response = AuthenticationClient.getResponse(resultCode, intent);

            switch (response.getType()) {
                // Response was successful and contains auth token
                case TOKEN:
                    api.setAccessToken(response.getAccessToken());
                    AUTH_TOKEN = response.getAccessToken();
                    // Handle successful response
                    break;

                // Auth flow returned an error
                case ERROR:
                    // Handle error response
                    break;

                // Most likely auth flow was cancelled
                default:
                    // Handle other cases
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(TAG, "onCreate - start");

        setTitle("Mood Mixer");
        BottomNavigationView navView = findViewById(R.id.bottom_nav_view);
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(navView, navController);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Mood Mixer");
    }

    public void setUpLogin() {
        AuthenticationRequest.Builder builder =
                new AuthenticationRequest.Builder(CLIENT_ID, AuthenticationResponse.Type.TOKEN, REDIRECT_URI);

        builder.setScopes(new String[]{"streaming"});
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
        setUpLogin();
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

    @Override
    public void onProfileFragmentInteraction(Profile button) {

    }
}