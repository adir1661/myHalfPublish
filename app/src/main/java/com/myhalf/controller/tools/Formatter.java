package com.myhalf.controller.tools;

import android.support.annotation.NonNull;

/**
 * Created by Simhony on 03/04/2018.
 */

public class Formatter {
    public static boolean isEmailValid(@NonNull String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
    public static boolean isPasswordValid(@NonNull String password) {
        if (password.length()>=6)
            return true;
        return false;
    }
    }
