package com.example.appnghenhackhongservice;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.appnghenhackhongservice.offline.ListenMuiscOfflineActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener
{
    Button btnListenToMusicOnline, btnListenToMusicOffline;
    Intent intent;

    private void addControls()
    {
        btnListenToMusicOffline = findViewById(R.id.btnListenToMusicOffline);
        btnListenToMusicOnline = findViewById(R.id.btnListenToMusicOnline);
    }

    private void addEvents()
    {
        btnListenToMusicOnline.setOnClickListener(this);
        btnListenToMusicOffline.setOnClickListener(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addControls();
        addEvents();
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.btnListenToMusicOffline:
                intent = new Intent(MainActivity.this, ListenMuiscOfflineActivity.class);
                startActivity(intent);
                break;
            case R.id.btnListenToMusicOnline:
                intent = new Intent(MainActivity.this, ListenMusicOnlineActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}
