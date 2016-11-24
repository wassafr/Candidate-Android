package com.smora.arch.webserver;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.util.Log;

import com.smora.arch.webserver.asynctask.StartServerTask;
import com.smora.arch.webserver.asynctask.StopServerTask;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import fi.iki.elonen.NanoHTTPD;

public class WebServer {

    public static final int SERVER_PORT = 8080;

    private static final String LOG_TAG = WebServer.class.getSimpleName();

    private static volatile WebServer singleton = null;

    public static WebServer with(final Context context) {
        if (context == null) {
            throw new IllegalArgumentException("Context cannot be null.");
        }
        if (singleton == null) {
            synchronized (WebServer.class) {
                if (singleton == null) {
                    singleton = new WebServer(context.getApplicationContext());
                }
            }
        }
        return singleton;
    }

    private final Server server;

    private WebServer(final Context context) {
        server = new Server(context);
    }

    public void start() throws WebServerException {
        synchronized (WebServer.class) {
            Log.v(LOG_TAG, "start");

            if (server.isAlive()) {
                Log.v(LOG_TAG, "server is already alive");
                return;
            }

            try {
                server.start();
                Log.v(LOG_TAG, "server started");
            } catch (IOException e) {
                Log.w(LOG_TAG, "error when starting server", e);
                throw new WebServerException(e);
            }
        }
    }

    public void stop() {
        synchronized (WebServer.class) {
            Log.v(LOG_TAG, "stop");

            if (!server.isAlive()) {
                Log.v(LOG_TAG, "server is already stopped");
                return;
            }

            server.stop();
            Log.v(LOG_TAG, "server stopped");
        }
    }

    public void startAsync(final StartCallback callback) {
        new StartServerTask(this, callback).execute();
    }

    public void stopAsync() {
        new StopServerTask(this).execute();
    }

    public interface StartCallback {
        void onServerStartSucceed();
        void onServerStartFailed();
    }

    private static class Server extends NanoHTTPD {

        private AssetManager assetManager;

        public Server(final Context context) {
            super(SERVER_PORT);
            assetManager = context.getAssets();
        }

        @Override
        public Response serve(IHTTPSession session) {
            if ("/favicon.ico".equals(session.getUri())) {
                return null;
            }

            final String fileName = getFileName(session.getUri());

            if (fileName.endsWith(".jpg")) {
                return serveJpg(fileName);
            }

            return serveJson(fileName);
        }

        private Response serveJpg(String fileName) {
            try {

                final AssetFileDescriptor fileDescriptor = assetManager.openFd(fileName);

                return newFixedLengthResponse(NanoHTTPD.Response.Status.OK, "image/jpeg", assetManager.open(fileName), fileDescriptor.getLength());

            } catch (IOException e) {
                Log.w(LOG_TAG, "Error server: ", e);
                Response response = newFixedLengthResponse("Error server : "  + e.getLocalizedMessage());
                if (e instanceof FileNotFoundException) {
                    response.setStatus(Response.Status.NOT_FOUND);
                } else {
                    response.setStatus(Response.Status.INTERNAL_ERROR);
                }
                return response;
            }
        }

        private Response serveJson(String fileName) {
            try {

                final InputStream is = assetManager.open(fileName);
                int size = is.available();
                byte buffer[] = new byte[size];
                is.read(buffer);
                is.close();
                return newFixedLengthResponse(new String(buffer));

            } catch (IOException e) {
                Log.w(LOG_TAG, "Error server: ", e);
                Response response = newFixedLengthResponse("Error server : "  + e.getLocalizedMessage());
                if (e instanceof FileNotFoundException) {
                    response.setStatus(Response.Status.NOT_FOUND);
                } else {
                    response.setStatus(Response.Status.INTERNAL_ERROR);
                }
                return response;
            }
        }

        private String getFileName(final String uri) {
            if (uri.startsWith("/images")) {
                return uri.substring(1) + ".jpg";
            }
            return uri.substring(1) + "/resp.json";
        }
    }

}
