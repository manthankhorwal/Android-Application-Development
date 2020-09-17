package com.example.ecommerce;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.ecommerce.Prevalent.prevalent;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class AdminaddNewProduct extends AppCompatActivity {
String categoryname,description,price,Pname,savecurrentdate,savecurrenttime;
Button addnewproductbtn;
Uri imageuri;
ImageView inputproductimage;
EditText inputproductname,inputproductdescription,inputproductprice;
String productrandomkey;
StorageReference productimagesRef;
static final int gallerypick=1;
String downloadimageUrl;
ProgressDialog loadingbar;
DatabaseReference productref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adminadd_new_product);

    categoryname=getIntent().getStringExtra("category");
       // Toast.makeText(this, categoryname, Toast.LENGTH_SHORT).show();
    addnewproductbtn=findViewById(R.id.addnewproduct_btn);
    inputproductimage=findViewById(R.id.select_product_image);
        inputproductname=findViewById(R.id.product_name);
        inputproductdescription=findViewById(R.id.product_description);
        productref=FirebaseDatabase.getInstance().getReference().child("Products");
        inputproductprice=findViewById(R.id.product_price);
      loadingbar=new ProgressDialog(this);

        productimagesRef= FirebaseStorage.getInstance().getReference().child("Product Images");
        inputproductimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
             opengallery();
            }
        });
    addnewproductbtn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v)
        {
        ValidateProductData();
        }
    });
    }

    private void ValidateProductData()
    {
        description=inputproductdescription.getText().toString();
        price=inputproductprice.getText().toString();
        Pname=inputproductname.getText().toString();
        if(imageuri==null)
        {
            Toast.makeText(this, "Product Image is mandatory", Toast.LENGTH_SHORT).show();
        }
        else if(Pname.length()==0)
        {
            Toast.makeText(this, "Enter Product Name", Toast.LENGTH_SHORT).show();
        }
        else if(description.length()==0)
        {
            Toast.makeText(this, "Enter Product Description", Toast.LENGTH_SHORT).show();

        }
        else if(price.length()==0)
        {
            Toast.makeText(this, "Enter Product price", Toast.LENGTH_SHORT).show();

        }
        else //everthing is good to add to firebase
        {
            StoreProductInformation();
        }
    }
    private void StoreProductInformation()
    {
        loadingbar.setTitle("Add new Product");
        loadingbar.setMessage("Please wait while we are adding your product");
       loadingbar.setCanceledOnTouchOutside(false);
       loadingbar.show();
        Calendar calendar=Calendar.getInstance();
        SimpleDateFormat currentdate=new SimpleDateFormat("MM dd,yyyy");
        savecurrentdate=currentdate.format(calendar.getTime());

        SimpleDateFormat currenttime=new SimpleDateFormat("HH:mm:ss a");
        savecurrenttime=currenttime.format(calendar.getTime());
     productrandomkey=savecurrentdate+savecurrenttime;

        final StorageReference filepath=productimagesRef.child(imageuri.getLastPathSegment() + productrandomkey +".jpg");
        final UploadTask uploadTask=filepath.putFile(imageuri);

        uploadTask.addOnFailureListener(new OnFailureListener()
        {
            @Override
            public void onFailure(@NonNull Exception e)
            {
                Toast.makeText(AdminaddNewProduct.this, e.toString(), Toast.LENGTH_SHORT).show();
            loadingbar.dismiss();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
            {
                Toast.makeText(AdminaddNewProduct.this, "Product image uploaded", Toast.LENGTH_SHORT).show();
                Task<Uri> urltask=uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if(!task.isSuccessful())
                        {
                            throw task.getException();
                        }
                         downloadimageUrl=filepath.getDownloadUrl().toString();
                        return filepath.getDownloadUrl();

                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task)
                    {
                       if(task.isSuccessful())
                       {
                  downloadimageUrl=task.getResult().toString();
                           Toast.makeText(AdminaddNewProduct.this, "getting product image url", Toast.LENGTH_SHORT).show();

                           SaveProductinfotodatabase();

                       }
                    }
                });




            }
        });

    }

    private void SaveProductinfotodatabase()
    {
        final HashMap<String,Object> productmap=new HashMap<>();
        productmap.put("pid",productrandomkey);
        productmap.put("date",savecurrentdate);
        productmap.put("time",savecurrenttime);
        productmap.put("description",description);
        productmap.put("image",downloadimageUrl);
        productmap.put("name",Pname);
        productmap.put("price",price);
        productmap.put("category",categoryname);
        productmap.put("productby", prevalent.currentonlineuser.getPhone());

   productref.child(productrandomkey).setValue(productmap).addOnCompleteListener(new OnCompleteListener<Void>() {
       @Override
       public void onComplete(@NonNull Task<Void> task) {
           if(task.isSuccessful())
           {
               Intent intent=new Intent(AdminaddNewProduct.this,Admin_category.class);
                AddtoMyProducts(productmap);

               loadingbar.dismiss();
               Toast.makeText(AdminaddNewProduct.this, "Product is added to app", Toast.LENGTH_SHORT).show();

               startActivity(intent);finish();

           }
           else
           {
               loadingbar.dismiss();
               Toast.makeText(AdminaddNewProduct.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
           }
       }
   });

    }

    private void AddtoMyProducts(HashMap<String, Object> productmap)
    {
        productmap.put("state","not booked");
        FirebaseDatabase.getInstance().getReference().child("MyProducts")
                .child(prevalent.currentonlineuser.getPhone()).child(productmap.get("pid").toString()).setValue(productmap);
    }

    private void opengallery()
    {
        Intent galleryintent=new Intent();
        galleryintent.setAction(Intent.ACTION_GET_CONTENT);
        galleryintent.setType("image/*");
        startActivityForResult(galleryintent,gallerypick);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        //Toast.makeText(this, "yooy", Toast.LENGTH_SHORT).show();
        if(requestCode==gallerypick && resultCode==RESULT_OK &&data!=null)
        {
            Toast.makeText(this, "Yes", Toast.LENGTH_SHORT).show();
            imageuri=data.getData();
            inputproductimage.setImageURI(imageuri);

        }



    }
}