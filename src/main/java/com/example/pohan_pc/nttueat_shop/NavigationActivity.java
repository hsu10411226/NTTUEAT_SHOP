package com.example.pohan_pc.nttueat_shop;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class NavigationActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase database;
    private DatabaseReference mRef;
    private ArrayList<food> list = new ArrayList<food>();
    private ListView food_listview;
    private Myadapter adapter;
    private final String[] tabTitles = {"Hamburger","Toast","乳酪餅","抓餅","抹醬吐司(厚片)","抹醬吐司(薄片)","燒餅","蛋餅","鐵板麵加蛋","飲料","點心"};
    private int position = 0;
    private TextView usermail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View HeaderLayout = navigationView.getHeaderView(0);
        usermail = (TextView)HeaderLayout.findViewById(R.id.useremail);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    Toast.makeText(NavigationActivity.this,"請先登入",Toast.LENGTH_SHORT).show();
                    finish();
                }else{
                    usermail.setText(user.getEmail().toString());
                }
            }
        };
        food_listview = (ListView)findViewById(R.id.show_item);
        adapter = new Myadapter(NavigationActivity.this,list);
        food_listview.setAdapter(adapter);
        food_listview.setOnItemClickListener(ListListener);
        new Thread(new Runnable() {
            public void run() {
                getDataFromFirebase(tabTitles[position]);
            }
        }).start();
    }

    ListView.OnItemClickListener ListListener = new ListView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int i_position, long id) {
            String itid = ((TextView) view.findViewById(R.id.itemid)).getText().toString();
            String value = ((TextView) view.findViewById(R.id.food_value)).getText().toString();
            String item = ((TextView) view.findViewById(R.id.food_title)).getText().toString();
            Intent intent = new Intent(NavigationActivity.this,CustomDialogActivity.class);
            Log.e("test",itid);
            intent.putExtra("id",itid);
            intent.putExtra("value",value);
            intent.putExtra("item",item);
            intent.putExtra("type",tabTitles[position]);
            startActivity(intent);
        }
    };

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            AlertDialog.Builder builder = new AlertDialog.Builder(NavigationActivity.this);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.call_hamburger) {
            position=0;
            getDataFromFirebase(tabTitles[position]);
        } else if (id == R.id.call_toast) {
            position=1;
            getDataFromFirebase(tabTitles[position]);
        } else if (id == R.id.call_cheesecake) {
            position=2;
            getDataFromFirebase(tabTitles[position]);
        } else if (id == R.id.call_catchcake) {
            position=3;
            getDataFromFirebase(tabTitles[position]);
        } else if (id == R.id.call_toastthick) {
            position=4;
            getDataFromFirebase(tabTitles[position]);
        } else if (id == R.id.call_toastthin) {
            position=5;
            getDataFromFirebase(tabTitles[position]);
        } else if (id == R.id.call_firecake) {
            position=6;
            getDataFromFirebase(tabTitles[position]);
        } else if (id == R.id.call_eggcake) {
            position=7;
            getDataFromFirebase(tabTitles[position]);
        } else if (id == R.id.call_noodleegg) {
            position=8;
            getDataFromFirebase(tabTitles[position]);
        } else if (id == R.id.call_drink) {
            position=9;
            getDataFromFirebase(tabTitles[position]);
        } else if (id == R.id.call_desert) {
            position=10;
            getDataFromFirebase(tabTitles[position]);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    static class ViewHolder {
        ImageView img;
        TextView foodname;
        TextView foodvalue;
        TextView itemid;
    }

    public class Myadapter extends BaseAdapter {
        private LayoutInflater myInflater;
        private ArrayList<food> foods;

        public Myadapter(Context context, ArrayList<food> f){
            myInflater = LayoutInflater.from(context);
            this.foods = f;
        }
        @Override
        public int getCount() {
            return foods.size();
        }

        @Override
        public Object getItem(int position) {
            return foods.get(position);
        }

        @Override
        public long getItemId(int position) {
            return foods.indexOf(getItem(position));
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if(convertView==null){
                convertView = myInflater.inflate(R.layout.food_item, null);
                holder = new ViewHolder();
                holder.foodname = (TextView)convertView.findViewById(R.id.food_title);
                holder.foodvalue = (TextView)convertView.findViewById(R.id.food_value);
                holder.img = (ImageView) convertView.findViewById(R.id.food_image);
                holder.itemid = (TextView)convertView.findViewById(R.id.itemid);
                convertView.setTag(holder);
            }
            else{
                holder = (ViewHolder)convertView.getTag();
            }
            food food2 = (food) getItem(position);
            holder.foodname.setText(food2.getFoodName());
            holder.foodvalue.setText(food2.getFoodValue());
            Glide.with(holder.img.getContext()).load(food2.getFoodImg()).into(holder.img);
            holder.itemid.setText(food2.getId());
            return convertView;

        }
    }

    private void getDataFromFirebase(final String want){
        new Thread(new Runnable() {
            public void run() {
                mRef = database.getReference("food-data/"+want);
                mRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        list.clear();
                        for(DataSnapshot ds : dataSnapshot.getChildren()){
                            food n = new food();
                            String sta = "";
                            if(ds.child("Status").getValue().toString().equals("false")){
                                sta = "(目前停賣)";
                            }
                            n.setFoodName((String)ds.child("foodName").getValue()+sta);
                            n.setFoodValue((String)ds.child("foodValue").getValue());
                            n.setFoodImg((String)ds.child("foodImg").getValue());
                            n.setId((String)ds.getKey());
                            list.add(n);
                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
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
