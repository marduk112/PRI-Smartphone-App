package com.example.pulsometer;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;

import com.example.pulsometer.Logic.AsyncTasks.RegisterUserTask;

public class RegisterActivity extends Activity {

    private EditText EmailView;
    private EditText PasswordView;
    private EditText ConfirmPasswordView;
    private RegisterUserTask authTask = null;
    private View ProgressView;
    private View RegisterFormView;
    private final RegisterActivity registerActivity = this;

    /*@Override
    public void onBackPressed() {
        //super.onBackPressed();
        finish();
    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //button
        final Animation animButton = AnimationUtils.loadAnimation(this, R.anim.anim_button);
        animButton.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                attemptLogin();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        EmailView = (EditText) findViewById(R.id.emailText);
        PasswordView = (EditText) findViewById(R.id.passwordText);
        ConfirmPasswordView = (EditText) findViewById(R.id.confirmPasswordText);

        Button mSignInButton = (Button) findViewById(R.id.register2Button);
        mSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(animButton);
            }
        });

        RegisterFormView = findViewById(R.id.register_form);
        ProgressView = findViewById(R.id.login_progress);
    }

    public void attemptLogin() {
        if (authTask != null) {
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
            authTask = new RegisterUserTask(email, password, confirmPassword, authTask,
                    registerActivity, RegisterFormView, ProgressView, getResources());
            authTask.execute((Void) null);
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


}
