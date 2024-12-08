package com.example.orderfood.models;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class SpecialContainer {
    private ImageView imageView;
    private TextView foodName;
    private TextView foodPrice;
    public SpecialContainer(ImageView imageView, TextView foodName, TextView foodPrice) {
        this.imageView = imageView;
        this.foodName = foodName;
        this.foodPrice = foodPrice;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }

    public TextView getFoodName() {
        return foodName;
    }

    public void setFoodName(TextView foodName) {
        this.foodName = foodName;
    }

    public TextView getFoodPrice() {
        return foodPrice;
    }

    public void setFoodPrice(TextView foodPrice) {
        this.foodPrice = foodPrice;
    }
}
