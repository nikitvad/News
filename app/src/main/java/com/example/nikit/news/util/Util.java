package com.example.nikit.news.util;

import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Collections;
import jp.wasabeef.glide.transformations.CropCircleTransformation;


/**
 * Created by nikit on 23.03.2017.
 */

public class Util {

    public static String getStringFromArrayOfString(String[] strings) {
        String result = "";
        for (String item : strings) {
            result = result + item + "\n";
        }
        return result;
    }

    public static String[] getListOfStringsFromString(String string) {
        return string.split("\n");
    }

    public static void loadCircleImage(ImageView imageView, String url) {
        Glide.with(imageView.getContext())
                .load(url)
                .bitmapTransform(new CropCircleTransformation(imageView.getContext()))
                .into(imageView);
    }

}
