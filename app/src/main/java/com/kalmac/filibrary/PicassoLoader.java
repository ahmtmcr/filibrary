package com.kalmac.filibrary;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class PicassoLoader {
    private static String BASE_IMAGE_URL = "https://image.tmdb.org/t/p/w500";
    public static void LoadImageToImageButton(String IMAGE_URL,ImageButton imageButton, int width, int height){
        Picasso.get().load(BASE_IMAGE_URL + IMAGE_URL).resize(width, height).into(imageButton);
    }
    public static void LoadImageToImageView(String IMAGE_URL, ImageView imageView, int width, int height){
        Picasso.get().load(BASE_IMAGE_URL + IMAGE_URL).resize(width, height).into(imageView);
    }
}
