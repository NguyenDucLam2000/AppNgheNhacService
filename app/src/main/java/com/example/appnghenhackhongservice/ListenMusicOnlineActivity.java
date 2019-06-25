package com.example.appnghenhackhongservice;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.example.appnghenhackhongservice.loaddata.LoadListSongFromAPI;

public class ListenMusicOnlineActivity extends AppCompatActivity
{
    Button btnGo;
    EditText edtLinkAPI;
    LoadListSongFromAPI loadListSongFromAPI;
    ProgressBar pgLoad;
    public static final String DATA = "Data";

    public void addControls()
    {
        btnGo = findViewById(R.id.btnGo);
        edtLinkAPI = findViewById(R.id.edtLinkAPI);
        pgLoad = findViewById(R.id.pgLoad);
        pgLoad.setVisibility(View.INVISIBLE);
        edtLinkAPI.setText("http://ecomobileapp.com/static/music.json");
    }

    public void addEvents()
    {
        btnGo.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //Toast.makeText(getApplicationContext(), "Click", Toast.LENGTH_LONG).show();
                //Log.e("A", "1");
                if (!edtLinkAPI.getText().toString().isEmpty())
                {
                    //Log.e("A", "2");
                    //Toast.makeText(getApplicationContext(), "Click", Toast.LENGTH_LONG).show();
                    pgLoad.setVisibility(View.VISIBLE);
                    loadListSongFromAPI = new LoadListSongFromAPI(edtLinkAPI.getText().toString());
                    loadListSongFromAPI.execute();
                    try
                    {
                        //Log.e("A", "3");
                        String data = loadListSongFromAPI.get();
                        //Toast.makeText(getApplicationContext(), data, Toast.LENGTH_LONG).show();
                        if(data != null)
                        {
                            Intent intent = new Intent(ListenMusicOnlineActivity.this, ListenToMusicOnlineActivity.class);
                            intent.putExtra(DATA, data);
                            startActivity(intent);
                        }
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nghe_nhac_online);
        addControls();
        addEvents();
    }
}
