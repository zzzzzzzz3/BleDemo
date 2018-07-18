package com.example.liangfei.bledemo;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;


/**
 * Created by Liangfei on 2018/6/15.
 */

public class LoadingDialog extends Dialog {


    public LoadingDialog(@NonNull Context context) {
        super(context, R.style.Dialog);
        setContentView(R.layout.dialog_loading);
    }

}
