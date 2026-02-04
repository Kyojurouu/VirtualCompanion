package com.example.virtualcompanion;

import android.content.Context;
import android.media.MediaPlayer;

public class MusicManager {

    private static MediaPlayer mediaPlayer;
    private static boolean isMusicEnabled = true;
    private static boolean isInitialized = false;

    // Initialize once
    public static synchronized void startMusic(Context context) {

        if (!isMusicEnabled) return;

        try {

            if (!isInitialized) {

                mediaPlayer = MediaPlayer.create(
                        context.getApplicationContext(),
                        R.raw.background_music
                );

                if (mediaPlayer == null) return;

                // Loop forever
                mediaPlayer.setLooping(true);
                mediaPlayer.setVolume(1f, 1f);

                // Extra safety
                mediaPlayer.setOnCompletionListener(mp -> {
                    mp.seekTo(0);
                    mp.start();
                });

                isInitialized = true;
            }

            if (!mediaPlayer.isPlaying()) {
                mediaPlayer.start();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Pause
    public static synchronized void pauseMusic() {

        try {
            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
            }
        } catch (Exception ignored) {}
    }

    // Resume
    public static synchronized void resumeMusic() {
        try {
            if (mediaPlayer != null && !mediaPlayer.isPlaying() && isMusicEnabled) {
                mediaPlayer.start();
            }
        } catch (Exception ignored) {}
    }

    // Stop fully (when app exits or goes to background)
    public static synchronized void stopMusic() {

        try {

            if (mediaPlayer != null) {

                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                }
                mediaPlayer.release();
                mediaPlayer = null;
                isInitialized = false;
            }

        } catch (Exception ignored) {}
    }

    // Enable / Disable
    public static void setMusicEnabled(boolean enabled) {

        isMusicEnabled = enabled;

        if (!enabled) {
            pauseMusic();
        } else {
            resumeMusic();
        }
    }

    public static boolean isMusicEnabled() {
        return isMusicEnabled;
    }

    // Check if playing
    public static boolean isPlaying() {
        return mediaPlayer != null && mediaPlayer.isPlaying();
    }
}