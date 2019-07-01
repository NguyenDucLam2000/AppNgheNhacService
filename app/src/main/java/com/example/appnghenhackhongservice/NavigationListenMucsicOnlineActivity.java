package com.example.appnghenhackhongservice;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.appnghenhackhongservice.adapter.MusicAdapter;
import com.example.appnghenhackhongservice.model.Song;
import com.example.appnghenhackhongservice.data.ParserJSON;

import java.util.ArrayList;

public class NavigationListenMucsicOnlineActivity extends AppCompatActivity {
    RecyclerView rvListSongs;
    MusicAdapter adapter;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<Song> listSong;

    private void addControls() {
        rvListSongs = findViewById(R.id.rvListSongs);
        listSong = new ArrayList<>();
        listSong = ParserJSON.getListSong(getApplicationContext(), getIntent().getStringExtra(ListenMusicOnlineActivity.DATA));
        adapter = new MusicAdapter(NavigationListenMucsicOnlineActivity.this, listSong);
        layoutManager = new LinearLayoutManager(this);
        rvListSongs.setLayoutManager(layoutManager);
        rvListSongs.setAdapter(adapter);
    }

    private void addEvents() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listen_to_music_offline);
        addControls();
        addEvents();
    }
}
