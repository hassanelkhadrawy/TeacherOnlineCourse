package com.example.teacheronlinecourse.Fonts;
import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;

public class Comfortaa_Bold extends AppCompatTextView {
    public Comfortaa_Bold(Context context) {
        super(context);
    }

    public Comfortaa_Bold(Context context, AttributeSet attrs) {
        super(context, attrs);
        setTypeface(Typeface.createFromAsset(context.getAssets(), "font/Comfortaa-Bold.ttf"));


    }
}