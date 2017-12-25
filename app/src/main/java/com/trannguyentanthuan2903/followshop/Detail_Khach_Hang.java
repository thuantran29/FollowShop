package com.trannguyentanthuan2903.followshop;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.trannguyentanthuan2903.followshop.dto.KhachHang;

public class Detail_Khach_Hang extends AppCompatActivity {

    TextView txtHoTenKh, txtDiaChiKh, txtSdtKh;
    ImageView imgDaiDienKh, imgBack;
    Button btnSua;
    String idKhachHang = null;
    String key;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail__khach__hang);

        SharedPreferences prefs = getSharedPreferences("nguoi_dung", MODE_PRIVATE);
        key = prefs.getString("key_nguoi_dung", null);
        AnhXa();

        mDatabase = FirebaseDatabase.getInstance().getReference();

        Intent intentDetail = getIntent();
        idKhachHang = intentDetail.getStringExtra("IdCuaKhachHang");

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mDatabase.child(key).child("KhachHang").child(idKhachHang).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                KhachHang khachHang = dataSnapshot.getValue(KhachHang.class);

                txtHoTenKh.setText(khachHang.HoTenKH);
                txtDiaChiKh.setText(khachHang.DiaChi);
                txtSdtKh.setText(khachHang.SDTKH);
                Picasso.with(Detail_Khach_Hang.this).
                        load(khachHang.HinhAnhKH).
                        placeholder(R.drawable.product_icon).
                        error(R.drawable.error).
                        into(imgDaiDienKh);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        btnSua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Detail_Khach_Hang.this, SuaThongTinKhActivity.class);
                intent.putExtra("IdCuaKhachHang", idKhachHang);
                startActivity(intent);
            }
        });
    }

    public void AnhXa() {
        imgBack = (ImageView) findViewById(R.id.previous);
        txtHoTenKh = (TextView) findViewById(R.id.suaHoTenKH);
        txtDiaChiKh = (TextView) findViewById(R.id.suaDiaChiKH);
        txtSdtKh = (TextView) findViewById(R.id.suaSDTKH);
        imgDaiDienKh = (ImageView) findViewById(R.id.suaDaiDienKH);
        btnSua = (Button) findViewById(R.id.buttonSuaThongTinKh);
    }
}
