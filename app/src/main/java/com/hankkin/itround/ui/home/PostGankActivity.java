package com.hankkin.itround.ui.home;

import android.content.ClipboardManager;
import android.content.Context;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.afollestad.materialdialogs.MaterialDialog;
import com.hankkin.itround.R;
import com.hankkin.itround.bean.PostGankBean;
import com.hankkin.itround.core.home.PostGankPresenter;
import com.hankkin.itround.core.home.PostGankView;
import com.hankkin.itround.ui.fg.HomeFragment;
import com.hankkin.library.base.MvpActivity;
import com.hankkin.library.utils.ToastUtils;

import butterknife.BindView;
import butterknife.OnClick;

public class PostGankActivity extends MvpActivity<PostGankView, PostGankPresenter> implements PostGankView {

    @BindView(R.id.tv_post_type)
    EditText etType;
    @BindView(R.id.et_gank_url)
    EditText etUrl;
    @BindView(R.id.et_gank_desc)
    EditText etDesc;
    @BindView(R.id.et_gank_who)
    EditText etWho;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_post_gank;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        initToolBarRightTxt(getResources().getString(R.string.post_gank),
                getResources().getString(R.string.post_gank_submit), new OnRightClickListener() {
                    @Override
                    public void rightClick() {
                        postClick();
                    }
                });
    }

    @Override
    protected void initData() {
        etUrl.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    ClipboardManager cmb = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    if (cmb != null) {
                        if (cmb.getText() != null) {
                            if (!TextUtils.isEmpty(cmb.getText().toString())) {
                                etUrl.setText(cmb.getText().toString());
                                cmb.setText("");
                            }
                        }
                    }

                }
            }
        });
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            ClipboardManager cmb = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            if (cmb != null) {
                if (cmb.getText() != null) {
                    if (!TextUtils.isEmpty(cmb.getText().toString())) {
                        etUrl.setText(cmb.getText().toString());
                    }
                }
            }
        }
    }

    @Override
    public PostGankPresenter initPresenter() {
        return new PostGankPresenter();
    }

    private void postClick() {
        String url = etUrl.getText().toString();
        String who = etWho.getText().toString();
        String type = etType.getText().toString();
        String desc = etDesc.getText().toString();
        if (TextUtils.isEmpty(url)) {
            ToastUtils.showShortToast(R.string.post_gank_url);
            return;
        }
        if (TextUtils.isEmpty(who)) {
            ToastUtils.showShortToast(R.string.post_gank_who);
            return;
        }
        if (TextUtils.isEmpty(type)) {
            ToastUtils.showShortToast(R.string.post_gank_type);
            return;
        }
        if (TextUtils.isEmpty(desc)) {
            ToastUtils.showShortToast(R.string.post_gank_desc);
            return;
        }
        showProgress();
        PostGankBean bean = new PostGankBean();
        bean.setDesc(desc);
        bean.setType(type);
        bean.setUrl(url);
        bean.setWho(who);
        getPresenter().postGankHttp(bean);
    }

    @Override
    public void toast(String msg) {
        hideProgress();
        ToastUtils.showShortToast(msg);
    }

    @Override
    public void postSuc(String msg) {
        ToastUtils.showShortToast(msg);
        hideProgress();
        finish();
    }

    @OnClick(R.id.tv_post_type)
    void selectType() {
        new MaterialDialog.Builder(activity)
                .items(HomeFragment.HOME_TAG)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(final MaterialDialog dialog, View itemView, int which, CharSequence text) {
                        etType.setText(HomeFragment.HOME_TAG[which]);
                    }
                }).show();
    }
}
