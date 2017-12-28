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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.SaveCallback;
import com.hankkin.itround.R;
import com.hankkin.itround.bean.UserBean;
import com.hankkin.itround.callback.QueryFollowCallBack;
import com.hankkin.itround.chat.AddRequestManager;
import com.hankkin.itround.chat.FriendsManager;
import com.hankkin.itround.chat.UserCacheUtils;
import com.hankkin.itround.core.person.PersonPresenter;
import com.hankkin.itround.core.person.PersonView;
import com.hankkin.itround.event.EventMap;
import com.hankkin.itround.manage.UserManager;
import com.hankkin.library.base.MvpActivity;
import com.hankkin.library.rx.RxBus;
import com.hankkin.library.utils.BitmapUtils;
import com.hankkin.library.utils.FileUtils;
import com.hankkin.library.utils.GlideUtils;
import com.hankkin.library.utils.LogUtils;
import com.hankkin.library.utils.ToastUtils;
import com.jakewharton.rxbinding2.view.RxView;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.leancloud.chatkit.activity.LCIMConversationActivity;
import cn.leancloud.chatkit.utils.LCIMConstants;
import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.functions.Consumer;

public class PersonActivity extends MvpActivity<PersonView, PersonPresenter> implements PersonView, View.OnClickListener {

    public static final String NAME = "昵称";
    public static final String SEX = "性别";
    public static final String EMAIL = "邮箱";
    public static final String BLOG = "博客地址";
    public static final String DESC = "个性签名";
    public static final int REQUEST_CODE_CHOOSE = 1000;
    public static final String CURRENT = "current";

    @BindView(R.id.iv_person)
    CircleImageView ivHeader;
    @BindView(R.id.tv_person_name)
    TextView tvName;
    @BindView(R.id.tv_person_name_set)
    TextView tvNameSet;
    @BindView(R.id.iv_person_sex)
    ImageView ivSex;
    @BindView(R.id.tv_person_stars)
    TextView tvStars;
    @BindView(R.id.tv_person_followers)
    TextView tvFollowers;
    @BindView(R.id.tv_person_desc)
    TextView tvDesc;
    @BindView(R.id.tv_person_desc_set)
    TextView tvDescSet;
    @BindView(R.id.tv_person_sex)
    TextView tvSexSet;
    @BindView(R.id.tv_person_blog)
    TextView tvBlog;
    @BindView(R.id.tv_person_email)
    TextView tvEmail;
    @BindView(R.id.rip_name)
    RelativeLayout rpName;
    @BindView(R.id.rip_sex)
    RelativeLayout rpSex;
    @BindView(R.id.rip_email)
    RelativeLayout rpEmail;
    @BindView(R.id.rip_blog)
    RelativeLayout rpBlog;
    @BindView(R.id.rip_desc)
    RelativeLayout rpDesc;
    @BindView(R.id.btn_person_add_friend)
    Button btnAddFriend;


    private String filePath;
    private UserBean userBean;
    private UserBean currentUser;
    private boolean isCurrent;
    private String uid;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_person;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        initToolBar(getResources().getString(R.string.person));
        rpName.setOnClickListener(this);
        rpSex.setOnClickListener(this);
        rpEmail.setOnClickListener(this);
        rpBlog.setOnClickListener(this);
        rpDesc.setOnClickListener(this);
        btnAddFriend.setOnClickListener(this);


