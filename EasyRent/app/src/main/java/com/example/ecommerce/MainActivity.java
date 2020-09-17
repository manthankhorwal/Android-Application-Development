package com.example.ecommerce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import io.paperdb.Paper;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.ecommerce.Prevalent.prevalent;
import com.example.ecommerce.model.Users;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity
{
Button joinNowButton,LoginNowButton;
ProgressDialog loadingbar;
DatabaseReference mref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Paper.init(this);
        joinNowButton=findViewById(R.id.main_join_now_btn);
        loadingbar=new ProgressDialog(this);
        LoginNowButton=findViewById(R.id.main_login_now_btn);
        LoginNowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,login.class);
                startActivity(intent);
            }
        });
        joinNowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,register.class);
                startActivity(intent);
            }
        });


        String userphonekey=Paper.book().read(prevalent.userphonekey);
        String userpasswordkey=Paper.book().read(prevalent.userpasswordkey);

            if(!TextUtils.isEmpty(userpasswordkey) && ! TextUtils.isEmpty(userpasswordkey))
            {
                loadingbar.setTitle("Already logged in");
                loadingbar.setMessage("Please wait...");
                loadingbar.setCanceledOnTouchOutside(false);
                loadingbar.show();
               Allowaccess(userphonekey,userpasswordkey);
            }
    }

    private void Allowaccess(final String phone, final String password)
    {
        DatabaseReference rootref;
        rootref= FirebaseDatabase.getInstance().getReference();

        rootref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child("users").child(phone).exists())
                {
                 loadingbar.dismiss();
                    Users userdata=snapshot.child("users").child(phone).getValue(Users.class);

                    if(userdata.getPhone().equals(phone))
                    {
                        if(userdata.getPassword().equals(password))
                        {
                            Toast.makeText(MainActivity.this, "LoggedIn Successful", Toast.LENGTH_SHORT).show();
                              prevalent.currentonlineuser=userdata;
                            Intent intent=new Intent(getApplicationContext(),HomeActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        }
                        else
                        {
                            Toast.makeText(MainActivity.this, "Wrong Password", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                else
                {
                   loadingbar.dismiss();
                    Toast.makeText(MainActivity.this, "User does not exists! Please Register", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(getApplicationContext(),register.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}