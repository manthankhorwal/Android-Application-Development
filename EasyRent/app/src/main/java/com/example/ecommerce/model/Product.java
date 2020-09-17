package com.example.ecommerce.model;

public class Product
{
    public String category;
    public String date;
    public String description;
    public String image;
    public String name;
    public String pid;
    public String price;
    public String time;
    public String productby;
    public String getProductby() {
        return productby;
    }

    public void setProductby(String productby) {
        this.productby = productby;
    }



    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Product(){}
    public Product(String category, String date, String description, String image, String name, String pid, String price, String time,String productby) {
        this.category = category;
        this.date = date;
        this.description = description;
        this.image = image;
        this.name = name;
        this.pid = pid;
        this.price = price;
        this.time = time;
        this.productby=productby;
    }


}
