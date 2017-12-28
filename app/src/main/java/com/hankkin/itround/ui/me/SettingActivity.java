package com.hankkin.itround.ui.me;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.hankkin.itround.R;
import com.hankkin.itround.manage.UserManager;
import com.hankkin.itround.ui.LoginActivity;
import com.hankkin.library.base.BaseAcitvity;
import com.hankkin.library.utils.ToastUtils;

import butterknife.BindView;

public class SettingActivity extends BaseAcitvity implements View.OnClickListener{

    @BindView(R.id.btn_set_logout)
    Button btnLogout;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_setting;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        initToolBar(getResources().getString(R.string.setting));
    }

    @Override
    protected void initData() {
        btnLogout.setOnClickListener(this);
    }

    @Override
    public void toast(String msg) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_set_logout:
                new MaterialDialog.Builder(activity)
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                logout();
                            }
                        })
                        .content(getResources().getString(R.string.alter_logout_content))
                        .positiveText(R.string.ok)
                        .negativeText(R.string.cancel)
                        .show();
                break;
                default:
                    break;
        }
    }

    private void logout(){
        UserManager.logoutUser();
        gotoActivity(LoginActivity.class);
        ToastUtils.showShortToast(getResources().getString(R.string.logout_hint));
    }
}
