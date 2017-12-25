package com.trannguyentanthuan2903.followshop;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.trannguyentanthuan2903.followshop.dto.KhachHang;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Calendar;

public class SuaThongTinKhActivity extends AppCompatActivity {
    EditText etHoTen, etDiachi, etSdt;
    ImageView imgDaiDien, imgBack;
    Button btLuu;
    String idKhachHang = null;
    final int REQUEST_CODE_CAMERA = 123;
    final int REQUEST_CODE_FOLDER = 456;
    private DatabaseReference mDatabase;
    private FirebaseStorage storage;
    String key;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sua_thong_tin_kh);

        SharedPreferences prefs = getSharedPreferences("nguoi_dung", MODE_PRIVATE);
        key = prefs.getString("key_nguoi_dung", null);
        anhXa();

        mDatabase = FirebaseDatabase.getInstance().getReference();
        storage = FirebaseStorage.getInstance();
        final StorageReference storageRef = storage.getReferenceFromUrl("gs://quanlybanhang-fd442.appspot.com");

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
                etHoTen.setText(khachHang.HoTenKH);
                etDiachi.setText(khachHang.DiaChi);
                etSdt.setText(khachHang.SDTKH);
                Picasso.with(SuaThongTinKhActivity.this).
                        load(khachHang.HinhAnhKH).
                        placeholder(R.drawable.product_icon).
                        error(R.drawable.error).
                        into(imgDaiDien);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        btLuu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                // Create a reference to "image.jpg" : Tạo thư mục LoaiSanPham để chứa hình
                StorageReference mountainsRef = storageRef.child("KhachHang/image" + calendar.getTimeInMillis() + ".png");

                // Get the data from an ImageView as bytes
                BitmapDrawable drawable = (BitmapDrawable) imgDaiDien.getDrawable();
                Bitmap bitmap = drawable.getBitmap();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos); // chuyển thành PNG
                byte[] data = baos.toByteArray();

                UploadTask uploadTask = mountainsRef.putBytes(data); // đẩy dữ liệu
                // check result
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Log.d("AAA", "Lỗi lưu hình loại " + exception.toString());
//                        Toast.makeText(ThemKhachHang.this, "Lưu hình loại Khách Hàng lỗi!", Toast.LENGTH_SHORT).show();
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                        Uri downloadUrl = taskSnapshot.getDownloadUrl();
//                        Toast.makeText(ThemKhachHang.this, "Lưu hình Khách Hàng thành công", Toast.LENGTH_SHORT).show();
                        // sau khi lưu hình -> lưu database LoạiSP
                        KhachHang loaiSanPham = new KhachHang(
                                null,
                                etHoTen.getText().toString().trim(),
                                etDiachi.getText().toString().trim(),
                                etSdt.getText().toString().trim(),
                                String.valueOf(downloadUrl)
                        );
                        mDatabase.child(key).child("KhachHang").child(idKhachHang).setValue(loaiSanPham, new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                if (databaseError == null) {
                                    Toast.makeText(SuaThongTinKhActivity.this, "Sửa thông tin thành công.", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(SuaThongTinKhActivity.this, "Lỗi thêm sửa thông tin!", Toast.LENGTH_SHORT).show();
                                }
                            } // Máy tính bảng Điện thoại Laptop
                        });
                    }
                });

                finish();
            }
        });
        imgDaiDien.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                xetDialog();
            }
        });
        xoaAvatar();
    }

    private void xoaAvatar() {
        imgDaiDien.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SuaThongTinKhActivity.this);
                builder.setTitle("Thông báo!");
                builder.setMessage("Bạn có muốn xóa Avatar");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        imgDaiDien.setImageResource(R.drawable.product_icon);
                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                });
                builder.show();
                return false;
            }
        });
    }
    private void xetDialog() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(SuaThongTinKhActivity.this);
        builder1.setTitle("Thông báo");
        builder1.setIcon(R.drawable.icon_a);
        builder1.setMessage("Chọn hình ảnh từ Photo hoặc Camera");
        builder1.setPositiveButton("Camera", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ActivityCompat.requestPermissions(SuaThongTinKhActivity.this,
                        new String[]{Manifest.permission.CAMERA}, REQUEST_CODE_CAMERA);
            }
        });
        builder1.setNegativeButton("Folder", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ActivityCompat.requestPermissions(SuaThongTinKhActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_FOLDER);
            }
        });

        builder1.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case REQUEST_CODE_CAMERA: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, REQUEST_CODE_CAMERA);
                } else {
                    Toast.makeText(SuaThongTinKhActivity.this, "Bạn không có quyền truy cập máy ảnh", Toast.LENGTH_SHORT).show();
                }
                return;
            }
            case REQUEST_CODE_FOLDER: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType("image/*");
                    startActivityForResult(intent, REQUEST_CODE_FOLDER);
                } else {
                    Toast.makeText(SuaThongTinKhActivity.this, "Bạn không có quyền truy cập thư viện ảnh", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_CODE_FOLDER && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(uri);
                BitmapFactory.Options options=new BitmapFactory.Options();
                options.inSampleSize = 8;
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream,null,options);
                imgDaiDien.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            //Nhớ thêm 2 quyền bên AndroidMainfrst
//            <uses-permission android:name="android.permission.CAMERA"/>
//            <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE"/>
        }

        if (requestCode == REQUEST_CODE_CAMERA && resultCode == RESULT_OK && data != null) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");//"data" ->default
            imgDaiDien.setImageBitmap(photo);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void anhXa() {
        etHoTen = (EditText) findViewById(R.id.suaHoTenKH);
        etDiachi = (EditText) findViewById(R.id.suaDiaChiKH);
        etSdt = (EditText) findViewById(R.id.suaSDTKH);
        btLuu = (Button) findViewById(R.id.buttonLuuThongTinKh);
        imgDaiDien = (ImageView) findViewById(R.id.suaDaiDienKH);
        imgBack = (ImageView) findViewById(R.id.previous);
    }
}
