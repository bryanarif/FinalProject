package com.bryan.jadwal.presensi;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bryan.jadwal.presensi.data.Data;


public class DetailJadwal extends AppCompatActivity {

    TextView tv_detailtanggal, tv_detailalamat, tv_detailjam, tv_detailAtasNama, tv_detailacara, tv_detailteam;
    private Data data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_jadwal);
        getData();
        findView();
        setData();
    }

    private void getData() {
        data = getIntent().getParcelableExtra("Data");
    }

    private void findView() {
        tv_detailtanggal = findViewById(R.id.tv_detailtanggal);
        tv_detailalamat = findViewById(R.id.tv_detaillokasi);
        tv_detailjam = findViewById(R.id.tv_detailjam);
        tv_detailAtasNama = findViewById(R.id.tv_detailAtasNama);
        tv_detailacara = findViewById(R.id.tv_detailAcara);
        tv_detailteam = findViewById(R.id.tv_detailteam);
    }

    private void setData() {
        tv_detailtanggal.setText(data.getTanggal());
        tv_detailalamat.setText(data.getAlamat());
        tv_detailjam.setText(data.getJam());
        tv_detailAtasNama.setText(data.getAtasnama());
        tv_detailacara.setText(data.getAcara());
        tv_detailteam.setText(data.getTeam());
    }

}
