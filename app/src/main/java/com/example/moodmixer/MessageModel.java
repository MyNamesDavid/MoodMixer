package com.example.moodmixer;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

public class MessageModel {

    void toast(String message, Context context) {

        Toast toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.TOP, 0, 120);
        toast.show();
    }
}
