package com.example.pulsometer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ExternalProvidersActivity extends Activity {

   /* @Override
    public void onBackPressed() {
        //super.onBackPressed();
        finish();
    }*/
    private final Context context = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_external_providers);
        Button registerViaGoogleButton = (Button) findViewById(R.id.register_login_via_google_button);
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, LoginViaExternalProviderActivity.class);
                intent.putExtra("provider", "Google");
                startActivity(intent);
            }
        };
        registerViaGoogleButton.setOnClickListener(onClickListener);
    }

}
