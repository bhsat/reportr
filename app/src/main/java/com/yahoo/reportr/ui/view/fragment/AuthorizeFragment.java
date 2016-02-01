package com.yahoo.reportr.ui.view.fragment;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.yahoo.reportr.R;
import com.yahoo.reportr.data.entity.AuthInfo;
import com.yahoo.reportr.data.entity.Blog;
import com.yahoo.reportr.data.entity.User;
import com.yahoo.reportr.data.service.BlogListService;
import com.yahoo.reportr.data.service.OntologyService;
import com.yahoo.reportr.ui.DaggerReportrComponent;
import com.yahoo.reportr.ui.presenter.BlogsPresenter;
import com.yahoo.reportr.ui.presenter.ReportrPresenter;
import com.yahoo.reportr.ui.presenter.UserPresenter;
import com.yahoo.reportr.ui.view.activity.UserActivity;
import com.yahoo.reportr.utils.Constants;
import com.yahoo.reportr.utils.MemCache;
import com.yahoo.reportr.utils.NetworkHelper;

import java.util.List;

/**
 * Created by bhavanis on 1/6/16.
 */
public class AuthorizeFragment extends Fragment {

    private final String TAG = AuthorizeFragment.class.getSimpleName();
    private EditText etUsername;
    private EditText etPassword;
    private Button btnLogin;
    private ReportrPresenter reportrPresenter;
    private UserPresenter userPresenter;
    SharedPreferences preferences;
    AuthorizeAsyncTask asyncTask;
    UserAsyncTask userAsyncTask;
    private NetworkHelper networkHelper;
    private User user;
    private String prefAuthkey;
    private String deviceStr;

    public static AuthorizeFragment newInstance() {
        AuthorizeFragment fragment = new AuthorizeFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        reportrPresenter = new ReportrPresenter();
        userPresenter = new UserPresenter();
        DaggerReportrComponent.builder().build().inject(reportrPresenter);
        DaggerReportrComponent.builder().build().inject(userPresenter);
        networkHelper = new NetworkHelper(getActivity());
        deviceStr = Settings.Secure.ANDROID_ID;
        System.out.println("Android device string: "+deviceStr);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        initializeView(view);
        return view;
    }

    private void initializeView(View view) {
        setupViews(view);
        hideProgressBar();
        prefAuthkey = preferences.getString(Constants.KEY_AUTHKEY, null);
        if ( prefAuthkey != null) {
            startOntoService(prefAuthkey, true);
            pollUser(prefAuthkey);
        } else {
            setupListeners();
        }
    }

    private void setupViews(View view) {
        etUsername = (EditText) view.findViewById(R.id.etUsername);
        etPassword = (EditText) view.findViewById(R.id.etPassword);
        btnLogin = (Button) view.findViewById(R.id.btnLogin);
        //progressBar = (ProgressBar) view.findViewById(R.id.progressBarFetch);
        preferences = getActivity().getSharedPreferences(Constants.SHARED_PREF, 0);
    }

