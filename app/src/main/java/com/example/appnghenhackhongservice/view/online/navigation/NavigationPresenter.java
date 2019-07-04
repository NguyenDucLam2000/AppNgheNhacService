package com.example.appnghenhackhongservice.view.online.navigation;

import com.example.appnghenhackhongservice.view.online.listmusic.ResultHander;

public class NavigationPresenter {
    ResultHander resultPlay;
    public NavigationPresenter(ResultHander resultPlay){
        this.resultPlay = resultPlay;
    }
    public void hander(int position, int size){
        if(position < 0 || position > size){
            resultPlay.successed(false, position);
        }
        else {
            resultPlay.successed(true, position);
        }
    }
}
