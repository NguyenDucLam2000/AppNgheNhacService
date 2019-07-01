package com.example.appnghenhackhongservice.data;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoadListSongFromAPI extends AsyncTask<Void, Void, String>
{
    private ProgressDialog progressDialog;
    private Context context;
    private String linkAPI;
    public static String REQUEST_METHOD = "GET";
    StringBuilder builder = new StringBuilder();

    public LoadListSongFromAPI(Context context, String linkAPI)
    {
        this.context = context;
        this.linkAPI = linkAPI;
    }

    public LoadListSongFromAPI(String linkAPI)
    {
        this.linkAPI = linkAPI;
    }

    public String getLinkAPI()
    {
        return linkAPI;
    }

    public void setLinkAPI(String linkAPI)
    {
        this.linkAPI = linkAPI;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
/*        progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("Thông báo");
        progressDialog.setMessage("Đang tải dữ liệu, vui lòng chờ");
        //progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(false);
        progressDialog.show();*/
    }

    @Override
    protected String doInBackground(Void... voids)
    {
        try
        {
            URL url = new URL(linkAPI);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.connect();
            //httpURLConnection.setRequestMethod(REQUEST_METHOD);
            //httpURLConnection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            String line = null;
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            while ((line = bufferedReader.readLine()) != null)
            {
                builder.append(line);
            }
            //inputStream.close();
            return builder.toString();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

/*
    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }
*/

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
/*        if(progressDialog != null){
            //progressDialog.cancel();
            progressDialog.dismiss();
        }*/
    }
}
