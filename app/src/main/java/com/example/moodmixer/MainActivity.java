package com.example.moodmixer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.telecom.Call;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
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
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyCallback;
import kaaes.spotify.webapi.android.SpotifyError;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Pager;
import kaaes.spotify.webapi.android.models.Playlist;
import kaaes.spotify.webapi.android.models.PlaylistSimple;
import kaaes.spotify.webapi.android.models.PlaylistTrack;
import kaaes.spotify.webapi.android.models.SavedTrack;
import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.TrackSimple;
import kaaes.spotify.webapi.android.models.UserPrivate;
import kaaes.spotify.webapi.android.models.UserPublic;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MainActivity
        extends AppCompatActivity
        implements MusicPlayerFragment.OnFragmentInteractionListener,
        SongFragment.OnSongListFragmentInteractionListener,
        UserProfileFragment.onProfileFragmentInteractionListener,
        PlaylistFragment.OnPlaylistFragmentInteractionListener,
        PlaylistAdderDialogueFragment.OnInputListener
{

    private static final String TAG = "MainActivity";
    private static final String CLIENT_ID = "a6d6003f62b54f1c9a3ea665f4ded656";
    private static final String REDIRECT_URI = "com.example.moodmixer://callback/";
    private static final int REQUEST_CODE = 1337;
    public static String token;
    private SpotifyAppRemote musicPlayer; // mSpotifyAppRemove
    private Songs tracks;
    private String songName;
    private String songArtist;
    private ImageView trackAlbumCover;
    SpotifyApi api = new SpotifyApi();
    SpotifyService spotify = api.getService();
    SpotifyModel spotifyModel;
    List<TrackSimple> tracksList = new ArrayList<>();
    Toolbar toolbar;
    private MessageModel message;
    private PreferenceManager preferences;
    public PropertyChangeListener listener;
    ImageButton playImageButton;

    public String userId;
    public String playlistId;

    static int random;

    static int style = 0;
    static boolean webLogin = false;

    @Override
    public Context getBaseContext() {
        return super.getBaseContext();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.preferences = new PreferenceManager(this);
        this.message = new MessageModel(MainActivity.TAG, this);

        BottomNavigationView navView = findViewById(R.id.bottom_nav_view);
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(navView, navController);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(preferences.getArtistName());

        setUpPlayImageButton();

        if(webLogin == false) {
            setUpLogin();
            webLogin = true;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        getSupportActionBar().setTitle(preferences.getArtistName());
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

        initUserId(spotify);
        spotify.getMyPlaylists(new Callback<Pager<PlaylistSimple>>(){
            @Override
            public void success(Pager<PlaylistSimple> playlistSimplePager, Response response) {
                if(playlistSimplePager.items.size() > 0){
                    for(int i = 0; i < playlistSimplePager.items.size(); i++){
                        if(playlistSimplePager.items.get(i).tracks.total != 0 && playlistSimplePager.items.get(i).tracks.total != 1)
                        {
                            Playlists playlistItem = new Playlists(playlistSimplePager.items.get(i).name);
                            PlaylistSingleton.get(getBaseContext()).addPlaylist(playlistItem);
                            playlistId = playlistSimplePager.items.get(i).id;
                        }
                    }
                }
            }
            @Override
            public void failure(RetrofitError error) {
                Log.d("failure", error.toString());
            }

        });

        spotify.getTopTracks(new Callback<Pager<Track>>() {
            @Override
            public void success(Pager<Track> trackPager, Response response) {
                List<Track> items = trackPager.items;
                for( Track pt : items){
                    Log.e("TEST", pt.name + " - " + pt.id);
                    Songs songlistItem = new Songs(pt.name, pt.artists.get(0).name, pt.uri, pt.duration_ms);
                    SonglistSingleton.get(getBaseContext()).addSonglist(songlistItem);
                    Log.e("TEST", pt.name + " - " + pt.id);
                }
            }

            @Override
            public void failure(RetrofitError error) {

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

    private void setUpPlayImageButton() {
        playImageButton = toolbar.findViewById(R.id.toolbar_playbutton);
        playImageButton.setOnClickListener((View v) -> {
            Log.d(TAG, "onClick: playImageButton Tapped");
            buttonEffect(playImageButton);
            onPlayPauseButtonTapped();
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
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
        musicPlayer.getPlayerApi().play(item.getSongUri());
        changeBackground();
    }

    @Override
    public void onPlaylistFragmentInteraction(Playlists item) {

    }

    @Override
    public void onProfileFragmentInteraction(Profile button) {
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        if (fragment instanceof PlaylistFragment) {
            PlaylistFragment playlistFragment = (PlaylistFragment) fragment;
            playlistFragment.setOnPlaylistFragmentInteractionListener(this);
        }
    }

    public void initUserId(final SpotifyService spotify) {
        spotify.getMe(new Callback<UserPrivate>() {
            @Override
            public void success(UserPrivate userPrivate, Response response) {
                spotify.getUser(userPrivate.id, new Callback<UserPublic>() {

                    @Override
                    public void success(UserPublic userPublic, Response response) {
                        userId = userPublic.id;
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.d(TAG, "failure: " + error.toString());
                    }
                });
                userId = userPrivate.id;
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d(TAG, "Failure: " + error.toString());
            }
        });
    }

    //Has to be used before oncreate
    public void changeBackground(){

        random = new Random().nextInt((4 - 0) + 1) + 0;

        switch (random){
            case 0:{
                style = R.style.AngryTheme;
                restartThis();
                break;
            }
            case 1:{
                style = R.style.SadTheme;
                restartThis();
                break;
            }
            case 2:{
                style = R.style.CalmTheme;
                restartThis();
                break;
            }
            case 3:{
                style = R.style.HappyTheme;
                restartThis();
                break;
            }
            case 4:{
                style = R.style.StressedTheme;
                restartThis();
                break;
            }
        }

    }

    @Override
    public Resources.Theme getTheme() {
        Resources.Theme theme = super.getTheme();
        if(style == 0){
            theme.applyStyle(R.style.DefaultTheme, true);
        }else {
            theme.applyStyle(style, true);
        }
        return theme;
    }

    private void restartThis() {
        finish();
        Intent intent = getIntent();
        startActivity(intent);
    }

    @Override
    public void OnSongListFragmentAddToPlaylistInteractionListener(Songs item) {
        FragmentManager fm = getSupportFragmentManager();
        PlaylistAdderDialogueFragment dialog = new PlaylistAdderDialogueFragment();
        dialog.show(fm, "PlaylistAdderDialogue");
    }

    @Override
    public void sendInput(String input) {
        Log.d(TAG, "sendInput: found incoming input: " + input);
        Playlists playlistItem = new Playlists(input);
        PlaylistSingleton.get(this).addPlaylist(playlistItem);
        
    }


    private void onPlayPauseButtonTapped() {

        if (spotifyModel.isConnected()) {

            if (spotifyModel.isPaused)
                spotifyModel.resume();

            else
                spotifyModel.pause();
        }
    }

    public static void buttonEffect(View button) {
        button.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        v.getBackground().setColorFilter(0xe0f47521, PorterDuff.Mode.SRC_ATOP);
                        v.invalidate();
                        break;
                    }
                    case MotionEvent.ACTION_UP: {
                        v.getBackground().clearColorFilter();
                        v.invalidate();
                        break;
                    }
                }
                return false;
            }
        });
    }
}

