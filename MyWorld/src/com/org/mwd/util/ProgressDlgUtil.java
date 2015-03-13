package com.org.mwd.util;

import com.org.myworld.R;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;

public class ProgressDlgUtil {
    static ProgressDialog progressDlg = null;
  
    /**
     * 启动进度条
     *
     * @param strMessage
     *            进度条显示的信息
     * @param activity
     *            当前的activity
     */
    @SuppressWarnings("rawtypes")
	public static void showProgressDlg(String strMessage, Context ctx,  final AsyncTask at) {
  
        if (null == progressDlg) {
            progressDlg = new ProgressDialog(ctx);
            //设置进度条样式
            progressDlg.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            //设置进度条标题
            progressDlg.setTitle(R.string.app_name);
            //提示的消息
            progressDlg.setMessage(strMessage);
            progressDlg.setIndeterminate(false);
            progressDlg.setCancelable(false);
            progressDlg.setIcon(R.drawable.icon);
            //设置ProgressDialog 的一个Button  
            progressDlg.setButton(DialogInterface.BUTTON_NEGATIVE, "取消", new DialogInterface.OnClickListener(){
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					at.cancel(true);
					progressDlg = null;
				}
            });
            progressDlg.show();
        }
    }
  
    /**
     * 结束进度条
     */
    public static void stopProgressDlg() {
        if (null != progressDlg) {
            progressDlg.dismiss();
            progressDlg = null;
        }
    }
}