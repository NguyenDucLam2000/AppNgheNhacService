package com.example.appnghenhackhongservice.view.play;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.appnghenhackhongservice.R;
import com.example.appnghenhackhongservice.adapter.MusicAdapter;
import com.example.appnghenhackhongservice.model.Song;
import com.example.appnghenhackhongservice.notification.MusicNotifycation;
import com.example.appnghenhackhongservice.receiver.NotifyListener;
import com.example.appnghenhackhongservice.service.MusicService;

import org.greenrobot.eventbus.EventBus;

import java.text.SimpleDateFormat;
import java.util.List;

public class PlayActivity extends AppCompatActivity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener, NotifyListener, AutoNext {
    private TextView txtPlayTime, txtSongName;
    private ImageView imgPlayorPause, imgNext, imgPrev;
    private SeekBar sbSong;
    private Song song;
    private List<Song> listSong;
    private int position;
    private int duration;
    private int mCurrentPosition;
    private Intent intent;
    private MusicService musicService;
    boolean isBound = false;
    private MusicService.BindService bindService;
    private Handler mHandler;
    private boolean change = true;
    public static boolean typeListSong = false;

    public void runOnUiThread() {
        mHandler = new Handler();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (musicService.getmMusicPlayer().getmMediaPlayer() != null) {
                    mCurrentPosition = musicService.getmMusicPlayer().getmMediaPlayer().getCurrentPosition();
                    sbSong.setProgress(mCurrentPosition);
                    txtPlayTime.setText((new SimpleDateFormat("mm:ss")).format(mCurrentPosition) + "/" + (new SimpleDateFormat("mm:ss")).format(duration));
                }
                mHandler.post(this);
            }
        });
    }

    ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            bindService = (MusicService.BindService) service;
            musicService = bindService.getService();
            isBound = true;
            musicService.getmMusicPlayer().setListSong(listSong);
            musicService.getmMusicPlayer().setSong(song);
            musicService.getmMusicPlayer().setPosition(position);
            if (musicService.getmMusicPlayer().getListSong() != null && musicService.getmMusicPlayer().getListSong().size() > 0) {
                if (typeListSong) {
                    musicService.getmMusicPlayer().startMusic();
                }
                musicService.getmMusicPlayer().showNotification();
                musicService.getmMusicPlayer().setAutoNext(PlayActivity.this);
                musicService.getmMusicPlayer().getReceiver().setUpdateUI(PlayActivity.this);
                initUI();
                runOnUiThread();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            musicService = null;
            isBound = false;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        doBindServie();
        addControls();
        addEvents();
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);

    }

    public void doBindServie() {
        typeListSong = getIntent().getBooleanExtra(MusicAdapter.TYPE, false);
        if (typeListSong) { // getIntent from Adapter
            listSong = getIntent().getParcelableArrayListExtra(MusicAdapter.LIST_SONG);
            position = getIntent().getIntExtra(MusicAdapter.POSITION, -1);

        }
        else { // getIntent from notitycation
            listSong = getIntent().getParcelableArrayListExtra(MusicNotifycation.LIST_SONG);
            position = getIntent().getIntExtra(MusicNotifycation.POSITION, -1);
        }
        bindService(new Intent(getApplicationContext(), MusicService.class), serviceConnection, Context.BIND_AUTO_CREATE);
        intent = new Intent(getApplicationContext(), MusicService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent);
        }
        else {
            startService(intent);
        }
    }

    private void addControls() {
        txtPlayTime = findViewById(R.id.txtPlayTime);
        txtSongName = findViewById(R.id.txtSongName);
        imgPlayorPause = findViewById(R.id.imgPlayorPause);
        imgNext = findViewById(R.id.imgNext);
        imgPrev = findViewById(R.id.imgPrev);
        sbSong = findViewById(R.id.sbSong);
    }

    private void initUI() {
        if (musicService.getmMusicPlayer().isPlaying()) {
            imgPlayorPause.setImageResource(R.drawable.pause);
        }
        else {
            imgPlayorPause.setImageResource(R.drawable.play);
        }
        txtSongName.setText(listSong.get(position).getSongName());
        duration = (int) listSong.get(position).gettime();
        txtPlayTime.setText("00:00/" + new SimpleDateFormat("mm:ss").format(duration));
        sbSong.setMax(duration);
    }

    private void addEvents() {
        imgPlayorPause.setOnClickListener(this);
        imgPrev.setOnClickListener(this);
        imgNext.setOnClickListener(this);
        sbSong.setOnSeekBarChangeListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgPlayorPause:
                playOrPause();
                break;
            case R.id.imgNext:
                next();
                break;
            case R.id.imgPrev:
                prev();
                break;
            default:
                break;
        }
    }

    private void next() {
        if (listSong.size() > 0) {
            if (musicService.getmMusicPlayer().isNext()) {
                position++;
                if (position == listSong.size()) {
                    position = 0;
                }
                if (change) {
                    musicService.getmMusicPlayer().next();
                }
                initUI();
                change = true;
            }
        }
    }

    private void prev() {
        if (listSong.size() > 0) {
            if (musicService.getmMusicPlayer().isNext()) {
                position--;
                if (position == -1) {
                    position = listSong.size() - 1;
                }
            }
            if (change) {
                musicService.getmMusicPlayer().prev();
            }
            initUI();
            change = true;
        }
    }

    private void playOrPause() {
        int resId = 0;
        if (change) {
            musicService.getmMusicPlayer().playOrPause();
        }
        if (musicService.getmMusicPlayer().isPlaying()) {
            resId = R.drawable.pause;
        }
        else {
            resId = R.drawable.play;
        }
        imgPlayorPause.setImageResource(resId);
        change = true;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        musicService.getmMusicPlayer().seekTo(seekBar.getProgress());
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (isBound) {
            unbindService(serviceConnection);
            isBound = false;
        }
    }

    @Override
    public void onBackPressed() {
        backtoOfflineScreen();
    }

    private void backtoOfflineScreen() {
        if (musicService != null && musicService.getmMusicPlayer() != null) {
            EventBus.getDefault().postSticky(musicService.getmMusicPlayer());
            finish();
        }
    }

    @Override
    public void onNext() {
        change = false;
        next();
    }

    @Override
    public void onPrev() {
        change = false;
        prev();
    }

    @Override
    public void onPlayOrPause() {
        change = false;
        playOrPause();
    }

    @Override
    public void onAutoNext() {
        next();
    }
}
