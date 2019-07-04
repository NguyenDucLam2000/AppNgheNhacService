package com.example.appnghenhackhongservice.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import com.example.appnghenhackhongservice.presenter.player.MusicPlayer;
import com.example.appnghenhackhongservice.notification.MusicNotifycation;

public class MusicService extends Service {

    private IBinder iBinder = new BindService();
    private MusicNotifycation mMusicNotifycation;
    public MusicPlayer mMusicPlayer;

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