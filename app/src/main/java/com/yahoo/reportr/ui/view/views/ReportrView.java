package com.yahoo.reportr.ui.view.views;

import android.support.annotation.NonNull;
import android.support.annotation.UiThread;

import com.yahoo.reportr.data.entity.Ontology;
import com.yahoo.reportr.data.entity.User;

import java.util.List;

/**
 * Created by bhavanis on 12/17/15.
 */
public interface ReportrView {

    @UiThread
    void addOntology(@NonNull List<Ontology> ontology);
}
