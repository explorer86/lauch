package com.project.linh.lauch;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.project.linh.lauch.utils.PrefUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by linh on 17-3-17.
 */

public class AppSelectActivity extends Activity {
    ListView   mLvAppSelect;
    private final String TAG = "xmlh";
    private List<ResolveInfo> mApps;
    private List<ResolveInfo> mSelectApps;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_select);
        mLvAppSelect = (ListView)findViewById(R.id.lv_app_select);

        initData();
        mLvAppSelect.setAdapter(new AppSelectAdapter());
        mLvAppSelect.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String packageName = mApps.get(position).activityInfo.packageName;
                ToggleButton toggleButton = (ToggleButton)view.findViewById(R.id.tb_app_select);
                Log.d(TAG,"A toggleButton.isChecked()="+toggleButton.isChecked());
                toggleButton.setChecked(!toggleButton.isChecked());
                Log.d(TAG,"B toggleButton.isChecked()="+toggleButton.isChecked());
                PrefUtils.setBoolean(getApplication(),packageName,toggleButton.isChecked());
            }
        });


    }

    void initData(){
        loadApps();
    }

    private void loadApps() {
        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        mApps = getPackageManager().queryIntentActivities(intent, 0);
        mSelectApps = new ArrayList<ResolveInfo>();
        for(int i=0;i<mApps.size();i++){
           // Log.d(TAG,"packagenamge="+mApps.get(i).activityInfo.packageName);
            if (mApps.get(i).activityInfo.packageName.equals("com.android.camera")){
                mSelectApps.add(mApps.get(i));
            }
        }

    }

    class AppSelectAdapter extends BaseAdapter{



        @Override
        public int getCount() {
            return mApps.size();
        }

        @Override
        public ResolveInfo getItem(int position) {
            return mApps.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Log.d(TAG,"getView position="+position);
            ViewHolder viewHolder =null;
            ResolveInfo resolveInfo = null;
            boolean appState = false;
            if(convertView==null){
                viewHolder = new ViewHolder();
                convertView = View.inflate(getApplication(),R.layout.app_select_item,null);
                viewHolder.appIcon = (ImageView) convertView.findViewById(R.id.iv_app_icon);
                viewHolder.appPackageName = (TextView)convertView.findViewById(R.id.tv_package_name);
                viewHolder.appAppName = (TextView)convertView.findViewById(R.id.tv_app_name);
                viewHolder.appIconVisiable = (ToggleButton)convertView.findViewById(R.id.tb_app_select);
                convertView.setTag(viewHolder);
            }else{
                viewHolder = (ViewHolder)convertView.getTag();
            }
            resolveInfo = mApps.get(position);
            viewHolder.appPackageName.setText(resolveInfo.activityInfo.packageName);
            viewHolder.appAppName.setText(resolveInfo.activityInfo.applicationInfo.loadLabel(getPackageManager()).toString());
            viewHolder.appIcon.setImageDrawable(resolveInfo.activityInfo.loadIcon(getPackageManager()));
            appState = PrefUtils.getBoolean(getApplication(),resolveInfo.activityInfo.packageName,false);
            Log.d(TAG,"C toggleButton.isChecked()="+viewHolder.appIconVisiable.isChecked());
            viewHolder.appIconVisiable.setChecked(appState);

            return convertView;
        }
    }

    class ViewHolder{
        ImageView appIcon;
        TextView appPackageName;
        TextView appAppName;
        ToggleButton appIconVisiable;
    }
}
