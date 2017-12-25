package com.trannguyentanthuan2903.followshop;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.trannguyentanthuan2903.followshop.dto.HoaDon;

import java.util.ArrayList;

public class Detail_Hoa_Don extends AppCompatActivity {

    TextView txtNgayHd, txtHoTenKHHd, txtSpHd, txtGiahd;
    ImageView imgHinhSPHd, imgBack;
    String idHoaDon = "";
    ArrayList<HoaDon> arrayHoaDon;
    String key;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail__hoa__don);

        SharedPreferences prefs = getSharedPreferences("nguoi_dung", MODE_PRIVATE);
        key = prefs.getString("key_nguoi_dung", null);

        AnhXa();

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mDatabase = FirebaseDatabase.getInstance().getReference();

        Intent intentHoaDon = getIntent();
        idHoaDon = intentHoaDon.getStringExtra("IdCuaHoaDon");

        Log.d("AAA", "Link: " + mDatabase.child("HoaDon").child(idHoaDon));

        mDatabase.child(key).child("HoaDon").child(idHoaDon).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                HoaDon hoaDon = dataSnapshot.getValue(HoaDon.class);
                txtNgayHd.setText(hoaDon.NgayLapHoaDon);
                txtHoTenKHHd.setText(hoaDon.TenKHHoaDon);
                txtSpHd.setText(hoaDon.TenSpHoaDon);
                txtGiahd.setText(Integer.parseInt(hoaDon.GiaSPHoaDon)*Integer.parseInt(hoaDon.Soluong)+"");
                Picasso.with(Detail_Hoa_Don.this).
                        load(hoaDon.LinkHinh).
                        placeholder(R.drawable.product_icon).
                        error(R.drawable.error).
                        into(imgHinhSPHd);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    private void AnhXa() {
        arrayHoaDon = new ArrayList<>();
        txtNgayHd = (TextView) findViewById(R.id.ngayHD);
        txtHoTenKHHd = (TextView) findViewById(R.id.tenKH);
        txtSpHd = (TextView) findViewById(R.id.tenSP);
        txtGiahd = (TextView) findViewById(R.id.giaSPHoaDon);
        imgHinhSPHd = (ImageView) findViewById(R.id.imageViewDetailHD);
        imgBack = (ImageView) findViewById(R.id.previous);
    }
}
