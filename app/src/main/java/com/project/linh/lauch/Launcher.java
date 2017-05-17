package com.project.linh.lauch;

import android.app.Activity;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.view.GestureDetectorCompat;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.project.linh.lauch.utils.PrefUtils;

/**
 * Created by linh on 17-3-16.
 */
public class Launcher extends Activity implements View.OnClickListener,View.OnKeyListener{
    private final String TAG="xmlh";
    private GridView mGridView;
    private List<ResolveInfo> mApps;
    private List<ResolveInfo> mSelectApps;
    private Button mBtSetting;
    private Button mBtWallPaper;
    private AppAdapter appAdapter;
    private GestureDetectorCompat gestureDetector;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initData();
        initView();




    }

    private void initData() {
        mSelectApps = new ArrayList<ResolveInfo>();
        loadApps();
    }

    private void initView() {
        mBtSetting = (Button) findViewById(R.id.bt_setting);
        mBtWallPaper = (Button) findViewById(R.id.bt_wallpaper);
        mGridView = (GridView) findViewById(R.id.gridView1);
        appAdapter = new AppAdapter(this, mSelectApps);
        mGridView.setAdapter(appAdapter);
        mBtSetting.setOnClickListener(this);
        mBtWallPaper.setOnClickListener(this);


        mGridView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                ResolveInfo info = mSelectApps.get(position);
                // 应用的包名
                String pkg = info.activityInfo.packageName;
                // 应用的主Activity
                String cls = info.activityInfo.name;
                ComponentName componentName = new ComponentName(pkg, cls);
                Intent intent = new Intent();
                intent.setComponent(componentName);
                startActivity(intent);
            }
        });

        gestureDetector = new GestureDetectorCompat(this, new GestureDetector.OnGestureListener() {
            @Override
            public boolean onDown(MotionEvent e) {
                Log.d(TAG,"GestureDetectorCompat onDown");
                return false;
            }

            @Override
            public void onShowPress(MotionEvent e) {
                Log.d(TAG,"GestureDetectorCompat onShowPress");
            }

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                Log.d(TAG,"GestureDetectorCompat onSingleTapUp");
                return false;
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                Log.d(TAG,"GestureDetectorCompat onScroll");
                return false;
            }

            @Override
            public void onLongPress(MotionEvent e) {
                Log.d(TAG,"GestureDetectorCompat onLongPress");
                Intent intent = new Intent(getApplicationContext(),AppSelectActivity.class);
                startActivity(intent);
            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                Log.d(TAG,"GestureDetectorCompat onFling");
                return false;
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.d(TAG,"onTouchEvent");
        gestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    /**
     * 加载app
     */
    private void loadApps() {
        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        mApps = getPackageManager().queryIntentActivities(intent, 0);
        mSelectApps.clear();
        for(int i=0;i<mApps.size();i++){
           // Log.d(TAG,"packagenamge="+mApps.get(i).activityInfo.packageName);
            String packageName = mApps.get(i).activityInfo.packageName;
            boolean isDisplay = PrefUtils.getBoolean(getApplication(),packageName,false);
           if (isDisplay){
               Log.d(TAG,"add packagenamge="+mApps.get(i).activityInfo.packageName);
               mSelectApps.add(mApps.get(i));
           }
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG,"onResume");
        loadApps();
        appAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()){
            case(R.id.bt_setting):
                intent = new Intent(this,AppSelectActivity.class);
                startActivity(intent);
                    break;
            case(R.id.bt_wallpaper):
                 intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent,200);
                break;
            }
        }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Bitmap bitmap;
        switch (requestCode){
            case 200:
                Log.d(TAG,"resultCOde="+resultCode);
                Uri uri = data.getData();
                Log.d(TAG,"uri="+uri);
                try {
                     bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),uri);
                    BitmapFactory.
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Toast.makeText(Launcher.this, "相册回调", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        Log.d(TAG,"A onKey keycode="+keyCode);
        switch (keyCode){

            case KeyEvent.KEYCODE_BACK:
                Log.d(TAG,"A onKey keycode="+keyCode);
                return true;
        }
        return false;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.d(TAG,"A onKeyDown keycode="+keyCode);
        Log.d(TAG,"A onKeyDown KEYCODE_BACK="+KeyEvent.KEYCODE_BACK);
        switch (keyCode){
            case KeyEvent.KEYCODE_BACK:
                Log.d(TAG," onKeyDown keycode="+keyCode);
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}