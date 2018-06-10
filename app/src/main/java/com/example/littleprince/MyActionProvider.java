package com.example.littleprince;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.support.v4.view.ActionProvider;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;

import com.example.littleprince.ImageList.ListActivity;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;


public class MyActionProvider extends ActionProvider {

    public MyActionProvider(Context context) {
        super(context);
    }

    @Override
    public View onCreateActionView() {
        return null;
    }

    @Override
    public void onPrepareSubMenu(SubMenu subMenu) {
        subMenu.clear();

        subMenu.add("云相册").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                ListActivity.getListContext().refreshcloud();
                return false;
            }
        });


        Cursor cur = getContext().getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Images.Media.BUCKET_DISPLAY_NAME},null,null,
                MediaStore.Images.Media.DATE_MODIFIED + " DESC");

        List<String> buckets=new ArrayList<>();

        if (cur != null) {
            if (cur.moveToFirst()) {
                while (!cur.isAfterLast()) {
                    buckets.add(cur.getString(0));
                    cur.moveToNext();
                }
            }
            cur.close();
        }
        LinkedHashSet<String> tempset=new LinkedHashSet<String>(buckets.size());
        tempset.addAll(buckets);
        buckets.clear();
        buckets.addAll(tempset);
        for(final String b:buckets){
            subMenu.add(b).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    ListActivity.getListContext().refresh(b);
                    return false;
                }
            });
        }

        super.onPrepareSubMenu(subMenu);
    }

    @Override
    public boolean hasSubMenu() {
        return true;
    }
}
