package com.yahoo.reportr.ui.view.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yahoo.reportr.R;
import com.yahoo.reportr.data.entity.Blog;
import com.yahoo.reportr.data.entity.Ontology;
import com.yahoo.reportr.data.entity.Topic;
import com.yahoo.reportr.ui.view.activity.OntologyActivity;
import com.yahoo.reportr.ui.view.activity.OntologyItemActivity;
import com.yahoo.reportr.ui.view.activity.PostListActivity;
import com.yahoo.reportr.ui.view.fragment.OntologyFragment;
import com.yahoo.reportr.ui.view.fragment.OntologyItemFragment;
import com.yahoo.reportr.utils.CircleTransform;
import com.yahoo.reportr.utils.Constants;

/**
 * Created by bhavanis on 1/8/16.
 */
public class OntologyRecyclerAdapter extends RecyclerView.Adapter<OntologyRecyclerAdapter.OntologyItemViewHolder> {

    List<Ontology> parents;
    Context context;

    public OntologyRecyclerAdapter(Context context) {
        this.context = context;
    }

    @Override
    public OntologyItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.ontology_group, parent, false);
        return new OntologyItemViewHolder(parent.getContext(), itemView);
    }

    @Override
    public void onBindViewHolder(OntologyItemViewHolder holder, int position) {
        Ontology ontology = parents.get(position);
        holder.tvOntoGroup.setText(ontology.getName());
        holder.ivDrillDown.setVisibility(View.VISIBLE);
    }

    @Override
    public int getItemCount() {
        return parents.size();
    }

    public class OntologyItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public Context context;
        public TextView tvOntoGroup;
        private ImageView ivDrillDown;
        //public RecyclerView rvOntoItem;

        public OntologyItemViewHolder(Context context, View itemView) {
            super(itemView);
            this.context = context;
            itemView.setOnClickListener(this);
            tvOntoGroup = (TextView) itemView.findViewById(R.id.ontoGroup);
            ivDrillDown = (ImageView) itemView.findViewById(R.id.ivDrillDown);
            //rvOntoItem = (RecyclerView) itemView.findViewById(R.id.rvOntoItem);
        }

        @Override
        public void onClick(View v) {
            int position = getPosition();
            Ontology ontology = parents.get(position);
            Intent intent = new Intent(context, OntologyItemActivity.class);
            intent.putExtra(Constants.PARENT_ONTO, ontology);
            intent.putExtra(Constants.ONTOLOGY_KIDS, ontology.getKids().toArray());
            context.startActivity(intent);
        }
    }

    public void addAll(List<Ontology> ontologyList) {
        this.parents = new ArrayList<>();
        parents.addAll(ontologyList);
        notifyDataSetChanged();
    }
}
