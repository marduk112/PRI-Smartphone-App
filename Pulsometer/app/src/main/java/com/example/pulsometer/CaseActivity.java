package com.example.pulsometer;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.pulsometer.Logic.AsyncTasks.LogoutTask;
import com.example.pulsometer.Model.AuthenticationDataViewModel;

public class CaseActivity extends Activity {

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Logout")
                .setMessage("Do you want to logout?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        new LogoutTask(auth.access_token).execute();
                        Intent intent = new Intent(context, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                })
                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private AuthenticationDataViewModel auth;
    private final Context context = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_case);
        Button button = (Button)findViewById(R.id.AnalisysButton);
        Intent intent = getIntent();
        auth = (AuthenticationDataViewModel)intent.getSerializableExtra("authData");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AnalisysActivity.class);
                intent.putExtra("authData", auth);
                startActivity(intent);
            }
        });
        Button button2 = (Button)findViewById(R.id.history);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, com.example.pulsometer.HistoryActivity.class);
                intent.putExtra("authData", auth);
                startActivity(intent);
            }
        });
        Button button3 = (Button)findViewById(R.id.PedometerButton);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PedometerActivity.class);
                intent.putExtra("authData", auth);
                startActivity(intent);
            }
        });
        Button button4 = (Button) findViewById(R.id.events);
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, EventsActivity.class);
                intent.putExtra("authData", auth);
                startActivity(intent);
            }
        });
    }
}
