package com.hankkin.itround.chat;

import android.content.Context;

import com.hankkin.library.utils.ToastUtils;

/**
 * Created by lzw on 14-9-27.
 */
public abstract class SimpleNetTask extends NetAsyncTask {
  protected SimpleNetTask(Context cxt) {
    super(cxt);
  }

  protected SimpleNetTask(Context cxt, boolean openDialog) {
    super(cxt, openDialog);
  }


  @Override
  protected void onPost(Exception e) {
    if (e != null) {
      e.printStackTrace();
      ToastUtils.showShortToast(e.getMessage());
      //Utils.toast(ctx, R.string.pleaseCheckNetwork);
    } else {
      onSucceed();
    }
  }

  protected abstract void doInBack() throws Exception;

  protected abstract void onSucceed();
}
