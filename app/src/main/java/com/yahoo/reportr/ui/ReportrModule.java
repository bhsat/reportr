package com.yahoo.reportr.ui;

import javax.inject.Singleton;
import com.yahoo.reportr.data.service.ReportrApi;
import com.yahoo.reportr.domain.interactor.ReportrInteractor;
import com.yahoo.reportr.domain.interactor.ReportrInteractorImpl;

import dagger.Module;
import dagger.Provides;

/**
 * Dependency injection module that provides objects that should live for the entire lifetime of the
 * application. This module primarily focuses on the reportr interactions.
 */
@Module
public final class ReportrModule {

    @Provides
    @Singleton
    ReportrInteractor provideReportrInteractor(ReportrApi reportrApi) {
        return new ReportrInteractorImpl(reportrApi);
    }
}
