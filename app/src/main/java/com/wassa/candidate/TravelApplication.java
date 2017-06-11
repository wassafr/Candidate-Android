package com.wassa.candidate;

import android.app.Application;

import io.realm.Realm;

/**
 * @author khadir
 * @since 10/06/2017
 */

public class TravelApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
    }
}
