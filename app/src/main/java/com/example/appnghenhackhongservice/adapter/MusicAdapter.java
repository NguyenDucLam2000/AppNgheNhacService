package com.example.appnghenhackhongservice.adapter;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.appnghenhackhongservice.R;
import com.example.appnghenhackhongservice.model.Song;
import com.example.appnghenhackhongservice.offline.OfflineScreenView;

import java.text.SimpleDateFormat;
import java.util.List;

public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.ViewHolder> {
    private List<Song> listSong;
    private Context context;
    public static final String LIST_SONG = "ListBaiHat";
    public static final String POSITION = "Position";
    public static final String DURATION = "Duration";
    public static final String TYPE = "Type";
    private OfflineScreenView offlineScreenView;

    public MusicAdapter(Context context, List<Song> listSong, OfflineScreenView offlineScreenView) {
        this.context = context;
        this.listSong = listSong;
        this.offlineScreenView = offlineScreenView;
    }

    public List<Song> getListSong() {
        return listSong;
    }

    public void setListSong(List<Song> listSong) {
        this.listSong = listSong;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        if (listSong.get(i).getImage() != null) {
            viewHolder.imgHinh.setImageBitmap(BitmapFactory.decodeFile(listSong.get(i).getImage()));
        }
        else {
            viewHolder.imgHinh.setImageResource(R.drawable.music);
        }
        if (listSong.get(i).getSingerName() != null) {
            viewHolder.txtSingerName.setText(listSong.get(i).getSingerName());
        }
        else {
            viewHolder.txtSingerName.setText("None");
        }
        viewHolder.txtSongName.setText(listSong.get(i).getSongName());
        viewHolder.txtSongDuration.setText(new SimpleDateFormat("mm:ss").format(listSong.get(i).gettime()));
    }

    @Override
    public int getItemCount() {
        return listSong.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imgHinh;
        TextView txtSingerName, txtSongName, txtSongDuration;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgHinh = itemView.findViewById(R.id.imgHinh);
            txtSongName = itemView.findViewById(R.id.txtSongName);
            txtSongDuration = itemView.findViewById(R.id.txtSongDuration);
            txtSingerName = itemView.findViewById(R.id.txtSingerName);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (offlineScreenView != null) {
                offlineScreenView.songclick(getAdapterPosition());
            }
        }
    }
}
