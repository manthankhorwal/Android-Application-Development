package com.example.ecommerce;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.Toolbar;

public class Admin_category extends AppCompatActivity
{
ImageView tshirts,sportstshirts,femaledresses,sweaters;
ImageView glasses,hats,walletandpusrs,shoes;
ImageView headphones,laptops,watches,mobilephone;
Button home_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_category);
home_btn=findViewById(R.id.home_button);
home_btn.setOnClickListener(new View.OnClickListener()
{
    @Override
    public void onClick(View v)
    {
       Intent intent=new Intent(Admin_category.this,HomeActivity.class);
       startActivity(intent);
    }
});
    }

    public void onclickitem(View view)
    {
      String tag=view.getTag().toString();
     //   Toast.makeText(this, tag, Toast.LENGTH_SHORT).show();
        if(tag.equals("1"))  //tshirts
        {
            Intent intent=new Intent(getApplicationContext(),AdminaddNewProduct.class);
  intent.putExtra("category","tshirts");
            startActivity(intent);
            finish();
        }
        if(tag.equals("2")) //sports
        {
            Intent intent=new Intent(getApplicationContext(),AdminaddNewProduct.class);
            intent.putExtra("category","sports_t_shirts");
            startActivity(intent);finish();

        }
        if(tag.equals("3"))  //female
        {
            Intent intent=new Intent(getApplicationContext(),AdminaddNewProduct.class);
            intent.putExtra("category","female_dresses");
            startActivity(intent);finish();
        }
        if(tag.equals("4")) //sweaters
        {
            Intent intent=new Intent(getApplicationContext(),AdminaddNewProduct.class);
            intent.putExtra("category","sweaters");
            startActivity(intent);finish();


        }
        if(tag.equals("5"))  //glasses
        {
            Intent intent=new Intent(getApplicationContext(),AdminaddNewProduct.class);
            intent.putExtra("category","glasses");
            startActivity(intent);finish();
        }
        if(tag.equals("6")) //purses
        {
            Intent intent=new Intent(getApplicationContext(),AdminaddNewProduct.class);
            intent.putExtra("category","purses");
            startActivity(intent);finish();

        }
        if(tag.equals("7"))  //hats
        {Intent intent=new Intent(getApplicationContext(),AdminaddNewProduct.class);
            intent.putExtra("category","hats");
            startActivity(intent);finish();
        }
        if(tag.equals("8")) //shoes
        {
            Intent intent=new Intent(getApplicationContext(),AdminaddNewProduct.class);
            intent.putExtra("category","shoes");
            startActivity(intent);finish();
        }
        if(tag.equals("9"))  //headphones
        {
            Intent intent=new Intent(getApplicationContext(),AdminaddNewProduct.class);
            intent.putExtra("category","headphones");
            startActivity(intent);finish();
        }
        if(tag.equals("10")) //laptop
        {
            Intent intent=new Intent(getApplicationContext(),AdminaddNewProduct.class);
            intent.putExtra("category","laptop");
            startActivity(intent);finish();

        }
        if(tag.equals("11"))  //watches
        {
            Intent intent=new Intent(getApplicationContext(),AdminaddNewProduct.class);
            intent.putExtra("category","watches");
            startActivity(intent);finish();
        }
        if(tag.equals("12")) //mobile
        {
            Intent intent=new Intent(getApplicationContext(),AdminaddNewProduct.class);
            intent.putExtra("category","mobile");
            startActivity(intent);finish();


        }


    }
}
