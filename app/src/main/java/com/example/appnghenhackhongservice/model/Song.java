package com.example.appnghenhackhongservice.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Song implements Parcelable, Comparable<Song>
{
    private String image;
    private long time;
    private int position;
    private String singerName, songName, data;


    public Song(int position, String image, long time, String singerName, String songName, String data)
    {
        this.image = image;
        this.time = time;
        this.singerName = singerName;
        this.songName = songName;
        this.data = data;
        this.position = position;
    }

    public Song(String image, long time, String singerName, String songName, String data)
    {
        this.image = image;
        this.time = time;
        this.singerName = singerName;
        this.songName = songName;
        this.data = data;
    }

    public String getData()
    {
        return data;
    }

    public void setData(String data)
    {
        this.data = data;
    }
    
    public int getPosition()
    {
        return position;
    }

    public void setPosition(int position)
    {
        this.position = position;
    }

    public Song()
    {
    }

    protected Song(Parcel in)
    {
        image = in.readString();
        time = in.readLong();
        singerName = in.readString();
        songName = in.readString();
        data = in.readString();
        position = in.readInt();
    }

    public static final Creator<Song> CREATOR = new Creator<Song>()
    {
        @Override
        public Song createFromParcel(Parcel in)
        {
            return new Song(in);
        }

        @Override
        public Song[] newArray(int size)
        {
            return new Song[size];
        }
    };

    public String getImage()
    {
        return image;
    }

    public void setImage(String image)
    {
        this.image = image;
    }

    public long gettime()
    {
        return time;
    }

    public void setTime(long time)
    {
        this.time = time;
    }

    public String getSingerName()
    {
        return singerName;
    }

    public void setSingerName(String singerName)
    {
        this.singerName = singerName;
    }

    public String getSongName()
    {
        return songName;
    }

    public void setSongName(String songName)
    {
        this.songName = songName;
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeString(image);
        dest.writeLong(time);
        dest.writeString(singerName);
        dest.writeString(songName);
        dest.writeString(data);
        dest.writeInt(position);
    }

    @Override
    public int compareTo(Song o)
    {
        if(this.position == o.position)
        {
            return 0;
        }
        else if(this.position > o.position)
        {
            return 1;
        }
        return -1;
    }

}
