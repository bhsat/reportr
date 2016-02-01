package com.yahoo.reportr.ui.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.yahoo.reportr.R;
import com.yahoo.reportr.data.entity.Topic;
import com.yahoo.reportr.ui.view.activity.BlogListActivity;
import com.yahoo.reportr.utils.Constants;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.concurrent.TimeUnit;

/**
 * Created by bhavanis on 1/13/16.
 */
public class TopicFragment extends Fragment {

    private Topic topic;
    private String topicName;
    private TextView tvTopicName;
    private TextView tvTopicSummary;
    private TextView tvTopicDesc;
    private TextView tvTopicExpires;
    private TextView tvTopicAmount;
    private Button btnTopic;
    private String authkey;

    public static TopicFragment newInstance(Bundle args) {
        TopicFragment fragment = new TopicFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        topic = args.getParcelable(Constants.KEY_TOPIC);
        topicName = args.getString(Constants.KEY_TOPIC_NAME);
        authkey = args.getString(Constants.KEY_AUTHKEY);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_topicdetail, container, false);
        initializeView(view);
        setupListener();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setText();
    }

    private void setupListener() {
        btnTopic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), BlogListActivity.class);
                i.putExtra(Constants.KEY_TOPIC, topic);
                i.putExtra(Constants.KEY_AUTHKEY, authkey);
                startActivity(i);

            }
        });
    }

    private void initializeView(View view) {
        tvTopicName = (TextView) view.findViewById(R.id.tvTopicName);
        tvTopicSummary = (TextView) view.findViewById(R.id.tvTopicSummary);
        tvTopicDesc = (TextView) view.findViewById(R.id.tvTopicDescription);
        tvTopicExpires = (TextView) view.findViewById(R.id.tvTopicExpires);
        tvTopicAmount = (TextView) view.findViewById(R.id.tvTopicAmount);
        btnTopic = (Button) view.findViewById(R.id.btnTopic);
    }

    private void setText() {
        tvTopicName.setText(topicName);
        tvTopicSummary.setText(topic.getSummary());
        tvTopicDesc.setText(topic.getDescription());
        DateFormat df = new SimpleDateFormat("MMMM d, hh:mm a");
        GregorianCalendar today = new GregorianCalendar();
        long diff = topic.getExpires().getTime() - today.getTime().getTime();
        tvTopicExpires.setText("Due on "+df.format(topic.getExpires())+", "+TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS)+" DAYS REMAINING");
        tvTopicAmount.setText("Compensation: "+topic.getAmount()+" coins");
    }
}
