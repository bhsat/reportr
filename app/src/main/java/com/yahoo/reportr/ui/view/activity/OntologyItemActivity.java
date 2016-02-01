package com.yahoo.reportr.ui.view.activity;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.yahoo.reportr.R;
import com.yahoo.reportr.data.entity.Ontology;
import com.yahoo.reportr.ui.view.fragment.OntologyFragment;
import com.yahoo.reportr.ui.view.fragment.OntologyItemFragment;
import com.yahoo.reportr.utils.Constants;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class OntologyItemActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private Ontology parent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ontology_item);
        //authkey = getIntent().getStringExtra(Constants.KEY_AUTHKEY);
        //timestamp = getIntent().getLongExtra(Constants.KEY_TSTAMP, 0);
        parent = getIntent().getParcelableExtra(Constants.PARENT_ONTO);
        Serializable s = getIntent().getSerializableExtra(Constants.ONTOLOGY_KIDS);
        Bundle args = new Bundle();
        args.putParcelable(Constants.PARENT_ONTO, parent);
        args.putSerializable(Constants.ONTOLOGY_KIDS, s);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Select Categories");
        OntologyItemFragment ontoFragment = OntologyItemFragment.newInstance(args);
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.flOntologyItem, ontoFragment).commit();
    }

}
