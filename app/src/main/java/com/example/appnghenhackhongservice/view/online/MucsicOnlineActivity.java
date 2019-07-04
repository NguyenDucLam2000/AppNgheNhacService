package com.example.appnghenhackhongservice.view.online;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.example.appnghenhackhongservice.R;
import com.example.appnghenhackhongservice.adapter.MusicAdapter;
import com.example.appnghenhackhongservice.model.ParserJSON;
import com.example.appnghenhackhongservice.model.Song;
import com.example.appnghenhackhongservice.presenter.PlayPresenter;
import com.example.appnghenhackhongservice.presenter.ResultPlay;
import com.example.appnghenhackhongservice.presenter.player.MusicPlayer;
import com.example.appnghenhackhongservice.view.offline.OfflineScreenView;
import com.example.appnghenhackhongservice.view.play.PlayActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import static com.example.appnghenhackhongservice.adapter.MusicAdapter.LIST_SONG;
import static com.example.appnghenhackhongservice.adapter.MusicAdapter.POSITION;
import static com.example.appnghenhackhongservice.adapter.MusicAdapter.TYPE;

public class MucsicOnlineActivity extends AppCompatActivity implements OfflineScreenView, ResultPlay {
    RecyclerView rvListSongs;
    private MusicAdapter adapter;
    RecyclerView.LayoutManager layoutManager;
    private List<Song> listSong;
    private MusicPlayer musicPlayer;
    private PlayPresenter playPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listen_offline);
        addControls();
    }

    private void addControls() {
        rvListSongs = findViewById(R.id.rvListSongs);
        listSong = new ArrayList<>();
        listSong = ParserJSON.getListSong(getApplicationContext(), getIntent().getStringExtra(NavigationActivity.DATA));
        adapter = new MusicAdapter(MucsicOnlineActivity.this, listSong, this);
        layoutManager = new LinearLayoutManager(this);
        rvListSongs.setLayoutManager(layoutManager);
        rvListSongs.setAdapter(adapter);
        playPresenter = new PlayPresenter(this);
    }

    @Override
    public void songclick(int position) {
        if (musicPlayer != null) {
            musicPlayer.release();
        }
        playPresenter.hander(position, listSong.size());
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onMusicEvent(MusicPlayer musicPlayer) {
        this.musicPlayer = musicPlayer;
    }


    @Override
    public void successed(boolean isSuccessed, int position) {
        if(isSuccessed){
            Intent intent = new Intent(this, PlayActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putParcelableArrayListExtra(LIST_SONG, (ArrayList<? extends Parcelable>) listSong);
            intent.putExtra(POSITION, position);
            intent.putExtra(TYPE, true);
            startActivity(intent);
        }
        else {
            Toast.makeText(getApplicationContext(), "Size is is illegaled", Toast.LENGTH_SHORT).show();
        }
    }
}
