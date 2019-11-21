package com.example.moodmixer;

import android.content.Context;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.spotify.android.appremote.api.ConnectionParams;
import com.spotify.android.appremote.api.Connector;
import com.spotify.android.appremote.api.SpotifyAppRemote;
import com.spotify.protocol.types.Image;
import com.spotify.protocol.types.PlayerState;
import com.spotify.protocol.types.Track;

import java.beans.*;

enum SpotifyProps {
    IsPaused,
    AlbumCover,
    SongArtist,
    SongName
}

public class SpotifyModel {

    private static final String CLIENT_ID = "a6d6003f62b54f1c9a3ea665f4ded656";
    private static final String REDIRECT_URI = "com.example.moodmixer://callback/";
    private SpotifyAppRemote musicPlayer; // mSpotifyAppRemote
    private Track track;
    public String songName;
    public String songArtist;
    public Boolean isPaused;
    private Context context;
    private String tag;
    MessageModel message;

    public PropertyChangeSupport propertyChange = new PropertyChangeSupport(this);

    SpotifyModel(String tag, Context context) {
        this.tag = tag;
        this.context = context;
        this.message = new MessageModel(tag, context);
    }

    public Boolean isConnected() {

        Boolean isConnected = false;
        if (SpotifyAppRemote.isSpotifyInstalled(context) && musicPlayer != null && musicPlayer.isConnected()) {
            isConnected = true;
        }
        return isConnected;
    }

    public void disconnect() {
        SpotifyAppRemote.disconnect(musicPlayer);
    }

    public void resume() {
        musicPlayer.getPlayerApi().resume();
    }

    public void pause() {
        musicPlayer.getPlayerApi().pause();
    }

    public void nextSong() {
        musicPlayer.getPlayerApi().skipNext();
    }

    public void previousSong() {
        musicPlayer.getPlayerApi().skipPrevious();
    }

    public void setUpConnectionToSpotify() {

        if (SpotifyAppRemote.isSpotifyInstalled(context)) {

            ConnectionParams connectionParams =
                    new ConnectionParams.Builder(CLIENT_ID)
                            .setRedirectUri(REDIRECT_URI)
                            .showAuthView(true)
                            .build();

            SpotifyAppRemote.connect(context, connectionParams,
                    new Connector.ConnectionListener() {
                        @Override
                        public void onConnected(SpotifyAppRemote spotifyAppRemote) {
                            musicPlayer = spotifyAppRemote;

                            subscribeToPlayerState();
                        }

                        public void onFailure(Throwable error) {
                            message.handleSpotifyOnFailureError(error);
                        }
                    });
        } else {
            Log.d(tag, "Requirement: Spotify must be installed on device with a premium membership");
        }
    }

    private void loadAlbumCover(PlayerState playerState) {
        // Get image from track
        musicPlayer.getImagesApi()
                .getImage(playerState.track.imageUri, Image.Dimension.LARGE)
                .setResultCallback(bitmap -> {
                    this.propertyChange.firePropertyChange(SpotifyProps.AlbumCover.toString(), null, bitmap);
                });
    }

    private void loadMusicResources(Track track, PlayerState playerState) {

        this.propertyChange.firePropertyChange(SpotifyProps.SongArtist.toString(), songArtist, track.artist.name); // So we tell our listener
        this.propertyChange.firePropertyChange(SpotifyProps.SongName.toString(), songName, track.name); // So we tell our listener

        songArtist = track.artist.name;
        songName = track.name;

        loadAlbumCover(playerState);
    }

    private void subscribeToPlayerState() {

        // Subscribe to PlayerState
        musicPlayer.getPlayerApi()
                .subscribeToPlayerState()
                .setEventCallback(playerState -> {

                    this.propertyChange.firePropertyChange(SpotifyProps.IsPaused.toString(), null, playerState.isPaused);
                    isPaused = playerState.isPaused;
                    this.track = playerState.track;
                    this.loadMusicResources(track, playerState);
                });
    }
}
