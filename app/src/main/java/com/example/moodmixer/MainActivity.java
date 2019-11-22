package com.example.moodmixer;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.telecom.Call;
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
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
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
        PlaylistFragment.OnPlaylistFragmentInteractionListener {

    private static final String TAG = "MainActivity";
    private static final String CLIENT_ID = "a6d6003f62b54f1c9a3ea665f4ded656";
    private static final String REDIRECT_URI = "com.example.moodmixer://callback/";
    private static final int REQUEST_CODE = 1337;
    public static String token;
    private ArrayList<Playlists> playlists = new ArrayList<>();
    private MySongRecyclerViewAdapter mSongRecyclerViewAdapter;
    public MyPlaylistRecyclerViewAdapter myPlaylistAdapter;
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
    private PreferenceManager preferences;

    public String userId;
    public String playlistId;


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

        setUpLogin();
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
                    Songs songlistItem = new Songs(pt.name, pt.type, pt.uri);
                    SonglistSingleton.get(getBaseContext()).addSonglist(songlistItem);
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
        musicPlayer.getPlayerApi().play(item.getSongUri());
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

    public void initPlaylistRecyclerview(){

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
}