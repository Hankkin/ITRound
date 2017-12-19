package com.hankkin.itround.adapter;

import android.text.TextUtils;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hankkin.itround.ITApplication;
import com.hankkin.itround.R;
import com.hankkin.itround.bean.UserBean;
import com.hankkin.library.utils.GlideUtils;

public class CircleFriendAdapter extends BaseQuickAdapter<UserBean, BaseViewHolder> {


        public CircleFriendAdapter() {
            super(R.layout.adapter_circle_friend);
        }

        @Override
        protected void convert(BaseViewHolder helper, final UserBean item) {
            helper.setText(R.id.tv_adapter_cir_name, item.getName());
//            helper.setText(R.id.tv_adapter_cir_desc, item.getDesc());

            if (item.getSex() == UserBean.BOY) {
                helper.setImageResource(R.id.iv_adapter_cir_sex, R.mipmap.da_ren_list_boy);
            } else {
                helper.setImageResource(R.id.iv_adapter_cir_sex, R.mipmap.da_ren_list_girl);

            }
            ImageView ivHeader = helper.getView(R.id.iv_cir_icon);
            if (!TextUtils.isEmpty(item.getAvatarUrl())) {
                GlideUtils.loadImageView(ITApplication.getInstance(), item.getAvatarUrl(), ivHeader);
            }
        }
        public int getLetterPosition(String letter){
            for (int i = 0 ; i < getData().size(); i++){
                if(getData().get(i).getUsername().equals(letter)){
                    return i;
                }
            }
            return -1;
        }

    }