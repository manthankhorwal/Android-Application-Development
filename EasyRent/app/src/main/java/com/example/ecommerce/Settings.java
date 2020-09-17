package com.example.ecommerce;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import de.hdodenhof.circleimageview.CircleImageView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecommerce.Prevalent.prevalent;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Settings extends AppCompatActivity
{
    String name,password,address,image,phone;
CircleImageView profileimageView;
EditText fullNameEditText,addressEditText,userPhoneEditText;
    StorageReference storageProfileRef;
    StorageTask uploadtask;
TextView settings_cancel,settings_update;
String currentuserphonenumber;
    String myUrl;
Uri imageuri;
int gallerypick=1;
ProgressDialog loadingbar;
String checker="";
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
storageProfileRef=FirebaseStorage.getInstance().getReference().child("Profile Images");
        currentuserphonenumber=getIntent().getStringExtra("Mobile number");
        //Toast.makeText(this, currentuserphonenumber, Toast.LENGTH_SHORT).show();
        profileimageView=findViewById(R.id.settings_profile_image);
        addressEditText=findViewById(R.id.settings_profile_address);
        userPhoneEditText=findViewById(R.id.settings_profile_number);
        fullNameEditText=findViewById(R.id.settings_profile_name);
        settings_cancel=findViewById(R.id.close_settings);
        settings_update=findViewById(R.id.update_settings);
        loadingbar=new ProgressDialog(this);
        loadingbar.setTitle("Profile");
        loadingbar.setTitle("Updating Profile...");

        userInfoDisplay(profileimageView,addressEditText,userPhoneEditText,fullNameEditText);
   settings_cancel.setOnClickListener(new View.OnClickListener() {
       @Override
       public void onClick(View v) {
           finish();
       }
   });
settings_update.setOnClickListener(new View.OnClickListener()
{
    @Override
    public void onClick(View v)
    {
       if(checker.equals("clicked"))
       {
           userInfoSaved();
       }
       else
       {
           updateUserOnly();
       }


    }
});
profileimageView.setOnClickListener(new View.OnClickListener()
{
    @Override
    public void onClick(View v)
    {
        checker="clicked";
        CropImage.activity(imageuri).setAspectRatio(1,1).start(Settings.this); // opens gallery automatically
    }
});

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
      if(requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode==RESULT_OK && data!=null)
      {
          CropImage.ActivityResult result=CropImage.getActivityResult(data);
          imageuri=result.getUri();
          profileimageView.setImageURI(imageuri);

      }
      else
      {
          Toast.makeText(this, "Error,Try Again", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(Settings.this,Settings.class));
      }
    }
    private void updateUserOnly()
    {

        DatabaseReference ref=FirebaseDatabase.getInstance().getReference().child("users");

        HashMap<String,Object> userMap=new HashMap<>();
        userMap.put("name",fullNameEditText.getText().toString());
        userMap.put("address",addressEditText.getText().toString());
        userMap.put("phone",userPhoneEditText.getText().toString());
        loadingbar.dismiss();
        Toast.makeText(Settings.this, "Profile Updated Sucessfully", Toast.LENGTH_SHORT).show();
        ref.child(prevalent.currentonlineuser.getPhone()).updateChildren(userMap);
        startActivity(new Intent(Settings.this,HomeActivity.class));


    }

    private void userInfoSaved()
    {
    if(TextUtils.isEmpty(fullNameEditText.getText().toString()))
    {
        Toast.makeText(this, "Enter Name", Toast.LENGTH_SHORT).show();
    }
       else if(TextUtils.isEmpty(addressEditText.getText().toString()))
        {
            Toast.makeText(this, "Enter Address", Toast.LENGTH_SHORT).show();
        }
       else if(TextUtils.isEmpty(userPhoneEditText.getText().toString()))
        {
            Toast.makeText(this, "Enter Mobile number", Toast.LENGTH_SHORT).show();
        }
       else if(checker.equals("clicked"))
    {
        uploadimage();
    }

    }

    private void uploadimage()
    {
        loadingbar.show();
        if(imageuri!=null)
        {
            final StorageReference fileref=storageProfileRef.child(prevalent.currentonlineuser.getPhone()+".jpg");

            fileref.putFile(imageuri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
                {
                     fileref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                         @Override
                         public void onSuccess(Uri uri)
                         {
                             Toast.makeText(Settings.this, "Image is uploaded successfully", Toast.LENGTH_SHORT).show();
                             myUrl=uri.toString();
                             DatabaseReference ref=FirebaseDatabase.getInstance().getReference().child("users");

                             HashMap<String,Object> userMap=new HashMap<>();
                             userMap.put("name",fullNameEditText.getText().toString());
                             userMap.put("address",addressEditText.getText().toString());
                             userMap.put("phone",userPhoneEditText.getText().toString());
                             userMap.put("image",myUrl);
                             loadingbar.dismiss();
                              ref.child(prevalent.currentonlineuser.getPhone()).updateChildren(userMap);
                             Toast.makeText(Settings.this, "Profile Updated Sucessfully", Toast.LENGTH_SHORT).show();
                            Intent intent=new Intent(Settings.this,HomeActivity.class);
                             intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);

                         }
                     }).addOnFailureListener(new OnFailureListener() {
                         @Override
                         public void onFailure(@NonNull Exception e) {
                             Toast.makeText(Settings.this, e.toString(), Toast.LENGTH_SHORT).show();
                             loadingbar.dismiss();

                         }
                     });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(Settings.this, e.toString(), Toast.LENGTH_SHORT).show();
                    loadingbar.dismiss();

                }
            });


        }
    }

    private void userInfoDisplay(final CircleImageView profileimageView, final EditText addressEditText, final EditText userPhoneEditText, final EditText fullNameEditText)
    {
        DatabaseReference userRef=FirebaseDatabase.getInstance().getReference().child("users").child(prevalent.currentonlineuser.getPhone());

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                if(snapshot.exists())
                {
                    if(snapshot.child("image").exists())
                    {
                        String image=snapshot.child("image").getValue().toString();

                        Picasso.get().load(image).into(profileimageView);
                    }
                    if(snapshot.child("address").exists())
                    {
                        String address=snapshot.child("address").getValue().toString();
                        addressEditText.setText(address);

                    }
                    String name=snapshot.child("name").getValue().toString();
                    String phone=snapshot.child("phone").getValue().toString();
                    fullNameEditText.setText(name);
                    userPhoneEditText.setText(phone);


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}