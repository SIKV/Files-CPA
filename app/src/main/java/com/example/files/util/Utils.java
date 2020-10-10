package com.example.files.util;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.text.DecimalFormat;
import java.util.Date;

public class Utils {

    public static void hideSoftInput(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        View currentFocus = activity.getCurrentFocus();

        if (currentFocus == null) {
            currentFocus = new View(activity);
        }

        imm.hideSoftInputFromWindow(currentFocus.getWindowToken(), 0);
    }

    public static String getTimeSting(long timeMillis) {
        return new Date(timeMillis).toString();
    }

    public static String getFileSize(long size) {
        // https://stackoverflow.com/a/18099948/7064179
        if (size <= 0) {
            return "0";
        }

        final String[] units = new String[] { "B", "KB", "MB", "GB", "TB" };
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));

        return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }
}