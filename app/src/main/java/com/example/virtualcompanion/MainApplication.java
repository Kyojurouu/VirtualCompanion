package com.example.virtualcompanion;

import android.app.Application;
import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class MainApplication extends Application {

    private int activityReferences = 0;
    private boolean isActivityChangingConfigurations = false;

    @Override
    public void onCreate() {
        super.onCreate();

        // Register activity lifecycle callbacks
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {
            }

            @Override
            public void onActivityStarted(@NonNull Activity activity) {
                if (++activityReferences == 1 && !isActivityChangingConfigurations) {
                    // App enters foreground
                    android.util.Log.d("MusicManager", "App in foreground - Starting music");
                    MusicManager.startMusic(MainApplication.this);
                }
            }

            @Override
            public void onActivityResumed(@NonNull Activity activity) {
            }

            @Override
            public void onActivityPaused(@NonNull Activity activity) {
            }

            @Override
            public void onActivityStopped(@NonNull Activity activity) {
                isActivityChangingConfigurations = activity.isChangingConfigurations();
                if (--activityReferences == 0 && !isActivityChangingConfigurations) {
                    // App enters background (user pressed home or switched apps)
                    android.util.Log.d("MusicManager", "App in background - Stopping music");
                    MusicManager.stopMusic();
                }
            }

            @Override
            public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {
            }

            @Override
            public void onActivityDestroyed(@NonNull Activity activity) {
            }
        });
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        // Stop music when app is terminated
        android.util.Log.d("MusicManager", "App terminated - Stopping music");
        MusicManager.stopMusic();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        // Pause music on low memory
        MusicManager.pauseMusic();
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        // Stop music if system needs memory
        if (level >= TRIM_MEMORY_MODERATE) {
            MusicManager.stopMusic();
        }
    }
}