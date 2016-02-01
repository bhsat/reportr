package com.yahoo.reportr.data.service;

import android.os.Bundle;

import com.google.android.gms.gcm.GcmListenerService;

/**
 * Created by bhavanis on 1/29/16.
 */
public class GcmMessageHandler extends GcmListenerService {

    @Override
    public void onMessageReceived(String from, Bundle data) {
        super.onMessageReceived(from, data);
    }
}
