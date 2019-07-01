package com.example.appnghenhackhongservice;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.appnghenhackhongservice.data.LoadListSongFromAPI;
import com.example.appnghenhackhongservice.intenet.Connectivity;

public class ListenMusicOnlineActivity extends AppCompatActivity {
    Button btnGo;
    EditText edtLinkAPI;
    LoadListSongFromAPI loadListSongFromAPI;
    public static final String DATA = "Data";
    ProgressBar pgLoad;

    public void addControls() {
        btnGo = findViewById(R.id.btnGo);
        pgLoad = findViewById(R.id.pgLoad);
        pgLoad.setVisibility(View.INVISIBLE);
        edtLinkAPI = findViewById(R.id.edtLinkAPI);
        edtLinkAPI.setText("http://ecomobileapp.com/static/music.json");
    }

    public void addEvents() {
        btnGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Connectivity.isConnected(ListenMusicOnlineActivity.this)) {
                    if (!edtLinkAPI.getText().toString().isEmpty()) {
                        pgLoad.setVisibility(View.VISIBLE);
                        loadListSongFromAPI = new LoadListSongFromAPI(ListenMusicOnlineActivity.this, edtLinkAPI.getText().toString());
                        loadListSongFromAPI.execute();
                        try {
                            String data = loadListSongFromAPI.get();
                            if (data != null) {
                                Intent intent = new Intent(ListenMusicOnlineActivity.this, NavigationListenMucsicOnlineActivity.class);
                                intent.putExtra(DATA, data);
                                startActivity(intent);
                            }
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                else {
                    Toast.makeText(getApplicationContext(), "Mạng không khả dụng ", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listen_to_music_online);
        addControls();
        addEvents();
    }
}
