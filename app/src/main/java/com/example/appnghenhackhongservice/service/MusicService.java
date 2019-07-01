package com.example.appnghenhackhongservice.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;

import com.example.appnghenhackhongservice.Player.MusicPlayer;
import com.example.appnghenhackhongservice.R;
import com.example.appnghenhackhongservice.notification.MusicNotifycation;

public class MusicService extends Service {

    private IBinder iBinder = new BindService();
    private MusicNotifycation mMusicNotifycation;
    private MusicPlayer mMusicPlayer;

    public MusicPlayer getmMusicPlayer() {
        return mMusicPlayer;
    }

    public void setmMusicPlayer(MusicPlayer mMusicPlayer) {
        this.mMusicPlayer = mMusicPlayer;
    }

    public MusicNotifycation getmMusicNotifycation() {
        return mMusicNotifycation;
    }

    public void setmMusicNotifycation(MusicNotifycation mMusicNotifycation) {
        this.mMusicNotifycation = mMusicNotifycation;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void startMyOwnForeground() {
        String NOTIFICATION_CHANNEL_ID = "com.example.simpleapp";
        String channelName = "My Background Service";
        NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE);
        chan.setLightColor(Color.BLUE);
        chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        assert manager != null;
        manager.createNotificationChannel(chan);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        Notification notification = notificationBuilder.setOngoing(true).setSmallIcon(R.mipmap.ic_launcher).setContentTitle("App is running in background").setPriority(NotificationManager.IMPORTANCE_MIN).setCategory(Notification.CATEGORY_SERVICE).build();
        startForeground(2, notification);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        if (mMusicPlayer == null){
            mMusicPlayer = new MusicPlayer(this);
            mMusicNotifycation = new MusicNotifycation(this);
            mMusicPlayer.setmMusicNotifycation(mMusicNotifycation);
            mMusicPlayer.registerNotificationActionsReceiver(true);
        }
        return iBinder;
    }

    public class BindService extends Binder {
        public MusicService getService() {
            return MusicService.this;
        }
    }
}