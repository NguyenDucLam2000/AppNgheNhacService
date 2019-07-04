package com.example.appnghenhackhongservice.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.appnghenhackhongservice.view.play.MusicPlayer;
import com.example.appnghenhackhongservice.notification.MusicNotifycation;

public class NotificationReceiver extends BroadcastReceiver {
    private NotifyListener listener;
    private MusicPlayer mMusicPlayer;
    public NotificationReceiver(MusicPlayer mMusicPlayer)
    {
        this.mMusicPlayer = mMusicPlayer;
    }

    public void setUpdateUI(NotifyListener updateUI) {
        this.listener = updateUI;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        final String action = intent.getAction();

        if (action != null)
        {
            switch (action)
            {
                case MusicNotifycation.NEXT_ACTION:
                    mMusicPlayer.next(); // Update Song
                    listener.onNext(); // Update UI
                    break;
                case MusicNotifycation.PLAY_PAUSE_ACTION:
                    mMusicPlayer.playOrPause(); // Update Song
                    listener.onPlayOrPause(); // Update UI
                    break;
                case MusicNotifycation.PREV_ACTION:
                    mMusicPlayer.prev(); // Update Song
                    listener.onPrev(); // Update UI
                    break;
            }
        }
    }
}