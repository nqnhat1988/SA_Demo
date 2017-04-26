package com.nhatdear.sademo.helpers;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.TextView;

import com.nhatdear.sademo.R;
import com.nhatdear.sademo.activities.SA_MainActivity;


/**
 * Created by nhatdear on 4/24/17.
 */

public class SA_Helper {
    //region STRING HELPER
    public static final String LAST_USERNAME = "LAST_USERNAME";
    public static final String LAST_ACCOUNT = "LAST_ACCOUNT";
    public static final String USER_PREF = "USER_SHAREDPREF";
    public static final String USERNAME_MUST_NOT_CONTAIN_SPECIAL_CHAR = "Username can only contain letters and numbers";
    public static final String EMAIL_IS_NOT_CORRECT_FORMAT = "Ups! That’s not a valid email address.";
    public static final String PLEASE_CLICK_BACK_AGAIN_TO_EXIT = "Click BACK again to exit";
    //endregion

    //region UI HELPER
    public static void showSnackbar(View view, String content) {
        Snackbar snackbar = Snackbar.make(view, content, Snackbar.LENGTH_LONG);
        View v = snackbar.getView();
        v.setBackgroundColor(ContextCompat.getColor(view.getContext(), R.color.snackBarColor));
        TextView textView = (TextView) v.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTypeface(Typeface.createFromAsset(view.getContext().getAssets(), "fonts/OpenSans-Regular.ttf"));
        textView.setTextSize(12);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        }
        textView.setTextColor(ContextCompat.getColor(view.getContext(), R.color.textColorPrimary));
        snackbar.show();
    }

    public static boolean softCheckValidString(String str) {
        return str != null && !str.trim().isEmpty();
    }

    public static void goToMainWithClearFlag(Context context) {
        Intent intent = new Intent(context, SA_MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }
}
