package com.example.appnghenhackhongservice.presenter;

public class PlayPresenter {
    ResultPlay resultPlay;
    public PlayPresenter(ResultPlay resultPlay){
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
