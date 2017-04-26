package com.nhatdear.sademo.helpers;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.text.style.UpdateAppearance;
import android.util.Log;
import android.view.View;

/**
 * Created by NhatNguyen on 12/27/2016.
 */

public class SA_TagSpan extends ClickableSpan implements UpdateAppearance {
    private static final String TAG_SEARCH = "TAG_SEARCH";
    private final String TAG = SA_TagSpan.class.getSimpleName();
    //private final int[] colors;
    private Context context;
    private float currentTextSize;
    private boolean allowClick = false;
    private String tagSearch;
    private Uri uri;
    public SA_TagSpan(Context context) {
        this.context = context;
    }

    public SA_TagSpan(Context context, float textSize) {
        this.context = context;
        this.currentTextSize = textSize;
    }

    public SA_TagSpan(Context context, boolean allowClick, String tagSearch) {
        this.context = context;
        this.allowClick = allowClick;
        this.tagSearch = tagSearch;
    }

    public SA_TagSpan(Context context, boolean allowClick, Uri parse) {
        this.context = context;
        this.allowClick = allowClick;
        this.uri = parse;
    }

    @Override
    public void onClick(View view) {
        if (allowClick) {
            if (tagSearch != null) {
                Log.d(TAG, "Tag click : " + tagSearch);
//                Intent intent = new Intent(context, DP_SearchActivity.class);
//                intent.putExtra(TAG_SEARCH, tagSearch);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                context.startActivity(intent);
            } else if (uri != null) {
                Log.d(TAG, "Uri click : " + uri);
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(uri);
                context.startActivity(intent);
            }
        }
    }

    @Override
    public void updateDrawState(TextPaint paint) {
        paint.setStyle(Paint.Style.FILL);
        paint.setFakeBoldText(true);
    }
}
