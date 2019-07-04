package com.example.appnghenhackhongservice.model;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

import wseemann.media.FFmpegMediaMetadataRetriever;

public class ParserJSON {
    public static Context context;

    public static ArrayList<Song> getListSong(Context context, String data) {
        ParserJSON.context = context;
        ArrayList<Song> listSong = new ArrayList<>();
        try {
            JSONObject jData = new JSONObject(data);
            JSONArray jListSong = jData.getJSONArray("results");
            int length = jListSong.length();
            for (int i = 0; i < length; ++i) {
                JSONObject jSong = jListSong.getJSONObject(i);
                final Song song = new Song();
                song.setSongName(jSong.getString("name"));
                song.setData(jSong.getString("src"));
                song.setPosition(jSong.getInt("position"));
                FFmpegMediaMetadataRetriever mmr = new FFmpegMediaMetadataRetriever();
                mmr.setDataSource(jSong.getString("src"));
                long duration = Long.parseLong(mmr.extractMetadata(FFmpegMediaMetadataRetriever.METADATA_KEY_DURATION));
                mmr.release();
                song.setTime(duration);
                listSong.add(song);
            }
            Collections.sort(listSong);
            return listSong;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
