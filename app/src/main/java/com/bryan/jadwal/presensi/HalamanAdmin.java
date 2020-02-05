package com.bryan.jadwal.presensi;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.bryan.jadwal.presensi.adapter.RvAdapterItemJadwal;
import com.bryan.jadwal.presensi.app.AppController;
import com.bryan.jadwal.presensi.data.Data;
import com.bryan.jadwal.presensi.util.Server;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static androidx.swiperefreshlayout.widget.SwipeRefreshLayout.*;
import static com.bryan.jadwal.presensi.adapter.RvAdapterItemJadwal.*;

public class HalamanAdmin extends AppCompatActivity implements OnRefreshListener, RvAdapterItemJadwalClick {

    public static final String TAG_ID = "id";
    public static final String TAG_TANGGAL = "tanggal";
    public static final String TAG_ALAMAT = "alamat";
    public static final String TAG_JAM = "jam";
    public static final String TAG_ATASNAMA = "atasnama";
    public static final String TAG_ACARA = "acara";
    public static final String TAG_TEAM = "team";
    private static final String TAG = HalamanAdmin.class.getSimpleName();
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    private static String url_select = Server.URL + "select.php";
    private static String url_insert = Server.URL + "insert.php";
    private static String url_edit = Server.URL + "edit.php";
    private static String url_update = Server.URL + "update.php";
    private static String url_delete = Server.URL + "delete.php";
    Toolbar toolbar;
    FloatingActionButton fab;
    RecyclerView list;
    SwipeRefreshLayout swipe;
    List<Data> itemList = new ArrayList<Data>();
    RvAdapterItemJadwal adapteritemjadwal;
    int success;
    AlertDialog.Builder dialog;
    LayoutInflater inflater;
    View dialogView;
    EditText txt_id, txt_tanggal, txt_alamat, txt_jam, txt_atasnama, txt_acara, txt_team;
    String id, tanggal, alamat, jam, atasnama, acara, team;
    String tag_json_obj = "json_obj_req";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_halaman);

        // menghubungkan variablel pada layout dan pada java
        fab = findViewById(R.id.fab_add);
        swipe = findViewById(R.id.swipe_refresh_layout);
        list = findViewById(R.id.list);

        // untuk mengisi data dari JSON ke dalam adapter

        adapteritemjadwal = new RvAdapterItemJadwal(this, this);
        list.setAdapter(adapteritemjadwal);

        // menamilkan widget refresh
        swipe.setOnRefreshListener(this);

        swipe.post(new Runnable() {
                       @Override
                       public void run() {
                           swipe.setRefreshing(true);
                           itemList.clear();
                           adapteritemjadwal.notifyDataSetChanged();
                           callVolley();
                       }
                   }
        );

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogForm("", "", "", "", "", "", "", "SIMPAN");
            }
        });
    }

    @Override
    public void onRefresh() {
        itemList.clear();
        adapteritemjadwal.notifyDataSetChanged();
        callVolley();
    }

    private void kosong() {
        txt_id.setText(null);
        txt_tanggal.setText(null);
        txt_alamat.setText(null);
        txt_jam.setText(null);
        txt_atasnama.setText(null);
    }

    // untuk menampilkan dialog form biodata
    private void DialogForm(String idx, String tanggalx, String alamatx, String jamx, String atasnamax, String acarax, String teamx, String button) {
        dialog = new AlertDialog.Builder(HalamanAdmin.this);
        inflater = getLayoutInflater();
        dialogView = inflater.inflate(R.layout.form_jadwal, null);
        dialog.setView(dialogView);
        dialog.setCancelable(true);
        dialog.setIcon(R.mipmap.icon);
        dialog.setTitle("Form Jadwal Proyek Baru");

        txt_id = dialogView.findViewById(R.id.txt_id);
        txt_tanggal = dialogView.findViewById(R.id.txt_tanggal);
        txt_alamat = dialogView.findViewById(R.id.txt_alamat);
        txt_jam = dialogView.findViewById(R.id.txt_jam);
        txt_atasnama = dialogView.findViewById(R.id.txt_atasnama);
        txt_acara = dialogView.findViewById(R.id.txt_acara);
        txt_team = dialogView.findViewById(R.id.txt_team);

        if (!idx.isEmpty()) {
            txt_id.setText(idx);
            txt_tanggal.setText(tanggalx);
            txt_alamat.setText(alamatx);
            txt_jam.setText(jamx);
            txt_atasnama.setText(atasnamax);
            txt_acara.setText(acarax);
            txt_team.setText(teamx);
        } else {
            kosong();
        }

        dialog.setPositiveButton(button, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                id = txt_id.getText().toString();
                tanggal = txt_tanggal.getText().toString();
                alamat = txt_alamat.getText().toString();
                jam = txt_jam.getText().toString();
                atasnama = txt_atasnama.getText().toString();
                acara = txt_acara.getText().toString();
                team = txt_team.getText().toString();

                simpan_update();
                dialog.dismiss();
            }
        });

        dialog.setNegativeButton("BATAL", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                kosong();
            }
        });

        dialog.show();
    }

    // untuk menampilkan semua data pada listview
    private void callVolley() {
        itemList.clear();
        adapteritemjadwal.notifyDataSetChanged();
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

                        item.setId(obj.getString(TAG_ID));
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
                adapteritemjadwal.setData(itemList);

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

    // fungsi untuk menyimpan atau update
    private void simpan_update() {
        String url;
        // jika id kosong maka simpan, jika id ada nilainya maka update
        if (id.isEmpty()) {
            url = url_insert;
        } else {
            url = url_update;
        }

        StringRequest strReq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Response: " + response);

                try {
                    JSONObject jObj = new JSONObject(response);
                    success = jObj.getInt(TAG_SUCCESS);

                    // Cek error node pada json
                    if (success == 1) {
                        Log.d("Add/update", jObj.toString());

                        callVolley();
                        kosong();

                        Toast.makeText(HalamanAdmin.this, jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
                        adapteritemjadwal.notifyDataSetChanged();

                    } else {
                        Toast.makeText(HalamanAdmin.this, jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Error: " + error.getMessage());
                Toast.makeText(HalamanAdmin.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters ke post url
                Map<String, String> params = new HashMap<String, String>();
                // jika id kosong maka simpan, jika id ada nilainya maka update
                if (id.isEmpty()) {
                    params.put("tanggal", tanggal);
                    params.put("alamat", alamat);
                    params.put("jam", jam);
                    params.put("atasnama", atasnama);
                    params.put("acara", acara);
                    params.put("team", team);
                } else {
                    params.put("id", id);
                    params.put("tanggal", tanggal);
                    params.put("alamat", alamat);
                    params.put("jam", jam);
                    params.put("atasnama", atasnama);
                    params.put("acara", acara);
                    params.put("team", team);
                }

                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(strReq, tag_json_obj);
    }

    // fungsi untuk get edit data
    private void edit(final String idx) {
        StringRequest strReq = new StringRequest(Request.Method.POST, url_edit, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Response: " + response);

                try {
                    JSONObject jObj = new JSONObject(response);
                    success = jObj.getInt(TAG_SUCCESS);

                    // Cek error node pada json
                    if (success == 1) {
                        Log.d("get edit data", jObj.toString());
                        String idx = jObj.getString(TAG_ID);
                        String tanggalx = jObj.getString(TAG_TANGGAL);
                        String alamatx = jObj.getString(TAG_ALAMAT);
                        String jamx = jObj.getString(TAG_JAM);
                        String atasnamax = jObj.getString(TAG_ATASNAMA);
                        String acarax = jObj.getString(TAG_ACARA);
                        String teamx = jObj.getString(TAG_TEAM);

                        DialogForm(idx, tanggalx, alamatx, jamx, atasnamax, acarax, teamx, "UPDATE");

                        adapteritemjadwal.notifyDataSetChanged();

                    } else {
                        Toast.makeText(HalamanAdmin.this, jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Error: " + error.getMessage());
                Toast.makeText(HalamanAdmin.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters ke post url
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", idx);

                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(strReq, tag_json_obj);
    }


    private void delete(final String idx) {
        StringRequest strReq = new StringRequest(Request.Method.POST, url_delete, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Response: " + response);

                try {
                    JSONObject jObj = new JSONObject(response);
                    success = jObj.getInt(TAG_SUCCESS);

                    // Cek error node pada json
                    if (success == 1) {
                        Log.d("delete", jObj.toString());

                        callVolley();

                        Toast.makeText(HalamanAdmin.this, jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();

                        adapteritemjadwal.notifyDataSetChanged();

                    } else {
                        Toast.makeText(HalamanAdmin.this, jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Error: " + error.getMessage());
                Toast.makeText(HalamanAdmin.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters ke post url
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", idx);

                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(strReq, tag_json_obj);
    }

    @Override
    public void onItemClick(int position) {
        final String idx = itemList.get(position).getId();

        final CharSequence[] dialogitem = {"Edit", "Delete"};
        dialog = new AlertDialog.Builder(HalamanAdmin.this);
        dialog.setCancelable(true);
        dialog.setItems(dialogitem, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        edit(idx);
                        break;
                    case 1:
                        delete(idx);
                        break;
                }
            }
        }).show();
    }
}