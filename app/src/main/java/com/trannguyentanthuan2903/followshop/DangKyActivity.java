package com.trannguyentanthuan2903.followshop;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class DangKyActivity extends AppCompatActivity {
    EditText etPass, etEmail;
    Button btSubmit;
    ImageView imgBack;
    private DatabaseReference mDatabase;
    private FirebaseStorage storage;
    FirebaseUser mUser;
    FirebaseAuth mAuth;
    String key_giaovien, email, password;
    String MY_PREFS_EMAIL = "email";
    String MY_KEY_PASS = "password";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_ky);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        storage = FirebaseStorage.getInstance();
        final StorageReference storageRef = storage.getReferenceFromUrl("gs://quanlybanhang-fd442.appspot.com");
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        key_giaovien = mUser.getUid();
        anhXa();
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        btSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register();
                sendData();
            }
        });
    }

    private void sendData() {
        SharedPreferences.Editor editgv = getSharedPreferences(MY_PREFS_EMAIL, MODE_PRIVATE).edit();
        editgv.putString("email", email);
        editgv.commit();
        SharedPreferences.Editor edigv = getSharedPreferences(MY_KEY_PASS, MODE_PRIVATE).edit();
        edigv.putString("pass", password);
        edigv.commit();
    }

    private void register() {
        email = etEmail.getText().toString().trim();
        password = etPass.getText().toString().trim();
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(DangKyActivity.this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(DangKyActivity.this, "Bạn cần đăng ký đúng Email và mật khẩu dài hơn 8 ký tự", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }


    private void anhXa() {
        imgBack = (ImageView) findViewById(R.id.previous);
        etPass = (EditText) findViewById(R.id.editTextPass);
        etEmail = (EditText) findViewById(R.id.editTextEmail);
        btSubmit = (Button) findViewById(R.id.buttonSubmit);
    }
}

