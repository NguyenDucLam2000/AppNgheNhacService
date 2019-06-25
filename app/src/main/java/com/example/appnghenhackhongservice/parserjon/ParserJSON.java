package com.example.appnghenhackhongservice.parserjon;

import android.content.Context;

import com.example.appnghenhackhongservice.model.BaiHat;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

import wseemann.media.FFmpegMediaMetadataRetriever;

public class ParserJSON
{
    public static Context context;
    public static ArrayList<BaiHat> getListSong(Context context, String data)
    {
        ParserJSON.context = context;
        ArrayList<BaiHat> listSong = new ArrayList<>();
        try
        {
            JSONObject jData = new JSONObject(data);
            JSONArray jListSong = jData.getJSONArray("results");
            int length = jListSong.length();
            for(int i = 0; i < length; ++i)
            {
                JSONObject jSong = jListSong.getJSONObject(i);
                final BaiHat baiHat = new BaiHat();
                baiHat.setTenBaiHat(jSong.getString("name"));
                baiHat.setData(jSong.getString("src"));
                baiHat.setVitri(jSong.getInt("position"));
                FFmpegMediaMetadataRetriever mmr = new FFmpegMediaMetadataRetriever();
                mmr.setDataSource(jSong.getString("src"));
                long duration = Long.parseLong(mmr.extractMetadata(FFmpegMediaMetadataRetriever.METADATA_KEY_DURATION));
                mmr.release();
                baiHat.setThoiGian(duration);
                listSong.add(baiHat);
                //Log.d("Song ", song.toString());
            }
            Collections.sort(listSong);
            return listSong;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }
}
