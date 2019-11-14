package com.dinominator.extensions.lib;


import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import com.dinominator.extensions.lib.model.SlidrConfig;
import com.dinominator.extensions.lib.model.SlidrInterface;
import com.dinominator.extensions.lib.widget.SliderPanel;

public final class Slidr {

    @NonNull
    public static SlidrInterface attach(@NonNull Activity activity) {
        return attach(activity, -1, -1);
    }

    @NonNull
    public static SlidrInterface attach(@NonNull Activity activity, @ColorInt int statusBarColor1,
                                        @ColorInt int statusBarColor2) {

        // Setup the slider panel and attach it to the decor
        final SliderPanel panel = attachSliderPanel(activity, null);

        // Set the panel slide listener for when it becomes closed or opened
        panel.setOnPanelSlideListener(new ColorPanelSlideListener(activity, statusBarColor1, statusBarColor2));

        // Return the lock interface
        return panel.getDefaultInterface();
    }

    @NonNull
    public static SlidrInterface attach(@NonNull Activity activity, @NonNull SlidrConfig config) {

        // Setup the slider panel and attach it to the decor
        final SliderPanel panel = attachSliderPanel(activity, config);

        // Set the panel slide listener for when it becomes closed or opened
        panel.setOnPanelSlideListener(new ConfigPanelSlideListener(activity, config));

        // Return the lock interface
        return panel.getDefaultInterface();
    }

    @NonNull
    private static SliderPanel attachSliderPanel(@NonNull Activity activity, @NonNull SlidrConfig config) {
        // Hijack the decorview
        ViewGroup decorView = (ViewGroup) activity.getWindow().getDecorView();
        View oldScreen = decorView.getChildAt(0);
        decorView.removeViewAt(0);

        // Setup the slider panel and attach it to the decor
        SliderPanel panel = new SliderPanel(activity, oldScreen, config);
        panel.addView(oldScreen);
        decorView.addView(panel, 0);
        return panel;
    }

    @NonNull
    public static SlidrInterface replace(@NonNull final View oldScreen, @NonNull final SlidrConfig config) {
        ViewGroup parent = (ViewGroup) oldScreen.getParent();
        ViewGroup.LayoutParams params = oldScreen.getLayoutParams();
        parent.removeView(oldScreen);

        // Setup the slider panel and attach it
        final SliderPanel panel = new SliderPanel(oldScreen.getContext(), oldScreen, config);

        panel.addView(oldScreen);
        parent.addView(panel, 0, params);

        // Set the panel slide listener for when it becomes closed or opened
        panel.setOnPanelSlideListener(new FragmentPanelSlideListener(oldScreen, config));

        // Return the lock interface
        return panel.getDefaultInterface();
    }

}
