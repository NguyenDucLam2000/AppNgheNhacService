package com.example.appnghenhackhongservice.view.online;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.appnghenhackhongservice.R;
import com.example.appnghenhackhongservice.presenter.MusicOnlinePresenter;
import com.example.appnghenhackhongservice.presenter.ResultHander;

public class NavigationActivity extends AppCompatActivity implements ResultHander {
    private Button btnGo;
    private EditText edtLinkAPI;
    public static final String DATA = "Data";
    private ProgressBar pgLoad;
    private MusicOnlinePresenter musicOnlinePresenter;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listen_online);
        addControls();
        addEvents();
    }

    @Override
    protected void onStart() {
        super.onStart();
        pgLoad.setVisibility(View.INVISIBLE);
    }

    public void addControls() {
        btnGo = findViewById(R.id.btnGo);
        pgLoad = findViewById(R.id.pgLoad);
        edtLinkAPI = findViewById(R.id.edtLinkAPI);
        edtLinkAPI.setText("http://ecomobileapp.com/static/music.json");
        musicOnlinePresenter = new MusicOnlinePresenter(this);
    }

    public void addEvents() {
        btnGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                musicOnlinePresenter.hander(NavigationActivity.this, edtLinkAPI.getText().toString());
            }
        });
    }

    @Override
    public void successed(boolean isSuccessed) {
        if(isSuccessed){
            pgLoad.setVisibility(View.VISIBLE);
            intent = new Intent(NavigationActivity.this, MucsicOnlineActivity.class);
            intent.putExtra(DATA, MusicOnlinePresenter.data);
            startActivity(intent);
        }
        else {
            if(!MusicOnlinePresenter.error){
                Toast.makeText(getApplicationContext(), "Network unavailable", Toast.LENGTH_LONG).show();
            }
            else {
                Toast.makeText(getApplicationContext(), "Link API is illegaled", Toast.LENGTH_LONG).show();
            }
        }
    }
}
