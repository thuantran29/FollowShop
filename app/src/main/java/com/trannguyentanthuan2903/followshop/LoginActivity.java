package com.trannguyentanthuan2903.followshop;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {
    TextView txtForgot, txtRegister;
    EditText edtUser, edtPass;
    Button btnLogin;
    FirebaseAuth mAuth;
    DatabaseReference mData;
    String key_nguoi_dung;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        anhXa();
        mData = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });

        txtRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentRegis = new Intent(LoginActivity.this, DangKyActivity.class);
                startActivity(intentRegis);
            }
        });
        txtForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentForgot = new Intent(LoginActivity.this, QuenMatKhauActivity.class);
                startActivity(intentForgot);
            }
        });

    }


    private void signIn() {
        String email = edtUser.getText().toString().trim();
        String password = edtPass.getText().toString().trim();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Intent intentLogin = new Intent(LoginActivity.this, MainActivity.class);
                            intentLogin.putExtra("key_nguoi_dung",key_nguoi_dung);
                            startActivity(intentLogin);
                        } else {
                            edtUser.setText("");
                            edtPass.setText("");
                            Toast.makeText(LoginActivity.this, "Bạn cần nhập đúng thông tin", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void anhXa() {
        txtForgot = (TextView) findViewById(R.id.textViewForgot);
        txtRegister = (TextView) findViewById(R.id.textViewRegis);
        edtPass = (EditText) findViewById(R.id.editTextPass);
        edtUser = (EditText) findViewById(R.id.editTextUser);
        btnLogin = (Button) findViewById(R.id.buttonLogin);

    }
}