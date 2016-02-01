package com.yahoo.reportr.ui.view.activity;

import android.app.ActivityManager;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.yahoo.reportr.R;
import com.yahoo.reportr.data.entity.Blog;
import com.yahoo.reportr.data.service.BlogListService;
import com.yahoo.reportr.ui.view.fragment.OntologyFragment;
import com.yahoo.reportr.utils.Constants;
import com.yahoo.reportr.utils.MemCache;

import java.util.ArrayList;
import java.util.List;

public class OntologyActivity extends AppCompatActivity {

    private String authkey;
    private Long timestamp;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ontology);
        authkey = getIntent().getStringExtra(Constants.KEY_AUTHKEY);
        timestamp = getIntent().getLongExtra(Constants.KEY_TSTAMP, 0);
        Bundle args = new Bundle();
        args.putString(Constants.KEY_AUTHKEY, authkey);
        args.putLong(Constants.KEY_TSTAMP, timestamp);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Select Categories");

        OntologyFragment ontoFragment = OntologyFragment.newInstance(args);
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.flOntology, ontoFragment).commit();
    }

}
