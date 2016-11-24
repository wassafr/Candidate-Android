package com.smora.arch.webserver.asynctask;

import android.os.AsyncTask;

import com.smora.arch.webserver.WebServer;

public class StopServerTask extends AsyncTask<Void, Void, Boolean> {

    private final WebServer webServer;

    public StopServerTask(final WebServer webServer) {
        if (webServer == null) {
            throw new IllegalArgumentException("WebServer cannot be null");
        }
        this.webServer = webServer;
    }


    @Override
    protected Boolean doInBackground(Void... voids) {
        webServer.stop();
        return true;
    }

}