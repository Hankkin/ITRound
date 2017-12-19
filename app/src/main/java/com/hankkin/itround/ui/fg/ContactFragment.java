package com.hankkin.itround.ui.fg;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hankkin.itround.R;
import com.hankkin.itround.adapter.CircleFriendAdapter;
import com.hankkin.itround.bean.UserBean;
import com.hankkin.itround.chat.AddRequestManager;
import com.hankkin.itround.chat.FriendsManager;
import com.hankkin.itround.event.EventMap;
import com.hankkin.itround.ui.circle.NewFriendActivity;
import com.hankkin.itround.widget.LetterComparator;
import com.hankkin.itround.widget.PinnedHeaderDecoration;
import com.hankkin.library.base.BaseFragment;
import com.hankkin.library.rx.RxBus;
import com.hankkin.library.view.EmptyLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cc.solart.wave.WaveSideBarView;
import cn.leancloud.chatkit.activity.LCIMConversationActivity;
import cn.leancloud.chatkit.utils.LCIMConstants;
import io.reactivex.functions.Consumer;

/**
 * Created by hankkin on 2017/10/12.
 * Blog: http://hankkin.cn
 * Mail: 1019283569@qq.com
 */

public class ContactFragment extends BaseFragment{
    private final static String TAG = "CircleFragment";

    @BindView(R.id.recycler_view)
    RecyclerView rv;
    @BindView(R.id.side_view)
    WaveSideBarView sideBarView;
    @BindView(R.id.refresh)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.empty)
    EmptyLayout emptyLayout;
    @BindView(R.id.iv_msg_tips)
    ImageView msgTipsView;



    private CircleFriendAdapter adapter;

    public static ContactFragment newInstance(int index) {
        ContactFragment fragment = new ContactFragment();
        Bundle args = new Bundle();
        args.putInt("index", index);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initViewsAndEvents(View view) {

        initSwipe(refreshLayout, null);
        rv.setLayoutManager(new LinearLayoutManager(activity));
        final PinnedHeaderDecoration decoration = new PinnedHeaderDecoration();
        decoration.registerTypePinnedHeader(1, new PinnedHeaderDecoration.PinnedHeaderCreator() {
            @Override
            public boolean create(RecyclerView parent, int adapterPosition) {
                return true;
            }
        });
        rv.addItemDecoration(decoration);
        adapter = new CircleFriendAdapter();
        rv.setAdapter(adapter);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_contact;
    }

    @Override
    protected void initData() {
        operateBus();
        sideBarView.setOnTouchLetterChangeListener(new WaveSideBarView.OnTouchLetterChangeListener() {
            @Override
            public void onLetterChange(String letter) {
                int pos = adapter.getLetterPosition(letter);
                if (pos != -1) {
                    rv.scrollToPosition(pos);
                    LinearLayoutManager mLayoutManager =
                            (LinearLayoutManager) rv.getLayoutManager();
                    mLayoutManager.scrollToPositionWithOffset(pos, 0);
                }
            }
        });

        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                UserBean userBean = (UserBean) adapter.getItem(position);
                Intent intent = new Intent(getContext(), LCIMConversationActivity.class);
                intent.putExtra(LCIMConstants.PEER_ID, userBean.getObjectId());
                intent.putExtra(LCIMConstants.PEER_NAME, userBean.getName());
                activity.startActivity(intent);
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                queryUser();
            }
        }).start();
        updateNewRequestBadge();

    }

    private void updateNewRequestBadge() {
        msgTipsView.setVisibility(
                AddRequestManager.getInstance().hasUnreadRequests() ? View.VISIBLE : View.GONE);
    }

    private void queryUser() {

        FriendsManager.fetchFriends(true, new FindCallback<UserBean>() {
            @Override
            public void done(List<UserBean> list, AVException e) {
                List<UserBean> userBeanList = new ArrayList<>();
                for (UserBean user : list){
                    userBeanList.add(user);
                }
                Collections.sort(userBeanList, new LetterComparator());

                adapter.setNewData(userBeanList);
                adapter.notifyDataSetChanged();
                refreshLayout.setRefreshing(false);
                refreshLayout.setEnabled(false);
                filterException(e);
            }
        });
    }

    private void operateBus(){
        RxBus.getDefault().toObservable(EventMap.BaseEvent.class)
                .subscribe(new Consumer<EventMap.BaseEvent>() {
                    @Override
                    public void accept(EventMap.BaseEvent event) throws Exception {
                        if (event instanceof EventMap.InvitationEvent){
                            AddRequestManager.getInstance().unreadRequestsIncrement();
                            updateNewRequestBadge();
                        }
                        else if (event instanceof EventMap.ContactRefreshEvent){
                            queryUser();
                        }

                    }
                });
    }

    @Override
    public void onStart() {
        super.onStart();
    }


    @OnClick(R.id.layout_new)
    void goNewRequest()
    {
        gotoActivity(NewFriendActivity.class);
    }



    @Override
    public void onResume() {
        super.onResume();
        updateNewRequestBadge();
    }


}
