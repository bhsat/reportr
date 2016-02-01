package com.yahoo.reportr.data.service;

import com.google.gson.JsonObject;
import com.yahoo.reportr.data.entity.ApiResponse;
import com.yahoo.reportr.data.entity.Ontology;

import java.util.List;

import retrofit.Call;
import retrofit.http.Body;
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
 * Created by bhavanis on 12/16/15.
 */
public interface ReportrApi {

    String HEADER = "authHash: 3086514801f8b0af75112217a2ad7963";
    String APPLICATION_JSON = "Content-Type: application/json";

    @FormUrlEncoded
    @POST("/marketplace/reportr/v1/authorize")
    Call<ApiResponse> authorize(@Field("userName") String username, @Field("password") String password, @Field("deviceStr") String deviceStr);

    @Headers(APPLICATION_JSON)
    @POST("/marketplace/reportr/v1/user/device")
    Call<ApiResponse> registerDevice(@Header("authHash") String authkey, @Body JsonObject body);

    @Headers(APPLICATION_JSON)
    @GET("/marketplace/reportr/v1/global-ontologies")
    Call<ApiResponse> getGlobalOntologies(@Header("authHash") String authkey);

    @Headers(APPLICATION_JSON)
    @GET("/marketplace/reportr/v1/user")
    Call<ApiResponse> getUserInfo(@Header("authHash") String authkey);

    @Headers(APPLICATION_JSON)
    @GET("/marketplace/reportr/v1/topics")
    Observable<ApiResponse> getTopics(@Header("authHash") String authkey);

    @Headers(APPLICATION_JSON)
    @GET("/marketplace/reportr/v1/tumblr/user/info")
    Call<ApiResponse> getBlogs(@Header("authHash") String authkey);

    @Headers(APPLICATION_JSON)
    @GET("/marketplace/reportr/v1/tumblr/blog/{name}/posts")
    Call<ApiResponse> getBlogPosts(@Header("authHash") String authkey, @Path("name") String name);

    @Headers(APPLICATION_JSON)
    @GET("/marketplace/reportr/v1/ontologies")
    Observable<ApiResponse> getReportrOntologies(@Header("authHash") String authkey);

    @Headers(APPLICATION_JSON)
    @POST("/marketplace/reportr/v1/ontologies")
    Call<ApiResponse> saveReportrOntologies(@Header("authHash") String authkey, @Body List<Integer> ids);

    @Headers(APPLICATION_JSON)
    @GET("/marketplace/reportr/v1/submissions")
    Observable<ApiResponse> getSubmissions(@Header("authHash") String authkey);

    @Headers(APPLICATION_JSON)
    @GET("/marketplace/reportr/v1/submission/{submissionId}")
    Observable<ApiResponse> getSubmissionById(@Header("authHash") String authkey, @Path("submissionId") Integer submissionId);

    @Headers(APPLICATION_JSON)
    @POST("/marketplace/reportr/v1/submission")
    Call<ApiResponse> createSubmission(@Header("authHash") String authkey, @Body JsonObject body);

    @Headers(APPLICATION_JSON)
    @FormUrlEncoded
    @PUT("/marketplace/reportr/v1/submission/{submissionId}")
    Observable<ApiResponse> updateSubmission(@Header("authHash") String authkey, @Path("submissionId") Integer submissionId, @Field("blogName") String blogName,
                                       @Field("postId") Long postId, @Field("topicId") Integer topicId);
}
