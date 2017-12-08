package com.hankkin.itround;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.GetCallback;
import com.avos.avoscloud.SaveCallback;
import com.hankkin.itround.manage.UserManager;
import com.hankkin.itround.ui.fg.CircleFragment;
import com.hankkin.itround.ui.fg.FindFragment;
import com.hankkin.itround.ui.fg.HomeFragment;
import com.hankkin.itround.ui.fg.MeFragment;
import com.hankkin.itround.utils.BottomNavigationViewHelper;
import com.hankkin.library.utils.ScreenUtils;
import com.hankkin.library.utils.StatusBarUtil;
import com.hankkin.library.utils.ToastUtils;
import com.hankkin.library.view.NoScrollViewPager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    private TextView mTextMessage;
    @BindView(R.id.vp) NoScrollViewPager vp;

    private HomeFragment homeFragment;
    private FindFragment findFragment;
    private CircleFragment circleFragment;
    private MeFragment meFragment;

    private boolean clickMe = false;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    if (clickMe){
                        vp.setPadding(0, 0, 0, 0);
                        resetFragmentView(homeFragment);
                        StatusBarUtil.setColor(MainActivity.this, getResources().getColor(R.color.colorPrimary), 0);
                    }
                    vp.setCurrentItem(0);
                    return true;
                case R.id.navigation_dashboard:
                    clickMe = true;

                    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
                        vp.setPadding(0, 0, 0, 0);
                    }
                    StatusBarUtil.setTransparentForImageViewInFragment(MainActivity.this, null);
                    vp.setCurrentItem(1);
                    return true;
                case R.id.navigation_circle:
                    if (clickMe){
                        vp.setPadding(0, 0, 0, 0);
//                        resetFragmentView(circleFragment);
                        StatusBarUtil.setColor(MainActivity.this, getResources().getColor(R.color.colorPrimary), 0);
                    }
                    vp.setCurrentItem(2);
                    return true;
                case R.id.navigation_notifications:
                    if (clickMe){
                        vp.setPadding(0, 0, 0, 0);
                        resetFragmentView(meFragment);
                        StatusBarUtil.setColor(MainActivity.this, getResources().getColor(R.color.colorPrimary), 0);
                    }
                    vp.setCurrentItem(3);

                    return true;
                    default:
                        break;

            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        BottomNavigationViewHelper.disableShiftMode(navigation);

        homeFragment = HomeFragment.newInstance(0);
        findFragment = FindFragment.newInstance(1);
        circleFragment = CircleFragment.newInstance(2);
        meFragment = MeFragment.newInstance(3);
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(homeFragment);
        fragments.add(findFragment);
        fragments.add(circleFragment);
        fragments.add(meFragment);
        vp.setAdapter(new MainFragmentAdapter(getSupportFragmentManager(),fragments));
        vp.setOffscreenPageLimit(4);

        clickMe = true;
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            vp.setPadding(0, 0, 0, 0);
        }
        StatusBarUtil.setTransparentForImageViewInFragment(MainActivity.this, null);
        StatusBarUtil.setColor(this, getResources().getColor(R.color.colorPrimary), 0);
        vp.setCurrentItem(0);


        vp.setPadding(0, StatusBarUtil.getStatusBarHeight(this), 0, 0);

        new Thread(new Runnable() {
            @Override
            public void run() {
                AVQuery<AVUser> query = new AVQuery<AVUser>("_User");
                query.getInBackground(UserManager.getUid(), new GetCallback<AVUser>() {
                    @Override
                    public void done(AVUser user, AVException e) {
                        if (e == null){
                            UserManager.saveUser(user);
                        }
                        else {
                            ToastUtils.showShortToast("请登录");
                        }
                    }
                });
            }
        }).start();


//        mTextMessage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                final int count = 10;
//                Observable.interval(0, 1, TimeUnit.SECONDS)
//                        .take(count + 1)
//                        .map(new Function<Long, Long>() {
//                            @Override
//                            public Long apply(@io.reactivex.annotations.NonNull Long aLong) throws Exception {
//                                return count - aLong;
//                            }
//                        })
//                        .observeOn(AndroidSchedulers.mainThread())
//                        .subscribe(new Observer<Long>() {
//                            @Override
//                            public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {
//
//                            }
//
//                            @Override
//                            public void onNext(@io.reactivex.annotations.NonNull Long aLong) {
//                                mTextMessage.setText("剩余" + aLong + "秒");
//                            }
//
//                            @Override
//                            public void onError(@io.reactivex.annotations.NonNull Throwable e) {
//
//                            }
//
//                            @Override
//                            public void onComplete() {
//                                mTextMessage.setText("发送验证码");
//                            }
//                        });
//                ;
//            }
//        });


        // 测试 SDK 是否正常工作的代码
        AVObject testObject = new AVObject("TestObject");
        testObject.put("words","Hello World!");
        testObject.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                if(e == null){
                    Log.d("saved","success!");
                }
            }
        });
    }



    public class MainFragmentAdapter extends FragmentPagerAdapter {
        private List<Fragment> mFragments;

        public MainFragmentAdapter(FragmentManager fm, List<Fragment> fragments) {
            super(fm);
            this.mFragments = fragments;
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }


    }



    public void resetFragmentView(Fragment fragment) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            View contentView = findViewById(android.R.id.content);
            if (contentView != null) {
                ViewGroup rootView;
                rootView = (ViewGroup) ((ViewGroup) contentView).getChildAt(0);
                if (rootView.getPaddingTop() != 0) {
                    rootView.setPadding(0, 0, 0, 0);
                }
            }
            if (fragment != null) {
                if (fragment.getView() != null) {
                    fragment.getView().setPadding(0, ScreenUtils.getStatusBarHeight(this), 0, 0);
                }
            }
        }
    }

}
