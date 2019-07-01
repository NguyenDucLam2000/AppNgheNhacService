package com.example.appnghenhackhongservice.offline;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.example.appnghenhackhongservice.PlaySongActivity;
import com.example.appnghenhackhongservice.R;
import com.example.appnghenhackhongservice.adapter.MusicAdapter;
import com.example.appnghenhackhongservice.data.ListSong;
import com.example.appnghenhackhongservice.data.LoadListSong;
import com.example.appnghenhackhongservice.model.Song;
import com.example.appnghenhackhongservice.service.MusicService;

import java.util.ArrayList;
import java.util.List;

import static com.example.appnghenhackhongservice.adapter.MusicAdapter.LIST_SONG;
import static com.example.appnghenhackhongservice.adapter.MusicAdapter.POSITION;
import static com.example.appnghenhackhongservice.adapter.MusicAdapter.TYPE;

public class ListenMuiscOfflineActivity extends AppCompatActivity implements ListSong,OfflineScreenView {
    List<Song> listSong;
    MusicAdapter musicAdapter;
    RecyclerView rvListSongs;
    RecyclerView.LayoutManager layoutManager;
    LoadListSong loadListSong;
    private MusicService musicService;
    private MusicService.BindService bindService;
    private void addControls() {
        rvListSongs = findViewById(R.id.rvListSongs);
        //layoutManager = new LinearLayoutManager(ListenMuiscOfflineActivity.this);
        layoutManager = new LinearLayoutManager(ListenMuiscOfflineActivity.this, LinearLayoutManager.VERTICAL, false);
        rvListSongs.setLayoutManager(layoutManager);
        musicAdapter = new MusicAdapter(getApplicationContext(), listSong,this);
        //Log.d("Adapter Size ", musicAdapter.getItemCount() + "");
        rvListSongs.setAdapter(musicAdapter);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 1) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getListSongs();
            }
            else {
                // Permission Denied
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public void requestRead() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }
        else {
            getListSongs();
        }
    }

    private void getListSongs() {
        loadListSong = new LoadListSong(getApplicationContext());
        loadListSong.execute();
        loadListSong.setListSong(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listen_to_music_offline);
        requestRead();
    }

    @Override
    public void getListSong(List<Song> listSongs) {
        listSong = new ArrayList<>();
        listSong = listSongs;
        addControls();

    }

    @Override
    public void songclick(int position) {
        Intent intent = new Intent(this, PlaySongActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putParcelableArrayListExtra(LIST_SONG, (ArrayList<? extends Parcelable>) listSong);
        intent.putExtra(POSITION, position);
        intent.putExtra(TYPE, true);
        //intent.putExtra(BAIHAT, listSong.get(getAdapterPosition()));
        //Toast.makeText(context, listSong.get(getAdapterPosition()).getTenBaiHat(), Toast.LENGTH_LONG).show();
        //intent.putExtra(DURATION, listSong.get(getAdapterPosition()).getThoiGian());
        //Log.d("Position", getAdapterPosition() + "");
        //Log.d("baiHat", listSong.get(getAdapterPosition()).getTenBaiHat());
        startActivity(intent);
    }
}
