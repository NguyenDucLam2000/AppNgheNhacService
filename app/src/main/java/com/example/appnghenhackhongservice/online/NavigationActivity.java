package com.example.appnghenhackhongservice.online;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.appnghenhackhongservice.R;
import com.example.appnghenhackhongservice.data.LoadListSongFromAPI;
import com.example.appnghenhackhongservice.intenet.Connectivity;

public class NavigationActivity extends AppCompatActivity {
    Button btnGo;
    EditText edtLinkAPI;
    LoadListSongFromAPI loadListSongFromAPI;
    public static final String DATA = "Data";
    ProgressBar pgLoad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listen_online);
        addControls();
        addEvents();
    }

    public void addControls() {
        btnGo = findViewById(R.id.btnGo);
        pgLoad = findViewById(R.id.pgLoad);
        edtLinkAPI = findViewById(R.id.edtLinkAPI);
        edtLinkAPI.setText("http://ecomobileapp.com/static/music.json");
    }

    public void addEvents() {
        pgLoad.setVisibility(View.INVISIBLE);
        btnGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Connectivity.isConnected(NavigationActivity.this)) {
                    if (!edtLinkAPI.getText().toString().isEmpty()) {
                        pgLoad.setVisibility(View.VISIBLE);
                        loadListSongFromAPI = new LoadListSongFromAPI(NavigationActivity.this, edtLinkAPI.getText().toString());
                        loadListSongFromAPI.execute();
                        try {
                            String data = loadListSongFromAPI.get();
                            if (data != null) {
                                Intent intent = new Intent(NavigationActivity.this, MucsicOnlineActivity.class);
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
                    Toast.makeText(getApplicationContext(), "Network unavailable ", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
