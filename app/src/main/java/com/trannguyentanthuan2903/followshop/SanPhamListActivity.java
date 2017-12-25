package com.trannguyentanthuan2903.followshop;

import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.trannguyentanthuan2903.followshop.adapter.AdapterSanPham;
import com.trannguyentanthuan2903.followshop.dto.SanPham;

import java.util.ArrayList;

public class SanPhamListActivity extends AppCompatActivity {
    ListView lvSanPham;
    ArrayList<SanPham> arraySP;
    AdapterSanPham adapter;
    FloatingActionButton buttonAdd;
    Toolbar toolBarSearch;
    String key;
    String idLoaiSP = "";
    private DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_san_pham_list);

        SharedPreferences prefs = getSharedPreferences("nguoi_dung", MODE_PRIVATE);
        key = prefs.getString("key_nguoi_dung", null);

        toolBarSearch = (Toolbar) findViewById(R.id.toolbarSearch);
        setSupportActionBar(toolBarSearch);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        //set Floattingbutton
        buttonAdd=(FloatingActionButton) findViewById(R.id.fabSanPham);
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(SanPhamListActivity.this,ThemSanPham.class);
                startActivity(intent);
            }
        });
// get ID Loại Sản phẩm
        Intent myIntent = SanPhamListActivity.this.getIntent();
        idLoaiSP = myIntent.getStringExtra("IdLoaiSanPham");
//        Toast.makeText(this, "Loại sp "+idLoaiSP, Toast.LENGTH_SHORT).show();
        lvSanPham = (ListView) findViewById(R.id.listViewSanPham);
        arraySP = new ArrayList<>();
        adapter = new AdapterSanPham(this, R.layout.dong_san_pham, arraySP);
        lvSanPham.setAdapter(adapter);

        lvSanPham.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(SanPhamListActivity.this, Detail_San_Pham.class);
                intent.putExtra("IdCuaSanPham", arraySP.get(i).IdSP);
                intent.putExtra("IdLoai",arraySP.get(i).IdLoaiSP);
                startActivity(intent);
            }
        });

        lvSanPham.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int pos, long l) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(SanPhamListActivity.this);
                builder1.setTitle("Thông báo");
                builder1.setIcon(R.drawable.icon_a);
                builder1.setMessage("Bạn có muốn xóa Sản phẩm?");
                builder1.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mDatabase.child(key).child("SanPham").child(arraySP.get(pos).IdSP).removeValue();
                        Toast.makeText(SanPhamListActivity.this, "Đã xóa sản phẩm "+ arraySP.get(pos).TenSP, Toast.LENGTH_SHORT).show();
                    }
                });
                builder1.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

                builder1.show();
                adapter.notifyDataSetChanged();
                return true;
            }
        });

        mDatabase.child(key).child("SanPham").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                SanPham sp = dataSnapshot.getValue(SanPham.class);
                if(idLoaiSP.equals(sp.IdLoaiSP)){
                    arraySP.add(new SanPham(
                            dataSnapshot.getKey(),
                            sp.IdLoaiSP,
                            sp.TenSP,
                            sp.GiaSP,
                            sp.MoTaSP,
                            sp.HinhSP
                    ));
                    adapter.notifyDataSetChanged();
                }


            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        SearchView searchView = (SearchView) menu.findItem(R.id.menuSearch).getActionView();
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView.setQueryHint("Search...");
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Toast.makeText(SanPhamListActivity.this, query, Toast.LENGTH_SHORT).show();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.filter(newText.trim());
                return false;
            }
        });
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) // Press Back Icon
        {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
