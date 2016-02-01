package com.yahoo.reportr.ui.presenter;

import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.yahoo.reportr.data.entity.ApiResponse;
import com.yahoo.reportr.domain.interactor.ReportrInteractor;
import com.yahoo.reportr.ui.DaggerReportrComponent;
import com.yahoo.reportr.ui.view.views.ReportrOntologyView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by bhavanis on 1/12/16.
 */
public class OntologyPresenter {

    private static final String TAG = OntologyPresenter.class.getSimpleName();
    private static final int HTTP_OK = 200;
    @Inject
    ReportrInteractor reportrInteractor;

    private ReportrOntologyView reportrOntoView;
    private Gson json;
    private JsonParser jsonParser;
    private boolean saveSuccess = false;

    public OntologyPresenter() {
        DaggerReportrComponent.builder().build().inject(this);
        json = new Gson();
        jsonParser = new JsonParser();
    }

    public void getReportrOntologies(String authkey) {
        reportrInteractor.getReportrOntologies(authkey)
                .subscribeOn(Schedulers.from(AsyncTask.THREAD_POOL_EXECUTOR))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<ApiResponse>() {
                    @Override
                    public void call(@NonNull final ApiResponse data) {
                        if (data != null) {
                            handleReportrOntologies(data);
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {

                    }
                });
    }

    public void saveReportrOntologies(String authkey, List<Integer> ids) {
        Call<ApiResponse> call = reportrInteractor.saveReportrOntologies(authkey, ids);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Response<ApiResponse> response, Retrofit retrofit) {
            }
            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    private void handleReportrOntologies(ApiResponse data) {
        List<Integer> ontoIds = new ArrayList<>();
        String result = json.toJson(data.getData());
        JsonArray arr = jsonParser.parse(result).getAsJsonArray();
        for (JsonElement ids : arr) {
            ontoIds.add(ids.getAsInt());
        }
        reportrOntoView.addReportrOntology(ontoIds);
    }
}
