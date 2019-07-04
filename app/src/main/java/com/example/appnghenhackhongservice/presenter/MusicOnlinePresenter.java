package com.example.appnghenhackhongservice.presenter;

import android.content.Context;

import com.example.appnghenhackhongservice.intenet.Connectivity;
import com.example.appnghenhackhongservice.model.LoadListSongFromAPI;

public class MusicOnlinePresenter {
    private ResultHander resultHander;
    private LoadListSongFromAPI loadListSongFromAPI;
    public static boolean error = false;
    public static String data;
    public MusicOnlinePresenter(ResultHander resultHander){
        this.resultHander = resultHander;
    }
    public void hander(Context context, String linkAPI){
        error = false;
        if(!linkAPI.isEmpty()){
            if(Connectivity.isConnected(context)){ // Have internet
                loadListSongFromAPI = new LoadListSongFromAPI(context, linkAPI);
                loadListSongFromAPI.execute();
                try {
                    data = loadListSongFromAPI.get();
                    if (data != null) {
                        resultHander.successed(true);
                        return;
                    }
                    error = true;
                    resultHander.successed(false);
                }
                catch (Exception e) {
                    error = true;
                    resultHander.successed(false);
                    e.printStackTrace();
                }
            }
        }
        resultHander.successed(false); // No internet
    }
}
