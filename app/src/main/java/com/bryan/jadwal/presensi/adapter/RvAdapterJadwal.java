package com.bryan.jadwal.presensi.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bryan.jadwal.presensi.R;
import com.bryan.jadwal.presensi.data.Data;

import java.util.ArrayList;
import java.util.List;

public class RvAdapterJadwal extends RecyclerView.Adapter<RvAdapterJadwal.MyViewHolder> {

    private final Context context;
    private final RvAdapterJadwalClick view;
    List<Data> list = new ArrayList<>();

    public RvAdapterJadwal(Context context, RvAdapterJadwalClick view) {
        this.context = context;
        this.view = view;
    }

    public void setData(List<Data> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_list_jadwal , parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.bind(list.get(position));
        holder.setView(view);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_tanggal, tv_alamat, tv_atasnama, tv_acara;
        Button detail;
        private ViewGroup item_jadwal;
        private Data data;


        MyViewHolder(@NonNull View v) {
            super(v);

            tv_tanggal = (TextView) v.findViewById(R.id.tv_tanggal);
            tv_alamat = (TextView) v.findViewById(R.id.tv_alamat);
            tv_atasnama = (TextView) v.findViewById(R.id.tv_atasnama);
            tv_acara = (TextView) v.findViewById(R.id.tv_acara);
            detail = (Button) v.findViewById(R.id.btn_detail);
            item_jadwal = v.findViewById(R.id.item_jadwal);
        }

        public void bind(Data data) {
            this.data = data;
            tv_tanggal.setText(data.getTanggal());
            tv_alamat.setText(data.getAlamat());
            tv_atasnama.setText(data.getAtasnama());
            tv_acara.setText(data.getAcara());
        }

        public void setView(final RvAdapterJadwalClick view) {
            detail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    view.onItemClick(data);
                }
            });
        }
    }

    public interface RvAdapterJadwalClick{
        void onItemClick(Data data);
    }

}
