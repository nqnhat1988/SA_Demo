package com.nhatdear.sademo.components;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;

import com.nhatdear.sademo.R;

/**
 * Created by NhatNguyen on 11/7/2016.
 * Apply custom font
 */

public class MyCustomTextView extends android.support.v7.widget.AppCompatTextView {

    public MyCustomTextView(Context context) {
        super(context);
    }

    public MyCustomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.MyCustomTextView,
                0, 0);

        try {
            String mFont = typedArray.getString(R.styleable.MyCustomTextView_tv_customFont);
            Typeface typeFace= Typeface.createFromAsset(getContext().getAssets(),"fonts/" + mFont);
            this.setTypeface(typeFace);
        } finally {
            typedArray.recycle();
        }
    }
}
