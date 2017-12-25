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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.trannguyentanthuan2903.followshop.dto.LoaiSanPham;
import com.trannguyentanthuan2903.followshop.dto.SanPham;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;

public class ThemSanPham extends AppCompatActivity {
    Spinner spIdLoai;
    EditText edtTenSP, edtGiaSP, edtMoTa;
    ImageView imgHinh, imgBack;
    Button btnThemSP;

    int REQUEST_CODE_IMAGE = 456;
    ArrayList<String> arrayIdLoai;
    ArrayList<String> arrayTenLoai;
    ArrayAdapter adapter;
    String idLoaiSP = "";

    final int REQUEST_CODE_CAMERA = 123;
    final int REQUEST_CODE_FOLDER = 456;

    private DatabaseReference mDatabase;
    private FirebaseStorage storage;
    String key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_them_san_pham);
        SharedPreferences prefs = getSharedPreferences("nguoi_dung", MODE_PRIVATE);
        key = prefs.getString("key_nguoi_dung", null);

        AnhXa();

        arrayIdLoai = new ArrayList<>();
        arrayTenLoai = new ArrayList<>();

        adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, arrayTenLoai);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spIdLoai.setAdapter(adapter);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        storage = FirebaseStorage.getInstance();
        final StorageReference storageRef = storage.getReferenceFromUrl("gs://quanlybanhang-fd442.appspot.com");
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        //Them san phẩm
        btnThemSP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                // Create a reference to "image.jpg" : Tạo thư mục SanPham để chứa hình
                StorageReference mountainsRef = storageRef.child("SanPham/image" + calendar.getTimeInMillis() + ".png");

                // Get the data from an ImageView as bytes
                BitmapDrawable drawable = (BitmapDrawable) imgHinh.getDrawable();
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
                        Toast.makeText(ThemSanPham.this, "Lưu hình sản phẩm lỗi!", Toast.LENGTH_SHORT).show();
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                        Uri downloadUrl = taskSnapshot.getDownloadUrl();
                        Toast.makeText(ThemSanPham.this, "Lưu hình SP thành công", Toast.LENGTH_SHORT).show();
                        // sau khi lưu hình -> lưu database LoạiSP
                        SanPham sanPham = new SanPham(
                                null,
                                idLoaiSP,
                                edtTenSP.getText().toString().trim(),
                                edtGiaSP.getText().toString().trim(),
                                edtMoTa.getText().toString().trim(),
                                String.valueOf(downloadUrl)

                        );
                        mDatabase.child(key).child("SanPham").push().setValue(sanPham, new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                if (databaseError == null) {
                                    Toast.makeText(ThemSanPham.this, "Thêm SP thành công.", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(ThemSanPham.this, "Lỗi thêm SP!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                });
            }
        });

        // lấy dữ liệu ID loại Sản phẩm đổ spinner
        mDatabase.child(key).child("LoaiSP").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                LoaiSanPham loaiSP = dataSnapshot.getValue(LoaiSanPham.class);
                arrayIdLoai.add(loaiSP.IdLoai);
                arrayTenLoai.add(loaiSP.TenLoai);
                adapter.notifyDataSetChanged();

                spIdLoai.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        idLoaiSP = arrayIdLoai.get(i);
//                        Toast.makeText(ThemSanPham.this, idLoaiSP, Toast.LENGTH_SHORT).show();
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
        imgHinh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                xetDialog();
            }
        });
        xoaAvatar();
        sendIdLoaiSP();
    }

    private void sendIdLoaiSP() {
        SharedPreferences.Editor editgv = getSharedPreferences("idLoaiSp", MODE_PRIVATE).edit();
        editgv.putString("key_giaovien", idLoaiSP);
        editgv.commit();
    }

    private void xoaAvatar() {
        imgHinh.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ThemSanPham.this);
                builder.setTitle("Thông báo!");
                builder.setMessage("Bạn có muốn xóa Avatar");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        imgHinh.setImageResource(R.drawable.product_icon);
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
        AlertDialog.Builder builder1 = new AlertDialog.Builder(ThemSanPham.this);
        builder1.setTitle("Thông báo");
        builder1.setIcon(R.drawable.icon_a);
        builder1.setMessage("Chọn hình ảnh từ Photo hoặc Camera");
        builder1.setPositiveButton("Camera", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ActivityCompat.requestPermissions(ThemSanPham.this,
                        new String[]{Manifest.permission.CAMERA}, REQUEST_CODE_CAMERA);
            }
        });
        builder1.setNegativeButton("Folder", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ActivityCompat.requestPermissions(ThemSanPham.this,
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
                    Toast.makeText(ThemSanPham.this, "Bạn không có quyền truy cập máy ảnh", Toast.LENGTH_SHORT).show();
                }
                return;
            }
            case REQUEST_CODE_FOLDER: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType("image/*");
                    startActivityForResult(intent, REQUEST_CODE_FOLDER);
                } else {
                    Toast.makeText(ThemSanPham.this, "Bạn không có quyền truy cập thư viện ảnh", Toast.LENGTH_SHORT).show();
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
                imgHinh.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            //Nhớ thêm 2 quyền bên AndroidMainfrst
//            <uses-permission android:name="android.permission.CAMERA"/>
//            <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE"/>
        }

        if (requestCode == REQUEST_CODE_CAMERA && resultCode == RESULT_OK && data != null) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");//"data" ->default
            imgHinh.setImageBitmap(photo);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void AnhXa() {
        btnThemSP = (Button) findViewById(R.id.buttonThemSPNhap);
        edtGiaSP = (EditText) findViewById(R.id.editTextGiaSPNhap);
        edtMoTa = (EditText) findViewById(R.id.editTextMoTaSPNhap);
        edtTenSP = (EditText) findViewById(R.id.editTextTenSPNhap);
        spIdLoai = (Spinner) findViewById(R.id.spinnerIdLoaiSP);
        imgHinh = (ImageView) findViewById(R.id.imageViewHinhSPNhap);
        imgBack = (ImageView) findViewById(R.id.previous);
    }
}
