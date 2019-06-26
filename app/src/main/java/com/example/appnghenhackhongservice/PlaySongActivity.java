package com.example.appnghenhackhongservice;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcelable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appnghenhackhongservice.adapter.MusicAdapter;
import com.example.appnghenhackhongservice.model.BaiHat;
import com.example.appnghenhackhongservice.service.MusicService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

public class PlaySongActivity extends AppCompatActivity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener
{
    TextView txtPlayTime, txtTenBaiHat;
    ImageView imgPlayorPause, imgNext, imgPrev;
    SeekBar sbBaiHat;
    BaiHat baiHat;
    Timer timer;
    List<BaiHat> listBaiHat;
    int position;
    int duration;
    int currentPosition;
    Intent intent;
    MusicService musicService;
    boolean isBound = false;
    BroadcastReceiver broadcastReceiver;
    Notification notification;
    PendingIntent pendingIntent;
    MusicService.BindService bindService;
    private static final int NOTIFYCATION_ID = 100;
    NotificationManager notificationManager;
    ServiceConnection serviceConnection = new ServiceConnection()
    {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service)
        {
            Log.d("AAA", "In");
            Toast.makeText(getApplicationContext(), "Service is conected", Toast.LENGTH_LONG).show();
            LocalBroadcastManager.getInstance(PlaySongActivity.this).registerReceiver(broadcastReceiver, new IntentFilter(MusicService.CURRENT_POSITION));
            bindService = (MusicService.BindService) service;
            musicService = bindService.getService();

            //Intent intent = new Intent(PlaySongActivity.this, MusicService.class);
/*            intent.putParcelableArrayListExtra(MusicAdapter.LISTBAIHAT, (ArrayList<? extends Parcelable>) listBaiHat);
            intent.putExtra(MusicAdapter.POSITION, position);*/
            //intent.putExtra(MusicAdapter.BAIHAT, listBaiHat.get(position));
            //startService(intent);

/*            musicService.setBaiHat(baiHat);
            musicService.setListBaiHat(listBaiHat);
            musicService.setPosition(position);*/
            musicService.startMusic();
            isBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name)
        {
            Toast.makeText(getApplicationContext(), "Service is Disconnected", Toast.LENGTH_LONG).show();
            //musicService = null;
            //Toast.makeText(getApplicationContext(), "Service Disconnected", Toast.LENGTH_SHORT).show();
            Log.d("onServiceDisconnected ", "Service Disconnected");
            isBound = false;
        }
    };

    public void doBindServie()
    {
        listBaiHat = new ArrayList<>();
        baiHat = getIntent().getParcelableExtra(MusicAdapter.BAIHAT);
        listBaiHat = getIntent().getParcelableArrayListExtra(MusicAdapter.LISTBAIHAT);
        position = getIntent().getIntExtra(MusicAdapter.POSITION, -1);

        bindService(new Intent(getApplicationContext(), MusicService.class), serviceConnection, Context.BIND_AUTO_CREATE);
        Intent intent = new Intent(getApplicationContext(), MusicService.class);
        intent.putParcelableArrayListExtra(MusicAdapter.LISTBAIHAT, (ArrayList<? extends Parcelable>) listBaiHat);
        intent.putExtra(MusicAdapter.POSITION, position);
        //intent.putExtra(MusicAdapter.BAIHAT, listBaiHat.get(position));
        startService(intent);
    }

    private void addControls()
    {
        txtPlayTime = findViewById(R.id.txtPlayTime);
        txtTenBaiHat = findViewById(R.id.txtTenBaiHat);
        imgPlayorPause = findViewById(R.id.imgPlayorPause);
        imgNext = findViewById(R.id.imgNext);
        imgPrev = findViewById(R.id.imgPrev);
        sbBaiHat = findViewById(R.id.sbBaiHat);

        //duration = (int) getIntent().getLongExtra(MusicAdapter.DURATION, - 1);
        //baiHat = getIntent().getParcelableExtra(MusicAdapter.BAIHAT);
        //Log.e("ListBaiHat ", listBaiHat.size() + "");
        if (listBaiHat != null && listBaiHat.size() > 0)
        //if(baiHat != null)
        {
            startMusic();
            updateUI();
        }
    }


    private void startMusic()
    {
        // Khởi động BoundService, nhận vào 3 tham số, intent, serviceconection, flag
        // BIND_AUTO_CREATE sẽ start nếu service chưa được khởi tạo, nếu đã tạo rồi sẽ k start nữa
        imgPlayorPause.setImageResource(R.drawable.pause);
        txtTenBaiHat.setText(listBaiHat.get(position).getTenBaiHat());

        duration = (int) listBaiHat.get(position).getThoiGian();
        txtPlayTime.setText("00:00/" + new SimpleDateFormat("mm:ss").format(duration));
        sbBaiHat.setMax(duration);
        showNotifycation();
    }

    private void addEvents()
    {
        imgPlayorPause.setOnClickListener(this);
        imgPrev.setOnClickListener(this);
        imgNext.setOnClickListener(this);
        sbBaiHat.setOnSeekBarChangeListener(this);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phat_bai_hat);
        doBindServie();
        addControls();
        addEvents();
    }

    private void updateUI()
    {
        broadcastReceiver = new BroadcastReceiver()
        {
            @Override
            public void onReceive(Context context, Intent intent)
            {
                currentPosition = intent.getIntExtra(MusicService.CURRENT_POSITION, -1);
                //Log.d("Send ", currentPosition + "");
                txtPlayTime.setText((new SimpleDateFormat("mm:ss")).format(currentPosition) + "/" + (new SimpleDateFormat("mm:ss")).format(duration));
                //txtPlayTime.setText((new SimpleDateFormat("mm:ss")).format(currentPosition) + "/" + duration / 60 + ":" + ((duration % 60) <= 9 ? ("0" + (duration % 60) ) : duration % 60));
                sbBaiHat.setProgress(currentPosition);
            }
        };
    }

    private void showNotifycation()
    {
        intent = new Intent(this, PlaySongActivity.class);
        pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        notification = new Notification.Builder(this).setContentTitle(listBaiHat.get(position).getTenCaSi()).setContentText(listBaiHat.get(position).getTenBaiHat()).setSmallIcon(R.drawable.music).setAutoCancel(true).setContentIntent(pendingIntent).build();
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFYCATION_ID, notification);
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.imgPlayorPause:
                xuLyPlayOrPause();
                break;
            case R.id.imgNext:
                xuLyNext();
                break;
            case R.id.imgPrev:
                xuLyPrev();
                break;
            default:
                break;
        }
    }

    private void xuLyNext()
    {
        if (listBaiHat.size() > 0)
        {
            if (isNext())
            {
                position++;
                if (position == listBaiHat.size())
                {
                    position = 0;
                }
                musicService.xuLyNext(position);
                startMusic();
            }
        }
    }

    private void xuLyPrev()
    {
        if (listBaiHat.size() > 0)
        {
            if (isNext())
            {
                position--;
                if (position == -1)
                {
                    position = listBaiHat.size() - 1;
                }
            }
            musicService.xuLyPrev(position);
            startMusic();
        }
    }

    private boolean isNext()
    {
        return musicService.getMediaPlayer() != null;
    }

    private void xuLyPlayOrPause()
    {
        if (musicService.isPlaying())
        {
            imgPlayorPause.setImageResource(R.drawable.play);
            musicService.pause();
        }
        else
        {
            imgPlayorPause.setImageResource(R.drawable.pause);
            musicService.play();
        }

    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
    {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar)
    {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar)
    {
        musicService.seekTo(seekBar.getProgress());
    }

    @Override
    protected void onPause()
    {
        super.onPause();
/*        if (isBound)
        {
            unbindService(serviceConnection);
        }
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);*/
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
/*        if (mediaPlayer != null)
        {
            if (mediaPlayer.isPlaying())
            {
                mediaPlayer.stop();
                mediaPlayer.release();
            }
        }*/
/*        Log.d("Destroy ", "Destroyed");
        if (isBound)
        {
            Log.d("Destroy 2", "Destroyed");
            unbindService(serviceConnection);
        }*/
        //Log.d("onDestroy", "onDestroy");
        //LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
    }

}
