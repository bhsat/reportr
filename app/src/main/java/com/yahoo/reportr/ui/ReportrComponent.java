package com.yahoo.reportr.ui;

import javax.inject.Singleton;

import com.yahoo.reportr.data.service.ServiceModule;
import com.yahoo.reportr.ui.presenter.BlogsPresenter;
import com.yahoo.reportr.ui.presenter.OntologyPresenter;
import com.yahoo.reportr.ui.presenter.ReportrPresenter;
import com.yahoo.reportr.ui.presenter.SubmissionPresenter;
import com.yahoo.reportr.ui.presenter.UserPresenter;

import dagger.Component;

@Singleton
@Component(
    modules = {
            ServiceModule.class,
            ReportrModule.class
    }
)
public interface ReportrComponent {
    void inject(ReportrPresenter presenter);
    void inject(UserPresenter presenter);
    void inject(OntologyPresenter presenter);
    void inject(SubmissionPresenter presenter);
    void inject(BlogsPresenter presenter);
}
