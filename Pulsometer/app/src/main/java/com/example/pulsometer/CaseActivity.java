package com.example.pulsometer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.pulsometer.Logic.AuthenticationData;
import com.example.pulsometer.R;

public class CaseActivity extends Activity {

    private AuthenticationData auth;
    private final Context context = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_case);
        Button button = (Button)findViewById(R.id.AnalisysButton);
        Intent intent = getIntent();
        auth = (AuthenticationData)intent.getSerializableExtra("authData");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AnalisysActivity.class);
                intent.putExtra("authData", auth);
                startActivity(intent);
            }
        });
    }
}
