package com.example.appnghenhackhongservice.data;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;

import com.example.appnghenhackhongservice.model.Song;

import java.util.ArrayList;

public class LoadListSong extends AsyncTask<Void, Void, ArrayList<Song>>
{
    private Context context;
    private ContentResolver musicResolver;
    private Cursor cursor;
    private ListSong listSong;

    public ListSong getListSong()
    {
        return listSong;
    }

    public void setListSong(ListSong listSong)
    {
        this.listSong = listSong;
    }

    public LoadListSong(Context context)
    {
        this.context = context;
    }

    @Override
    protected ArrayList<Song> doInBackground(Void... voids)
    {
        ArrayList<Song> listSong = new ArrayList<>();
        musicResolver = context.getContentResolver();
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        //Uri uri = MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI;
        cursor = musicResolver.query(uri, null, null, null, null, null);
        if (cursor != null)
        {
            while (cursor.moveToNext())
            {
                String image = null;
                long time = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
                String singerName = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
                String songName = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
                String data = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
                //String hinh = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART));
                Song song = new Song(image , time, singerName, songName, data);
                listSong.add(song);
            }
        }
        return listSong;
    }

    @Override
    protected void onPostExecute(ArrayList<Song> songs)
    {
        super.onPostExecute(songs);
        listSong.getListSong(songs);
    }
}
