package com.example.ecommerce.model;

public class MyOrdersclass
{
    String date;
    String discount;
    String image;
    String pid;
    String price;
    String pname;
    String productby;
    String quantity;
    String time;
   public MyOrdersclass(){}

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public String getProductby() {
        return productby;
    }

    public void setProductby(String productby) {
        this.productby = productby;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public MyOrdersclass(String date, String discount, String image, String pid, String price, String pname, String productby, String quantity, String time) {
        this.date = date;
        this.discount = discount;
        this.image = image;
        this.pid = pid;
        this.price = price;
        this.pname = pname;
        this.productby = productby;
        this.quantity = quantity;
        this.time = time;
    }



}
