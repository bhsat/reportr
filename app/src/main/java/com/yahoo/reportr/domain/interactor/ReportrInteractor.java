package com.yahoo.reportr.domain.interactor;

import com.yahoo.reportr.data.entity.ApiResponse;
import com.yahoo.reportr.data.entity.Ontology;

import java.util.List;

import retrofit.Call;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.Headers;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;
import rx.Observable;

/**
 * Created by bhavanis on 12/17/15.
 */
public interface ReportrInteractor {

    Call<ApiResponse> authorize(String username, String password, String deviceStr);

    Call<ApiResponse> getGlobalOntologies(String authkey);

    Call<ApiResponse> getUserInfo(String authkey);

    Call<ApiResponse> registerDevice(String authkey, String token);

    Observable<ApiResponse> getTopics(String authkey);

    Call<ApiResponse> getBlogs(String authkey);

    Call<ApiResponse> getBlogPosts(String authkey, String name);

    Observable<ApiResponse> getReportrOntologies(String authkey);

    Call<ApiResponse> saveReportrOntologies(String authkey, List<Integer> ontologyIds);

    Observable<ApiResponse> getSubmissions(String authkey);

    Observable<ApiResponse> getSubmissionById(String authkey, Integer submissionId);

    Call<ApiResponse> createSubmission(String authkey, String blogName,
                                             Long postId, Integer topicId);

    Observable<ApiResponse> updateSubmission(String authkey, Integer submissionId, String blogName,
                                             Long postId, Integer topicId);
}
