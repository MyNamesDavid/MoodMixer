package com.example.moodmixer;
import android.widget.SeekBar;
import android.os.Handler;
import com.spotify.protocol.client.ErrorCallback;


import com.spotify.android.appremote.api.SpotifyAppRemote;

final class TrackProgressBar {

    private static final int LOOP_DURATION = 500;
    private final SeekBar mSeekBar;
    private final Handler mHandler;
    private SpotifyAppRemote mSpotifyAppRemote;

    TrackProgressBar(SpotifyAppRemote mSpotifyAppRemote, SeekBar seekBar) {
        mSeekBar = seekBar;
        mSeekBar.setOnSeekBarChangeListener(mSeekBarChangeListener);
        mHandler = new Handler();
        this.mSpotifyAppRemote = mSpotifyAppRemote;
    }

    private final SeekBar.OnSeekBarChangeListener mSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
//            mSpotifyAppRemote.getPlayerApi().seekTo(seekBar.getProgress())
//                    .setErrorCallback(mErrorCallback);
        }
    };

    private final Runnable mSeekRunnable = new Runnable() {
        @Override
        public void run() {
            int progress = mSeekBar.getProgress();
            mSeekBar.setProgress(progress + LOOP_DURATION);
            mHandler.postDelayed(mSeekRunnable, LOOP_DURATION);
        }
    };

    private TrackProgressBar(SeekBar seekBar) {
        mSeekBar = seekBar;
        mSeekBar.setOnSeekBarChangeListener(mSeekBarChangeListener);
        mHandler = new Handler();
    }

    private void setDuration(long duration) {
        mSeekBar.setMax((int) duration);
    }

    private void update(long progress) {
        mSeekBar.setProgress((int) progress);
    }

    private void pause() {
        mHandler.removeCallbacks(mSeekRunnable);
    }

    private void unpause() {
        mHandler.removeCallbacks(mSeekRunnable);
        mHandler.postDelayed(mSeekRunnable, LOOP_DURATION);
    }
}
