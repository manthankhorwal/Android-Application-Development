package com.example.ecommerce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import io.paperdb.Paper;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecommerce.Prevalent.prevalent;
import com.example.ecommerce.model.Users;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class login extends AppCompatActivity
{
EditText inputnumber,inputpassword;
Button loginbutton;
ProgressDialog loadingbar;
ImageView ss;
CheckBox chkboxrememberme;
TextView admin,notadmin;
String parentdbname="users";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        inputnumber=findViewById(R.id.login_mobile_number);
        inputpassword=findViewById(R.id.login_password);
        loadingbar=new ProgressDialog(this);
        chkboxrememberme=findViewById(R.id.remember_me);
        admin=findViewById(R.id.admin_text);
        notadmin=findViewById(R.id.not_admin_text);
        Paper.init(this);
    loginbutton=findViewById(R.id.login);

    loginbutton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v)
        {
            loginuser();
            
        }
    });
admin.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v)
    {
     loginbutton.setText("Login Admin");
     admin.setVisibility(View.INVISIBLE);
     notadmin.setVisibility(View.VISIBLE);
     parentdbname="Admins";
    }
});
notadmin.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v)
    {
        loginbutton.setText("Login");
        admin.setVisibility(View.VISIBLE);
        notadmin.setVisibility(View.INVISIBLE);
        parentdbname="users";

    }
});
    }

    private void loginuser()
    {
        String phone=inputnumber.getText().toString();
        String password=inputpassword.getText().toString();
         if(phone.length()==0)
    {
        Toast.makeText(this, "Enter Mobile Number", Toast.LENGTH_SHORT).show();
    }
    else if(password.length()==0)
    {
        Toast.makeText(this, "Enter password", Toast.LENGTH_SHORT).show();
    }
    else
    {
        loadingbar.setTitle("Login Account");
        loadingbar.setMessage("Please wait while we are checking credentials");
        loadingbar.setCanceledOnTouchOutside(false);
        loadingbar.show();
        AllowAccesToAccount(phone,password);
    }
    }

    private void AllowAccesToAccount(final String phone, final String password)
    {
        Paper.book().write(prevalent.userphonekey,phone);
        if(chkboxrememberme.isChecked())
        {
            Paper.book().write(prevalent.userpasswordkey,password);
        }

        DatabaseReference rootref;
        rootref= FirebaseDatabase.getInstance().getReference();

        rootref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(parentdbname).child(phone).exists())
                {
                    loadingbar.dismiss();
                    Users userdata=snapshot.child(parentdbname).child(phone).getValue(Users.class);
                        if(userdata.getPassword().equals(password))
                        {
                                Toast.makeText(login.this, "LoggedIn Successful", Toast.LENGTH_SHORT).show();
                               Intent intent = new Intent(getApplicationContext(), HomeActivity.class);

                                  Paper.book().write("Profile_name",userdata.getName());
                                 prevalent.currentonlineuser=userdata;
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();


                        }
                        else
                        {
                            Toast.makeText(login.this, "Wrong Password", Toast.LENGTH_SHORT).show();
                        }
                    }

                else
                {
                    loadingbar.dismiss();
                    Toast.makeText(login.this, "User does not exists! Please Register", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(getApplicationContext(),register.class);

                    startActivity(intent);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}