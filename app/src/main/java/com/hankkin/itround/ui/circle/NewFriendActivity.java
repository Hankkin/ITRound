package com.hankkin.itround.ui.circle;

import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.DeleteCallback;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.SaveCallback;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCreatedCallback;
import com.avos.avoscloud.im.v2.messages.AVIMTextMessage;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hankkin.itround.Constant;
import com.hankkin.itround.ITApplication;
import com.hankkin.itround.R;
import com.hankkin.itround.bean.UserBean;
import com.hankkin.itround.chat.AddRequest;
import com.hankkin.itround.chat.AddRequestManager;
import com.hankkin.itround.chat.ConversationUtils;
import com.hankkin.itround.event.EventMap;
import com.hankkin.library.base.BaseAcitvity;
import com.hankkin.library.rx.RxBus;
import com.hankkin.library.utils.GlideUtils;
import com.hankkin.library.utils.LogUtils;
import com.hankkin.library.view.CustomLoadMoreView;
import com.hankkin.library.view.RecycleViewDivider;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class NewFriendActivity extends BaseAcitvity implements SwipeRefreshLayout.OnRefreshListener,BaseQuickAdapter.RequestLoadMoreListener{

    @BindView(R.id.refresh)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.rv_new_friend)
    RecyclerView rv;

    private NewFriendAdapter adapter;

    private int dataCount = 0;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_new_friend2;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        initToolBar(getResources().getString(R.string.find_new_friend));
        initSwipe(refreshLayout,this);
        rv.setLayoutManager(new LinearLayoutManager(activity));
        rv.addItemDecoration(new RecycleViewDivider(activity,RecyclerView.VERTICAL));
        adapter = new NewFriendAdapter();
        rv.setAdapter(adapter);
        adapter.setLoadMoreView(new CustomLoadMoreView());
        adapter.setOnLoadMoreListener(this,rv);

        adapter.setOnItemLongClickListener(new BaseQuickAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(BaseQuickAdapter adapter, View view, int position) {
                new MaterialDialog.Builder(activity)
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                new AddRequest().deleteInBackground(new DeleteCallback() {
                                    @Override
                                    public void done(AVException e) {
                                        loadMoreAddRequest(true);
                                    }
                                });
                            }
                        })
                        .content(getResources().getString(R.string.alter_delete_friend_sms))
                        .positiveText(R.string.ok)
                        .negativeText(R.string.cancel)
                        .show();
                return false;
            }
        });
    }

    @Override
    protected void initData() {
        loadMoreAddRequest(true);
    }

    private void loadMoreAddRequest(final boolean isRefresh) {
        AddRequestManager.getInstance().findAddRequests(isRefresh ? 0 : adapter.getData().size(), 20, new FindCallback<AddRequest>() {
            @Override
            public void done(List<AddRequest> list, AVException e) {
                AddRequestManager.getInstance().markAddRequestsRead(list);
                final List<AddRequest> filters = new ArrayList<AddRequest>();
                if (null != list) {
                    LogUtils.e(getClass().getName(),list.size()+"");
                    for (AddRequest addRequest : list) {
                        if (addRequest.getFromUser() != null) {
                            filters.add(addRequest);
                        }
                    }
                }
                refreshLayout.setRefreshing(false);
                adapter.setNewData(filters);
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void toast(String msg) {

    }

    @Override
    public void onRefresh() {
        initData();
    }

    @Override
    public void onLoadMoreRequested() {
        refreshLayout.setEnabled(false);
        if (dataCount < Constant.PAGESIZE){
            adapter.loadMoreEnd(false);
        }
        else {
            loadMoreAddRequest(false);
        }
        refreshLayout.setEnabled(true);
    }

    public class NewFriendAdapter extends BaseQuickAdapter<AddRequest, BaseViewHolder> {


        public NewFriendAdapter() {
            super(R.layout.adapter_circle_friend);
        }

        @Override
        protected void convert(BaseViewHolder helper, final AddRequest item) {
            helper.setText(R.id.tv_adapter_cir_name, item.getFromUser().getUsername());

            if (Integer.parseInt(String.valueOf(item.getFromUser().get("sex"))) == UserBean.BOY) {
                helper.setImageResource(R.id.iv_adapter_cir_sex, R.mipmap.da_ren_list_boy);
            } else {
                helper.setImageResource(R.id.iv_adapter_cir_sex, R.mipmap.da_ren_list_girl);

            }
            ImageView ivHeader = helper.getView(R.id.iv_cir_icon);
            if (!TextUtils.isEmpty(item.getFromUser().getAvatarUrl())) {
                GlideUtils.loadImageView(ITApplication.getInstance(), item.getFromUser().getAvatarUrl(), ivHeader);
            }
            TextView tvAgree = helper.getView(R.id.tv_adapter_new_friend_agree);
            TextView tvAgreed = helper.getView(R.id.tv_adapter_new_friend_agreed);
            if (item.getStatus() == AddRequest.STATUS_WAIT){
                tvAgree.setVisibility(View.VISIBLE);
                tvAgreed.setVisibility(View.GONE);
            }
            else if (item.getStatus() == AddRequest.STATUS_DONE) {
                tvAgree.setVisibility(View.GONE);
                tvAgreed.setVisibility(View.VISIBLE);
            }
            helper.setOnClickListener(R.id.tv_adapter_new_friend_agree, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    agreeAddRequest(item);
                }
            });
        }


    }


    private void agreeAddRequest(final AddRequest addRequest) {
        showProgress();
        AddRequestManager.getInstance().agreeAddRequest(addRequest, new SaveCallback() {
            @Override
            public void done(AVException e) {
                hideProgress();
                if (filterException(e)) {
                    if (addRequest.getFromUser() != null) {
                        sendWelcomeMessage(addRequest.getFromUser().getObjectId());
                    }
                    loadMoreAddRequest(false);
                    RxBus.getDefault().post(new EventMap.ContactRefreshEvent());
                }
            }
        });
    }

    public void sendWelcomeMessage(String toUserId) {
        ConversationUtils.createSingleConversation(toUserId, new AVIMConversationCreatedCallback() {
            @Override
            public void done(AVIMConversation avimConversation, AVIMException e) {
                if (e == null) {
                    AVIMTextMessage message = new AVIMTextMessage();
                    message.setText(getString(R.string.toast_message_when_agree_request));
                    avimConversation.sendMessage(message, null);
                }
            }
        });
    }
}
