package com.yahoo.reportr.ui.view.activity;

import android.content.SharedPreferences;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.yahoo.reportr.R;
import com.yahoo.reportr.ui.DaggerReportrComponent;
import com.yahoo.reportr.ui.presenter.ReportrPresenter;
import com.yahoo.reportr.ui.view.fragment.AuthorizeFragment;
import com.yahoo.reportr.ui.view.fragment.UserFragment;
import com.yahoo.reportr.utils.Constants;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        AuthorizeFragment fragment = AuthorizeFragment.newInstance();
        getSupportFragmentManager().beginTransaction().add(R.id.flLogin, fragment).commit();
    }
}
