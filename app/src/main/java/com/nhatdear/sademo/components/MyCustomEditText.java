package com.nhatdear.sademo.components;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;

import com.nhatdear.sademo.R;


/**
 * Created by NhatNguyen on 11/10/2016.
 */

public class MyCustomEditText extends android.support.v7.widget.AppCompatEditText {
    public MyCustomEditText(Context context) {
        super(context);
    }

    public MyCustomEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.MyCustomButtonView,
                0, 0);

        try {
            String mFont = typedArray.getString(R.styleable.MyCustomEditText_et_customFont);
            Typeface typeFace= Typeface.createFromAsset(getContext().getAssets(),"fonts/" + mFont);
            this.setTypeface(typeFace);
        } finally {
            typedArray.recycle();
        }
    }

    public MyCustomEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.MyCustomButtonView,
                0, 0);

        try {
            String mFont = typedArray.getString(R.styleable.MyCustomEditText_et_customFont);
            Typeface typeFace= Typeface.createFromAsset(getContext().getAssets(),"fonts/" + mFont);
            this.setTypeface(typeFace);
        } finally {
            typedArray.recycle();
        }

    }
}
