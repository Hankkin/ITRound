package com.hankkin.itround.chat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import com.hankkin.itround.Constant;
import com.hankkin.itround.event.EventMap;

import org.json.JSONException;
import org.json.JSONObject;

import cn.leancloud.chatkit.utils.LCIMNotificationUtils;
import de.greenrobot.event.EventBus;

/**
 * Created by wli on 15/7/10.
 * 自定义推送逻辑
 */
public class LeanchatReceiver extends BroadcastReceiver {

  public final static String AVOS_DATA = "com.avoscloud.Data";

  @Override
  public void onReceive(Context context, Intent intent) {
    String action = intent.getAction();
    if (!TextUtils.isEmpty(action)) {
      if (action.equals(Constant.INVITATION_ACTION)) {
        String avosData = intent.getStringExtra(AVOS_DATA);
        if (!TextUtils.isEmpty(avosData)) {
          try {
            JSONObject json = new JSONObject(avosData);
            if (null != json) {
              String alertStr = json.getString(PushManager.AVOS_ALERT);

              Intent notificationIntent = new Intent(context, NotificationBroadcastReceiver.class);
              notificationIntent.putExtra(Constant.NOTOFICATION_TAG, Constant.NOTIFICATION_SYSTEM);
              LCIMNotificationUtils.showNotification(context, "LeanChat", alertStr, notificationIntent);
            }
          } catch (JSONException e) {
          }
        }
      }
    }
    EventBus.getDefault().post(new EventMap.InvitationEvent());
  }
}