package com.trannguyentanthuan2903.followshop;

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
import com.trannguyentanthuan2903.followshop.dto.SanPham;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Calendar;

public class SuaSanPhamActivity extends AppCompatActivity {
    EditText etTenSP, etGiaSp, etChiTietSp;
    ImageView imgBack, imgDaiDienSp;
    Button btLuu;
    private DatabaseReference mDatabase;
    private FirebaseStorage storage;
    final int REQUEST_CODE_CAMERA = 123;
    final int REQUEST_CODE_FOLDER = 456;
    String idSanPham = "";
    String idLoaiSP;
    String key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sua_san_pham);
        SharedPreferences pres = getSharedPreferences("nguoi_dung", MODE_PRIVATE);
        key = pres.getString("key_nguoi_dung", null);

        anhXa();


        Intent myIntent = SuaSanPhamActivity.this.getIntent();
        idSanPham = myIntent.getStringExtra("IdCuaSanPham");
        idLoaiSP=myIntent.getStringExtra("IdLoai");

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mDatabase = FirebaseDatabase.getInstance().getReference();
        storage = FirebaseStorage.getInstance();
        final StorageReference storageRef = storage.getReferenceFromUrl("gs://quanlybanhang-fd442.appspot.com");

        mDatabase.child(key).child("SanPham").child(idSanPham).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                SanPham sanpham = dataSnapshot.getValue(SanPham.class);

                etTenSP.setText(sanpham.TenSP);
                etGiaSp.setText(sanpham.GiaSP);
                etChiTietSp.setText(sanpham.MoTaSP);
                Picasso.with(SuaSanPhamActivity.this)
                        .load(sanpham.HinhSP)
                        .placeholder(R.drawable.product_icon)
                        .error(R.drawable.error)
                        .into(imgDaiDienSp);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        btLuu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                // Create a reference to "image.jpg" : Tạo thư mục SanPham để chứa hình
                StorageReference mountainsRef = storageRef.child("SanPham/image" + calendar.getTimeInMillis() + ".png");

                // Get the data from an ImageView as bytes
                BitmapDrawable drawable = (BitmapDrawable) imgDaiDienSp.getDrawable();
                Bitmap bitmap = drawable.getBitmap();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos); // chuyển thành PNG
                byte[] data = baos.toByteArray();

                UploadTask uploadTask = mountainsRef.putBytes(data); // đẩy dữ liệu
                // check result
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Log.d("AAA", "Lỗi lưu hình SP: " + exception.toString());
                        Toast.makeText(SuaSanPhamActivity.this, "Lưu hình sản phẩm lỗi!", Toast.LENGTH_SHORT).show();
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                        Uri downloadUrl = taskSnapshot.getDownloadUrl();
                        Toast.makeText(SuaSanPhamActivity.this, "Lưu hình SP thành công", Toast.LENGTH_SHORT).show();
                        // sau khi lưu hình -> lưu database LoạiSP
                        SanPham sanPham = new SanPham(
                                null,
                                idLoaiSP,
                                etTenSP.getText().toString().trim(),
                                etGiaSp.getText().toString().trim(),
                                etChiTietSp.getText().toString().trim(),
                                String.valueOf(downloadUrl)

                        );
                        mDatabase.child(key).child("SanPham").child(idSanPham).setValue(sanPham, new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                if (databaseError == null) {
                                    Toast.makeText(SuaSanPhamActivity.this, "Sửa SP thành công.", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(SuaSanPhamActivity.this, "Lỗi sửa SP!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                });
            }
        });

        imgDaiDienSp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                xetDialog();
            }
        });
    }

    private void xetDialog() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(SuaSanPhamActivity.this);
        builder1.setTitle("Thông báo");
        builder1.setIcon(R.drawable.icon_a);
        builder1.setMessage("Chọn hình ảnh từ Photo hoặc Camera");
        builder1.setPositiveButton("Camera", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ActivityCompat.requestPermissions(SuaSanPhamActivity.this,
                        new String[]{android.Manifest.permission.CAMERA}, REQUEST_CODE_CAMERA);
            }
        });
        builder1.setNegativeButton("Folder", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ActivityCompat.requestPermissions(SuaSanPhamActivity.this,
                        new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_FOLDER);
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
                    Toast.makeText(SuaSanPhamActivity.this, "Bạn không có quyền truy cập máy ảnh", Toast.LENGTH_SHORT).show();
                }
                return;
            }
            case REQUEST_CODE_FOLDER: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType("image/*");
                    startActivityForResult(intent, REQUEST_CODE_FOLDER);
                } else {
                    Toast.makeText(SuaSanPhamActivity.this, "Bạn không có quyền truy cập thư viện ảnh", Toast.LENGTH_SHORT).show();
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
                //resize image before load
                BitmapFactory.Options options=new BitmapFactory.Options();
                options.inSampleSize = 8;
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream,null,options);
                imgDaiDienSp.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            //Nhớ thêm 2 quyền bên AndroidMainfrst
//            <uses-permission android:name="android.permission.CAMERA"/>
//            <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE"/>
        }

        if (requestCode == REQUEST_CODE_CAMERA && resultCode == RESULT_OK && data != null) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");//"data" ->default
            imgDaiDienSp.setImageBitmap(photo);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void anhXa() {
        etTenSP = (EditText) findViewById(R.id.suaTenSp);
        etGiaSp = (EditText) findViewById(R.id.suaGiaSp);
        etChiTietSp = (EditText) findViewById(R.id.suaChiTietSp);
        imgBack = (ImageView) findViewById(R.id.previous);
        imgDaiDienSp = (ImageView) findViewById(R.id.suaHinhSP);
        btLuu = (Button) findViewById(R.id.buttonSuaSP);
    }
}
