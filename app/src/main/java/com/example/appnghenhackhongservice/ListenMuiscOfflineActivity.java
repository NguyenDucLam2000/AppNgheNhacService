package com.example.appnghenhackhongservice;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.example.appnghenhackhongservice.adapter.MusicAdapter;
import com.example.appnghenhackhongservice.loaddata.ListSong;
import com.example.appnghenhackhongservice.loaddata.LoadListSong;
import com.example.appnghenhackhongservice.model.BaiHat;

import java.util.ArrayList;
import java.util.List;

public class ListenMuiscOfflineActivity extends AppCompatActivity implements ListSong
{
    List<BaiHat> listBaiHat;
    MusicAdapter musicAdapter;
    RecyclerView rvDanhSachBaiHat;
    RecyclerView.LayoutManager layoutManager;
    LoadListSong loadListSong;

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
        loadListSong = new LoadListSong(getApplicationContext());
        loadListSong.execute();
        loadListSong.setListSong(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nghe_nhac_offline);
        requestRead();
    }

    @Override
    public void getListSong(List<BaiHat> listSongs)
    {
        listBaiHat = new ArrayList<>();
        listBaiHat = listSongs;
        addControls();

    }
}
