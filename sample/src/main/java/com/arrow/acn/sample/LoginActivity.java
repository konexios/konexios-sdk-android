/*
 * Copyright (c) 2017 Arrow Electronics, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License 2.0
 * which accompanies this distribution, and is available at
 * http://apache.org/licenses/LICENSE-2.0
 *
 * Contributors: Arrow Electronics, Inc.
 */

package com.arrow.acn.sample;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.arrow.acn.api.AcnApiService;
import com.arrow.acn.api.AcnApiServiceFactory;
import com.arrow.acn.api.listeners.RegisterAccountListener;
import com.arrow.acn.api.models.AccountRequest;
import com.arrow.acn.api.models.AccountResponse;
import com.arrow.acn.api.models.ApiError;


/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {
    private final static String TAG = LoginActivity.class.getSimpleName();
    public final static String ACCOUNT_RESPONSE_EXTRA = "account_response";
    // DEV
    public static final String BASE_IOT_CONNECT_URL_DEV = "http://pgsdev01.arrowconnect.io:12001";
    //TODO: replace with real keys
    public static final String DEFAULT_API_KEY = "api key goes here";
    public static final String DEFAULT_API_SECRET = "api secret goes here";

    private static final String ACCOUNT_LOGIN_SP_KEY = "com.arrow.kronos.sample.login_key";
    private static final String ACCOUNT_PASSWORD_SP_KEY = "com.arrow.kronos.sample.password_key";
    private static final String CODE_PASSWORD_SP_KEY = "com.arrow.kronos.sample.code_key";

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private EditText mCode;
    private View mProgressView;
    private View mLoginFormView;

    private AcnApiService mAcnApiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);

        mPasswordView = (EditText) findViewById(R.id.password);
        mCode = (EditText) findViewById(R.id.code);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);

        mAcnApiService = AcnApiServiceFactory.createAcnApiService();
        //TODO: replace DEFAULT_API_KEY and DEFAULT_API_SECRET with valid keys
        mAcnApiService.setRestEndpoint(BASE_IOT_CONNECT_URL_DEV, DEFAULT_API_KEY,
                DEFAULT_API_SECRET);

        initViews();
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        final String email = mEmailView.getText().toString();
        final String password = mPasswordView.getText().toString();
        final String code = mCode.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            AccountRequest model = new AccountRequest();
            model.setName("Some Name");
            model.setEmail(email.toLowerCase());
            model.setPassword(password);
            if (!TextUtils.isEmpty(code)) {
                model.setCode(code);
            }

            mAcnApiService.registerAccount(model, new RegisterAccountListener() {
                @Override
                public void onAccountRegistered(AccountResponse accountResponse) {
                    Log.v(TAG, "onAccountRegistered");
                    saveCredentials(email, password, code);
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.putExtra(ACCOUNT_RESPONSE_EXTRA, accountResponse);
                    startActivity(intent);
                    finish();
                    showProgress(false);
                }

                @Override
                public void onAccountRegisterFailed(ApiError e) {
                    Log.v(TAG, "onAccountRegisterFailed");
                    mPasswordView.setError(getString(R.string.error_fatal));
                    mPasswordView.requestFocus();
                    showProgress(false);
                }
            });
        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    private void saveCredentials(String login, String password, String code) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(ACCOUNT_LOGIN_SP_KEY, login);
        editor.putString(ACCOUNT_PASSWORD_SP_KEY, password);
        editor.putString(CODE_PASSWORD_SP_KEY, code);
        editor.commit();
    }

    private void initViews() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        String login = sp.getString(ACCOUNT_LOGIN_SP_KEY, "");
        String password = sp.getString(ACCOUNT_PASSWORD_SP_KEY, "");
        String code = sp.getString(CODE_PASSWORD_SP_KEY, "");
        if (!TextUtils.isEmpty(login) && !TextUtils.isEmpty(password)) {
            mEmailView.setText(login);
            mPasswordView.setText(password);
            mCode.setText(code);
            attemptLogin();
        }
    }
}

