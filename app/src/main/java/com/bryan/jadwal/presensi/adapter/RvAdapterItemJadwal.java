package com.bryan.jadwal.presensi.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bryan.jadwal.presensi.R;
import com.bryan.jadwal.presensi.data.Data;

import java.util.ArrayList;
import java.util.List;

public class RvAdapterItemJadwal extends RecyclerView.Adapter<RvAdapterItemJadwal.MyViewHolder> {

    private final Context context;
    List<Data> list = new ArrayList<>();
    private RvAdapterItemJadwalClick view = null;

    public RvAdapterItemJadwal(Context context) {
        this.context = context;
    }

    public RvAdapterItemJadwal(Context context, RvAdapterItemJadwalClick view) {
        this.view = view;
        this.context = context;
    }

    public void setData(List<Data> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RvAdapterItemJadwal.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.list_row, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RvAdapterItemJadwal.MyViewHolder holder, int position) {
        holder.bind(list.get(position));
        if (view != null) {
            holder.setOnItemClick(view, position);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public interface RvAdapterItemJadwalClick {
        void onItemClick(int position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView id, tanggal, alamat, jam, atasnama, acara, team;
        private ViewGroup item;

        public MyViewHolder(@NonNull View v) {
            super(v);
            id = v.findViewById(R.id.id);
            tanggal = v.findViewById(R.id.tanggal);
            alamat = v.findViewById(R.id.alamat);
            jam = v.findViewById(R.id.jam);
            atasnama = v.findViewById(R.id.atasnama);
            acara = v.findViewById(R.id.acara);
            team = v.findViewById(R.id.team);
            item = v.findViewById(R.id.item);
        }

        public void bind(Data data) {
            id.setText(data.getId());
            tanggal.setText(data.getTanggal());
            alamat.setText(data.getAlamat());
            jam.setText(data.getJam());
            atasnama.setText(data.getAtasnama());
            acara.setText(data.getAcara());
            team.setText(data.getTeam());
        }

        public void setOnItemClick(final RvAdapterItemJadwalClick view, final int position) {
            item.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (view != null) {
                        view.onItemClick(position);
                    }
                    return false;
                }
            });
        }

    }
}
