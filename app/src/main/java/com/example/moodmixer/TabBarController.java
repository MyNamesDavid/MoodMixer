package com.example.moodmixer;

import android.content.Intent;
import android.content.Context;

import static androidx.core.content.ContextCompat.startActivity;

final public class TabBarController {

    private Context context;

    public TabBarController(Context context) {
        this.context = context;
    }


    public void openMusicPlayerActivity() {

//        Intent intent = new Intent(context, MusicPlayerActivity.class);
//        context.startActivity(intent);
    }



    public void openUserProfileActivity() {

        Intent intent = new Intent(context, UserProfileActivity.class);
        context.startActivity(intent);
    }


}
