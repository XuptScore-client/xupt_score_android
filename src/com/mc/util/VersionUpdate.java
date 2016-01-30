package com.mc.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.xy.fy.main.R;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * 
 * @author machao 更新版本
 * 
 */
public class VersionUpdate {

  private Context mContext;

  // 提示语
  public String updateMsg = "有最新的软件包哦，亲快下载吧~";

  // 返回的安装包url
  public String apkUrl;

  private Dialog noticeDialog;

  private Dialog downloadDialog;
  /* 下载包安装路径 */
  private static final String savePath = Environment.getExternalStorageDirectory().getPath()
      + "/xuptscore/";

  public static final String saveFileName = savePath + "xuptscore.apk";//

  /* 进度条与通知ui刷新的handler和msg常量 */
  private ProgressBar mProgress;

  private static final int DOWN_UPDATE = 1;

  private static final int DOWN_OVER = 2;

  private int progress;

  private Thread downLoadThread;

  private boolean interceptFlag = false;

  private Handler mHandler = new Handler() {
    public void handleMessage(Message msg) {
      switch (msg.what) {
      case DOWN_UPDATE:
        mProgress.setProgress(progress);
        break;
      case DOWN_OVER:

        installApk();
        break;
      default:
        break;
      }
    };
  };

  public VersionUpdate(Context context) {
    this.mContext = context;
  }

  // 外部接口让主Activity调用
  public void checkUpdateInfo() {
    showNoticeDialog();
  }

  private void showNoticeDialog() {
    AlertDialog.Builder builder = new Builder(mContext);
    builder.setTitle("软件版本更新");
    System.out.println("mcmcmcm:" + updateMsg);
    String[] msg = updateMsg.split(" ");
    updateMsg = "";
    for (int i = 0; i < msg.length; i++) {
      updateMsg += msg[i] + "\n";
    }
    builder.setMessage(updateMsg);
    builder.setPositiveButton("下载", new OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
        dialog.dismiss();
        showDownloadDialog();
        // 进入浏览器下载
      }
    });
    builder.setNegativeButton("以后再说", new OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
        dialog.dismiss();
      }
    });
    noticeDialog = builder.create();
    noticeDialog.show();
    
    setDialogFontSize(noticeDialog,15);
  }

  private void setDialogFontSize(Dialog dialog, int size) {
    Window window = dialog.getWindow();
    View view = window.getDecorView();
    setViewFontSize(view, size);
  }

  private void setViewFontSize(View view, int size) {
    if (view instanceof ViewGroup) {
      ViewGroup parent = (ViewGroup) view;
      int count = parent.getChildCount();
      for (int i = 0; i < count; i++) {
        setViewFontSize(parent.getChildAt(i), size);
      }
    } else if (view instanceof TextView) {
      TextView textview = (TextView) view;
      textview.setTextSize(size);
    }
  }

  private void showDownloadDialog() {
    AlertDialog.Builder builder = new Builder(mContext);
    builder.setTitle("软件版本更新");

    final LayoutInflater inflater = LayoutInflater.from(mContext);
    View v = inflater.inflate(R.layout.progress, null);
    mProgress = (ProgressBar) v.findViewById(R.id.progress);

    builder.setView(v);
    builder.setNegativeButton("取消", new OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
        dialog.dismiss();
        interceptFlag = true;
      }
    });
    downloadDialog = builder.create();
    downloadDialog.show();

    downloadApk();
  }

  private Runnable mdownApkRunnable = new Runnable() {
    @Override
    public void run() {
      try {
        URL url = new URL(apkUrl);

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.connect();
        int length = conn.getContentLength();
        InputStream is = conn.getInputStream();

        File file = new File(savePath);
        if (!file.exists()) {
          file.mkdir();
        }
        String apkFile = saveFileName;
        File ApkFile = new File(apkFile);
        FileOutputStream fos = new FileOutputStream(ApkFile);

        int count = 0;
        byte buf[] = new byte[1024];

        do {
          int numread = is.read(buf);
          count += numread;
          progress = (int) (((float) count / length) * 100);
          // 更新进度
          mHandler.sendEmptyMessage(DOWN_UPDATE);
          if (numread <= 0) {
            // 下载完成通知安装
            mHandler.sendEmptyMessage(DOWN_OVER);
            break;
          }
          fos.write(buf, 0, numread);
        } while (!interceptFlag);// 点击取消就停止下载.

        fos.close();
        is.close();
      } catch (MalformedURLException e) {
        e.printStackTrace();
      } catch (IOException e) {
        e.printStackTrace();
      }

    }
  };

  /**
   * 下载apk
   * 
   * @param url
   */

  private void downloadApk() {
    downLoadThread = new Thread(mdownApkRunnable);
    downLoadThread.start();
  }

  /**
   * 安装apk
   * 
   * @param url
   */
  private void installApk() {
    File apkfile = new File(saveFileName);
    if (!apkfile.exists()) {
      return;
    }
    Intent i = new Intent(Intent.ACTION_VIEW);
    i.setDataAndType(Uri.parse("file://" + apkfile.toString()),
        "application/vnd.android.package-archive");
    mContext.startActivity(i);
    // MainActivity.uninstall();//卸载

  }
}
