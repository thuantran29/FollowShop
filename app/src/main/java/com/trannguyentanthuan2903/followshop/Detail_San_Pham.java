package com.trannguyentanthuan2903.followshop;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
import com.trannguyentanthuan2903.followshop.dto.SanPham;

import java.util.ArrayList;

public class Detail_San_Pham extends AppCompatActivity {

    ImageView imgHinh, imgBack;
    TextView txtTen, txtGia, txtMoTa;
    Button btnMua, btSuaSp;
    ArrayList<SanPham> arraySanPham;

    String idSanPham = "";
    String idLoai = "";

    private DatabaseReference mDatabase;
    String key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail__san__pham);

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

        Intent intentDetail = getIntent();
        idSanPham = intentDetail.getStringExtra("IdCuaSanPham");
        idLoai = intentDetail.getStringExtra("IdLoai");
        Log.d("AAA", "Link: " + mDatabase.child(key).child("SanPham").child(idSanPham));

        mDatabase.child(key).child("SanPham").child(idSanPham).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                SanPham sanpham = dataSnapshot.getValue(SanPham.class);
                txtTen.setText(sanpham.TenSP);
                txtGia.setText(sanpham.GiaSP);
                txtMoTa.setText(sanpham.MoTaSP);
                Picasso.with(Detail_San_Pham.this)
                        .load(sanpham.HinhSP)
                        .placeholder(R.drawable.product_icon)
                        .error(R.drawable.error)
                        .into(imgHinh);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        btnMua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Detail_San_Pham.this, ThemHoaDon.class);
                intent.putExtra("IdCuaSanPham", idSanPham);
                startActivity(intent);
            }
        });
        btSuaSp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Detail_San_Pham.this, SuaSanPhamActivity.class);
                intent.putExtra("IdCuaSanPham", idSanPham);
                intent.putExtra("IdLoai", idLoai);
                startActivity(intent);
            }
        });

    }

    private void AnhXa() {
        btnMua = (Button) findViewById(R.id.buttonLuuSuaSP);
        btSuaSp = (Button) findViewById(R.id.buttonSuaSP);
        txtGia = (TextView) findViewById(R.id.suaGiaSp);
        txtMoTa = (TextView) findViewById(R.id.suaChiTietSP);
        txtTen = (TextView) findViewById(R.id.suaTenSp);
        imgHinh = (ImageView) findViewById(R.id.suaHinhSP);
        imgBack = (ImageView) findViewById(R.id.previous);
        arraySanPham = new ArrayList<>();
    }
}
