package com.smora.arch.webserver.asynctask;

import android.os.AsyncTask;
import android.util.Log;

import com.smora.arch.webserver.WebServer;
import com.smora.arch.webserver.WebServerException;

import java.lang.ref.WeakReference;

public class StartServerTask extends AsyncTask<Void, Void, Boolean> {

    private static final String LOG_TAG = StartServerTask.class.getSimpleName();

    private final WebServer webServer;
    private final WeakReference<WebServer.StartCallback> callbackRef;

    public StartServerTask(WebServer webServer, final WebServer.StartCallback callback) {
        if (webServer == null) {
            throw new IllegalArgumentException("Context cannot be null");
        }
        this.webServer = webServer;
        this.callbackRef = new WeakReference<>(callback);
    }


    @Override
    protected Boolean doInBackground(Void... voids) {
        try {
            webServer.start();
        } catch (WebServerException e) {
            Log.w(LOG_TAG, "doInBackground, start server failed: ", e);
            return false;
        }
        return true;
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        if (callbackRef.get() == null) {
            return;
        }
        if (!aBoolean) {
            callbackRef.get().onServerStartFailed();
            return;
        }
        callbackRef.get().onServerStartSucceed();
    }

}