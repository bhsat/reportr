package com.yahoo.reportr.ui.view.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.annotation.UiThread;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.yahoo.reportr.R;
import com.yahoo.reportr.data.entity.AuthInfo;
import com.yahoo.reportr.data.entity.Ontology;
import com.yahoo.reportr.data.entity.User;
import com.yahoo.reportr.ui.DaggerReportrComponent;
import com.yahoo.reportr.ui.presenter.OntologyPresenter;
import com.yahoo.reportr.ui.presenter.ReportrPresenter;
//import com.yahoo.reportr.ui.view.adapter.OntologyAdapter;
import com.yahoo.reportr.ui.view.activity.UserActivity;
import com.yahoo.reportr.ui.view.adapter.OntologyRecyclerAdapter;
import com.yahoo.reportr.ui.view.views.ReportrOntologyView;
import com.yahoo.reportr.ui.view.views.ReportrView;
import com.yahoo.reportr.utils.Constants;
import com.yahoo.reportr.utils.DividerItemDecoration;
import com.yahoo.reportr.utils.OntologyUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bhavanis on 12/18/15.
 */
public class OntologyFragment extends Fragment implements ReportrView, ReportrOntologyView {

    private static final String TAG = OntologyFragment.class.getSimpleName();
    private ReportrPresenter reportrPresenter;
    private OntologyPresenter ontologyPresenter;
    private RecyclerView listView;
    //private OntologyAdapter ontologyAdapter;
    private OntologyRecyclerAdapter recyclerAdapter;
    private String authkey;
    private long tstamp;
    SharedPreferences preferences;
    private List<Integer> ids;
    private ImageView ivChecked;
    private SwipeRefreshLayout swipeRefreshLayout;
    private OntologyAsyncTask asyncTask;

    public static OntologyFragment newInstance(Bundle args) {
        OntologyFragment fragment = new OntologyFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        reportrPresenter = new ReportrPresenter();
        ontologyPresenter = new OntologyPresenter();
        //ontologyAdapter = new OntologyAdapter(getActivity());
        recyclerAdapter = new OntologyRecyclerAdapter(getActivity());
        DaggerReportrComponent.builder().build().inject(reportrPresenter);
        DaggerReportrComponent.builder().build().inject(ontologyPresenter);
        Bundle args = getArguments();
        authkey = args.getString(Constants.KEY_AUTHKEY);
        tstamp = args.getLong(Constants.KEY_TSTAMP);
        preferences = getActivity().getSharedPreferences(Constants.SHARED_PREF, 0);
        setHasOptionsMenu(true);
        ids = new ArrayList<>();
        fetchDataFromCache();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ontology, container, false);
        initializeView(view);;
        return view;
    }

    private void fetchDataFromCache() {
        String onto = preferences.getString(Constants.KEY_ONTOLOGY, "");
        Log.d(TAG, "Ontology from cache: " + onto);
        if (onto.isEmpty()) {
            fetchData();
        } else {
            addOntoToAdapter(onto);
        }
    }

    private void addOntoToAdapter(String onto) {
        List<Ontology> list = new ArrayList<>();
        OntologyUtil util = new OntologyUtil();
        list.addAll(util.getOntologyList(onto));
        recyclerAdapter.addAll(list);
        recyclerAdapter.notifyDataSetChanged();
    }

    private void fetchData() {
        asyncTask = new OntologyAsyncTask(this);
        asyncTask.execute();
    }

    public class OntologyAsyncTask extends AsyncTask<String, Void, String> {

        OntologyFragment fragment;
        String onto;
        public OntologyAsyncTask(OntologyFragment fragment){
            this.fragment = fragment;
        }

        @Override
        protected String doInBackground(String... params) {
            onto = reportrPresenter.getGlobalOntologies(authkey);
            return onto;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            fragment.swipeRefreshLayout.setRefreshing(true);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            fragment.swipeRefreshLayout.setRefreshing(false);
            if (s != null) {
                addOntoToAdapter(s);
            }
            preferences.edit().putString(Constants.KEY_ONTOLOGY, s).commit();
        }
    }
    @Override
    @UiThread
    public void addOntology(@NonNull List<Ontology> onto) {
        recyclerAdapter.addAll(onto);
        recyclerAdapter.notifyDataSetChanged();
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onDestroy() {
        unsubscribe();
        super.onDestroy();
    }

    public void unsubscribe() {
        reportrPresenter.unsubscribe();
    }

    private void initializeView(View view) {
        listView = (RecyclerView) view.findViewById(R.id.rvOntoList);
        listView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeOntoContainer);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchData();
            }
        });
        // Configure the refreshing colors
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        //listView.setGroupIndicator(null);
        //listView.setClickable(true);
        listView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.scrollToPosition(0);

        //Set LayoutManager to RecyclerView
        listView.setLayoutManager(layoutManager);
        listView.setAdapter(recyclerAdapter);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.onto_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_onto:
                Intent i = new Intent(getActivity(), UserActivity.class);
                startActivity(i);
                return true;
        }
        /*if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }*/
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void addReportrOntology(@NonNull List<Integer> ontology) {
        ids.addAll(ontology);

    }

    public void getReportrOntologies() {
        ontologyPresenter.getReportrOntologies(authkey);
    }
}
