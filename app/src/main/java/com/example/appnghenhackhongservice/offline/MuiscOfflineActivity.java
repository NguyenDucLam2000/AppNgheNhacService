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

import com.example.appnghenhackhongservice.Player.MusicPlayer;
import com.example.appnghenhackhongservice.R;
import com.example.appnghenhackhongservice.adapter.MusicAdapter;
import com.example.appnghenhackhongservice.data.ListSong;
import com.example.appnghenhackhongservice.data.LoadListSong;
import com.example.appnghenhackhongservice.model.Song;
import com.example.appnghenhackhongservice.view.PlayActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import static com.example.appnghenhackhongservice.adapter.MusicAdapter.LIST_SONG;
import static com.example.appnghenhackhongservice.adapter.MusicAdapter.POSITION;
import static com.example.appnghenhackhongservice.adapter.MusicAdapter.TYPE;
/*import static com.example.appnghenhackhongservice.service.MusicService.mMusicPlayer;*/

public class MuiscOfflineActivity extends AppCompatActivity implements ListSong, OfflineScreenView {
    private List<Song> listSong;
    private MusicAdapter musicAdapter;
    private RecyclerView rvListSongs;
    private RecyclerView.LayoutManager layoutManager;
    private LoadListSong loadListSong;
    private MusicPlayer musicPlayer;
    private void addControls() {
        rvListSongs = findViewById(R.id.rvListSongs);
        layoutManager = new LinearLayoutManager(MuiscOfflineActivity.this, LinearLayoutManager.VERTICAL, false);
        rvListSongs.setLayoutManager(layoutManager);
        musicAdapter = new MusicAdapter(getApplicationContext(), listSong,this);
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
        setContentView(R.layout.activity_listen_offline);
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
        if(musicPlayer!=null){
            musicPlayer.release();
        }
        Intent intent = new Intent(this, PlayActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putParcelableArrayListExtra(LIST_SONG, (ArrayList<? extends Parcelable>) listSong);
        intent.putExtra(POSITION, position);
        intent.putExtra(TYPE, true);
        //startActivityForResult(intent,1);
        startActivity(intent);
    }

/*    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==1&&resultCode==RESULT_OK){
            if(data!=null){
//                Gson gson = new Gson();
//                mJsonMusicservice = data.getStringExtra("musicservice");
//                serviceConnection = gson.fromJson(mJsonMusicservice,MusicPlayer.class);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }*/
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

    @Subscribe(threadMode = ThreadMode.MAIN,sticky = true)
    public void onMusicEvent(MusicPlayer musicPlayer) {
        this.musicPlayer = musicPlayer;
    };
}
