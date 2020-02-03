package com.example.teacheronlinecourse.Fonts;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatEditText;

public class Comfortaa_Regular extends AppCompatEditText {
    public Comfortaa_Regular(Context context) {
        super(context);
    }

    public Comfortaa_Regular(Context context, AttributeSet attrs) {
        super(context, attrs);
        setTypeface(Typeface.createFromAsset(context.getAssets(), "font/Comfortaa-Regular.ttf"));


    }
}