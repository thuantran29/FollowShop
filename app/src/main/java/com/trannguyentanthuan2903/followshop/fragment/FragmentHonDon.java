package com.trannguyentanthuan2903.followshop.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
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
import com.trannguyentanthuan2903.followshop.Detail_Hoa_Don;
import com.trannguyentanthuan2903.followshop.R;
import com.trannguyentanthuan2903.followshop.adapter.AdapterHoaDon;
import com.trannguyentanthuan2903.followshop.dto.HoaDon;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Administrator on 3/15/2017.
 */

public class FragmentHonDon extends Fragment {

    ListView lstHoaDon;
    ArrayList<HoaDon> mangHoaDon;
    AdapterHoaDon adapterHoaDon;

    String key;
    private DatabaseReference mDatabase;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_hoadon, container, false);

        SharedPreferences prefs = getActivity().getSharedPreferences("nguoi_dung", MODE_PRIVATE);
        key = prefs.getString("key_nguoi_dung", null);

        //Anh Xa
        mDatabase = FirebaseDatabase.getInstance().getReference();
        lstHoaDon = (ListView) view.findViewById(R.id.listViewHoaDon);

        // set adapter
        mangHoaDon = new ArrayList<>();
        adapterHoaDon = new AdapterHoaDon(getActivity(), R.layout.dong_hoa_don, mangHoaDon);
        lstHoaDon.setAdapter(adapterHoaDon);

        lstHoaDon.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(), Detail_Hoa_Don.class);
                intent.putExtra("IdCuaHoaDon", mangHoaDon.get(i).IdHoaDon);
                startActivity(intent);
            }
        });

        lstHoaDon.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int pos, long l) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
                builder1.setTitle("Thông báo");
                builder1.setIcon(R.drawable.icon_a);
                builder1.setMessage("Bạn có muốn thanh toán hóa đơn?");
                builder1.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mDatabase.child(key).child("HoaDon").child(mangHoaDon.get(pos).IdHoaDon).removeValue();
                        Toast.makeText(getActivity(), "Bạn đã thanh toán", Toast.LENGTH_SHORT).show();
                    }
                });
                builder1.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

                builder1.show();
                adapterHoaDon.notifyDataSetChanged();
                return true;
            }
        });

        mDatabase.child(key).child("HoaDon").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                HoaDon hoaDon = dataSnapshot.getValue(HoaDon.class);
                mangHoaDon.add(new HoaDon(dataSnapshot.getKey(),
                        hoaDon.NgayLapHoaDon,
                        hoaDon.TenKHHoaDon,
                        hoaDon.TenSpHoaDon,
                        hoaDon.Soluong,
                        hoaDon.GiaSPHoaDon,
                        hoaDon.LinkHinh));
                adapterHoaDon.notifyDataSetChanged();
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
