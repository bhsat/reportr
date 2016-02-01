package com.yahoo.reportr.domain.interactor;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.yahoo.reportr.data.entity.ApiResponse;
import com.yahoo.reportr.data.service.ReportrApi;

import java.util.List;

import retrofit.Call;
import rx.Observable;

/**
 * Created by bhavanis on 12/17/15.
 */
public class ReportrInteractorImpl implements ReportrInteractor {

    private final ReportrApi reportrApi;

    public ReportrInteractorImpl(ReportrApi reportrApi) {
        this.reportrApi = reportrApi;
    }

    @Override
    public Call<ApiResponse> authorize(String username, String password, String deviceStr) {
        return reportrApi.authorize(username, password, deviceStr);
    }

    @Override
    public Call<ApiResponse> getGlobalOntologies(String authkey) {
        return reportrApi.getGlobalOntologies(authkey);
    }

    @Override
    public Call<ApiResponse> getUserInfo(String authkey) {
        return reportrApi.getUserInfo(authkey);
    }

    @Override
    public Call<ApiResponse> registerDevice(String authkey, String token) {
        JsonObject obj = new JsonObject();
        obj.addProperty("token", token);
        return reportrApi.registerDevice(authkey, obj);
    }

    @Override
    public Observable<ApiResponse> getTopics(String authkey) {
        return reportrApi.getTopics(authkey);
    }

    @Override
    public Call<ApiResponse> getBlogs(String authkey) {
        return reportrApi.getBlogs(authkey);
    }

    @Override
    public Call<ApiResponse> getBlogPosts(String authkey, String name) {
        return reportrApi.getBlogPosts(authkey, name);
    }

    @Override
    public Observable<ApiResponse> getReportrOntologies(String authkey) {
        return reportrApi.getReportrOntologies(authkey);
    }

    @Override
    public Call<ApiResponse> saveReportrOntologies(String authkey, List<Integer> ontologyIds) {
        return reportrApi.saveReportrOntologies(authkey, ontologyIds);
    }

    @Override
    public Observable<ApiResponse> getSubmissions(String authkey) {
        return reportrApi.getSubmissions(authkey);
    }

    @Override
    public Observable<ApiResponse> getSubmissionById(String authkey, Integer submissionId) {
        return reportrApi.getSubmissionById(authkey, submissionId);
    }

    @Override
    public Call<ApiResponse> createSubmission(String authkey, String blogName, Long postId, Integer topicId) {
        JsonObject obj = new JsonObject();
        obj.addProperty("blogName", blogName);
        obj.addProperty("postId", postId);
        obj.addProperty("topicId", topicId);
        return reportrApi.createSubmission(authkey, obj);
    }

    @Override
    public Observable<ApiResponse> updateSubmission(String authkey, Integer submissionId, String blogName, Long postId, Integer topicId) {
        return reportrApi.updateSubmission(authkey, submissionId, blogName, postId, topicId);
    }

}
