package com.wassa.candidate;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.smora.arch.webserver.WebServer;

/**
 * This Activity is used to start and stop LocalWebserver
 */
public class LaunchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WebServer.with(this).startAsync(startServerCallback);
    }

    @Override
    protected void onResume() {
        if (getIntent().getBooleanExtra("already_launch", false)) {
            finish();
        } else {
            getIntent().putExtra("already_launch", true);
        }
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        WebServer.with(this).stopAsync();
        super.onDestroy();
    }

    private final WebServer.StartCallback startServerCallback = new WebServer.StartCallback() {
        public void onServerStartSucceed() {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }

        public void onServerStartFailed() {
            Toast.makeText(getApplicationContext(), "Error when starting local server", Toast.LENGTH_LONG).show();
        }
    };
}
