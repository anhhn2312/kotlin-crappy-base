package com.dinominator.extensions.lib.model;

public interface SlidrListener {

    void onSlideStateChanged(int state);

    void onSlideChange(float percent);

    void onSlideOpened();

    void onSlideClosed();
}
