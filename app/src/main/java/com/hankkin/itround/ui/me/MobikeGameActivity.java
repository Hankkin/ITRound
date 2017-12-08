package com.hankkin.itround.ui.me;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.hankkin.itround.R;
import com.hankkin.library.base.BaseAcitvity;
import com.mobike.library.MobikeView;

import butterknife.BindView;

public class MobikeGameActivity extends BaseAcitvity {

    @BindView(R.id.mobike_view)
    MobikeView mobikeView;

    private SensorManager sensorManager;
    private Sensor sensor;

    //将图片添加进数组
    private int[] images = {R.mipmap.mb1,R.mipmap.mb2,
            R.mipmap.mb3,R.mipmap.mb4,
            R.mipmap.mb5,R.mipmap.mb6,
            R.mipmap.mb7,R.mipmap.mb8
    };


    @Override
    protected int getLayoutId() {
        return R.layout.activity_mobike_game;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        initToolBar("玩玩");
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
    protected void initData() {
    }

    @Override
    public void toast(String msg) {

    }


    @Override
    protected void onStart() {
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
