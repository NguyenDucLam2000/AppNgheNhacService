package com.example.appnghenhackhongservice.loaddata;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;

import com.example.appnghenhackhongservice.model.BaiHat;

import java.util.ArrayList;

public class LoadListSong extends AsyncTask<Void, Void, ArrayList<BaiHat>>
{
    private Context context;
    private ContentResolver musicResolver;
    private Cursor cursor;

    public LoadListSong(Context context)
    {
        this.context = context;
    }

    @Override
    protected ArrayList<BaiHat> doInBackground(Void... voids)
    {
        ArrayList<BaiHat> listBaiHat = new ArrayList<>();
        musicResolver = context.getContentResolver();
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        //Uri uri = MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI;
        cursor = musicResolver.query(uri, null, null, null, null, null);
        if (cursor != null)
        {
            while (cursor.moveToNext())
            {
                String hinh = null;
                long thoigian = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
                String tenCaSi = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
                String tenBaiHat = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
                String data = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
                //String hinh = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART));
                BaiHat baiHat = new BaiHat(hinh , thoigian, tenCaSi, tenBaiHat, data);
                listBaiHat.add(baiHat);
            }
        }
        return listBaiHat;
    }

}
