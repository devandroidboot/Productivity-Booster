package com.codegama.productivity_booster.activity;

import android.app.Activity;
import android.app.ActivityGroup;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;

import java.util.ArrayList;

public class TabGroupActivity extends ActivityGroup {
    private ArrayList<String> mIdList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mIdList = new ArrayList<String>();
    }

    public void startChildActivity(String id, Intent intent) {
        Window window = getLocalActivityManager().startActivity(id, intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        if (window != null) {
            mIdList.add(id);
            setContentView(window.getDecorView());
        }
    }

    @Override
    public void onBackPressed() {
        int length = mIdList.size();
        if (length > 1) {
            Activity current = getLocalActivityManager().getActivity(mIdList.get(length-1));
            current.finish();
        }
    }
}
