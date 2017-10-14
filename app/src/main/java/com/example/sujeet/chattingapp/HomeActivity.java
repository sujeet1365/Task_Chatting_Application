package com.example.sujeet.chattingapp;

import android.*;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.sujeet.chattingapp.Activity.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class HomeActivity extends AppCompatActivity {

    Button btn;
    ProgressDialog progressDialog;

    EditText edt_name,edt_contact;
    Boolean check_login;
    static String sp_user_name;
    static String name,contact;
    final int MY_PERMISSIONS_READ = 1,MY_PERMISSIONS_WRITE=786,MY_PERMISSIONS_CAMERA=999;
    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 24601;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        progressDialog = new ProgressDialog(HomeActivity.this);
        progressDialog.setMessage("Processing......");
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    && ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
                if(ActivityCompat.shouldShowRequestPermissionRationale(this,
                        android.Manifest.permission.READ_CONTACTS)){

                }

            } else {

                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.READ_CONTACTS},
                        MY_PERMISSIONS_REQUEST_READ_CONTACTS);
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_WRITE);
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_CAMERA);
            }
        }


        sharedPreferences = getSharedPreferences("Login",MODE_PRIVATE);
        check_login = sharedPreferences.getBoolean("key1", false);
        sp_user_name = sharedPreferences.getString("Username", "");

        if(check_login)
        {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
        else{
            edt_name = (EditText)findViewById(R.id.edt_name);
            edt_contact = (EditText)findViewById(R.id.edt_contact);
            btn = (Button)findViewById(R.id.btn);

            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    progressDialog.show();
                    name=edt_name.getText().toString();
                    contact=edt_contact.getText().toString();
                    if(edt_name.getText().toString().equals(""))
                        edt_name.setError("Empty Field");
                    else if(edt_contact.getText().toString().equals(""))
                        edt_contact.setError("Empty Field");
                    else
                    {
                        SharedPreferences.Editor ed = sharedPreferences.edit();
                        ed.putBoolean("key1", true);
                        ed.putString("username", name);
                        ed.putString("contact",contact);
                        ed.commit();

                        //String uid = firebaseUser.getUid();
                        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                        final DatabaseReference databaseReference = firebaseDatabase.getReference().child("users").child(contact);
//                        databaseReference.child("name").setValue(name);
//                        databaseReference.child("Contact").setValue(contact);
//                        startActivity(new Intent(HomeActivity.this,MainActivity.class));
//                        finish();

                        databaseReference.child("name").setValue(name).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()) {
                                    databaseReference.child("contact").setValue(edt_contact.getText().toString());
                                    Toast.makeText(HomeActivity.this,"Successfully Registered", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(HomeActivity.this,MainActivity.class));
                                    progressDialog.dismiss();
                                    finish();
                                }
                                else {
                                    Toast.makeText(HomeActivity.this, "Registration Failed", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


                    }
                }
            });
        }



    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_READ: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {

                }
                return;
            }
            case MY_PERMISSIONS_WRITE:{
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted,
                } else {

                    // permission denied,
                }
                return;

            }
            case MY_PERMISSIONS_REQUEST_READ_CONTACTS:{
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted,
                } else {

                    // permission denied,
                }
                return;

            }

        }
    }

}
