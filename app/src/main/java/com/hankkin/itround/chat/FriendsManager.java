package com.hankkin.itround.chat;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.hankkin.itround.bean.UserBean;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by wli on 15/12/1.
 */
public class FriendsManager {

  private static volatile List<String> friendIds = new ArrayList<String>();


  public static List<String> getFriendIds() {
    return friendIds;
  }

  public static void setFriendIds(List<String> friendList) {
    friendIds.clear();
    if (friendList != null) {
      friendIds.addAll(friendList);
    }
  }

  public static void fetchFriends(boolean isForce, final FindCallback<UserBean> findCallback) {
    AVQuery.CachePolicy policy =AVQuery.CachePolicy.CACHE_ELSE_NETWORK;
    UserBean.getCurrentUser().findFriendsWithCachePolicy(policy, new FindCallback<UserBean>() {
      @Override
      public void done(List<UserBean> list, AVException e) {
        if (null != e) {
          findCallback.done(null, e);
        } else {
          final List<String> userIds = new ArrayList<String>();
          for (UserBean user : list) {
            userIds.add(user.getObjectId());
          }
          UserCacheUtils.fetchUsers(userIds, new UserCacheUtils.CacheUserCallback() {
            @Override
            public void done(List<UserBean> list1, Exception e) {
              setFriendIds(userIds);
              findCallback.done(list1, null);
            }
          });
        }
      }
    });
  }
}
