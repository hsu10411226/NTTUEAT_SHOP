package com.example.pohan_pc.nttueat_shop;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.ListMenuPresenter;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.qrcode.encoder.QRCode;

import java.util.ArrayList;
import java.util.HashMap;

public class OrderManage extends AppCompatActivity {

    private Button New,Process,Take;
    private ImageView Refresh;
    private ListView OrderList;
    private int press_position = 0;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase database;
    private DatabaseReference mRef;
    private ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String,String>>();
    private SimpleAdapter adapter;
    private String[] status = {"New","Process","Take","Finish"};
    private String[] dialog = {"接下該筆訂單?","訂單已完成?","是否取餐?"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_manage);

        New = (Button)findViewById(R.id.New);
        Process = (Button)findViewById(R.id.Process);
        Take = (Button)findViewById(R.id.Take);
        Refresh = (ImageView)findViewById(R.id.Update);
        OrderList = (ListView)findViewById(R.id.OrderList);
        New.setOnClickListener(btnListener);
        Process.setOnClickListener(btnListener);
        Take.setOnClickListener(btnListener);
        Refresh.setOnClickListener(imgListener);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    Toast.makeText(OrderManage.this,"請先登入",Toast.LENGTH_SHORT).show();
                    finish();
                }else{

                }
            }
        };


        adapter = new SimpleAdapter(this,list,android.R.layout.simple_list_item_2,new String[]{"Id","Status"},new int[]{android.R.id.text1,android.R.id.text2});
        OrderList.setAdapter(adapter);
        OrderList.setOnItemClickListener(ListviewListener);
        getData();

    }

    private ListView.OnItemClickListener ListviewListener = new ListView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
            AlertDialog.Builder builder = new AlertDialog.Builder(OrderManage.this)
                    .setTitle("確認")
                    .setMessage(dialog[press_position])
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .setPositiveButton("確認", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String cid = ((TextView) view.findViewById(android.R.id.text1)).getText().toString();
                            mRef = database.getReference("Order/"+cid);
                            mRef.child("orderStatus").setValue(status[press_position+1]);
                            getData();
                        }
                    });
            builder.show();

        }
    };

    private Button.OnClickListener btnListener = new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.New:
                    press_position = 0;
                    getData();
                    break;
                case R.id.Process:
                    press_position = 1;
                    getData();
                    break;
                case R.id.Take:
                    press_position = 2;
                    getData();
                    break;
            }
        }
    };

    private ImageView.OnClickListener imgListener = new ImageView.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(press_position==2){
                Intent i = new Intent();
                i.setClass(OrderManage.this, QrCode.class);
                startActivity(i);
                getData();
            }else{
                getData();
            }
        }
    };

    private void getData(){
        list.clear();
        mRef = database.getReference("Order/");
        showProgress();
        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    if(ds.child("orderStatus").getValue().toString().equals(status[press_position])){
                        HashMap<String,String> n = new HashMap<String, String>();
                        n.put("Id",(String)ds.child("orderId").getValue());
                        String q = (String)ds.child("orderPerson").getValue()+"\n";
                        for(int i=0;i<ds.child("orderItem").getChildrenCount();i++){
                            food g = ds.child("orderItem/"+String.valueOf(i)).getValue(food.class);
                            q += g.getFoodName() + "  " + g.getFoodValue() + "\n";
                        }
                        n.put("Status",q);
                        list.add(n);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void showProgress(){
        final Dialog dialog = ProgressDialog.show(OrderManage.this,"資料更新中","請稍後...",true);
        new Thread(new Runnable() {
                @Override
                public void run() {
                    try{
                        Thread.sleep(1000);
                    }catch (Exception e){
                        e.printStackTrace();
                    }finally {
                        dialog.dismiss();
                    }
                }
            }).start();
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
