package com.example.files.util;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class Utils {

    public static void hideSoftInput(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        View currentFocus = activity.getCurrentFocus();

        if (currentFocus == null) {
            currentFocus = new View(activity);
        }

        imm.hideSoftInputFromWindow(currentFocus.getWindowToken(), 0);
    }
}
