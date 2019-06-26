package com.example.appnghenhackhongservice;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.example.appnghenhackhongservice.adapter.MusicAdapter;
import com.example.appnghenhackhongservice.loaddata.LoadListSong;
import com.example.appnghenhackhongservice.model.BaiHat;
import com.example.appnghenhackhongservice.service.MusicService;

import java.util.ArrayList;
import java.util.List;

public class ListenMuiscOfflineActivity extends AppCompatActivity
{
    List<BaiHat> listBaiHat;
    MusicAdapter musicAdapter;
    RecyclerView rvDanhSachBaiHat;
    RecyclerView.LayoutManager layoutManager;
    LoadListSong loadListSong;
    MusicService musicService;
    int position;
    boolean isBound = false;
    BaiHat baiHat;

    private void addControls()
    {
        rvDanhSachBaiHat = findViewById(R.id.rvDanhSachBaiHat);
        //layoutManager = new LinearLayoutManager(ListenMuiscOfflineActivity.this);
        layoutManager = new LinearLayoutManager(ListenMuiscOfflineActivity.this, LinearLayoutManager.VERTICAL, false);
        rvDanhSachBaiHat.setLayoutManager(layoutManager);
        musicAdapter = new MusicAdapter(getApplicationContext(), listBaiHat);
        //Log.d("Adapter Size ", musicAdapter.getItemCount() + "");
        rvDanhSachBaiHat.setAdapter(musicAdapter);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
    {
        if (requestCode == 1)
        {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                layListBaiHat();
            }
            else
            {
                // Permission Denied
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public void requestRead()
    {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
        {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }
        else
        {
            layListBaiHat();
        }
    }

    private void layListBaiHat()
    {
        listBaiHat = new ArrayList<>();
        loadListSong = new LoadListSong(getApplicationContext());
        loadListSong.execute();
        try
        {
            listBaiHat = loadListSong.get();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nghe_nhac_offline);
        requestRead();
        doBindServie();
        addControls();
    }

    ServiceConnection serviceConnection = new ServiceConnection()
    {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service)
        {
            Toast.makeText(getApplicationContext(), "Service is conected", Toast.LENGTH_LONG).show();
            musicService = ((MusicService.BindService) service).getService();

            //Intent intent = new Intent(PlaySongActivity.this, MusicService.class);
/*            intent.putParcelableArrayListExtra(MusicAdapter.LISTBAIHAT, (ArrayList<? extends Parcelable>) listBaiHat);
            intent.putExtra(MusicAdapter.POSITION, position);*/
            //intent.putExtra(MusicAdapter.BAIHAT, listBaiHat.get(position));
            //startService(intent);

/*            musicService.setBaiHat(baiHat);
            musicService.setListBaiHat(listBaiHat);
            musicService.setPosition(position);
            musicService.startMusic();*/
            isBound = true;
            Toast.makeText(getApplicationContext(), listBaiHat.get(position).getTenBaiHat(), Toast.LENGTH_LONG).show();
        }

        @Override
        public void onServiceDisconnected(ComponentName name)
        {
            //Toast.makeText(getApplicationContext(), "Service is Disconnected", Toast.LENGTH_LONG).show();
            //musicService = null;
            //Toast.makeText(getApplicationContext(), "Service Disconnected", Toast.LENGTH_SHORT).show();
            Log.d("onServiceDisconnected ", "Service Disconnected");
            isBound = false;
        }
    };
    public void doBindServie()
    {
        bindService(new Intent(getApplicationContext(), MusicService.class),
                serviceConnection, Context.BIND_AUTO_CREATE);

        Intent startNotStickyIntent = new Intent(getApplicationContext(), MusicService.class);
        startService(startNotStickyIntent);
    }
}
