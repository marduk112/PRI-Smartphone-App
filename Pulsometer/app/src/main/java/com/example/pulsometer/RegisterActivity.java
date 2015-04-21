package com.example.pulsometer;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.pulsometer.R;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

public class RegisterActivity extends Activity {

    private EditText EmailView;
    private EditText PasswordView;
    private EditText ConfirmPasswordView;
    private UserRegisterTask AuthTask = null;
    private View ProgressView;
    private View RegisterFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        EmailView = (EditText) findViewById(R.id.emailText);
        PasswordView = (EditText) findViewById(R.id.passwordText);
        ConfirmPasswordView = (EditText) findViewById(R.id.confirmPasswordText);

        Button mSignInButton = (Button) findViewById(R.id.register2Button);
        mSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        RegisterFormView = findViewById(R.id.register_form);
        ProgressView = findViewById(R.id.login_progress);
    }

    public void attemptLogin() {
        if (AuthTask != null) {
            return;
        }

        // Reset errors.
        EmailView.setError(null);
        PasswordView.setError(null);
        ConfirmPasswordView.setError(null);
        // Store values at the time of the login attempt.
        String email = EmailView.getText().toString();
        String password = PasswordView.getText().toString();
        String confirmPassword = ConfirmPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;


        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password, confirmPassword)) {
            PasswordView.setError(getString(R.string.error_invalid_password));
            focusView = PasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            EmailView.setError(getString(R.string.error_field_required));
            focusView = EmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            EmailView.setError(getString(R.string.error_invalid_email));
            focusView = EmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            //showProgress(true);
            AuthTask = new UserRegisterTask(email, password, confirmPassword);
            AuthTask.execute((Void) null);
        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password, String confirmPassword) {
        //TODO: Replace this with your own logic
        return password.length() > 4 && password.equals(confirmPassword);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

           RegisterFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            RegisterFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    RegisterFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            ProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            ProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    ProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            ProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            RegisterFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    public class UserRegisterTask extends AsyncTask<Void, Void, HttpResponse> {

        private final String mEmail;
        private final String mPassword;
        private final String mConfirmPassword;

        UserRegisterTask(String email, String password, String confirmPassword) {
            mEmail = email;
            mPassword = password;
            mConfirmPassword = confirmPassword;
        }

        @Override
        protected HttpResponse doInBackground(Void... params) {
            try {
                HttpClient client = new DefaultHttpClient();
                String url = "http://pulsometerrest.apphb.com/api/Account/Register";
                HttpPost post = new HttpPost(url);
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                TextView resultIdText = (TextView) findViewById(R.id.email);
                nameValuePairs.add(new BasicNameValuePair("Email", mEmail));
                nameValuePairs.add(new BasicNameValuePair("Password", mPassword));
                nameValuePairs.add(new BasicNameValuePair("ConfirmPassword", mConfirmPassword));
                post.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
                HttpResponse response = client.execute(post);
                return response;
            } catch (Exception e) {
                Log.e("MainActivity", e.getMessage(), e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(final HttpResponse success) {
            AuthTask = null;
            showProgress(false);

            if (success.getStatusLine().getStatusCode() == 200) {
                finish();
            } else {
                PasswordView.setError(Integer.toString(success.getStatusLine().getStatusCode()));
                PasswordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            AuthTask = null;
            showProgress(false);
        }
    }
}
