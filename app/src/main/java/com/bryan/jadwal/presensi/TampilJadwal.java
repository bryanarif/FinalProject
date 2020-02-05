package com.bryan.jadwal.presensi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.bryan.jadwal.presensi.adapter.RvAdapterJadwal;
import com.bryan.jadwal.presensi.app.AppController;
import com.bryan.jadwal.presensi.data.Data;
import com.bryan.jadwal.presensi.util.Server;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TampilJadwal extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, RvAdapterJadwal.RvAdapterJadwalClick {

    RecyclerView list;
    SwipeRefreshLayout swipe;
    List<Data> itemList = new ArrayList<Data>();
    RvAdapterJadwal adapterjadwal;
    TextView tv_tanggal, tv_alamat, tv_atasnama, tv_acara;


    private static final String TAG = TampilJadwal.class.getSimpleName();

    private static String url_select 	 = Server.URL + "select.php";

    public static final String TAG_TANGGAL  = "tanggal";
    public static final String TAG_ALAMAT   = "alamat";
    public static final String TAG_JAM      = "jam";
    public static final String TAG_ATASNAMA = "atasnama";
    public static final String TAG_ACARA    = "acara";
    public static final String TAG_TEAM     = "team";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    String tag_json_obj = "json_obj_req";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tampil_jadwal);

        swipe   = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout2);

        // menghubungkan variablel pada layout dan pada java
        list  =  findViewById(R.id.list);

        // untuk mengisi data dari JSON ke dalam adapter
        tv_tanggal = (TextView) findViewById(R.id.tv_tanggal);
        tv_alamat = (TextView) findViewById(R.id.tv_alamat);
        tv_atasnama = (TextView) findViewById(R.id.tv_atasnama);
        tv_acara = (TextView) findViewById(R.id.tv_acara);
        adapterjadwal = new RvAdapterJadwal(this, this);
        adapterjadwal.setData(itemList);
        list.setAdapter(adapterjadwal);

        // menamilkan widget refresh
        swipe.setOnRefreshListener(this);
        swipe.post(new Runnable() {
                       @Override
                       public void run() {
                           swipe.setRefreshing(true);
                           itemList.clear();
                           adapterjadwal.notifyDataSetChanged();
                           callVolley();
                       }
                   }
        );
    }

    private void kosong(){
        tv_tanggal.setText(null);
        tv_alamat.setText(null);
        tv_atasnama.setText(null);
        tv_acara.setText(null);
    }

    // untuk menampilkan semua data pada listview
    private void callVolley(){
        itemList.clear();
        adapterjadwal.notifyDataSetChanged();
        swipe.setRefreshing(true);

        // membuat request JSON
        JsonArrayRequest jArr = new JsonArrayRequest(url_select, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d(TAG, response.toString());

                // Parsing json
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject obj = response.getJSONObject(i);

                        Data item = new Data();

                        item.setTanggal(obj.getString(TAG_TANGGAL));
                        item.setAlamat(obj.getString(TAG_ALAMAT));
                        item.setJam(obj.getString(TAG_JAM));
                        item.setAtasnama(obj.getString(TAG_ATASNAMA));
                        item.setAcara(obj.getString(TAG_ACARA));
                        item.setTeam(obj.getString(TAG_TEAM));

                        // menambah item ke array
                        itemList.add(item);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                // notifikasi adanya perubahan data pada adapter
                adapterjadwal.setData(itemList);

                swipe.setRefreshing(false);
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                swipe.setRefreshing(false);
            }
        });

        // menambah request ke request queue
        AppController.getInstance().addToRequestQueue(jArr);
    }

    @Override
    public void onRefresh() {
        itemList.clear();
        adapterjadwal.notifyDataSetChanged();
        callVolley();
    }

    @Override
    public void onItemClick(final Data data) {
        Intent intent = new Intent(TampilJadwal.this, DetailJadwal.class);
        intent.putExtra("Data", data);
        startActivity(intent);
    }
}
