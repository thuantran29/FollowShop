package com.trannguyentanthuan2903.followshop.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.trannguyentanthuan2903.followshop.R;
import com.trannguyentanthuan2903.followshop.SanPhamListActivity;
import com.trannguyentanthuan2903.followshop.SuaLoaiSanPhamActivity;
import com.trannguyentanthuan2903.followshop.ThemLoaiSanPham;
import com.trannguyentanthuan2903.followshop.adapter.AdapterLoaiSanPham;
import com.trannguyentanthuan2903.followshop.dto.LoaiSanPham;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Administrator on 3/15/2017.
 */

public class FragmentSanPham extends Fragment {
    FloatingActionButton floatingActionButton;
    ListView lvLoaiSP;
    ArrayList<LoaiSanPham> arrayLoaiSP;
    AdapterLoaiSanPham adapter;
    String key;
    private DatabaseReference mDatabase;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sanpham, container, false);
        SharedPreferences prefs = getActivity().getSharedPreferences("nguoi_dung", MODE_PRIVATE);
        key = prefs.getString("key_nguoi_dung", null);
        //Anh xa
        mDatabase = FirebaseDatabase.getInstance().getReference();
        lvLoaiSP = (ListView) view.findViewById(R.id.listViewLoaiSanPham);
        floatingActionButton = (FloatingActionButton) view.findViewById(R.id.fabLoaiSanPham);
        //Gọi floating toi SanphamList
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ThemLoaiSanPham.class);
                startActivity(intent);
            }
        });
        //set Adapter


        arrayLoaiSP = new ArrayList<>();
        adapter = new AdapterLoaiSanPham(getActivity(), R.layout.dong_loai_san_pham, arrayLoaiSP);
        lvLoaiSP.setAdapter(adapter);
        //Set sự kiện ListView click
        lvLoaiSP.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(), SanPhamListActivity.class);
                intent.putExtra("IdLoaiSanPham", arrayLoaiSP.get(i).IdLoai);
                startActivity(intent);
            }
        });
        lvLoaiSP.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int pos, long l) {

                AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
                builder1.setTitle("Thông báo");
                builder1.setIcon(R.drawable.icon_a);
                builder1.setMessage("Bạn có muốn sửa Loại Sản Phẩm?");
                builder1.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                       Intent idLoaiSpIntent= new Intent(getActivity(),SuaLoaiSanPhamActivity.class);
                        idLoaiSpIntent.putExtra("MaCuaLoaiPham", arrayLoaiSP.get(pos).MaLoai);
                        startActivity(idLoaiSpIntent);
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

        mDatabase.child(key).child("LoaiSP").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                LoaiSanPham loaiSP = dataSnapshot.getValue(LoaiSanPham.class);
                arrayLoaiSP.add(new LoaiSanPham(dataSnapshot.getKey(),
                        loaiSP.IdLoai, loaiSP.TenLoai, loaiSP.HinhLoai));
                adapter.notifyDataSetChanged();
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

}
