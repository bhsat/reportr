package com.yahoo.reportr.ui.presenter;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.yahoo.reportr.data.entity.ApiResponse;
import com.yahoo.reportr.data.entity.AuthInfo;
import com.yahoo.reportr.data.entity.Ontology;
import com.yahoo.reportr.domain.interactor.ReportrInteractor;
import com.yahoo.reportr.ui.DaggerReportrComponent;
import com.yahoo.reportr.ui.view.views.ReportrView;
import com.yahoo.reportr.utils.OntologyUtil;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;
import rx.Subscription;

/**
 * Created by bhavanis on 12/17/15.
 */
public class ReportrPresenter {

    private static final String TAG = ReportrPresenter.class.getSimpleName();
    private static final int HTTP_OK = 200;
    @Inject
    ReportrInteractor reportrInteractor;

    //private ReportrView reportrView;
    private Subscription subscription;
    private Gson json;
    private JsonParser jsonParser;
    private AuthInfo authInfo;
    private String globalOnto;
    private boolean status;

    public ReportrPresenter() {
        DaggerReportrComponent.builder().build().inject(this);
        json = new Gson();
        jsonParser = new JsonParser();
        authInfo = new AuthInfo();
    }

    public AuthInfo authorize(String username, String password, String deviceStr) {
        Call<ApiResponse> call = reportrInteractor.authorize(username, password, deviceStr);
        try {
            Response<ApiResponse> response = call.execute();
            if (response.code() == HTTP_OK) {
                ApiResponse apiResponse = response.body();
                AuthInfo info = handleAuthorization(apiResponse);
                authInfo.setAuthKey(info.getAuthKey());
                authInfo.setTstamp(info.getTstamp());
                authInfo.setCode(response.code());
            } else {
                String msg = handleAuthError(response.errorBody().string());
                authInfo.setCode(response.code());
                authInfo.setMessage(msg);
                Log.e(TAG, "Authorization error: "+msg);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return authInfo;
    }

    public boolean registerDevice(String authkey, String token) {
        status = false;
        Call<ApiResponse> call = reportrInteractor.registerDevice(authkey, token);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Response<ApiResponse> response, Retrofit retrofit) {
                if (response.code() == HTTP_OK) {
                    status = true;
                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
        return status;
    }

    private String handleAuthError(String error) {
        String msg = "";
        JsonObject obj = jsonParser.parse(error).getAsJsonObject();
        JsonObject data = obj.getAsJsonObject("data");
        if (data != null) {
            if (data.has("message")) {
                msg = data.get("message").getAsString();
            }
        }
        return msg;
    }

    public String getGlobalOntologies(String authkey) {
        Call<ApiResponse> call = reportrInteractor.getGlobalOntologies(authkey);
        try {
            Response<ApiResponse> response = call.execute();
            if (response.code() == HTTP_OK) {
                ApiResponse apiResponse = response.body();
                globalOnto = handleGlobalOntologies(apiResponse);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return globalOnto;
    }

    private AuthInfo handleAuthorization(ApiResponse apiResponse) {
        String result = json.toJson(apiResponse.getData());
        JsonObject obj = jsonParser.parse(result).getAsJsonObject();
        AuthInfo info = new AuthInfo();
        if (obj.has("authKey")) {
            info.setAuthKey(obj.get("authKey").getAsString());
            info.setTstamp(obj.get("tstamp").getAsLong());
        }
        return info;
    }


    private String handleGlobalOntologies(ApiResponse response) {
        String result = json.toJson(response.getData());
        OntologyUtil util = new OntologyUtil();
        List<Ontology> list = new ArrayList<>();
        list.addAll(util.getOntologyList(result));
        Log.i(TAG, "List of ontologies " + list.toString());
        return result;
    }

    public void unsubscribe() {
        if (subscription != null) {
            subscription.unsubscribe();
        }
    }
}
