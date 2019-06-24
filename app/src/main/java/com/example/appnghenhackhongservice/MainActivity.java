package com.example.appnghenhackhongservice;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener
{
    Button btnNgheNhacOnline, btnNgheNhacOffline;
    Intent intent;

    private void addControls()
    {
        btnNgheNhacOffline = findViewById(R.id.btnNgheNhacOffline);
        btnNgheNhacOnline = findViewById(R.id.btnNgheNhacOnline);
    }

    private void addEvents()
    {
        btnNgheNhacOnline.setOnClickListener(this);
        btnNgheNhacOffline.setOnClickListener(this);
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
            case R.id.btnNgheNhacOffline:
                intent = new Intent(MainActivity.this, NgheNhacOfflineActivity.class);
                startActivity(intent);
                break;
            case R.id.btnNgheNhacOnline:
                intent = new Intent(MainActivity.this, NgheNhacOnlineActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}
