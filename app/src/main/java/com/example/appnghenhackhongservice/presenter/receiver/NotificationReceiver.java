package com.example.appnghenhackhongservice.presenter.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.appnghenhackhongservice.presenter.player.MusicPlayer;
import com.example.appnghenhackhongservice.notification.MusicNotifycation;

public class NotificationReceiver extends BroadcastReceiver {
    private UpdateUI updateUI;
    private MusicPlayer mMusicPlayer;
    public NotificationReceiver(MusicPlayer mMusicPlayer)
    {
        this.mMusicPlayer = mMusicPlayer;
    }

    public void setUpdateUI(UpdateUI updateUI) {
        this.updateUI = updateUI;
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
                    updateUI.onNext(); // Update UI
                    break;
                case MusicNotifycation.PLAY_PAUSE_ACTION:
                    mMusicPlayer.playOrPause(); // Update Song
                    updateUI.onPlayOrPause(); // Update UI
                    break;
                case MusicNotifycation.PREV_ACTION:
                    mMusicPlayer.prev(); // Update Song
                    updateUI.onPrev(); // Update UI
                    break;
            }
        }
    }
}