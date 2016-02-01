package com.yahoo.reportr.ui.view.views;

import android.support.annotation.NonNull;
import android.support.annotation.UiThread;

import com.yahoo.reportr.data.entity.Topic;
import com.yahoo.reportr.data.entity.User;

import java.util.List;

/**
 * Created by bhavanis on 1/8/16.
 */
public interface UserView {

    @UiThread
    void addUserInfo(@NonNull User user);

    @UiThread
    void addTopics(@NonNull List<Topic> topics);
}
