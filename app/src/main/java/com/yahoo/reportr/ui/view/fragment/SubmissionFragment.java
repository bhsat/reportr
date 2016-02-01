package com.yahoo.reportr.ui.view.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.yahoo.reportr.R;
import com.yahoo.reportr.data.entity.AuthInfo;
import com.yahoo.reportr.data.entity.Post;
import com.yahoo.reportr.data.entity.Submission;
import com.yahoo.reportr.data.entity.Topic;
import com.yahoo.reportr.ui.DaggerReportrComponent;
import com.yahoo.reportr.ui.presenter.SubmissionPresenter;
import com.yahoo.reportr.ui.view.activity.UserActivity;
import com.yahoo.reportr.ui.view.views.SubmissionView;
import com.yahoo.reportr.utils.Constants;

import java.util.List;

/**
 * Created by bhavanis on 1/13/16.
 */
public class SubmissionFragment extends Fragment implements SubmissionView {

    private String authkey;
    private Topic topic;
    private String blogName;
    private Post post;
    private TextView tvPreviewTitle;
    private TextView tvPreviewBody;
    private Button btnSubmit;
    private SubmissionPresenter submissionPresenter;
    private String response;
    private SubmissionAsyncTask asyncTask;
    private ProgressBar progressBar;

    public static SubmissionFragment newInstance(Bundle args) {
        SubmissionFragment fragment = new SubmissionFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        authkey = args.getString(Constants.KEY_AUTHKEY);
        topic = args.getParcelable(Constants.KEY_TOPIC);
        blogName = args.getString(Constants.KEY_BLOG_NAME);
        post = args.getParcelable(Constants.KEY_POST);
        submissionPresenter = new SubmissionPresenter(this);
        DaggerReportrComponent.builder().build().inject(submissionPresenter);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_submission, container, false);
        initializeView(view);
        setupListener();
        progressBar.setVisibility(View.INVISIBLE);
        tvPreviewTitle.setText(post.getTitle());
        tvPreviewBody.setText(post.getBody());
        return view;
    }

    private void setupListener() {
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createSubmission();
            }
        });
    }

    private void initializeView(View view) {
        tvPreviewTitle = (TextView) view.findViewById(R.id.tvPreviewTitle);
        tvPreviewBody = (TextView) view.findViewById(R.id.tvPreviewBody);
        btnSubmit = (Button) view.findViewById(R.id.btnSubmitArticle);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBarSubmit);
    }

    private void createSubmission() {
        asyncTask = new SubmissionAsyncTask(this);
        asyncTask.execute();
    }

    public class SubmissionAsyncTask extends AsyncTask<Object, Void, String> {

        SubmissionFragment fragment;
        public SubmissionAsyncTask(SubmissionFragment fragment){
            this.fragment = fragment;
        }

        @Override
        protected String doInBackground(Object... params) {
            response = submissionPresenter.createSubmission(authkey, blogName, post.getId(), topic.getId());
            return response;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //Toast toast = Toast.makeText(getContext(), s, Toast.LENGTH_LONG);
            //toast.getView().setBackgroundColor(Color.GREEN);
            //toast.show();
            progressBar.setVisibility(View.GONE);
            Toast.makeText(getContext(), s, Toast.LENGTH_LONG).show();
            Intent i = new Intent(getActivity(), UserActivity.class);
            startActivity(i);
        }
    }

    @Override
    public void addSubmissions(@NonNull List<Submission> submissions) {
    }
}
