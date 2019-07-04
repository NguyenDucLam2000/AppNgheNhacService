package com.example.appnghenhackhongservice.Player;

import android.content.Context;
import android.content.IntentFilter;
import android.media.MediaPlayer;

import com.example.appnghenhackhongservice.model.Song;
import com.example.appnghenhackhongservice.notification.MusicNotifycation;
import com.example.appnghenhackhongservice.recevier.NotificationReceiver;
import com.example.appnghenhackhongservice.service.MusicService;

import java.util.List;

public class MusicPlayer implements MediaPlayer.OnCompletionListener {
    private MusicService mMusicService;
    private List<Song> listSong;
    private Song song;
    private int position;
    private NotificationReceiver receiver;
    private Context mContext;
    private MediaPlayer mMediaPlayer;
    private MusicNotifycation mMusicNotifycation;
    private AutoNext autoNext;

    public void setAutoNext(AutoNext autoNext) {
        this.autoNext = autoNext;
    }

    public MediaPlayer getmMediaPlayer() {
        return mMediaPlayer;
    }

    public NotificationReceiver getReceiver() {
        return receiver;
    }

    public void setReceiver(NotificationReceiver receiver) {
        this.receiver = receiver;
    }

    public MusicNotifycation getmMusicNotifycation() {
        return mMusicNotifycation;
    }

    public void setmMusicNotifycation(MusicNotifycation mMusicNotifycation) {
        this.mMusicNotifycation = mMusicNotifycation;
    }

    public MusicPlayer(MusicService mMusicService) {
        this.mMusicService = mMusicService;
        mContext = mMusicService.getApplicationContext();
    }

    public Song getSongSelected()
    {
        return listSong.get(position);
    }

    public List<Song> getListSong() {
        return listSong;
    }

    public void setListSong(List<Song> listSong) {
        this.listSong = listSong;
    }

    public Song getSong() {
        return song;
    }

    public void setSong(Song song) {
        this.song = song;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    private void registerActionsReceiver() {
        receiver = new NotificationReceiver(mMusicService.getmMusicPlayer());
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MusicNotifycation.PREV_ACTION);
        intentFilter.addAction(MusicNotifycation.PLAY_PAUSE_ACTION);
        intentFilter.addAction(MusicNotifycation.NEXT_ACTION);
        mMusicService.registerReceiver(receiver, intentFilter);
    }

    private void unregisterActionsReceiver() {
        if (mMusicService != null && receiver != null) {
            try {
                mMusicService.unregisterReceiver(receiver);
            }
            catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
        }
    }

    public void registerNotificationActionsReceiver(final boolean isReceiver)
    {
        if (isReceiver)
        {
            registerActionsReceiver();
        }
        else
        {
            unregisterActionsReceiver();
        }
    }

    public void next() {
        if (listSong.size() > 0) {
            if (isNext()) {
                position++;
                if (position == listSong.size()) {
                    position = 0;
                }
                release();
                startMusic();
                showNotification();
            }
        }
    }

    public void release(){
        if (mMediaPlayer != null){
            mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    public void prev() {
        if (listSong.size() > 0) {
            if (isNext()) {
                position--;
                if (position == -1) {
                    position = listSong.size() - 1;
                }
            }
            release();
            startMusic();
            showNotification();

        }
    }
    public void startMusic() {
        if (listSong != null && listSong.size() > 0) {
            mMediaPlayer = new MediaPlayer();
            mMediaPlayer.setOnCompletionListener(this);
            try {
                if (position != -1) {
                    mMediaPlayer.setDataSource(listSong.get(position).getData());
                    mMediaPlayer.prepare();
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            mMediaPlayer.start();
        }
    }

    public void pause() {
        mMediaPlayer.pause();
    }

    public boolean isPlaying() {
        return mMediaPlayer != null && mMediaPlayer.isPlaying();
    }

    public void seekTo(int msec) {
        if (mMediaPlayer != null) {
            mMediaPlayer.seekTo(msec);
        }
    }

    public void play() {
        mMediaPlayer.start();
    }


    public boolean isNext() {
        return mMusicService.getmMusicPlayer().getmMediaPlayer() != null;
    }

    public void playOrPause() {
        if (isPlaying()) {
            mMusicService.stopForeground(false);
            mMediaPlayer.pause();
            mMusicService.getmMusicNotifycation().getmNotificationManager().notify(MusicNotifycation.NOTIFYCATION_ID, mMusicNotifycation.createNotification());
        }
        else {
            mMediaPlayer.start();
            mMusicService.startForeground(MusicNotifycation.NOTIFYCATION_ID, mMusicService.getmMusicNotifycation().createNotification());
        }
    }
    public void showNotification() {
        mMusicService.startForeground(MusicNotifycation.NOTIFYCATION_ID, mMusicNotifycation.createNotification());
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        autoNext.onAutoNext();
    }
}
