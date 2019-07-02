package com.example.appnghenhackhongservice.notification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.session.MediaSessionManager;
import android.os.Build;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.MediaSessionCompat;

import com.example.appnghenhackhongservice.view.PlayActivity;
import com.example.appnghenhackhongservice.R;
import com.example.appnghenhackhongservice.model.Song;
import com.example.appnghenhackhongservice.service.MusicService;

import java.util.ArrayList;
import java.util.List;

public class MusicNotifycation {
    public static final int NOTIFYCATION_ID = 100;
    public static final String PLAY_PAUSE_ACTION = "PLAYPAUSE";
    public static final String NEXT_ACTION = "NEXT";
    public static final String PREV_ACTION = "PREV";
    private final String CHANNEL_ID = "CHANNEL_ID";
    public static final String SONG_SELECTED = "NOTY_SONG_SELECTED";
    public static final String LIST_SONG = "NOTY_LIST_SONG";
    public static final String POSITION = "NOTY_POSITION";
    private List<Song> listSong;
    private final int REQUEST_CODE = 100;
    private Context context;
    private NotificationManager mNotificationManager;
    private MusicService mMusicService;
    private NotificationCompat.Builder mNotificationBuilder;
    private MediaSessionManager mediaSessionManager;
    private MediaSessionCompat mediaSession;
    private MediaControllerCompat.TransportControls transportControls;
    private Song song;

    public MusicNotifycation(MusicService mMusicService) {
        this.mMusicService = mMusicService;
        mNotificationManager = (NotificationManager) mMusicService.getSystemService(Context.NOTIFICATION_SERVICE);
        context = mMusicService.getBaseContext();
    }

    public NotificationManager getmNotificationManager() {
        return mNotificationManager;
    }

    public void setmNotificationManager(NotificationManager mNotificationManager) {
        this.mNotificationManager = mNotificationManager;
    }

    public MusicService getmMusicService() {
        return mMusicService;
    }

    public void setmMusicService(MusicService mMusicService) {
        this.mMusicService = mMusicService;
    }

    public final NotificationCompat.Builder getNotificationBuilder() {
        return mNotificationBuilder;
    }

    private PendingIntent playerAction(String action) {
        Intent pauseIntent = new Intent();
        pauseIntent.setAction(action);
        return PendingIntent.getBroadcast(mMusicService, REQUEST_CODE, pauseIntent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    public Notification createNotification() {
        song = mMusicService.getmMusicPlayer().getSongSelected();
        listSong = new ArrayList<>();
        listSong = mMusicService.getmMusicPlayer().getListSong();
        mNotificationBuilder = new NotificationCompat.Builder(mMusicService, CHANNEL_ID);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel();
        }
        Intent openPlayerIntent = new Intent(mMusicService, PlayActivity.class);
        openPlayerIntent.putExtra(SONG_SELECTED, song);
        openPlayerIntent.putParcelableArrayListExtra(LIST_SONG, (ArrayList<? extends Parcelable>) listSong);
        openPlayerIntent.putExtra(POSITION, mMusicService.getmMusicPlayer().getPosition());
        openPlayerIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent contentIntent = PendingIntent.getActivity(mMusicService, REQUEST_CODE, openPlayerIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        String artist = song.getSingerName();
        String songTitle = song.getSongName();
        initMediaSession(song);
        mNotificationBuilder.setShowWhen(false).setSmallIcon(R.drawable.ic_play).setColor(context.getResources().getColor(R.color.colorAccent)).setContentTitle(songTitle).setContentText(artist).setContentIntent(contentIntent).addAction(notificationAction(PREV_ACTION)).addAction(notificationAction(PLAY_PAUSE_ACTION)).addAction(notificationAction(NEXT_ACTION)).setVisibility(NotificationCompat.VISIBILITY_PUBLIC);
        mNotificationBuilder.setStyle(new android.support.v4.media.app.NotificationCompat.MediaStyle().setMediaSession(mediaSession.getSessionToken()).setShowActionsInCompactView(0, 1, 2));
        return mNotificationBuilder.build();
    }

    @NonNull
    private NotificationCompat.Action notificationAction(String action) {
        int icon;
        switch (action) {
            default:
            case PREV_ACTION:
                icon = R.drawable.ic_skip_previous;
                break;
            case PLAY_PAUSE_ACTION:
                icon = mMusicService.getmMusicPlayer().isPlaying() ? R.drawable.ic_pause : R.drawable.ic_play;
                break;
            case NEXT_ACTION:
                icon = R.drawable.ic_skip_next;
                break;
        }
        return new NotificationCompat.Action.Builder(icon, action, playerAction(action)).build();
    }

    @RequiresApi(26)
    private void createNotificationChannel() {

        if (mNotificationManager.getNotificationChannel(CHANNEL_ID) == null) {
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, mMusicService.getString(R.string.app_name), NotificationManager.IMPORTANCE_LOW);
            notificationChannel.setDescription(mMusicService.getString(R.string.app_name));
            notificationChannel.enableLights(false);
            notificationChannel.enableVibration(false);
            notificationChannel.setShowBadge(false);
            mNotificationManager.createNotificationChannel(notificationChannel);
        }
    }

    private void initMediaSession(Song song) {
        mediaSessionManager = ((MediaSessionManager) context.getSystemService(Context.MEDIA_SESSION_SERVICE));
        mediaSession = new MediaSessionCompat(context, "AudioPlayer");
        transportControls = mediaSession.getController().getTransportControls();
        mediaSession.setActive(true);
        mediaSession.setFlags(MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);
    }
}
