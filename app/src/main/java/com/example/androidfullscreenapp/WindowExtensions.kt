package com.example.androidfullscreenapp

import android.view.Window
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.LifecycleCoroutineScope
import kotlinx.coroutines.delay

private const val DELAY_HIDING_SYSTEM_BARS = 1500L

fun Window.registerHidingSystemBarsAutomatically(lifecycleScope: LifecycleCoroutineScope) {
    // Lay out your app behind the system bars
    WindowCompat.setDecorFitsSystemWindows(this, false)

    // Optional: set the system bars light and content dark.
    WindowCompat.getInsetsController(this, decorView).isAppearanceLightNavigationBars = true
    WindowCompat.getInsetsController(this, decorView).isAppearanceLightStatusBars = true

    ViewCompat.setOnApplyWindowInsetsListener(decorView) { _, windowInsets ->
        val isVisibleSystemBars = windowInsets.isVisible(WindowInsetsCompat.Type.systemBars())
        if (isVisibleSystemBars) {
            lifecycleScope.launchWhenCreated {
                delay(DELAY_HIDING_SYSTEM_BARS)
                hideSystemBars()
            }
        }

        // Return CONSUMED if you don't want want the window insets to keep being
        // passed down to descendant views.
        WindowInsetsCompat.CONSUMED
    }
}

fun Window.hideSystemBars() {
    WindowCompat.getInsetsController(this, decorView).run {
        // Configure the behavior of the hidden system bars
        systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        // Hide both the status bar and the navigation bar
        hide(WindowInsetsCompat.Type.systemBars())
    }
}