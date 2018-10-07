package com.example.pohan_pc.nttueat_shop;

import android.*;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class QrCode extends AppCompatActivity {

    private IntentIntegrator integrator;
    private FirebaseDatabase database;
    private DatabaseReference mRef;
    private TextView id,person,item,value;
    private Button ok,fail;
    private String idresult;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_code);

        if(ContextCompat.checkSelfPermission(QrCode.this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(QrCode.this,new String[]{android.Manifest.permission.CAMERA},0);
        }
        database = FirebaseDatabase.getInstance();

        id = (TextView)findViewById(R.id.text_OrderID);
        person = (TextView)findViewById(R.id.text_OrderPerson);
        item = (TextView)findViewById(R.id.text_OrderItem);
        value = (TextView)findViewById(R.id.text_OrderTotal);
        ok = (Button)findViewById(R.id.takeok);
        fail = (Button)findViewById(R.id.fail);
        ok.setOnClickListener(btnListener);
        fail.setOnClickListener(btnListener);
        integrator = new IntentIntegrator(QrCode.this);
        initiateScanning();
    }

    private Button.OnClickListener btnListener = new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.takeok:
                    mRef = database.getReference("Order/"+idresult);
                    mRef.child("orderStatus").setValue("Finish");
                    finish();
                    break;
                case R.id.fail:
                    finish();
            }
        }
    };

    private void initiateScanning(){
        integrator.setCaptureActivity(CaptureActivityAnyOrientation.class);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setPrompt("請掃描");
        integrator.setCameraId(0);  // Use a specific camera of the device
        integrator.setOrientationLocked(false);
        integrator.setBeepEnabled(false);
        integrator.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        String a="";
        if (scanResult != null) {
            if(data!=null){
                a = data.getStringExtra("SCAN_RESULT");
                mRef = database.getReference("Order/"+a);
                mRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.child("orderStatus").getValue().toString().equals("Take")){
                            idresult = dataSnapshot.child("orderId").getValue().toString();
                            id.setText("訂單編號 : "+dataSnapshot.child("orderId").getValue().toString());
                            person.setText("訂購人 : "+dataSnapshot.child("orderPerson").getValue().toString());
                            String q = "";
                            for(int i=0;i<dataSnapshot.child("orderItem").getChildrenCount();i++){
                                food g = dataSnapshot.child("orderItem/"+String.valueOf(i)).getValue(food.class);
                                q += g.getFoodName() + "  " + g.getFoodValue() + "\n";
                            }
                            item.setText("訂購物品 : " + q);
                            value.setText("總金額 : " + dataSnapshot.child("orderTotal").getValue().toString());
                            String c = dataSnapshot.child("orderId").getValue().toString() + "\n";
                            c += dataSnapshot.child("orderPerson").getValue().toString()+ "\n";
                            c += dataSnapshot.child("orderPhone").getValue().toString()+ "\n";
                            c += dataSnapshot.child("orderStatus").getValue().toString()+ "\n";
                            c += dataSnapshot.child("orderTime").getValue().toString()+ "\n";
                            c += dataSnapshot.child("orderTotal").getValue().toString()+ "\n";
                            c += dataSnapshot.child("orderItem").getValue().toString();
                            Toast.makeText(QrCode.this,c,Toast.LENGTH_LONG).show();
                        }
                        else{
                            Toast.makeText(QrCode.this,"非有效訂單",Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        finish();
                    }
                });
            }
            else{
                finish();
            }
        }
        else{
            finish();
        }
        // else continue with any other code you need in the method
        super.onActivityResult(requestCode, resultCode, data);
    }
}
