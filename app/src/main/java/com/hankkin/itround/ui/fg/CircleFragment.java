package com.hankkin.itround.ui.fg;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.hankkin.itround.R;
import com.hankkin.library.base.BaseFragment;
import com.mobike.library.MobikeView;

import butterknife.BindView;

/**
 * Created by hankkin on 2017/10/12.
 * Blog: http://hankkin.cn
 * Mail: 1019283569@qq.com
 */

public class CircleFragment extends BaseFragment{

    @BindView(R.id.mobike_view) MobikeView mobikeView;

    private SensorManager sensorManager;
    private Sensor sensor;

    //将图片添加进数组
    private int[] images = {R.mipmap.mb_android,R.mipmap.mb_chrome,
            R.mipmap.mb_ios,R.mipmap.ic_launcher,
            R.mipmap.mb_ios1,R.mipmap.mb_google,
            R.mipmap.mb_qq,
            };

    public static CircleFragment newInstance(int index) {
        CircleFragment fragment = new CircleFragment();
        Bundle args = new Bundle();
        args.putInt("index", index);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initViewsAndEvents(View view) {
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER;
        for (int i = 0; i < images.length; i++) {
            ImageView imageView = new ImageView(activity);
            imageView.setImageResource(images[i]);
            imageView.setTag(R.id.mobike_view_circle_tag,true);
            mobikeView.addView(imageView,layoutParams);
        }
        sensorManager = (SensorManager) activity.getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_circle;
    }

    @Override
    protected void initData() {

    }


    @Override
    public void onStart() {
        super.onStart();
        mobikeView.getmMobike().onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        mobikeView.getmMobike().onStop();
    }

    @Override
    public void onResume() {
        super.onResume();
        sensorManager.registerListener(listener,sensor,SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    public void onPause() {
        super.onPause();
        sensorManager.unregisterListener(listener);
    }

    private SensorEventListener listener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            //Sensor.TYPE_ACCELEROMETER:三轴加速度感应器 返回三个坐标轴的加速度  单位m/s2
            if (sensorEvent.sensor.getType()==Sensor.TYPE_ACCELEROMETER){
                float x = sensorEvent.values[0]*5.0f;
                float y = sensorEvent.values[1]*5.0f;
                mobikeView.getmMobike().onSensorChanged(-x,y);
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    };
}
