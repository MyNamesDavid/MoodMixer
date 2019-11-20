package com.example.moodmixer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationResponse;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyCallback;
import kaaes.spotify.webapi.android.SpotifyError;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Pager;
import kaaes.spotify.webapi.android.models.SavedTrack;
import retrofit.client.Response;

import static com.spotify.sdk.android.authentication.LoginActivity.REQUEST_CODE;

public class IntroductionFragment extends Fragment {

    private static final String TAG = "IntroductionFragment";

    private Button genre1Button;
    private Button genre2Button;
    private Button genre3Button;
    private Button genre4Button;
    private Button authorizeButton;
    public static String token;
    SpotifyApi api = new SpotifyApi();
    SpotifyService spotify = api.getService();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.introduction_fragment, null);
        setUpGenre1Button(rootView);
        setUpGenre2Button(rootView);
        setUpGenre3Button(rootView);
        setUpGenre4Button(rootView);
        setUpAuthorizeSpotifyButton(rootView);
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        authorizeButton.setOnClickListener((View view) -> {
            Log.d(TAG, "onClick: onActivityResult");
            MusicPlayerFragment.buttonEffect(authorizeButton);
            MessageModel message = new MessageModel("IntroductionFragment", getContext());
            // Check if result comes from the correct activity
            if (requestCode == REQUEST_CODE) {
                AuthenticationResponse response = AuthenticationClient.getResponse(resultCode, data);

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
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onStop() {
        super.onStop();

    }

    private void setUpGenre1Button(View rootView) {

        genre1Button = rootView.findViewById(R.id.play_imagebutton);
        genre1Button.setOnClickListener((View view) -> {
            Log.d(TAG, "onClick: genre1Button Tapped");
            MusicPlayerFragment.buttonEffect(genre1Button);
        });
    }

    private void setUpGenre2Button(View rootView) {

        genre2Button = rootView.findViewById(R.id.play_imagebutton);
        genre2Button.setOnClickListener((View view) -> {
            Log.d(TAG, "onClick: genre2Button Tapped");
            MusicPlayerFragment.buttonEffect(genre1Button);
        });
    }

    private void setUpGenre3Button(View rootView) {

        genre3Button = rootView.findViewById(R.id.play_imagebutton);
        genre3Button.setOnClickListener((View view) -> {
            Log.d(TAG, "onClick: genre3Button Tapped");
            MusicPlayerFragment.buttonEffect(genre1Button);
        });
    }

    private void setUpGenre4Button(View rootView) {

        genre4Button = rootView.findViewById(R.id.play_imagebutton);
        genre4Button.setOnClickListener((View view) -> {
            Log.d(TAG, "onClick: genre4Button Tapped");
            MusicPlayerFragment.buttonEffect(genre1Button);
        });
    }

    private void setUpAuthorizeSpotifyButton(View rootView) {

        authorizeButton = rootView.findViewById(R.id.authorize_spotify_button);
    }

    private void startSpotifyService() {
        api.setAccessToken(token);
        spotify = api.getService();
        initSpotifyInfo(spotify);
    }

    private void initSpotifyInfo(final SpotifyService spotify) {
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
}
