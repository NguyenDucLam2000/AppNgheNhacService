package com.example.appnghenhackhongservice;

import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.example.appnghenhackhongservice.Adapter.AdapterBaiHat;
import com.example.appnghenhackhongservice.Model.BaiHat;

import java.util.ArrayList;
import java.util.List;

public class NgheNhacOfflineActivity extends AppCompatActivity
{
    List<BaiHat> listBaiHat;
    AdapterBaiHat adapterBaiHat;
    RecyclerView rvDanhSachBaiHat;
    ContentResolver musicResolver;
    Cursor cursor;
    RecyclerView.LayoutManager layoutManager;

    private void addControls()
    {
        rvDanhSachBaiHat = findViewById(R.id.rvDanhSachBaiHat);
        //layoutManager = new LinearLayoutManager(NgheNhacOfflineActivity.this);
        layoutManager = new LinearLayoutManager(NgheNhacOfflineActivity.this, LinearLayoutManager.VERTICAL, false);
        rvDanhSachBaiHat.setLayoutManager(layoutManager);
        adapterBaiHat = new AdapterBaiHat(getApplicationContext(), listBaiHat);
        //Log.d("Adapter Size ", adapterBaiHat.getItemCount() + "");
        rvDanhSachBaiHat.setAdapter(adapterBaiHat);
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
        musicResolver = getContentResolver();
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        //Uri uri = MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI;
        cursor = musicResolver.query(uri, null, null, null, null, null);
        if (cursor != null)
        {
            while (cursor.moveToNext())
            {
                String hinh = null;
                long thoigian = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION)) / 1000;
                String tenCaSi = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
                String tenBaiHat = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
                String data = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
                //String hinh = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART));
                BaiHat baiHat = new BaiHat(hinh , thoigian, tenCaSi, tenBaiHat, data);
                listBaiHat.add(baiHat);
            }
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nghe_nhac_offline);
        requestRead();
        addControls();
    }
}
