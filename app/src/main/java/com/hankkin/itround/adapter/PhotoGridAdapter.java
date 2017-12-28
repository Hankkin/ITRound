package com.hankkin.itround.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.hankkin.itround.R;
import com.hankkin.library.utils.GlideUtils;
import com.hankkin.library.utils.ScreenUtils;
import com.hankkin.library.utils.SizeUtils;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class PhotoGridAdapter extends BaseAdapter {
    public static final String FLAG = "flag";
    private List<String> pathList;

    private Context context;
    private LayoutInflater inflate;
    private int mWidth;


    public PhotoGridAdapter(List<String> listUrls, Context context) {
        this.pathList = listUrls;
        this.context = context;
        inflate = LayoutInflater.from(context);

        int screenWidth = ScreenUtils.getScreenWidth();
        mWidth = (screenWidth - SizeUtils.dp2px(20)) / 3;
    }

    @Override
    public int getCount() {
        return pathList.size();
    }

    @Override
    public String getItem(int position) {
        return pathList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void setPathList(List<String> pathList) {
        this.pathList = pathList;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflate.inflate(R.layout.adapter_photo_item, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        final String imgUrl = getItem(position);
        GlideUtils.loadImageViewAnim(context, imgUrl, holder.imageView, mWidth, mWidth, holder.pb);

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        holder.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pathList.remove(position);
                notifyDataSetChanged();
                if (pathList.size() < 9) {

                }

            }
        });
        return convertView;
    }


    class ViewHolder {
        @BindView(R.id.imageView)
        ImageView imageView;
        @BindView(R.id.photo_progressbar)
        ProgressBar pb;
        @BindView(R.id.iv_photo_delete)
        ImageView ivDelete;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

    }

}