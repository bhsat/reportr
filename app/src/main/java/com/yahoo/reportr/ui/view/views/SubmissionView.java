package com.yahoo.reportr.ui.view.views;

import android.support.annotation.NonNull;
import android.support.annotation.UiThread;

import com.yahoo.reportr.data.entity.Ontology;
import com.yahoo.reportr.data.entity.Submission;

import java.util.List;

/**
 * Created by bhavanis on 1/12/16.
 */
public interface SubmissionView {

    @UiThread
    void addSubmissions(@NonNull List<Submission> submissions);
}
