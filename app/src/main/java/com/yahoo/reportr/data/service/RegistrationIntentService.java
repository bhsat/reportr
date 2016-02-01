package com.yahoo.reportr.data.service;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import com.yahoo.reportr.R;
import com.yahoo.reportr.ui.presenter.ReportrPresenter;
import com.yahoo.reportr.utils.Constants;

import java.io.IOException;

/**
 * Created by bhavanis on 1/29/16.
 */
public class RegistrationIntentService extends IntentService {

    private static final String TAG = RegistrationIntentService.class.getSimpleName();
    SharedPreferences preferences;

    public RegistrationIntentService() {
        super("registration-service");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        preferences = getSharedPreferences(Constants.SHARED_PREF, 0);
        InstanceID instanceID = InstanceID.getInstance(this);
        String senderId = getResources().getString(R.string.gcm_defaultSenderId);
        try {
            // request token that will be used by the server to send push notifications
            String token = instanceID.getToken(senderId, GoogleCloudMessaging.INSTANCE_ID_SCOPE);
            Log.d(TAG, "GCM Registration Token: " + token);

            preferences.edit().putString(Constants.KEY_TOKEN, token).apply();
            // pass along this data
            sendTokenToServer(token);
        } catch (IOException e) {
            e.printStackTrace();
            preferences.edit().putBoolean(Constants.TOKEN_SENT, false).apply();
        }
    }

    private void sendTokenToServer(String token) {
        String authkey = preferences.getString(Constants.KEY_AUTHKEY, null);
        ReportrPresenter presenter = new ReportrPresenter();
        boolean status = presenter.registerDevice(authkey, token);
        if (status) {
            preferences.edit().putBoolean(Constants.TOKEN_SENT, true).apply();
        } else {
            preferences.edit().putBoolean(Constants.TOKEN_SENT, false).apply();
        }
    }
}
