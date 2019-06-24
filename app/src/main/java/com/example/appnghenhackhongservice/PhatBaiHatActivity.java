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
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Parcelable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.appnghenhackhongservice.Adapter.AdapterBaiHat;
import com.example.appnghenhackhongservice.Model.BaiHat;
import com.example.appnghenhackhongservice.Service.MusicService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

public class PhatBaiHatActivity extends AppCompatActivity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener
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
    MediaMetadataRetriever mediaMetadataRetriever;
    BroadcastReceiver broadcastReceiver;
    Notification notification;
    PendingIntent pendingIntent;
    private static final int NOTIFYCATION_ID = 100;
    NotificationManager notificationManager;
    Handler handler;

    private void addControls()
    {
        txtPlayTime = findViewById(R.id.txtPlayTime);
        txtTenBaiHat = findViewById(R.id.txtTenBaiHat);
        imgPlayorPause = findViewById(R.id.imgPlayorPause);
        imgNext = findViewById(R.id.imgNext);
        imgPrev = findViewById(R.id.imgPrev);
        sbBaiHat = findViewById(R.id.sbBaiHat);
        listBaiHat = new ArrayList<>();
        listBaiHat = getIntent().getParcelableArrayListExtra(AdapterBaiHat.LISTBAIHAT);
        position = getIntent().getIntExtra(AdapterBaiHat.POSITION, -1);
        baiHat = getIntent().getParcelableExtra(AdapterBaiHat.BAIHAT);
        timer = new Timer();
        //Log.e("ListBaiHat ", listBaiHat.size() + "");
        if (listBaiHat != null && listBaiHat.size() > 0)
        //if(baiHat != null)
        {
            startMusic();
            updateUI();
/*            handler = new Handler()
            {
                @Override
                public void handleMessage(Message msg)
                {
                    super.handleMessage(msg);

                }
            };*/
        }
    }

    private void startMusic()
    {
        // Khởi động BoundService, nhận vào 3 tham số, intent, serviceconection, flag
        // BIND_AUTO_CREATE sẽ start nếu service chưa được khởi tạo, nếu đã tạo rồi sẽ k start nữa
        if(!isBound)
        {
            intent = new Intent(PhatBaiHatActivity.this, MusicService.class);
            intent.putParcelableArrayListExtra(AdapterBaiHat.LISTBAIHAT, (ArrayList<? extends Parcelable>) listBaiHat);
            intent.putExtra(AdapterBaiHat.POSITION, position);
            bindService(intent, serviceConnection, BIND_AUTO_CREATE);
        }
        imgPlayorPause.setImageResource(R.drawable.pause);
        txtTenBaiHat.setText(listBaiHat.get(position).getTenBaiHat());
        mediaMetadataRetriever = new MediaMetadataRetriever();
        mediaMetadataRetriever.setDataSource(listBaiHat.get(position).getData());
        // Lấy ra thời lượng của bài hát
        duration = Integer.parseInt(mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION));
        Log.e("Soure ", listBaiHat.get(position).getData());
        txtPlayTime.setText("00:00/" + new SimpleDateFormat("mm:ss").format(duration));
        //txtPlayTime.setText((new SimpleDateFormat("mm:ss")).format(mediaPlayer.getDuration()));
        sbBaiHat.setMax(duration);
        showNotifycation();
    }

    ServiceConnection serviceConnection = new ServiceConnection()
    {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service)
        {
            musicService = ((MusicService.BindService) service).getService();
            musicService.setListBaiHat(listBaiHat);
            musicService.setPosition(position);
            musicService.startMusic();
            //startService(intent);
            isBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name)
        {
            musicService = null;
            isBound = false;
        }
    };

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
        addControls();
        addEvents();
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, new IntentFilter(MusicService.CURRENT_POSITION));
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
                sbBaiHat.setProgress(currentPosition);
            }
        };
    }

    private void showNotifycation()
    {
        intent = new Intent(this, PhatBaiHatActivity.class);
        pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        notification = new Notification.Builder(this).setContentTitle(listBaiHat.get(position).getTenCaSi())
                .setContentText(listBaiHat.get(position).getTenBaiHat())
                .setSmallIcon(R.drawable.music).setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .addAction(R.drawable.prev, "Prev", pendingIntent)
                .addAction(R.drawable.play, "PlayOrPause", pendingIntent)
                .addAction(R.drawable.next, "Next", pendingIntent).build();
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
        if(listBaiHat.size() > 0)
        {
            position++;
            musicService.xuLyNext(position);
            startMusic();
        }

    }

    private void xuLyPrev()
    {
        if(listBaiHat.size() > 0)
        {
            position--;
            musicService.xuLyPrev(position);
            startMusic();
        }
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
        unbindService(serviceConnection);
        //Log.d("onDestroy", "onDestroy");
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
    }

}
