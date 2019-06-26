package com.example.appnghenhackhongservice.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.appnghenhackhongservice.R;
import com.example.appnghenhackhongservice.model.BaiHat;

import java.util.List;

public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.ViewHolder>
{
    private List<BaiHat> listBaiHat;
    private Context context;
    Intent intent;
    public static String BAIHAT = "BaiHat";
    public static String LISTBAIHAT = "ListBaiHat";
    public static String POSITION = "Position";
    public static String DURATION = "Duration";
    private boolean isSend = false;
    private SongClicked songClicked;

    public SongClicked getSongClicked()
    {
        return songClicked;
    }

    public void setSongClicked(SongClicked songClicked)
    {
        this.songClicked = songClicked;
    }

    public MusicAdapter()
    {

    }

    public MusicAdapter(Context context, List<BaiHat> listBaiHat)
    {
        this.context = context;
        this.listBaiHat = listBaiHat;
    }

    public List<BaiHat> getListBaiHat()
    {
        return listBaiHat;
    }

    public void setListBaiHat(List<BaiHat> listBaiHat)
    {
        this.listBaiHat = listBaiHat;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
    {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.recycleview_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i)
    {
        if (listBaiHat.get(i).getHinh() != null)
        {
            viewHolder.imgHinh.setImageBitmap(BitmapFactory.decodeFile(listBaiHat.get(i).getHinh()));
        }
        else
        {
            viewHolder.imgHinh.setImageResource(R.drawable.music);
        }
        if (listBaiHat.get(i).getTenCaSi() != null)
        {
            viewHolder.txtTenCaSi.setText(listBaiHat.get(i).getTenCaSi());
        }
        else
        {
            viewHolder.txtTenCaSi.setText("None");
        }
        viewHolder.txtTenBaiHat.setText(listBaiHat.get(i).getTenBaiHat());
        //Log.d("Name", listBaiHat.get(i).getTenBaiHat());
        long time = listBaiHat.get(i).getThoiGian() / 1000;
        viewHolder.txtThoiGianBaiHat.setText((time / 60) + ":" + (time % 60 <= 9 ? ("0" + time % 60) : time % 60));
    }

    @Override
    public int getItemCount()
    {
        return listBaiHat.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        ImageView imgHinh;
        TextView txtTenCaSi, txtTenBaiHat, txtThoiGianBaiHat;

        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            imgHinh = itemView.findViewById(R.id.imgHinh);
            txtTenBaiHat = itemView.findViewById(R.id.txtTenBaiHat);
            txtThoiGianBaiHat = itemView.findViewById(R.id.txtThoiGianBaiHat);
            txtTenCaSi = itemView.findViewById(R.id.txtTenCaSi);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v)
        {
            //Toast.makeText(context, listBaiHat.get(getAdapterPosition()).getThoiGian() + "", Toast.LENGTH_LONG).show();
/*            intent = new Intent(context, PlaySongActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);*/
            //intent.putExtra(BAIHAT, listBaiHat.get(getAdapterPosition()));
/*            if(isSend == false)
            {
                isSend = true;
            }*/
/*            intent.putParcelableArrayListExtra(LISTBAIHAT, (ArrayList<? extends Parcelable>) listBaiHat);
            intent.putExtra(POSITION, getAdapterPosition());
            intent.putExtra(BAIHAT, listBaiHat.get(getAdapterPosition()));
            //intent.putExtra(DURATION, listBaiHat.get(getAdapterPosition()).getThoiGian());
            //Log.d("Position", getAdapterPosition() + "");
            //Log.d("baiHat", listBaiHat.get(getAdapterPosition()).getTenBaiHat());
            context.startActivity(intent);*/
        }
    }
}
