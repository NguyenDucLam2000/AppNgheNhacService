package com.example.appnghenhackhongservice.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class BaiHat implements Parcelable
{
    private String hinh;
    private long thoiGian;

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
    }

    @Override
    public String toString()
    {
        return "BaiHat{" + "hinh='" + hinh + '\'' + ", thoiGian=" + thoiGian + ", tenCaSi='" + tenCaSi + '\'' + ", tenBaiHat='" + tenBaiHat + '\'' + ", data='" + data + '\'' + '}';
    }
}
