package com.example.ecommerce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class register extends AppCompatActivity {
Button createAccountButton;
EditText inputname,inputmobilenumber,inputpassword;
 ProgressDialog loadingbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        createAccountButton=findViewById(R.id.Register);
        inputmobilenumber=findViewById(R.id.register_mobile_number);
        inputpassword=findViewById(R.id.register_password);
        inputname=findViewById(R.id.register_username);
        loadingbar=new ProgressDialog(this);
   createAccountButton.setOnClickListener(new View.OnClickListener() {
       @Override
       public void onClick(View v) {
           Createaccount();
       }
   });
    }

    private void Createaccount()
    {
        String name=inputname.getText().toString();
        String phone=inputmobilenumber.getText().toString();
        String password=inputpassword.getText().toString();
        if(name.length()==0)
        {
            Toast.makeText(this, "Enter Username", Toast.LENGTH_SHORT).show();
        }
        else if(phone.length()==0)
        {
            Toast.makeText(this, "Enter Mobile Number", Toast.LENGTH_SHORT).show();
        }
       else if(password.length()==0)
        {
            Toast.makeText(this, "Enter password", Toast.LENGTH_SHORT).show();
        }
       else
        {
loadingbar.setTitle("Create Account");
loadingbar.setMessage("Please wait while we are checking credentials");
loadingbar.setCanceledOnTouchOutside(false);
loadingbar.show();
 validatePhonenumber(name,phone,password);
        }

    }

    private void validatePhonenumber(final String name, final String phone, final String password)
    {
        final DatabaseReference rootref;
        rootref= FirebaseDatabase.getInstance().getReference();
        rootref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                if(!(snapshot.child("users").child(phone).exists()))
                {
                    Map<String,Object> mymap=new HashMap<>();
                    mymap.put("name",name);
                    mymap.put("phone",phone);
                    mymap.put("password",password);
                    rootref.child("users").child(phone).setValue(mymap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {
                                loadingbar.hide();
                                Toast.makeText(register.this, "Account Created Succesfully", Toast.LENGTH_SHORT).show();
                                Intent intent=new Intent(getApplicationContext(),login.class);
                                startActivity(intent);
                            }
                            else
                            {
                                loadingbar.hide();
                                Toast.makeText(register.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                else
                {
                    loadingbar.hide();
                    Toast.makeText(register.this, "Mobile number already exists", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(getApplicationContext(),login.class);
                    startActivity(intent);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

}