    private void setupListeners() {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();
                if (networkHelper.isNetworkAvailable()) {
                    doAuthorize(username, password, deviceStr);
                } else {
                    Toast.makeText(getActivity(), Constants.NETWORK_ERROR, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void doAuthorize(String username, String password, String deviceStr) {
        asyncTask = new AuthorizeAsyncTask(this, username);
        asyncTask.execute(username, password, deviceStr);
    }

    public void authorized(String authkey) {
        //pollUser(authkey);
        Intent i = new Intent(getActivity(), UserActivity.class);
        Log.i(TAG, authkey);
        i.putExtra(Constants.KEY_AUTHKEY, authkey);
        i.putExtra(Constants.KEY_TSTAMP, preferences.getLong(Constants.KEY_TSTAMP, 0));
        getActivity().startActivity(i);
    }


    private void callBlogService(String authkey) {
        Intent i = new Intent(getActivity(), BlogListService.class);
        i.putExtra(Constants.KEY_AUTHKEY, authkey);
        getActivity().startService(i);
    }

    private void pollUser(String authkey) {
        showProgressBar();
        userAsyncTask = new UserAsyncTask(this);
        userAsyncTask.execute(authkey);
    }

    public class UserAsyncTask extends AsyncTask<String, Void, String> {

        private AuthorizeFragment fragment;
        private String authkey;
        ProgressDialog dialog;

        public UserAsyncTask(AuthorizeFragment fragment) {
            this.fragment = fragment;
            this.dialog = new ProgressDialog(fragment.getActivity());
        }

        @Override
        protected String doInBackground(String... params) {
            this.authkey = params[0];
            user = userPresenter.getUserInfo(authkey);
            MemCache.getInstance().getLru().put(Constants.USER_OBJECT, user);
            BlogsPresenter blogsPresenter = new BlogsPresenter();
            List<Blog> blogs = blogsPresenter.getBlogs(authkey);
            MemCache.getInstance().getLru().put(Constants.BLOG_LIST, blogs);
            return authkey;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            String user = preferences.getString("username", "");
            dialog.setMessage("Logging in as " + user);
            dialog.setIndeterminate(true);
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
            callBlogService(authkey);
            authorized(authkey);
        }
    }

    private void saveToPreferences(AuthInfo authInfo) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(Constants.KEY_AUTHKEY, authInfo.getAuthKey());
        editor.putLong(Constants.KEY_TSTAMP, authInfo.getTstamp());
        editor.commit();
    }

    public void showProgressBar() {
        etUsername.setVisibility(View.GONE);
        etPassword.setVisibility(View.GONE);
        btnLogin.setVisibility(View.GONE);
    }

    public void hideProgressBar() {
        etUsername.setVisibility(View.VISIBLE);
        etPassword.setVisibility(View.VISIBLE);
    }

    private void cacheGlobalOnto(String ontology) {
        Log.d(TAG, "Ontology to be saved: " + ontology);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(Constants.KEY_ONTOLOGY, ontology);
        editor.commit();
    }

    public void startOntoService(String authkey, boolean callOnto) {
        Intent i = new Intent(getActivity(), OntologyService.class);
        i.putExtra(Constants.KEY_AUTHKEY, authkey);
        i.putExtra(Constants.KEY_DEVICE_STR, deviceStr);
        i.putExtra(Constants.KEY_REGISTER, callOnto);
        getActivity().startService(i);
    }

    public class AuthorizeAsyncTask extends AsyncTask<String, Void, Boolean> {

        AuthorizeFragment fragment;
        AuthInfo authInfo;
        ProgressDialog dialog;
        private String username;

        public AuthorizeAsyncTask(AuthorizeFragment fragment, String username){
            this.fragment = fragment;
            this.dialog = new ProgressDialog(fragment.getActivity());
            this.username = username;
        }

        @Override
        protected Boolean doInBackground(String... params) {
            authInfo = reportrPresenter.authorize(params[0], params[1], params[2]);
            if (authInfo.getAuthKey() != null) {
                saveToPreferences(authInfo);
                user = userPresenter.getUserInfo(authInfo.getAuthKey());
                MemCache.getInstance().getLru().put(Constants.USER_OBJECT, user);
                BlogsPresenter blogsPresenter = new BlogsPresenter();
                List<Blog> blogs = blogsPresenter.getBlogs(authInfo.getAuthKey());
                MemCache.getInstance().getLru().put(Constants.BLOG_LIST, blogs);
                preferences.edit().putString("username", params[0]).commit();
                if (preferences.getString(Constants.KEY_ONTOLOGY, "").isEmpty()) {
                    cacheGlobalOnto(reportrPresenter.getGlobalOntologies(authInfo.getAuthKey()));
                }
                return true;
            }
            return false;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setMessage("Logging in as "+username);
            dialog.setIndeterminate(true);
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.show();
            //fragment.showProgressBar();
        }

        @Override
        protected void onPostExecute(Boolean s) {
            super.onPostExecute(s);
            if (!s) {
                fragment.hideProgressBar();
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
                Toast.makeText(getContext(), authInfo.getMessage(), Toast.LENGTH_LONG).show();
                etUsername.setText("");
                etPassword.setText("");
            } else if(fragment!=null && fragment.getActivity()!=null) {
                fragment.hideProgressBar();
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
                callBlogService(authInfo.getAuthKey());
                startOntoService(authInfo.getAuthKey(), false);
                authorized(authInfo.getAuthKey());
                this.fragment = null;
            }
        }
    }
}
