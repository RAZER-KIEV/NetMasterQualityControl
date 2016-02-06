package ua.kiev.netmaster.netmasterqualitycontrol.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.gson.Gson;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;

import ua.kiev.netmaster.netmasterqualitycontrol.R;
import ua.kiev.netmaster.netmasterqualitycontrol.activities.LoginActivity;
import ua.kiev.netmaster.netmasterqualitycontrol.activities.MainActivity;
import ua.kiev.netmaster.netmasterqualitycontrol.domain.Employee;
import ua.kiev.netmaster.netmasterqualitycontrol.domain.MyDownTask;
import ua.kiev.netmaster.netmasterqualitycontrol.loger.L;

public class MyService extends Service implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{

    private NotificationManager notificationManager;
    private Notification notification2;
    private Timer timer;
    private TimerTask tTask;
    private long interval = 30000;

    private Employee me;
    private GoogleApiClient mGoogleApiClient;
    private boolean mResolvingError = false;
    //private static String LOG_TAG ="myLogs";
    private Location mLastLocation;
    private MyBinder binder = new MyBinder();
    private Gson gson;
    private String result;


    public void onCreate() {
        super.onCreate();
        L.l("MyService onCreate");
        me = LoginActivity.getEmployee();
        L.l(me.toString());
        gson = LoginActivity.gson;
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        timer = new Timer();
        buildGoogleApiClient();
    }

    public void onDestroy() {
        L.l("MyService onDestroy");
        mGoogleApiClient.disconnect();
        super.onDestroy();
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        L.l("MyService onStartCommand");
        sendNotif(startId);
        startForeground(startId, notification2);

        if (!mResolvingError) {  // more about this later
            mGoogleApiClient.connect();
        }

        schedule();

        return START_STICKY;
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    public IBinder onBind(Intent intent) {
        L.l("MyService onBind");
        return binder;
    }

    public void onRebind(Intent intent) {
        super.onRebind(intent);
        L.l("MyService onRebind");
    }

    public boolean onUnbind(Intent intent) {
        L.l("MyService onUnbind");
        return super.onUnbind(intent);
    }


    void schedule() {
        L.l("MyService schedule");
        if (tTask != null) tTask.cancel();
        if (interval > 0) {
            tTask = new TimerTask() {
                public void run() {
                    testLocation();
                }
            };
            timer.schedule(tTask, 0, interval);
        }
    }

    void sendNotif(int startId) {
        L.l("MyService sendNotif()");
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, 0);
        // 1-я часть
        notification2 = new Notification.Builder(this)
                .setContentText("Text in status bar")
                .setSmallIcon(R.drawable.ic_plusone_small_off_client) //Todo: Change icon
                .setWhen(System.currentTimeMillis())
                .setContentText("Big eye watch on you ;)")
                .setContentTitle("GPS Service works!")
                .setContentIntent(pIntent)
                .setAutoCancel(true)
                .setVibrate(new long[]{300l, 100l})
                .build();

        notification2.flags |= Notification.FLAG_NO_CLEAR;
        notificationManager.notify(startId, notification2);
    }

    @Override
    public void onConnected(Bundle bundle) {
        L.l("MyService.  onConnected()");
       try {
           mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                   mGoogleApiClient);
       }catch (SecurityException ex){
           ex.printStackTrace();
       }
        if (mLastLocation != null) {
            L.l(String.valueOf(mLastLocation.getLatitude()));
            L.l(String.valueOf(mLastLocation.getLongitude()));
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        L.l("MyService.  onConnectionSuspended()");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        L.l("MyService.  onConnectionFailed()");
    }

    private void testLocation(){
        L.l("MyService.  testLocation()");
       try {
           mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                   mGoogleApiClient);
       }catch (SecurityException ex){
           ex.printStackTrace();
       }
        if (mLastLocation != null) {
            L.l(String.valueOf(mLastLocation.getLatitude()));
            L.l(String.valueOf(mLastLocation.getLongitude()));
            me.setLastLat(mLastLocation.getLatitude());
            me.setLastLong(mLastLocation.getLongitude());
            me.setLastOnline(new Date()); // TODO: 2/5/2016 migrate to server
            Map<String,String> params = new HashMap<>();
            params.put(getString(R.string.employee), gson.toJson(me));
            try {
                result = new MyDownTask(params,this).execute().get();
                L.l("my location updated on server = "+result);
            } catch (InterruptedException|ExecutionException e) {
                e.printStackTrace();
            }
        }
    }

    public Location getmLastLocation(){
        return mLastLocation;
    }

    public class MyBinder extends Binder {
        public MyService getService() {
            return MyService.this;
        }
    }
}
