package com.hankkin.itround.ui.circle;

import android.Manifest;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.avos.avoscloud.AVFile;
import com.hankkin.itround.R;
import com.hankkin.itround.adapter.PhotoGridAdapter;
import com.hankkin.itround.bean.FinsNewsBean;
import com.hankkin.itround.bean.UserBean;
import com.hankkin.itround.core.find.AddNewsPresenter;
import com.hankkin.itround.core.find.AddNewsView;
import com.hankkin.itround.utils.ImgUploadUtils;
import com.hankkin.library.base.MvpActivity;
import com.hankkin.library.utils.FileUtils;
import com.hankkin.library.utils.ToastUtils;
import com.hankkin.library.view.NoScrollGridView;
import com.jakewharton.rxbinding2.view.RxView;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import butterknife.BindView;
import io.reactivex.functions.Consumer;

import static com.hankkin.itround.ui.PersonActivity.REQUEST_CODE_CHOOSE;

public class AddNewsActivity extends MvpActivity<AddNewsView,AddNewsPresenter> implements View.OnClickListener,AddNewsView {

    @BindView(R.id.gv_img)
    NoScrollGridView ngvImg;
    @BindView(R.id.et_add_news)
    EditText etContent;
    @BindView(R.id.ll_add_news_camera)
    LinearLayout llCamera;
    @BindView(R.id.tv_add_news_content_limit)
    TextView tvLimit;
    @BindView(R.id.ll_add_news_tag)
    LinearLayout llTag;
    @BindView(R.id.tv_add_news_tag)
    TextView tvTag;


    private PhotoGridAdapter photoGridAdapter;
    private List<String> urls = new ArrayList<>();
    private List<Uri> mSelected = new ArrayList<>();
    private List<AVFile> fileList = new ArrayList<>();

    @Override
    protected int getLayoutId() {
        return R.layout.activity_add_news;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        initToolBarRightTxt(getResources().getString(R.string.circle_add_news_title),
                getResources().getString(R.string.ok), new OnRightClickListener() {
                    @Override
                    public void rightClick() {
                        uploadNews();
                    }
                });
    }

    @Override
    protected void initData() {

        llTag.setOnClickListener(this);
        photoGridAdapter = new PhotoGridAdapter(urls, activity);
        ngvImg.setAdapter(photoGridAdapter);


        RxView.clicks(llCamera)
                .compose(new RxPermissions(activity).ensureEach(Manifest.permission.READ_EXTERNAL_STORAGE))
                .subscribe(new Consumer<Permission>() {
                    @Override
                    public void accept(Permission permission) throws Exception {
                        if (permission.granted) {
                            Matisse.from(activity)
                                    .choose(MimeType.allOf())
                                    .countable(true)
                                    .maxSelectable(9)
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

        etContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 500) {
                    toast(getResources().getString(R.string.circle_add_news_content_limit));
                    return;
                }
                tvLimit.setText(charSequence.length() + "/500");
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {
            mSelected = Matisse.obtainResult(data);
            urls.clear();
            for (Uri uri : mSelected) {
                urls.add(FileUtils.getRealFilePath(activity, uri));
                try {
                    fileList.add(AVFile.withAbsoluteLocalPath(UUID.randomUUID()+".jpg",FileUtils.getRealFilePath(activity,uri)));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
            photoGridAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void toast(String msg) {
        ToastUtils.showShortToast(msg);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_add_news_tag:
                selectTag();
                break;
            default:
                break;
        }
    }

    private void selectTag() {
        final String[] arrays = getResources().getStringArray(R.array.addNewsTags);
        new MaterialDialog.Builder(activity)
                .items(R.array.addNewsTags)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(final MaterialDialog dialog, View itemView, int which, CharSequence text) {
                        tvTag.setText(arrays[which]);
                    }
                }).show();
    }

    private void uploadNews(){
        if (TextUtils.isEmpty(etContent.getText().toString())){
            toast(getResources().getString(R.string.cirle_add_news_content_not_empty));
            return;
        }
        if (TextUtils.isEmpty(tvTag.getText().toString())){
            toast(getResources().getString(R.string.circle_add_news_select_tag_hint));
            return;
        }
        showProgress();
        final FinsNewsBean finsNewsBean = new FinsNewsBean();
        finsNewsBean.setContent(etContent.getText().toString());
        finsNewsBean.setOwner(UserBean.getCurrentUser());
        finsNewsBean.setTag(tvTag.getText().toString());
        ImgUploadUtils.getInstance().uploadImg(fileList, new ImgUploadUtils.UploadImgCallBack() {
            @Override
            public void uploadSuc(List<AVFile> files) {
                finsNewsBean.setUrlsList(fileList);
                getPresenter().addNewsHttp(finsNewsBean);
            }
        });
    }

    @Override
    public AddNewsPresenter initPresenter() {
        return new AddNewsPresenter();
    }

    @Override
    public void addNewsSuc() {
        hideProgress();
        toast(getResources().getString(R.string.toast_add_news_suc));
        finish();
    }

    @Override
    public void addNewsFail(String msg) {
        toast(msg);
        hideProgress();
    }
}
