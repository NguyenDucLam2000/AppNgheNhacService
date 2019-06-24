package com.example.appnghenhackhongservice.Service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.example.appnghenhackhongservice.Model.BaiHat;

import java.util.ArrayList;
import java.util.List;

public class MusicService extends Service
{
    public static final String CURRENT_POSITION = "position";
    MediaPlayer mediaPlayer;
    private List<BaiHat> listBaiHat;
    private int position;
    private IBinder iBinder = new BindService();
    Intent intent;
    LocalBroadcastManager localBroadcastManager;
    Thread thread;

    public int getPosition()
    {
        return position;
    }

    public void setPosition(int position)
    {
        this.position = position;
    }

    @Override
    public void onCreate()
    {
        super.onCreate();
        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        listBaiHat = new ArrayList<>();
    }

    @Override
    public IBinder onBind(Intent intent)
    {
        return iBinder;
    }

    public void startMusic()
    {
/*        listBaiHat = intent.getParcelableArrayListExtra(AdapterBaiHat.LISTBAIHAT);
        position = intent.getIntExtra(AdapterBaiHat.POSITION, -1);*/
        if (listBaiHat != null && listBaiHat.size() > 0)
        {
            mediaPlayer = new MediaPlayer();
            try
            {
                if (position != -1)
                {
                    mediaPlayer.setDataSource(listBaiHat.get(position).getData());
                    mediaPlayer.prepare();
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            mediaPlayer.start();
            updateUI();
        }
    }
    public void updateUI()
    {
        thread = new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                while (mediaPlayer != null)
                {
                    try
                    {
                        guiDuLieu();
                        Thread.sleep(1000);
                        //Log.d("ThreadName ", thread.getName());
                    }
                    catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                    //guiDuLieu();
                }
            }
        });
        thread.start();
    }

    private void guiDuLieu()
    {
        intent = new Intent(CURRENT_POSITION);
        intent.putExtra(CURRENT_POSITION, mediaPlayer.getCurrentPosition());
        Log.d("Value ", mediaPlayer.getCurrentPosition() + "");
        localBroadcastManager.sendBroadcast(intent);
    }

    public void xuLyNext(int position)
    {
        if (isNext())
        {
            if (position == listBaiHat.size())
            {
                this.position = 0;
            }
            else
            {
                this.position = position;
            }
            mediaPlayer.stop();
            mediaPlayer.release();
            startMusic();
        }
    }

    public void xuLyPrev(int position)
    {
        if (isNext())
        {
            if (position == -1)
            {
                this.position = listBaiHat.size() - 1;
            }
            else
            {
                this.position = position;
            }
            mediaPlayer.stop();
            mediaPlayer.release();
            startMusic();
        }
    }

    private boolean isNext()
    {
        return mediaPlayer != null;
    }

    public void pause()
    {
        mediaPlayer.pause();
    }

    public boolean isPlaying()
    {
        return mediaPlayer != null && mediaPlayer.isPlaying();
    }

    public void seekTo(int msec)
    {
        if (mediaPlayer != null)
        {
            mediaPlayer.seekTo(msec);
        }
    }

    public void play()
    {
        mediaPlayer.start();
    }

    public class BindService extends Binder
    {
        public MusicService getService()
        {
            return MusicService.this;
        }
    }

    public List<BaiHat> getListBaiHat()
    {
        return listBaiHat;
    }

    public void setListBaiHat(List<BaiHat> listBaiHat)
    {
        this.listBaiHat = listBaiHat;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
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
        return false;
        //return onUnbind(intent);
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        Toast.makeText(getApplicationContext(), "Service is destroyed", Toast.LENGTH_SHORT).show();
    }
}