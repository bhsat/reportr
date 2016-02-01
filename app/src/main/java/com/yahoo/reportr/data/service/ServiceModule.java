package com.yahoo.reportr.data.service;

import javax.inject.Named;
import javax.inject.Singleton;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;

import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.logging.HttpLoggingInterceptor;
import com.yahoo.reportr.utils.Constants;

import dagger.Module;
import dagger.Provides;

/**
 * Dependency injection module that provides objects that should live for the entire lifetime of the
 * application using this SDK. This module primarily focuses on the service/API endpoints for getting
 * data for the sdk and providing the required dependencies for the same.
 */
@Module
public class ServiceModule {

    public static final HttpUrl BASE_API_URL = HttpUrl.parse("http://dispatchr-stage.marketplace.yahoo.com:4080/marketplace/");
    //public static final HttpUrl BASE_API_URL = HttpUrl.parse("https://marketplace.yahoo.com");

    @Provides @Singleton
    HttpUrl provideReportrBaseUrl() {
        return BASE_API_URL;
    }

    @Provides @Singleton
    OkHttpClient provideOkHttpClient() {
        return new OkHttpClient();
    }

    @Provides @Singleton
    ReportrApi provideReportrApi(HttpUrl baseUrl, OkHttpClient client) {
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        client.interceptors().add(httpLoggingInterceptor);
        Retrofit retrofit = new Retrofit.Builder()
                .client(client)
                .baseUrl(baseUrl)
                //.addConverterFactory(new GsonStringConverterFactory())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        return retrofit.create(ReportrApi.class);
    }
}
