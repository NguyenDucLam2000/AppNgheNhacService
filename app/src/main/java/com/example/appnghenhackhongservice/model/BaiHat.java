package com.example.appnghenhackhongservice.model;

import android.os.Parcel;
import android.os.Parcelable;

public class BaiHat implements Parcelable, Comparable<BaiHat>
{
    private String hinh;
    private long thoiGian;
    private int vitri;

    public BaiHat(int vitri, String hinh, long thoiGian, String tenCaSi, String tenBaiHat, String data)
    {
        this.hinh = hinh;
        this.thoiGian = thoiGian;
        this.tenCaSi = tenCaSi;
        this.tenBaiHat = tenBaiHat;
        this.data = data;
        this.vitri = vitri;
    }

    public BaiHat(String hinh, long thoiGian, String tenCaSi, String tenBaiHat, String data)
    {
        this.hinh = hinh;
        this.thoiGian = thoiGian;
        this.tenCaSi = tenCaSi;
        this.tenBaiHat = tenBaiHat;
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

    private String tenCaSi, tenBaiHat, data;

    public int getVitri()
    {
        return vitri;
    }

    public void setVitri(int vitri)
    {
        this.vitri = vitri;
    }

    public BaiHat()
    {
    }

    protected BaiHat(Parcel in)
    {
        hinh = in.readString();
        thoiGian = in.readLong();
        tenCaSi = in.readString();
        tenBaiHat = in.readString();
        data = in.readString();
        vitri = in.readInt();
    }

    public static final Creator<BaiHat> CREATOR = new Creator<BaiHat>()
    {
        @Override
        public BaiHat createFromParcel(Parcel in)
        {
            return new BaiHat(in);
        }

        @Override
        public BaiHat[] newArray(int size)
        {
            return new BaiHat[size];
        }
    };

    public String getHinh()
    {
        return hinh;
    }

    public void setHinh(String hinh)
    {
        this.hinh = hinh;
    }

    public long getThoiGian()
    {
        return thoiGian;
    }

    public void setThoiGian(long thoiGian)
    {
        this.thoiGian = thoiGian;
    }

    public String getTenCaSi()
    {
        return tenCaSi;
    }

    public void setTenCaSi(String tenCaSi)
    {
        this.tenCaSi = tenCaSi;
    }

    public String getTenBaiHat()
    {
        return tenBaiHat;
    }

    public void setTenBaiHat(String tenBaiHat)
    {
        this.tenBaiHat = tenBaiHat;
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeString(hinh);
        dest.writeLong(thoiGian);
        dest.writeString(tenCaSi);
        dest.writeString(tenBaiHat);
        dest.writeString(data);
        dest.writeInt(vitri);
    }

    @Override
    public int compareTo(BaiHat o)
    {
        if(this.vitri == o.vitri)
        {
            return 0;
        }
        else if(this.vitri > o.vitri)
        {
            return 1;
        }
        return -1;
    }

}
