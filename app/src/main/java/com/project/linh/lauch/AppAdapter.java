package com.project.linh.lauch;


import android.content.Context;
import android.content.pm.ResolveInfo;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import java.util.List;

/**
 * Created by linh on 17-3-16.
 */

public class AppAdapter extends BaseAdapter {
    List<ResolveInfo> mApps;
    Context context;

    public AppAdapter(Context context, List<ResolveInfo> apps) {
        super();
        this.context = context;
        this.mApps = apps;
    }

    @Override
    public int getCount() {
        return mApps.size();
    }

    @Override
    public Object getItem(int position) {
        return mApps.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView iv;
        if (convertView == null) {
            iv = new ImageView(context);
            iv.setScaleType(ScaleType.FIT_CENTER);
            iv.setLayoutParams(new GridView.LayoutParams(200,200));
        } else {
            iv = (ImageView) convertView;
        }
        ResolveInfo info = mApps.get(position);
        iv.setImageDrawable(info.activityInfo.loadIcon(context.getPackageManager()));
        return iv;
    }

}