        showProgress();
        isCurrent = getIntent().getBooleanExtra(CURRENT,false);
        userBean = (UserBean) getIntent().getSerializableExtra("user");
        uid = getIntent().getStringExtra("id");
        currentUser = UserBean.getCurrentUser();
        LogUtils.e(getClass().getName(),currentUser.getFollowers()+"");

    }

    @Override
    protected void initData() {
        if (!isCurrent){
            showProgress();
            userBean = UserCacheUtils.getCachedUser(uid);
            UserManager.getUserFollow(userBean, new QueryFollowCallBack() {
                @Override
                public void querySuc(UserBean userBean1) {
                    userBean = userBean1;
                    setViews(userBean);
                }

                @Override
                public void quetFail(String msg) {
                    hideProgress();
                }
            });
        }
        else {
            setViews(currentUser);
        }
        rxBind();
    }

    private void rxBind() {
        if (!isCurrent) {
            return;
        }
        RxView.clicks(ivHeader)
                .compose(new RxPermissions(activity).ensureEach(Manifest.permission.READ_EXTERNAL_STORAGE))
                .subscribe(new Consumer<Permission>() {
                    @Override
                    public void accept(Permission permission) throws Exception {
                        if (permission.granted) {
                            Matisse.from(activity)
                                    .choose(MimeType.allOf())
                                    .countable(true)
                                    .maxSelectable(1)
                                    .gridExpectedSize(getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
                                    .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                                    .thumbnailScale(0.85f)
                                    .imageEngine(new GlideEngine())
                                    .forResult(REQUEST_CODE_CHOOSE);
                        } else if (permission.shouldShowRequestPermissionRationale) {
                            toast("Denied permission without ask never again");
                        } else {
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
            filePath = FileUtils.getRealFilePath(activity, mSelected.get(0));
            final UserBean userBean = UserBean.getCurrentUser();
            userBean.saveAvatar(filePath, new SaveCallback() {
                @Override
                public void done(AVException e) {
                    filterException(e);
                    setViews(userBean);
                    RxBus.getDefault().post(new EventMap.UpdateUserEvent());
                }
            });
        }
    }

    /**
     * 赋值
     */
    private void setViews(final UserBean userBean) {
        if (userBean != null) {
            tvName.setText(userBean.getName());
            tvNameSet.setText(userBean.getName());
            if (userBean.getSex() == UserBean.BOY) {
                ivSex.setImageResource(R.mipmap.da_ren_list_boy);
                tvSexSet.setText(getResources().getString(R.string.boy));
            } else {
                ivSex.setImageResource(R.mipmap.da_ren_list_girl);
                tvSexSet.setText(getResources().getString(R.string.girl));
            }
            tvDesc.setText(userBean.getDesc());
            tvEmail.setText(userBean.getEmail());
            tvDescSet.setText(userBean.getDesc());
            tvBlog.setText(userBean.getBlog());
            if (!TextUtils.isEmpty(userBean.getAvatarUrl())) {
                GlideUtils.loadImageView(activity, userBean.getAvatarUrl(), ivHeader);
            }
            tvFollowers.setText(String.valueOf(userBean.getFollowers()));
            tvStars.setText(String.valueOf(userBean.getStars()));
        }


        if (isCurrent) {
            btnAddFriend.setVisibility(View.GONE);
            hideProgress();
        } else {
            btnAddFriend.setVisibility(View.VISIBLE);
            currentUser = UserCacheUtils.getCachedUser(userBean.getObjectId());
            List<String> friendIds = new ArrayList<String>(FriendsManager.getFriendIds());
            final UserBean user = UserBean.getCurrentUser();
            friendIds.add(user.getObjectId());
            if (friendIds.contains(userBean.getObjectId())) {
                btnAddFriend.setText(getResources().getString(R.string.send_sms));
            } else {
                btnAddFriend.setText(getResources().getString(R.string.cirle_add_friend));
            }
        }
        hideProgress();

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

    /**
     * 显示输入框
     */
    private void showInputDialog(final String title) {
        if (!isCurrent) {
            return;
        }
        new MaterialDialog.Builder(this)
                .content("请输入" + title)
                .inputType(InputType.TYPE_CLASS_TEXT
                        | InputType.TYPE_TEXT_VARIATION_PERSON_NAME
                        | InputType.TYPE_TEXT_FLAG_CAP_WORDS)
                .inputRange(2, 50)
                .positiveText("确定")
                .input("", "", false, new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                        showProgress();
                        String key = "";
                        if (NAME.equals(title)) {
                            key = "name";
                        } else if (EMAIL.equals(title)) {
                            key = "email";
                        } else if (BLOG.equals(title)) {
                            key = "blog";
                        } else if (DESC.equals(title)) {
                            key = "desc";
                        }
                        getPresenter().updateUserHttp(key, String.valueOf(input));
                    }
                }).show();
    }

    @Override
    public void updateInfo(UserBean userBean) {
        if (userBean != null) {
            if (!TextUtils.isEmpty(filePath)){
                final UserBean leanchatUser = UserBean.getCurrentUser();
                leanchatUser.saveAvatar(userBean.getAvatarUrl(), null);
            }

            setViews(userBean);
            RxBus.getDefault().post(new EventMap.UpdateUserEvent());
        }
        hideProgress();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rip_name:
                showInputDialog(NAME);
                break;
            case R.id.rip_sex:
                if (!isCurrent) {
                    return;
                }
                new MaterialDialog.Builder(this)
                        .title("请选择性别")
                        .items(R.array.sex)
                        .itemsCallbackSingleChoice(0, new MaterialDialog.ListCallbackSingleChoice() {
                            @Override
                            public boolean onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
                                showProgress();
                                getPresenter().updateUserHttp("sex", which);
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
            case R.id.btn_person_add_friend:
                if (getResources().getString(R.string.cirle_add_friend).equals(btnAddFriend.getText().toString())) {
                    AddRequestManager.getInstance().createAddRequestInBackground(this, currentUser);
                } else {
                    Intent intent = new Intent(activity, LCIMConversationActivity.class);
                    intent.putExtra(LCIMConstants.PEER_ID, userBean.getObjectId());
                    intent.putExtra(LCIMConstants.PEER_NAME, userBean.getName());
                    activity.startActivity(intent);
                    finish();
                }
                break;
            default:
                break;
        }
    }
}
