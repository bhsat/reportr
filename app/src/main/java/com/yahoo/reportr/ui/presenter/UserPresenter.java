package com.yahoo.reportr.ui.presenter;

import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.yahoo.reportr.data.entity.ApiResponse;
import com.yahoo.reportr.data.entity.Topic;
import com.yahoo.reportr.data.entity.User;
import com.yahoo.reportr.domain.interactor.ReportrInteractor;
import com.yahoo.reportr.ui.DaggerReportrComponent;
import com.yahoo.reportr.ui.view.views.UserView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import retrofit.Call;
import retrofit.Response;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by bhavanis on 1/8/16.
 */
public class UserPresenter {

    private static final String TAG = UserPresenter.class.getSimpleName();
    private static final int HTTP_OK = 200;
    @Inject
    ReportrInteractor reportrInteractor;

    private Gson json;
    private JsonParser jsonParser;
    private List<Topic> topicList;
    private User user;

    public UserPresenter() {
        DaggerReportrComponent.builder().build().inject(this);
        json = new Gson();
        jsonParser = new JsonParser();
    }

    public User getUserInfo(String authkey) {
        Call<ApiResponse> call = reportrInteractor.getUserInfo(authkey);
        try {
            Response<ApiResponse> response = call.execute();
            if (response.code() == HTTP_OK) {
                user = handleUserInfo(response.body());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return user;
    }

    public List<Topic> getTopics(String authkey) {
        topicList = new ArrayList<>();
        reportrInteractor.getTopics(authkey)
                .subscribeOn(Schedulers.from(AsyncTask.THREAD_POOL_EXECUTOR))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<ApiResponse>() {
                    @Override
                    public void call(@NonNull final ApiResponse data) {
                        if (data != null) {
                            topicList.addAll(handleUserTopics(data));
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {

                    }
                });
        return topicList;
    }

    private List<Topic> handleUserTopics(ApiResponse data) {
        String result = json.toJson(data.getData());
        JsonArray obj = jsonParser.parse(result).getAsJsonArray();
        List<Topic> topics = new ArrayList<>();
        topics.addAll(parseTopics(obj));
        //userView.addTopics(topics);
        return topics;
    }

    private User handleUserInfo(ApiResponse response) {
        String result = json.toJson(response.getData());
        JsonObject obj = jsonParser.parse(result).getAsJsonObject();
        User user = new User();
        if (obj != null) {
            if (obj.has("emailId")) {
                user.setEmailId(obj.get("emailId").getAsString());
            }
            if (obj.has("balance")) {
                user.setBalance(obj.get("balance").getAsInt());
            }
            if (obj.has("ontologies")) {
                List<Integer> ontologies = new ArrayList<>();
                JsonArray ontoList = obj.getAsJsonArray("ontologies");
                for (JsonElement ontoId : ontoList) {
                    ontologies.add(ontoId.getAsInt());
                }
                user.setOntologies(ontologies);
            }
            if (obj.has("topics")) {
                JsonArray topics = obj.getAsJsonArray("topics");
                List<Topic> topicList = new ArrayList<>();
                topicList.addAll(parseTopics(topics));
                user.setTopics(topicList);
            }
        }
        Log.d(TAG, new Gson().toJson(user));
        return user;
    }

    private List<Topic> parseTopics(JsonArray topics) {
        List<Topic> topicList = new ArrayList<>();
        for (JsonElement topicElement : topics) {
            JsonObject topicObj = topicElement.getAsJsonObject();
            Topic topic = new Topic();
            if (topicObj.has("id")) {
                topic.setId(topicObj.get("id").getAsInt());
            }
            if (topicObj.has("amount")) {
                topic.setAmount(topicObj.get("amount").getAsInt());
            }
            if (topicObj.has("createDate")) {
                topic.setCreateDate(new Date(topicObj.get("createDate").getAsLong()));
            }
            if (topicObj.has("summary")) {
                topic.setSummary(topicObj.get("summary").getAsString());
            }
            if (topicObj.has("description")) {
                topic.setDescription(topicObj.get("description").getAsString());
            }
            if (topicObj.has("expires")) {
                topic.setExpires(new Date(topicObj.get("expires").getAsLong()));
            }
            if (topicObj.has("embargoUntil")) {
                topic.setEmbargoUntil(new Date(topicObj.get("embargoUntil").getAsLong()));
            }
            if (topicObj.has("active")) {
                topic.setActive(topicObj.get("active").getAsBoolean());
            }
            if (topicObj.has("paid")) {
                topic.setPaid(topicObj.get("paid").getAsBoolean());
            }
            if (topicObj.has("submissionsCount")) {
                topic.setSubmissionsCount(topicObj.get("submissionsCount").getAsInt());
            }
            if (topicObj.has("ontologies")) {
                Set<Integer> ontologies = new HashSet<>();
                JsonArray ontoArr = topicObj.getAsJsonArray("ontologies");
                for (JsonElement onto : ontoArr) {
                    ontologies.add(onto.getAsInt());
                }
                topic.setOntologies(ontologies);
            }
            topicList.add(topic);
        }
        return topicList;
    }
}
