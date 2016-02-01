package com.yahoo.reportr.ui.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yahoo.reportr.R;
import com.yahoo.reportr.data.entity.Ontology;
import com.yahoo.reportr.data.entity.Topic;
import com.yahoo.reportr.ui.view.activity.TopicActivity;
import com.yahoo.reportr.utils.Constants;
import com.yahoo.reportr.utils.OntologyUtil;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by bhavanis on 1/8/16.
 */
public class TopicsRecyclerAdapter extends RecyclerView.Adapter<TopicsRecyclerAdapter.TopicItemViewHolder> {

    private List<Topic> topics;
    SharedPreferences preferences;
    Map<Integer, String> ontoMap;
    private String authKey;

    public TopicsRecyclerAdapter(Context context, String authKey) {
        this.authKey = authKey;
        ontoMap = new HashMap<>();
        preferences = context.getSharedPreferences(Constants.SHARED_PREF, 0);
        ontoMap = getOntoMap();
        this.topics = new ArrayList<>();
    }

    @Override
    public TopicItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.fragment_topicitem, parent, false);
        return new TopicItemViewHolder(parent.getContext(), itemView);
    }

    @Override
    public void onBindViewHolder(TopicItemViewHolder holder, int position) {
        Topic topic = topics.get(position);
        holder.tvSummary.setText(topic.getSummary());
        holder.tvDesc.setText(topic.getDescription());
        StringBuffer buffer = new StringBuffer();
        for (Integer id : topic.getOntologies()) {
            if (ontoMap.containsKey(id)) {
                buffer.append(ontoMap.get(id)).append(",");
            }
        }
        String text = buffer.substring(0, buffer.lastIndexOf(","));
        holder.tvTopic.setText(text);
        GregorianCalendar today = new GregorianCalendar();
        long diff = topic.getExpires().getTime() - today.getTime().getTime();
        holder.tvExpiry.setText(TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS)+" DAYS REMAINING");
        holder.tvCoins.setText(topic.getAmount()+" COINS");
    }

    @Override
    public int getItemCount() {
        return topics.size();
    }

    public class TopicItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView tvTopic;
        public Context context;
        public TextView tvSummary;
        public TextView tvDesc;
        public TextView tvExpiry;
        public TextView tvCoins;

        public TopicItemViewHolder(Context context, View itemView) {
            super(itemView);
            this.context = context;
            itemView.setOnClickListener(this);
            tvTopic = (TextView) itemView.findViewById(R.id.tvTopic);
            tvSummary = (TextView) itemView.findViewById(R.id.tvSummary);
            tvDesc = (TextView) itemView.findViewById(R.id.tvDesc);
            tvExpiry = (TextView) itemView.findViewById(R.id.tvExpiry);
            tvCoins = (TextView) itemView.findViewById(R.id.tvCoins);
        }

        @Override
        public void onClick(View v) {
            int position = getPosition();
            Topic topic = topics.get(position);
            Intent intent = new Intent(context, TopicActivity.class);
            intent.putExtra(Constants.KEY_TOPIC, topic);
            StringBuffer buffer = new StringBuffer();
            for (Integer id : topic.getOntologies()) {
                if (ontoMap.containsKey(id)) {
                    buffer.append(ontoMap.get(id)).append(",");
                }
            }
            String text = buffer.substring(0, buffer.lastIndexOf(","));
            intent.putExtra(Constants.KEY_TOPIC_NAME, text);
            intent.putExtra(Constants.KEY_AUTHKEY, authKey);
            context.startActivity(intent);
        }
    }

    public void addAll(List<Topic> topicList) {
        topics.clear();
        topics.addAll(topicList);
        notifyDataSetChanged();
    }

    private Map<Integer, String> getOntoMap() {
        String onto = preferences.getString(Constants.KEY_ONTOLOGY, null);
        if (onto != null) {
            List<Ontology> list = new ArrayList<>();
            OntologyUtil util = new OntologyUtil();
            list.addAll(util.getOntologyList(onto));
            for (Ontology ontology : list) {
                ontoMap.put(ontology.getId(), ontology.getName());
                for (Ontology child : ontology.getKids()) {
                    ontoMap.put(child.getId(), child.getName());
                }
            }
        }
        return ontoMap;
    }
}
