package com.hankkin.itround.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.hankkin.itround.R;
import com.hankkin.itround.bean.UserBean;
import com.hankkin.itround.core.person.PersonPresenter;
import com.hankkin.itround.core.person.PersonView;
import com.hankkin.itround.event.EventMap;
import com.hankkin.itround.manage.UserManager;
import com.hankkin.library.base.MvpActivity;
import com.hankkin.library.rx.RxBus;
import com.hankkin.library.utils.BitmapUtils;
import com.hankkin.library.utils.GlideUtils;
import com.hankkin.library.utils.ToastUtils;
import com.hankkin.library.view.RippleView;
import com.jakewharton.rxbinding2.view.RxView;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;

import java.util.List;

import butterknife.BindView;
import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.functions.Consumer;

public class PersonActivity extends MvpActivity<PersonView,PersonPresenter> implements PersonView,RippleView.OnRippleCompleteListener{

    public static final String NAME = "昵称";
    public static final String SEX = "性别";
    public static final String EMAIL = "邮箱";
    public static final String BLOG = "博客地址";
    public static final String DESC = "个性签名";
    public static final int REQUEST_CODE_CHOOSE = 1000;

    @BindView(R.id.iv_person) CircleImageView ivHeader;
    @BindView(R.id.tv_person_name) TextView tvName;
    @BindView(R.id.tv_person_name_set) TextView tvNameSet;
    @BindView(R.id.iv_person_sex) ImageView ivSex;
    @BindView(R.id.tv_person_stars) TextView tvStars;
    @BindView(R.id.tv_person_followers) TextView tvFollowers;
    @BindView(R.id.tv_person_desc) TextView tvDesc;
    @BindView(R.id.tv_person_desc_set) TextView tvDescSet;
    @BindView(R.id.tv_person_sex) TextView tvSexSet;
    @BindView(R.id.tv_person_blog) TextView tvBlog;
    @BindView(R.id.tv_person_email) TextView tvEmail;
    @BindView(R.id.rip_name) RippleView rpName;
    @BindView(R.id.rip_sex) RippleView rpSex;
    @BindView(R.id.rip_email) RippleView rpEmail;
    @BindView(R.id.rip_blog) RippleView rpBlog;
    @BindView(R.id.rip_desc) RippleView rpDesc;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_person;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        initToolBar(getResources().getString(R.string.person));
        rpName.setOnRippleCompleteListener(this);
        rpSex.setOnRippleCompleteListener(this);
        rpEmail.setOnRippleCompleteListener(this);
        rpBlog.setOnRippleCompleteListener(this);
        rpDesc.setOnRippleCompleteListener(this);

        rxBind();
    }

    @Override
    protected void initData() {
        UserBean userBean = UserManager.readUser();
        setViews(userBean);
    }

    private void rxBind(){
        RxView.clicks(ivHeader)
                .compose(new RxPermissions(activity).ensureEach(Manifest.permission.READ_EXTERNAL_STORAGE))
                .subscribe(new Consumer<Permission>() {
                    @Override
                    public void accept(Permission permission) throws Exception {
                        if (permission.granted){
                            Matisse.from(activity)
                                    .choose(MimeType.allOf())
                                    .countable(true)
                                    .maxSelectable(1)
                                    .gridExpectedSize(getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
                                    .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                                    .thumbnailScale(0.85f)
                                    .imageEngine(new GlideEngine())
                                    .forResult(REQUEST_CODE_CHOOSE);
                        }
                        else if (permission.shouldShowRequestPermissionRationale){
                            toast("Denied permission without ask never again");
                        }
                        else {
                            toast("权限被拒绝，无法启用存储功能");
                        }
                    }
                });
    }


    List<Uri> mSelected;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {
            showProgress();
            mSelected = Matisse.obtainResult(data);
            Bitmap bitmap = BitmapUtils.getBitmapFromUri(mSelected.get(0),activity);
            getPresenter().updateIcon(bitmap);
        }
    }

    /**
     * 赋值
     */
    private void setViews(UserBean userBean){
        if (userBean != null){
            tvName.setText(userBean.getName());
            tvNameSet.setText(userBean.getName());
            if (userBean.getSex() == UserBean.BOY){
                ivSex.setImageResource(R.mipmap.da_ren_list_boy);
                tvSexSet.setText(getResources().getString(R.string.boy));
            }
            else {
                ivSex.setImageResource(R.mipmap.da_ren_list_girl);
                tvSexSet.setText(getResources().getString(R.string.girl));
            }
            tvDesc.setText(userBean.getDesc());
            tvEmail.setText(userBean.getEmail());
            tvDescSet.setText(userBean.getDesc());
            tvBlog.setText(userBean.getBlog());
            if (!TextUtils.isEmpty(userBean.rerturnHeaderImageUrl())){
                GlideUtils.loadImageView(activity,userBean.rerturnHeaderImageUrl(),ivHeader);
            }
            tvFollowers.setText(String.valueOf(userBean.getFollowers()));
            tvStars.setText(String.valueOf(userBean.getStars()));
        }
    }

    @Override
    public PersonPresenter initPresenter() {
        return new PersonPresenter();
    }

    @Override
    public void toast(String msg) {
        hideProgress();
        ToastUtils.showShortToast(msg);
    }

    @Override
    public void onComplete(RippleView rippleView) {
        switch (rippleView.getId()){
            case R.id.rip_name:
                showInputDialog(NAME);
                break;
            case R.id.rip_sex:
                new MaterialDialog.Builder(this)
                        .title("请选择性别")
                        .items(R.array.sex)
                        .itemsCallbackSingleChoice(0, new MaterialDialog.ListCallbackSingleChoice() {
                            @Override
                            public boolean onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
                                showProgress();
                                getPresenter().updateUserHttp("sex",which);
                                return true;
                            }
                        }).positiveText("确定")
                        .show();
                break;
            case R.id.rip_email:
                showInputDialog(EMAIL);
                break;
            case R.id.rip_blog:
                showInputDialog(BLOG);
                break;
            case R.id.rip_desc:
                showInputDialog(DESC);
                break;
            default:
                break;
        }
    }

    /**
     * 显示输入框
     */
    private void showInputDialog(final String title){
        new MaterialDialog.Builder(this)
                .content("请输入"+title)
                .inputType(InputType.TYPE_CLASS_TEXT
                        | InputType.TYPE_TEXT_VARIATION_PERSON_NAME
                        | InputType.TYPE_TEXT_FLAG_CAP_WORDS)
                .inputRange(2,50)
                .positiveText("确定")
                .input("", "", false, new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                        showProgress();
                        String key = "";
                        if (NAME.equals(title)){
                            key = "name";
                        }
                        else if (EMAIL.equals(title)){
                            key = "email";
                        }
                        else if (BLOG.equals(title)){
                            key = "blog";
                        }
                        else if (DESC.equals(title)){
                            key = "desc";
                        }
                        getPresenter().updateUserHttp(key,String.valueOf(input));
                    }
                }).show();
    }

    @Override
    public void updateInfo(UserBean userBean) {
        if (userBean != null){
            setViews(userBean);
            RxBus.getDefault().post(new EventMap.UpdateUserEvent());
        }
        hideProgress();
    }
}
