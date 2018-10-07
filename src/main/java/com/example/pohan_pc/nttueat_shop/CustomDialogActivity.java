package com.example.pohan_pc.nttueat_shop;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by POHAN-PC on 2017/12/23.
 */

public class CustomDialogActivity extends Activity{
    Button btn_confirm,btn_cancel;
    TextView text_content,text_close;
    private FirebaseDatabase database;
    private DatabaseReference mRef;
    EditText amount;
    CheckBox drink_big;
    private String status="";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_dialog);

        Intent intent = this.getIntent();
        final String item = intent.getStringExtra("item");
        final String value = intent.getStringExtra("value");
        final String type = intent.getStringExtra("type");
        final String id = intent.getStringExtra("id");

        String resource = "food-data/"+type;

        Log.e("resource",resource);

        database = FirebaseDatabase.getInstance();
        mRef = database.getReference(resource);


        btn_confirm = (Button)findViewById(R.id.btn_confirm);
        btn_cancel = (Button)findViewById(R.id.btn_cancel);
        text_content = (TextView)findViewById(R.id.confirm_foodname);
        text_close = (TextView)findViewById(R.id.text_close);
        amount = (EditText)findViewById(R.id.money);
        drink_big = (CheckBox)findViewById(R.id.sell);

        amount.setText(value);

        text_content.setText("確定要設定 "+item+" 嗎?");

        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(amount.getText().toString())){
                    amount.setError("此欄位不可為空");
                }else{
                    if(Integer.parseInt(amount.getText().toString())>0){
                        if(drink_big.isChecked()){
                            status = "false";
                        }else{
                            status = "true";
                        }
                        mRef.child(id).child("foodValue").setValue(amount.getText().toString());
                        mRef.child(id).child("Status").setValue(status);
                        finish();
                    }
                    else{
                        amount.setError("金額錯誤");
                    }
                }
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        text_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
