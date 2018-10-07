package com.example.pohan_pc.nttueat_shop;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Selection extends AppCompatActivity {

    private Button order_manage,menu_manage;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection);
        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    Toast.makeText(Selection.this,"請先登入",Toast.LENGTH_SHORT).show();
                    finish();
                }else{

                }
            }
        };

        order_manage = (Button)findViewById(R.id.order_manage);
        menu_manage = (Button)findViewById(R.id.menu_manage);

        order_manage.setOnClickListener(btnListener);
        menu_manage.setOnClickListener(btnListener);
    }

    private Button.OnClickListener btnListener = new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent i = new Intent();
            switch (v.getId()){
                case R.id.order_manage:
                    i.setClass(Selection.this,OrderManage.class);
                    break;
                case R.id.menu_manage:
                    i.setClass(Selection.this,NavigationActivity.class);
                    break;
            }
            startActivity(i);
        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if((keyCode == KeyEvent.KEYCODE_BACK)){
            AlertDialog.Builder builder = new AlertDialog.Builder(Selection.this);
            builder.setTitle("即將登出")
                    .setMessage("確定要登出此帳號?")
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .setPositiveButton("確定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mAuth.signOut();
                            finish();
                        }
                    });
            builder.show();
        }
        return true;
    }

    @Override
    protected void onStart() {
        mAuth.addAuthStateListener(mAuthListener);
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
}
