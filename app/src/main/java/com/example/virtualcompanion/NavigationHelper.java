package com.example.virtualcompanion;

import android.content.Context;
import android.widget.ImageView;

public class NavigationHelper {

    /**
     * Highlights the active navigation icon with visual effects
     */
    public static void setActive(Context context, ImageView icon) {
        if (icon == null) return;

        // Gold color
        icon.setColorFilter(context.getResources().getColor(R.color.nav_active));

        // Scale up 30%
        icon.setScaleX(1.3f);
        icon.setScaleY(1.3f);

        // Full brightness
        icon.setAlpha(1.0f);
    }

    /**
     * Sets inactive navigation icon style
     */
    public static void setInactive(Context context, ImageView icon) {
        if (icon == null) return;

        // Gray color
        icon.setColorFilter(context.getResources().getColor(R.color.nav_inactive));

        // Normal size
        icon.setScaleX(1.0f);
        icon.setScaleY(1.0f);

        // Dimmed
        icon.setAlpha(0.6f);
    }
}