package com.yahoo.reportr.ui.presenter;

import java.io.IOException;
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

import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.yahoo.reportr.data.entity.ApiResponse;
import com.yahoo.reportr.data.entity.Submission;
import com.yahoo.reportr.domain.interactor.ReportrInteractor;
import com.yahoo.reportr.ui.DaggerReportrComponent;
import com.yahoo.reportr.ui.view.views.SubmissionView;
import com.yahoo.reportr.utils.Constants;

/**
 * Created by bhavanis on 1/12/16.
 */
public class SubmissionPresenter {

    private static final String TAG = SubmissionPresenter.class.getSimpleName();
    private static final int HTTP_OK = 200;
    @Inject
    ReportrInteractor reportrInteractor;

    private SubmissionView submissionView;
    private Subscription subscription;
    private Gson json;
    private JsonParser jsonParser;
    private List<Submission> submissions;
    private Submission submissionById;
    private boolean saveSuccess = false;
    private String submissionResponse;
    private Integer updateSubmissionId;

    public SubmissionPresenter(@NonNull SubmissionView view) {
        submissionView = view;
        DaggerReportrComponent.builder().build().inject(this);
        json = new Gson();
        jsonParser = new JsonParser();
    }

    public List<Submission> getSubmissions(String authkey) {
        submissions = new ArrayList<>();
        subscription = reportrInteractor.getSubmissions(authkey)
                .subscribeOn(Schedulers.from(AsyncTask.THREAD_POOL_EXECUTOR))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<ApiResponse>() {
                    @Override
                    public void call(@NonNull final ApiResponse data) {
                        if (data != null) {
                            submissions.addAll(handleSubmissions(data));
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {

                    }
                });
        return submissions;
    }

    public Submission getSubmissionById(String authkey, Integer submissionId) {
        submissionById = new Submission();
        reportrInteractor.getSubmissionById(authkey, submissionId)
                .subscribeOn(Schedulers.from(AsyncTask.THREAD_POOL_EXECUTOR))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<ApiResponse>() {
                    @Override
                    public void call(@NonNull final ApiResponse data) {
                        if (data != null) {
                            handleSubmissionById(data);
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {

                    }
                });
        return submissionById;
    }

    public String createSubmission(String authkey, String blogName, Long postId, Integer topicId) {
        Call<ApiResponse> call = reportrInteractor.createSubmission(authkey, blogName, postId, topicId);
        try {
            Response<ApiResponse> response = call.execute();
            if (response.code() == HTTP_OK) {
                submissionResponse = Constants.SUBMISSION_SUCCESS;
            } else {
                submissionResponse = handleCreateSubmission(response.errorBody().string());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return submissionResponse;
    }

    private String handleCreateSubmission(String response) {
        String message = "";
        JsonObject obj = jsonParser.parse(response).getAsJsonObject();
        Log.d(TAG, "Submission response " + obj.toString());
        if (obj.has("data")) {
            JsonObject body = obj.get("data").getAsJsonObject();
            if (body.has("message")) {
                message = body.get("message").getAsString();
            }
        }
        return message;
    }

    public Integer updateSubmission(String authkey, Integer submissionId, String blogName,
                                    Long postId, Integer topicId) {
        reportrInteractor.updateSubmission(authkey, submissionId, blogName, postId, topicId)
                .subscribeOn(Schedulers.from(AsyncTask.THREAD_POOL_EXECUTOR))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<ApiResponse>() {
                    @Override
                    public void call(@NonNull final ApiResponse data) {
                        if (data != null) {
                            String result = json.toJson(data.getData());
                            JsonObject obj = jsonParser.parse(result).getAsJsonObject();
                            if (obj.has("id")) {
                                updateSubmissionId = obj.get("id").getAsInt();
                            }
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {

                    }
                });
        return updateSubmissionId;
    }

    private void handleSubmissionById(ApiResponse data) {
        String result = json.toJson(data.getData());
        JsonObject obj = jsonParser.parse(result).getAsJsonObject();
        parseSubmission(obj, submissionById);
    }

    private List<Submission> handleSubmissions(ApiResponse data) {
        List<Submission> submissionList = new ArrayList<>();
        String result = json.toJson(data.getData());
        JsonArray arr = jsonParser.parse(result).getAsJsonArray();
        for (JsonElement jsonElement : arr) {
            Submission submission = new Submission();
            JsonObject obj = jsonElement.getAsJsonObject();
            parseSubmission(obj, submission);
            submissionList.add(submission);
        }
        return submissionList;
    }

    private void parseSubmission(JsonObject obj, Submission submission) {
        if (obj != null) {
            if (obj.has("submissionId")) {
                submission.setSubmissionId(obj.get("submissionId").getAsInt());
            }
            if (obj.has("tumblrUrl")) {
                submission.setTumblrUrl(obj.get("tumblrUrl").getAsString());
            }
            if (obj.has("reporterId")) {
                submission.setReporterId(obj.get("reporterId").getAsInt());
            }
            if (obj.has("topicId")) {
                submission.setTopicId(obj.get("topicId").getAsInt());
            }
            if (obj.has("evaluateTime")) {
                submission.setEvaluateTime(obj.get("evaluateTime").getAsLong());
            }
            if (obj.has("createdTime")) {
                submission.setCreatedTime(obj.get("createdTime").getAsLong());
            }
            if (obj.has("accepted")) {
                submission.setAccepted(obj.get("accepted").getAsBoolean());
            }
            if (obj.has("reporterEmail")) {
                submission.setReporterEmail(obj.get("reporterEmail").getAsString());
            }
            if (obj.has("postId")) {
                submission.setPostId(obj.get("postId").getAsLong());
            }
            if (obj.has("blogName")) {
                submission.setBlogName(obj.get("blogName").getAsString());
            }
        }
    }

    public void unsubscribe() {
        if (subscription != null) {
            subscription.unsubscribe();
        }
    }
}
