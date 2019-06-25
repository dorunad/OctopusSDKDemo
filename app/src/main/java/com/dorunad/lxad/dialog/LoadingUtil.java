package com.dorunad.lxad.dialog;

import android.app.Activity;
import android.view.Window;
public class LoadingUtil {
    static LoadingDialog sLoadingDialog;

    public static LoadingDialog showMaterLoading(Activity context, String text) {
        if (sLoadingDialog != null)
            return sLoadingDialog;
        sLoadingDialog = new LoadingDialog(context);
        sLoadingDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        sLoadingDialog.setCanceledOnTouchOutside(false);
        sLoadingDialog.show(text);
        if (!context.isFinishing()) {
            sLoadingDialog.show();
        }
        return sLoadingDialog;
    }

    public static void hideMaterLoading() {
        if ((sLoadingDialog != null) && (sLoadingDialog.isShowing())) {
            sLoadingDialog.dismiss();
            sLoadingDialog = null;
        }
    }
}
