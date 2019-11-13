package com.example.moodmixer;

import android.content.Context;
import android.nfc.Tag;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

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

class MessageModel {

    private final String TAG;
    private final Context context;

    MessageModel(String tag, Context context) {
        this.TAG = tag;
        this.context = context;
    }

    void toast(String message) {

        Toast toast = Toast.makeText(this.context, message, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.TOP, 0, 120);
        toast.show();
    }

    void handleSpotifyOnFailureError(Throwable error) {
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

    private void logError(Throwable throwable, String msg) {
        Log.e(TAG, msg, throwable);
    }

    void logDebug(String message) {
        Log.d(TAG, message);
    }
}
