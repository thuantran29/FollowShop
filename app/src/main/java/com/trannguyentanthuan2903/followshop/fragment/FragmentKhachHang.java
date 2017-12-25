package com.trannguyentanthuan2903.followshop.fragment;

import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.trannguyentanthuan2903.followshop.Detail_Khach_Hang;
import com.trannguyentanthuan2903.followshop.R;
import com.trannguyentanthuan2903.followshop.ThemKhachHang;
import com.trannguyentanthuan2903.followshop.adapter.AdapterKhachHang;
import com.trannguyentanthuan2903.followshop.dto.KhachHang;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Administrator on 3/15/2017.
 */

public class FragmentKhachHang extends Fragment implements SearchView.OnQueryTextListener,MenuItem.OnActionExpandListener{

    FloatingActionButton floatingActionButton;
    ListView lstKhachHang;
    AdapterKhachHang adapterKhachHang ;
    ArrayList<KhachHang> mangKhachHang;
    String key;
    String idLoaiSP = "";
    private DatabaseReference mDatabase;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_khachhang, container, false);


        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.keepSynced(true);
        SharedPreferences prefs = getActivity().getSharedPreferences("nguoi_dung", MODE_PRIVATE);
        key = prefs.getString("key_nguoi_dung", null);

        // ánh xạ
        lstKhachHang = (ListView) view.findViewById(R.id.listViewKhachHang);
        floatingActionButton = (FloatingActionButton) view.findViewById(R.id.fab2);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ThemKhachHang.class);
                startActivity(intent);
            }
        });

        Intent myIntent = getActivity().getIntent();
        idLoaiSP = myIntent.getStringExtra("IdLoaiSanPham");

        //set adapter
        mangKhachHang = new ArrayList<>();
        adapterKhachHang = new AdapterKhachHang(getActivity(), R.layout.dong_khach_hang, mangKhachHang);
        lstKhachHang.setAdapter(adapterKhachHang);


        lstKhachHang.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(), Detail_Khach_Hang.class);
                intent.putExtra("IdCuaKhachHang", mangKhachHang.get(i).MaKH);
                startActivity(intent);
            }
        });

        lstKhachHang.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(final AdapterView<?> adapterView, View view, final int pos, long l) {

                AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
                builder1.setTitle("Thông báo");
                builder1.setIcon(R.drawable.icon_a);
                builder1.setMessage("Bạn có muốn xóa khách hàng?");
                builder1.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mDatabase.child(key).child("KhachHang").child(mangKhachHang.get(pos).MaKH).removeValue();
                        Toast.makeText(getActivity(), "Đã xóa khách hàng "+ mangKhachHang.get(pos).HoTenKH, Toast.LENGTH_SHORT).show();
                    }
                });
                builder1.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

                builder1.show();
                adapterKhachHang.notifyDataSetChanged();
                return true;
            }
        });

        mDatabase.child(key).child("KhachHang").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                KhachHang kh = dataSnapshot.getValue(KhachHang.class);
                mangKhachHang.add(new KhachHang(dataSnapshot.getKey(),
                        kh.HoTenKH,
                        kh.SDTKH,
                        kh.DiaChi,
                        kh.HinhAnhKH));

                adapterKhachHang.notifyDataSetChanged();
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

        return view;

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getActivity().getMenuInflater().inflate(R.menu.main_menu,menu);
        SearchView searchView = (SearchView) menu.findItem(R.id.menuSearch).getActionView();
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        searchView.setQueryHint("Search...");
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getActivity().getComponentName()));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Toast.makeText(getActivity(), query, Toast.LENGTH_SHORT).show();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapterKhachHang.filter(newText.trim());
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    @Override
    public boolean onMenuItemActionExpand(MenuItem menuItem) {
        return false;
    }

    @Override
    public boolean onMenuItemActionCollapse(MenuItem menuItem) {
        return false;
    }
}
