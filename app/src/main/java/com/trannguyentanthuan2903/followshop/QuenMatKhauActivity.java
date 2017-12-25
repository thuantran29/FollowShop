package com.trannguyentanthuan2903.followshop;

import android.content.Intent;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class QuenMatKhauActivity extends AppCompatActivity {
    EditText etEmail;
    Button btSend;
    FirebaseUser user;
    FirebaseAuth auth;
    ImageView imgBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quen_mat_khau);
        user = FirebaseAuth.getInstance().getCurrentUser();
        auth = FirebaseAuth.getInstance();
        anhXa();
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        btSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetPassword(etEmail.getText().toString().trim());
            }
        });
    }

    private void resetPassword(String mail) {
        auth.sendPasswordResetEmail(mail)
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            startActivity(new Intent(QuenMatKhauActivity.this,LoginActivity.class));
                            Toast.makeText(QuenMatKhauActivity.this, "Đã gửi mail cho bạn", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(QuenMatKhauActivity.this, "Thất bại xin vui lòng gửi lại", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void anhXa() {
        imgBack= (ImageView) findViewById(R.id.previous);
        etEmail = (EditText) findViewById(R.id.editTextEmailForgot);
        btSend = (Button) findViewById(R.id.buttonSendForgot);
    }
}

