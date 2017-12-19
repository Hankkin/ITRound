package com.hankkin.itround.chat;

import com.hankkin.itround.bean.UserBean;

import java.util.ArrayList;
import java.util.List;

import cn.leancloud.chatkit.LCChatKitUser;
import cn.leancloud.chatkit.LCChatProfileProvider;
import cn.leancloud.chatkit.LCChatProfilesCallBack;

public class CustomUserProvider implements LCChatProfileProvider {

  private static LCChatKitUser getThirdPartUser(UserBean UserBean) {
    return new LCChatKitUser(UserBean.getObjectId(), UserBean.getUsername(), UserBean.getAvatarUrl());
  }

  private static List<LCChatKitUser> getThirdPartUsers(List<UserBean> UserBeans) {
    List<LCChatKitUser> thirdPartUsers = new ArrayList<>();
    for (UserBean user : UserBeans) {
      thirdPartUsers.add(getThirdPartUser(user));
    }
    return thirdPartUsers;
  }


  @Override
  public void fetchProfiles(List<String> list, final LCChatProfilesCallBack callBack) {
    UserCacheUtils.fetchUsers(list, new UserCacheUtils.CacheUserCallback() {
      @Override
      public void done(List<UserBean> userList, Exception e) {
        callBack.done(getThirdPartUsers(userList), e);
      }
    });
  }
}