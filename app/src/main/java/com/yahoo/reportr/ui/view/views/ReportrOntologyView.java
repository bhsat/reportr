package com.yahoo.reportr.ui.view.views;

import java.util.List;

import android.support.annotation.NonNull;
import android.support.annotation.UiThread;

import com.yahoo.reportr.data.entity.Ontology;

/**
 * Created by bhavanis on 12/17/15.
 */
public interface ReportrOntologyView {

    @UiThread
    void addReportrOntology(@NonNull List<Integer> ontology);
}
