package com.hankkin.library.view;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;

import com.hankkin.library.R;

import butterknife.ButterKnife;


/**
 * Created by Hankkin on 16/4/8.
 */
public class ProgressDialog extends Dialog {
    private Context context;

    public ProgressDialog(Context context) {
        super(context, R.style.progress);
        this.context = context;
        init();
    }

    private void init(){
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_progress);
        ButterKnife.bind(this);

        setCanceledOnTouchOutside(false);

    }


    @Override
    public void onBackPressed() {
        dismiss();
    }
}
