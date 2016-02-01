package com.yahoo.reportr.data.service;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;

import com.yahoo.reportr.data.entity.User;
import com.yahoo.reportr.ui.presenter.ReportrPresenter;
import com.yahoo.reportr.ui.presenter.UserPresenter;
import com.yahoo.reportr.utils.Constants;
import com.yahoo.reportr.utils.MemCache;

/**
 * Created by bhavanis on 1/26/16.
 */
public class OntologyService extends IntentService {

    public OntologyService() {
        super("ontology-service");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String authkey = intent.getStringExtra(Constants.KEY_AUTHKEY);
        String deviceStr = intent.getStringExtra(Constants.KEY_DEVICE_STR);
        boolean callOnto = intent.getBooleanExtra(Constants.KEY_REGISTER, false);
        ReportrPresenter reportrPresenter = new ReportrPresenter();
        if (callOnto) {
            String ontology = reportrPresenter.getGlobalOntologies(authkey);
            SharedPreferences preferences = getSharedPreferences(Constants.SHARED_PREF, 0);
            preferences.edit().putString(Constants.KEY_ONTOLOGY, ontology).commit();
        } else {
            reportrPresenter.registerDevice(authkey, deviceStr);
        }
    }
}
