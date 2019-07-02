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
    private StringBuilder builder = new StringBuilder();

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
    protected String doInBackground(Void... voids)
    {
        try
        {
            URL url = new URL(linkAPI);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.connect();
            String line = null;
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            while ((line = bufferedReader.readLine()) != null)
            {
                builder.append(line);
            }
            return builder.toString();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }
}
