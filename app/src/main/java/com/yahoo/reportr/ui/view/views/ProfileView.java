package com.yahoo.reportr.ui.view.views;

import android.support.annotation.NonNull;
import android.support.annotation.UiThread;

/**
 * Created by bhavanis on 1/14/16.
 */
public interface ProfileView {

    @UiThread
    void addImageUrl(@NonNull String url);
}
