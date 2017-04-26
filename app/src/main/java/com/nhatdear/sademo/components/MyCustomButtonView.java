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

public class MyCustomButtonView extends android.support.v7.widget.AppCompatButton {
    private String mFont;

    public MyCustomButtonView(Context context) {
        super(context);
    }

    public MyCustomButtonView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.MyCustomButtonView,
                0, 0);

        try {
            mFont = typedArray.getString(R.styleable.MyCustomButtonView_bt_customFont);
            Typeface typeFace= Typeface.createFromAsset(getContext().getAssets(),"fonts/" + mFont);
            this.setTypeface(typeFace);
        } finally {
            typedArray.recycle();
        }
    }

    public MyCustomButtonView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.MyCustomButtonView,
                0, 0);

        try {
            mFont = typedArray.getString(R.styleable.MyCustomButtonView_bt_customFont);
            Typeface typeFace= Typeface.createFromAsset(getContext().getAssets(),"fonts/" + mFont);
            this.setTypeface(typeFace);
        } finally {
            typedArray.recycle();
        }
    }
}
