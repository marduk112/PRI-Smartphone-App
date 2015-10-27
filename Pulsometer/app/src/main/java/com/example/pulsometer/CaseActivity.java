package com.example.pulsometer;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.pulsometer.Logic.GlobalVariables;
import com.example.pulsometer.Model.AuthenticationDataViewModel;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

public class CaseActivity extends Activity {

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Logout")
                .setMessage("Do you want to logout?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
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
                new LogoutTask().execute();
                Intent intent = new Intent(context, com.example.pulsometer.HistoryActivity.class);
                intent.putExtra("authData", auth);
                startActivity(intent);
            }
        });
    }

    private class LogoutTask extends AsyncTask<Void, Void, HttpResponse> {

        @Override
        protected HttpResponse doInBackground(Void... params) {
            try {
                HttpClient client = new DefaultHttpClient();
                String url = GlobalVariables.BaseUrlForRest + "api/Account/Logout";
                HttpPost post = new HttpPost(url);
                post.addHeader("Authorization", "Bearer " + GlobalVariables.AccessToken);
                HttpResponse response = client.execute(post);
                return response;
            } catch (Exception e) {
                Log.e("HistoryActivity", e.getMessage(), e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(HttpResponse result) {
            try {
                if (result.getStatusLine().getStatusCode() == 200){
                    System.out.println("OK");
                }
                else {
                    System.out.println("ERROR " + result.getStatusLine().getReasonPhrase() + result.getStatusLine().getStatusCode());
                }
            }catch(Exception e){
                Log.e("HistoryActivity", e.getMessage(), e);
            }
        }

        @Override
        protected void onCancelled() {

        }
    }
}
