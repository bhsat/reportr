package com.yahoo.reportr.ui.view.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.yahoo.reportr.R;
import com.yahoo.reportr.data.entity.Ontology;
import com.yahoo.reportr.data.entity.Topic;
import com.yahoo.reportr.data.entity.User;
import com.yahoo.reportr.ui.DaggerReportrComponent;
import com.yahoo.reportr.ui.presenter.OntologyPresenter;
import com.yahoo.reportr.ui.view.adapter.OntologyKidRecyclerAdapter;
import com.yahoo.reportr.ui.view.views.ReportrOntologyView;
import com.yahoo.reportr.utils.Constants;
import com.yahoo.reportr.utils.DividerItemDecoration;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by bhavanis on 1/21/16.
 */
public class OntologyItemFragment extends Fragment implements ReportrOntologyView{

    private Ontology parentOntology;
    private List<Ontology> kids;
    private RecyclerView listView;
    private List<Integer> reportrOntologyIds;
    private OntologyKidRecyclerAdapter recyclerAdapter;
    SharedPreferences preferences;
    private OntologyPresenter ontologyPresenter;
    private String authkey;
    OntologyItemFragment fragment;

    public static OntologyItemFragment newInstance(Bundle args) {
        OntologyItemFragment fragment = new OntologyItemFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ontologyPresenter = new OntologyPresenter();
        DaggerReportrComponent.builder().build().inject(ontologyPresenter);
        Bundle args = getArguments();
        parentOntology = args.getParcelable(Constants.PARENT_ONTO);
        Serializable s = args.getSerializable(Constants.ONTOLOGY_KIDS);
        preferences = getActivity().getSharedPreferences(Constants.SHARED_PREF, 0);
        authkey = preferences.getString(Constants.KEY_AUTHKEY, "");
        recyclerAdapter = new OntologyKidRecyclerAdapter(getActivity(), OntologyItemFragment.this);
        getReportrOntologiesFromCache();
        serializeKids(s);
        recyclerAdapter.addAll(kids);
    }

    private void serializeKids(Serializable s) {
        Object[] o = (Object[]) s;
        if (o != null) {
            kids = new ArrayList<>();
            for (int i = 0; i < o.length; i++) {
                if (o[i] instanceof Ontology) {
                    kids.add((Ontology) o[i]);
                }
            }
        }
    }

    private void getReportrOntologiesFromCache() {
        String str = preferences.getString(Constants.USER_ONTO, null);
        System.out.println("Onto string: "+str);
        List<Integer> ids = new ArrayList<>();
        if (str != null) {
            String arr = str.substring(1, str.length() - 1);
            for (String s : arr.split(",")) {
                ids.add(Integer.valueOf(s.trim()));
            }
        }
        recyclerAdapter.addReportrOntologies(ids);
        recyclerAdapter.notifyDataSetChanged();
        //User user = new Gson().fromJson(json, User.class);
        //reportrOntologyIds.addAll(user.getOntologies());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_child_ontology, container, false);
        initializeView(v);
        //setupListener();
        return v;
    }

    private void initializeView(View view) {
        listView = (RecyclerView) view.findViewById(R.id.rvOntoItem);
        listView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));

        listView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.scrollToPosition(0);

        //Set LayoutManager to RecyclerView
        listView.setLayoutManager(layoutManager);
        listView.setAdapter(recyclerAdapter);
        //listView.setGroupIndicator(null);
        //listView.setClickable(true);
    }

    public void getReportrOntologies() {
        ontologyPresenter.getReportrOntologies(authkey);
    }

    public void saveReportrOntologies(List<Integer> ids) {
        ontologyPresenter.saveReportrOntologies(authkey, ids);
        if (!ids.isEmpty()) {
            preferences.edit().putString(Constants.USER_ONTO, ids.toString()).commit();
        } else {
            preferences.edit().putString(Constants.USER_ONTO, null).commit();
        }
        recyclerAdapter.addReportrOntologies(ids);
        recyclerAdapter.notifyDataSetChanged();
    }

    @Override
    public void addReportrOntology(@NonNull List<Integer> ontology) {
        if (ontology != null) {
            preferences.edit().putString(Constants.USER_ONTO, ontology.toString()).commit();
        } else {
            preferences.edit().putString(Constants.USER_ONTO, null).commit();
        }
        recyclerAdapter.addReportrOntologies(ontology);
        recyclerAdapter.notifyDataSetChanged();
    }
}
