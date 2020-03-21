package com.aosama.it.utiles;

import android.app.AlertDialog;
import android.content.Context;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.aosama.it.R;

import org.apache.commons.lang3.StringUtils;

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
}
