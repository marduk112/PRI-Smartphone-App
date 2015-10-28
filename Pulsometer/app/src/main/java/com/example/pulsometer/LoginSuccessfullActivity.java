package com.example.pulsometer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.pulsometer.Model.AuthenticationDataViewModel;

public class LoginSuccessfullActivity extends Activity {

    private AuthenticationDataViewModel auth;
    private final Context context = this;

    @Override
     public void onBackPressed() {
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_successfull);
        //Bundle extras = getIntent().getExtras();
        //auth = (AuthenticationDataViewModel)extras.get("authData");

        Button button = (Button)findViewById(R.id.to_case_activity);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CaseActivity.class);
//                intent.putExtra("authData", auth);
                startActivity(intent);
                finish();
            }
        });
    }
}
