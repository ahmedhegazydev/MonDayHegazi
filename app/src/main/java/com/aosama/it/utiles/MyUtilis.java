package com.aosama.it.utiles;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.aosama.it.R;

import org.apache.commons.lang3.StringUtils;

import androidx.core.content.ContextCompat;
import dmax.dialog.SpotsDialog;

public class MyUtilis {
    public static AlertDialog myDialog(Context context) {
        AlertDialog dialog = new SpotsDialog.Builder().setContext(context).setTheme(R.style.CustomDialog).build();
        dialog.setCancelable(false);
        return dialog;
    }

    public static void showKeyboard(EditText editText) {
        editText.post(new Runnable() {
            @Override
            public void run() {
                editText.requestFocus();
                InputMethodManager imm = (InputMethodManager) editText.getContext()
                        .getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
            }
        });
    }


    public static String formateDate(String name) {
        String formattedDate = "";
        //2020-03-15T21:00:00.000Z
        formattedDate = StringUtils.substringBefore(name, "T");
        return formattedDate;
//        return name;
    }

    public static void changeColor(Context mContext, View imageView, int color) {
        Drawable background = imageView.getBackground();
        if (background instanceof ShapeDrawable) {
            ((ShapeDrawable) background).getPaint().setColor(ContextCompat
                    .getColor(mContext, color));
        } else if (background instanceof GradientDrawable) {
            ((GradientDrawable) background).setColor(ContextCompat
                    .getColor(mContext, color));
        } else if (background instanceof ColorDrawable) {
            ((ColorDrawable) background).setColor(ContextCompat
                    .getColor(mContext, color));
        }
    }
}
