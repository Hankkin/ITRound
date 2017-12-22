package com.hankkin.itround.chat;

import com.hankkin.itround.bean.UserBean;

import java.util.ArrayList;
import java.util.List;

import cn.leancloud.chatkit.LCChatKitUser;
import cn.leancloud.chatkit.LCChatProfileProvider;
import cn.leancloud.chatkit.LCChatProfilesCallBack;

/**
 * Created by wli on 15/12/4.
 */
public class LeanchatUserProvider implements LCChatProfileProvider {

  private static LCChatKitUser getThirdPartUser(UserBean leanchatUser) {
    return new LCChatKitUser(leanchatUser.getObjectId(), leanchatUser.getUsername(), leanchatUser.getAvatarUrl());
  }

  private static List<LCChatKitUser> getThirdPartUsers(List<UserBean> leanchatUsers) {
    List<LCChatKitUser> thirdPartUsers = new ArrayList<>();
    for (UserBean user : leanchatUsers) {
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
