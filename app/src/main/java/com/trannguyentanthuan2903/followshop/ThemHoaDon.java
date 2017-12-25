package com.trannguyentanthuan2903.followshop;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.trannguyentanthuan2903.followshop.dto.HoaDon;
import com.trannguyentanthuan2903.followshop.dto.KhachHang;
import com.trannguyentanthuan2903.followshop.dto.SanPham;

import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ThemHoaDon extends AppCompatActivity {

    TextView  txtTenSPHD, txtGiaHD, txtNgayHD;
    EditText etSoluong;
    Button btnLapHD;
    ImageView imgHinhHD, imgBack;
    Spinner spTenKH;
    ArrayAdapter adapter;
    ArrayList<String> arrayTenKH;
    ArrayList<String> arrayMaKH;
    String idSanPham = "";
    String idTenKH = "";
    String gia;


    private DatabaseReference mDatabase;
    private FirebaseStorage storage;
    String key;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_them_hoa_don);

        SharedPreferences prefs = getSharedPreferences("nguoi_dung", MODE_PRIVATE);
        key = prefs.getString("key_nguoi_dung", null);

        Toast.makeText(this, ""+ key, Toast.LENGTH_SHORT).show();
        AnhXa();

        arrayTenKH = new ArrayList<>();
        arrayMaKH = new ArrayList<>();
        adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, arrayTenKH);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spTenKH.setAdapter(adapter);

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mDatabase = FirebaseDatabase.getInstance().getReference();
        storage = FirebaseStorage.getInstance();

        final StorageReference storageRef = storage.getReferenceFromUrl("gs://quanlybanhang-fd442.appspot.com");

        //get id cua sanpham và khachhàng
        Intent intentHD = getIntent();
        idSanPham = intentHD.getStringExtra("IdCuaSanPham");

//        Intent intentKH = getIntent();
//        idKhachHang = intentKH.getStringExtra("IdCuaKhachHang");

//        get tensp, giasp
        mDatabase.child(key).child("SanPham").child(idSanPham).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                SanPham sanpham = dataSnapshot.getValue(SanPham.class);
                txtTenSPHD.setText(sanpham.TenSP);
                txtGiaHD.setText(sanpham.GiaSP);
                Picasso.with(ThemHoaDon.this)
                        .load(sanpham.HinhSP)
                        .placeholder(R.drawable.product_icon)
                        .error(R.drawable.error)
                        .into(imgHinhHD);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mDatabase.child(key).child("KhachHang").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                KhachHang khachHang = dataSnapshot.getValue(KhachHang.class);
                arrayMaKH.add(khachHang.MaKH);
                arrayTenKH.add(khachHang.HoTenKH);
                adapter.notifyDataSetChanged();

                spTenKH.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        idTenKH = arrayMaKH.get(i);
//                        Toast.makeText(ThemHoaDon.this, idTenKH, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
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
        //button thêm Hóa đơn
        btnLapHD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                // Create a reference to "image.jpg" : Tạo thư mục LoaiSanPham để chứa hình
                StorageReference mountainsRef = storageRef.child("HoaDon/image" + calendar.getTimeInMillis() + ".png");

                // Get the data from an ImageView as bytes
                BitmapDrawable drawable = (BitmapDrawable) imgHinhHD.getDrawable();
                Bitmap bitmap = drawable.getBitmap();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos); // chuyển thành PNG
                byte[] data = baos.toByteArray();

                UploadTask uploadTask = mountainsRef.putBytes(data); // đẩy dữ liệu
                // check result
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Log.d("AAA", "Lỗi lưu hình Hóa Đơn: " + exception.toString());
//                        Toast.makeText(ThemHoaDon.this, "Lưu hình Hóa Đơn lỗi!", Toast.LENGTH_SHORT).show();
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                        Uri downloadUrl = taskSnapshot.getDownloadUrl();
//                        Toast.makeText(ThemHoaDon.this, "Lưu hình Hóa Đơn thành công", Toast.LENGTH_SHORT).show();
                        // sau khi lưu hình -> lưu database LoạiSP
                        HoaDon hoaDon = new HoaDon(
                                null,
                                txtNgayHD.getText().toString().trim(),
                                spTenKH.getSelectedItem().toString(),
                                txtTenSPHD.getText().toString().trim(),
                                etSoluong.getText().toString().trim(),
                                txtGiaHD.getText().toString().trim(),
//                                String.valueOf(Integer.parseInt(txtGiaHD.getText().toString().trim()) * Integer.parseInt(etSoluong.getText().toString())),
                                String.valueOf(downloadUrl)
                        );
                        mDatabase.child(key).child("HoaDon").push().setValue(hoaDon, new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                if (databaseError == null) {
                                    Toast.makeText(ThemHoaDon.this, "Thêm Hóa Đơn thành công.", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(ThemHoaDon.this, "Lỗi thêm Hóa Đơn!", Toast.LENGTH_SHORT).show();
                                }
                            } // Máy tính bảng Điện thoại Laptop
                        });
                    }
                });
                finish();
            }
        });
        // set ngày lập hóa dơn
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String now = df.format(new Date());
        txtNgayHD.setText( now);


    }

    private void AnhXa() {
        spTenKH = (Spinner) findViewById(R.id.editTextTenKHHD);
        txtTenSPHD = (TextView) findViewById(R.id.textViewSPHd);
        txtGiaHD = (TextView) findViewById(R.id.textViewGiaHd);
        txtNgayHD = (TextView) findViewById(R.id.textViewNgayLapHd);
        btnLapHD = (Button) findViewById(R.id.buttonHoaDon);
        imgHinhHD = (ImageView) findViewById(R.id.imageViewHIinhHD);
        imgBack = (ImageView) findViewById(R.id.previous);
        etSoluong = (EditText) findViewById(R.id.soLuong);
    }
}
