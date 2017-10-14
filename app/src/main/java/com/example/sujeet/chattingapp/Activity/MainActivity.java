package com.example.sujeet.chattingapp.Activity;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.example.sujeet.chattingapp.Adapter.ContactListRecyclerAdapter;
import com.example.sujeet.chattingapp.Adapter.Contact_ListAdapter;
import com.example.sujeet.chattingapp.Adapter.PagerAdapter;
import com.example.sujeet.chattingapp.HomeActivity;
import com.example.sujeet.chattingapp.R;
import com.example.sujeet.chattingapp.User;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager viewPager;

    static boolean isInitialized = false;
    SharedPreferences sharedPreferences;
    public static ArrayList<User> filtered_userList;
    public static ArrayList grp_list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        if(!isInitialized){
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
            isInitialized = true;
        }else {
            Log.d("Offline","Already Initialized");
        }

        sharedPreferences = getSharedPreferences("Login",MODE_PRIVATE);

        String id = sharedPreferences.getString("contact","");

        MainActivity.grp_list = new ArrayList();
//        grp_list.add(id);

        tabLayout = (TabLayout)findViewById(R.id.tabLayout);
        viewPager = (ViewPager)findViewById(R.id.viewPager);

        PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setBackgroundTintList(ColorStateList.valueOf(Color
                .parseColor("#00000000")));
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    showDialog();

                }
        });


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        super.onCreateOptionsMenu(menu);
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            SharedPreferences.Editor ed = sharedPreferences.edit();
            ed.putBoolean("key1", false);
            ed.putString("Username", "");
            ed.commit();
            startActivity(new Intent(MainActivity.this,HomeActivity.class));
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void showDialog(){
        Contact_ListAdapter adapter = new Contact_ListAdapter(this,filtered_userList);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Create Group");
        builder.setAdapter(adapter,null);
        builder.setCancelable(false);
        builder.setPositiveButton("Create", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                Intent intent = new Intent(MainActivity.this,GroupChatActivity.class);
                Set<String> set = new HashSet<String>();
                set.addAll(grp_list);
                SharedPreferences.Editor ed = sharedPreferences.edit();
                ed.putStringSet("list",set);
                ed.commit();
                startActivity(intent);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                return;
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }


}
