package com.yahoo.reportr.ui.view.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.yahoo.reportr.R;
import com.yahoo.reportr.data.entity.Ontology;
import com.yahoo.reportr.ui.view.activity.OntologyActivity;
import com.yahoo.reportr.ui.view.activity.OntologyItemActivity;
import com.yahoo.reportr.ui.view.fragment.OntologyFragment;
import com.yahoo.reportr.ui.view.fragment.OntologyItemFragment;
import com.yahoo.reportr.utils.Constants;

/**
 * Created by bhavanis on 1/8/16.
 */
public class OntologyKidRecyclerAdapter extends RecyclerView.Adapter<OntologyKidRecyclerAdapter.OntologyKidViewHolder> {

    List<Ontology> kids;
    Context mContext;
    List<Integer> reportrOntologyIds;
    OntologyItemFragment fragment;

    public OntologyKidRecyclerAdapter(Context context, OntologyItemFragment itemFragment) {
        this.kids = new ArrayList<>();
        this.mContext = context;
        this.fragment = itemFragment;
    }

    @Override
    public OntologyKidViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.ontology_item, parent, false);
        return new OntologyKidViewHolder(parent.getContext(), itemView);
    }

    @Override
    public void onBindViewHolder(OntologyKidViewHolder holder, int position) {
        Ontology ontology = kids.get(position);
        holder.tvOntoItem.setText(ontology.getName());
        holder.ivChecked.setImageResource(R.drawable.ic_checked);
        if (reportrOntologyIds.contains(ontology.getId())) {
            holder.ivChecked.setVisibility(View.VISIBLE);
        } else {
            holder.ivChecked.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return kids.size();
    }

    public class OntologyKidViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public Context context;
        public TextView tvOntoItem;
        public ImageView ivChecked;

        public OntologyKidViewHolder(Context context, View itemView) {
            super(itemView);
            this.context = context;
            itemView.setOnClickListener(this);
            tvOntoItem = (TextView) itemView.findViewById(R.id.ontoItem);
            ivChecked = (ImageView) itemView.findViewById(R.id.ivChecked);
        }

        @Override
        public void onClick(View v) {
            int position = getPosition();
            Ontology ontology = kids.get(position);
            if (reportrOntologyIds.contains(ontology.getId())) {
                ivChecked.setVisibility(View.INVISIBLE);
                reportrOntologyIds.remove((Integer) ontology.getId());
            } else {
                ivChecked.setVisibility(View.VISIBLE);
                reportrOntologyIds.add((Integer)ontology.getId());
            }
            //OntologyItemFragment fragment = OntologyItemFragment.newInstance(new Bundle());
            fragment.saveReportrOntologies(reportrOntologyIds);
            //fragment.getReportrOntologies();
            //Toast.makeText(context, String.valueOf(ontology.getId()), Toast.LENGTH_LONG).show();
        }
    }

    public void addAll(List<Ontology> ontologyList) {
        kids.addAll(ontologyList);
        notifyDataSetChanged();
    }

    public void addReportrOntologies(List<Integer> ids) {
        this.reportrOntologyIds = new ArrayList<>();
        reportrOntologyIds.addAll(ids);
        notifyDataSetChanged();
    }
}
