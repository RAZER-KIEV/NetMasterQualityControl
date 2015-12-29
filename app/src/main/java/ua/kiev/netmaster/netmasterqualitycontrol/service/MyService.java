package ua.kiev.netmaster.netmasterqualitycontrol.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.Timer;
import java.util.TimerTask;

public class MyService extends Service implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{
    // TODO: 26.12.2015
    private NotificationManager notificationManager;
    private Notification notification2;
    private Timer timer;
    private TimerTask tTask;
    private long interval = 3000;

    private GoogleApiClient mGoogleApiClient;
    private boolean mResolvingError = false;
    private static String LOG_TAG ="myLogs";
    private Location mLastLocation;
    private MyBinder binder = new MyBinder();


    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onConnected(Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    public class MyBinder extends Binder {
        public MyService getService() {
            return MyService.this;
        }
    }

}
