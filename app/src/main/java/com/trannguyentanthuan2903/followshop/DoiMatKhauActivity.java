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

public class DoiMatKhauActivity extends AppCompatActivity {
    EditText etNewPass;
    Button btNewPass;
    FirebaseAuth mAuth;
    FirebaseUser user;
    ImageView imgBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doi_mat_khau);
        mAuth= FirebaseAuth.getInstance();
        anhXa();
        if (mAuth.getCurrentUser()!=null){
            btNewPass.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    updateNewPass();
                }
            });
        }

    }

    private void updateNewPass() {
        user=mAuth.getCurrentUser();
        user.updatePassword(etNewPass.getText().toString().trim()).addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(DoiMatKhauActivity.this, "Đã đổi mật khẩu!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(DoiMatKhauActivity.this,LoginActivity.class));
                }
            }
        });
    }

    private void anhXa() {
        etNewPass=(EditText) findViewById(R.id.editTextNewPass);
        btNewPass= (Button) findViewById(R.id.buttonNewPass);
        imgBack =(ImageView) findViewById(R.id.previous);
    }
}