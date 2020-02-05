package com.bryan.jadwal.presensi.data;


import android.os.Parcel;
import android.os.Parcelable;

public class Data implements Parcelable {
    private String id, tanggal, alamat, jam, atasnama, acara, team;

    public Data() {
    }

    public Data(String id, String tanggal, String alamat, String jam, String atasnama, String acara, String team) {
        this.id = id;
        this.tanggal = tanggal;
        this.alamat = alamat;
        this.jam = jam;
        this.atasnama = atasnama;
        this.acara = acara;
        this.team = team;
    }

    protected Data(Parcel in) {
        tanggal = in.readString();
        alamat = in.readString();
        jam = in.readString();
        atasnama = in.readString();
        acara = in.readString();
        team = in.readString();
    }

    public static final Creator<Data> CREATOR = new Creator<Data>() {
        @Override
        public Data createFromParcel(Parcel in) {
            return new Data(in);
        }

        @Override
        public Data[] newArray(int size) {
            return new Data[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getJam() {
        return jam;
    }

    public void setJam(String jam) {
        this.jam = jam;
    }

    public String getAtasnama() {
        return atasnama;
    }

    public void setAtasnama(String atasnama) {
        this.atasnama = atasnama;
    }

    public String getAcara() {
        return acara;
    }

    public void setAcara(String acara) {
        this.acara = acara;
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }


    @Override
    public int describeContents() {
        return hashCode();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(tanggal);
        dest.writeString(alamat);
        dest.writeString(jam);
        dest.writeString(atasnama);
        dest.writeString(acara);
        dest.writeString(team);
    }
}
