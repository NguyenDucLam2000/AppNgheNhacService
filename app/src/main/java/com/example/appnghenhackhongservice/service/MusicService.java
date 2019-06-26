package com.example.appnghenhackhongservice.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.example.appnghenhackhongservice.R;
import com.example.appnghenhackhongservice.adapter.MusicAdapter;
import com.example.appnghenhackhongservice.model.BaiHat;

import java.util.ArrayList;
import java.util.List;

public class MusicService extends Service {
    public static final String CURRENT_POSITION = "position";
    MediaPlayer mediaPlayer;
    private List<BaiHat> listBaiHat;
    private int position;
    private IBinder iBinder = new BindService();
    Intent intent;
    Intent intentStartCommand;
    LocalBroadcastManager localBroadcastManager;
    Thread thread;
    BaiHat baiHat;

    public BaiHat getBaiHat() {
        return baiHat;
    }

    public void setBaiHat(BaiHat baiHat) {
        this.baiHat = baiHat;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void startMyOwnForeground() {
        String NOTIFICATION_CHANNEL_ID = "com.example.simpleapp";
        String channelName = "My Background Service";
        NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE);
        chan.setLightColor(Color.BLUE);
        chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        assert manager != null;
        manager.createNotificationChannel(chan);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        Notification notification = notificationBuilder.setOngoing(true)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("App is running in background")
                .setPriority(NotificationManager.IMPORTANCE_MIN)
                .setCategory(Notification.CATEGORY_SERVICE)
                .build();
        startForeground(2, notification);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startMyOwnForeground();
        } else
            startForeground(1, new Notification());

        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        Toast.makeText(this, "onCreate", Toast.LENGTH_LONG).show();
        listBaiHat = new ArrayList<>();
    }

    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("ad", "1");
        intentStartCommand = intent;
        //super.onStartCommand(intent, flags, startId);
        Toast.makeText(this, "onStartComand", Toast.LENGTH_LONG).show();
        listBaiHat = intent.getParcelableArrayListExtra(MusicAdapter.LISTBAIHAT);
        position = intent.getIntExtra(MusicAdapter.POSITION, -1);
        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        Toast.makeText(this, "onBind", Toast.LENGTH_LONG).show();
        return iBinder;
    }

    public void startMusic() {
        //Log.d("----","Soure " + listBaiHat.get(position).getData());
/*        listBaiHat = intentStartCommand.getParcelableArrayListExtra(MusicAdapter.LISTBAIHAT);
        position = intentStartCommand.getIntExtra(MusicAdapter.POSITION, -1);*/
        //baiHat = intent.getParcelableExtra(MusicAdapter.BAIHAT);
        //if(baiHat != null)
        if (listBaiHat != null && listBaiHat.size() > 0) {
            Log.e("---", "Soure " + listBaiHat.get(position).getData());
            mediaPlayer = new MediaPlayer();
            try {
                if (position != -1) {
                    mediaPlayer.setDataSource(listBaiHat.get(position).getData());
                    mediaPlayer.prepare();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            mediaPlayer.start();
            updateUI();
        }
    }

    public void updateUI() {
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (mediaPlayer != null) {
                    try {
                        if (mediaPlayer.isPlaying()) {
                            guiDuLieu();
                        }
                        Thread.sleep(500);
                        //Log.d("ThreadName ", thread.getName());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        thread.start();
    }

    public void setSongsandSong(BaiHat baiHat, List<BaiHat> listBaiHat) {
        this.baiHat = baiHat;
        this.listBaiHat = listBaiHat;
    }

    private void guiDuLieu() {
        intent = new Intent(CURRENT_POSITION);
        intent.putExtra(CURRENT_POSITION, mediaPlayer.getCurrentPosition());
        //Log.d("Value ", mediaPlayer.getCurrentPosition() + "");
        localBroadcastManager.sendBroadcast(intent);
    }

    public void xuLyNext(int position) {
        this.position = position;
        mediaPlayer.stop();
        mediaPlayer.release();
        startMusic();
    }

    public void xuLyPrev(int position) {
        this.position = position;
        mediaPlayer.stop();
        mediaPlayer.release();
        startMusic();
    }

    public void pause() {
        mediaPlayer.pause();
    }

    public boolean isPlaying() {
        return mediaPlayer != null && mediaPlayer.isPlaying();
    }

    public void seekTo(int msec) {
        if (mediaPlayer != null) {
            mediaPlayer.seekTo(msec);
        }
    }

    public void play() {
        mediaPlayer.start();
    }

    public class BindService extends Binder {
        public MusicService getService() {
            return MusicService.this;
        }
    }

    public List<BaiHat> getListBaiHat() {
        return listBaiHat;
    }

    public void setListBaiHat(List<BaiHat> listBaiHat) {
        this.listBaiHat = listBaiHat;
    }

/*    @Override
    public boolean onUnbind(Intent intent)
    {
        //Toast.makeText(getApplicationContext(), "onUnbind", Toast.LENGTH_SHORT).show();
        // giải phóng tài nguyên khi nó không được liên kết
        if (mediaPlayer != null)
        {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
        //return super.onUnbind(intent);
        return false;
    }*/


/*    @Override
    public void onDestroy()
    {
        super.onDestroy();
        //Toast.makeText(getApplicationContext(), "Service is destroyed", Toast.LENGTH_SHORT).show();
        if (mediaPlayer != null)
        {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }*/
}