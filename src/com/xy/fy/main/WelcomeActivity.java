package com.xy.fy.main;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import com.mc.util.CrashHandler;
import com.mc.util.H5Log;
import com.mc.util.HttpUtilMc;
import com.mc.util.Util;
import com.xy.fy.asynctask.GetPicAsynctask;
import com.xy.fy.asynctask.LoginAsynctask;
import com.xy.fy.util.ConnectionUtil;
import com.xy.fy.util.StaticVarUtil;
import com.xy.fy.util.ViewUtil;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageView;

public class WelcomeActivity extends Activity {

  private ImageView welcome;

  @SuppressLint("NewApi")
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    requestWindowFeature(Window.FEATURE_NO_TITLE);
    Util.setContext(getApplicationContext());
    Util.setLanguageShare(WelcomeActivity.this);

    if (!ConnectionUtil.isConn(getApplicationContext())) {
      ConnectionUtil.setNetworkMethod(WelcomeActivity.this);
      Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
      startActivity(intent);
      finish();
      return;
    }

    try {
      if (!Util.isDebug(getApplicationContext())) {
        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init(this);
      }
    } catch (Exception e) {
      // TODO: handle exception
      e.printStackTrace();
    }

    try {
      int alwaysFinish = Settings.Global.getInt(getContentResolver(),
          Settings.Global.ALWAYS_FINISH_ACTIVITIES, 0);
      if (alwaysFinish == 1) {
        Dialog dialog = null;
        dialog = new AlertDialog.Builder(this)
            .setMessage("由于您已开启'不保留活动',导致i呼部分功能无法正常使用.我们建议您点击左下方'设置'按钮,在'开发者选项'中关闭'不保留活动'功能.")
            .setNegativeButton("取消", new OnClickListener() {
              @Override
              public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
              }
            }).setPositiveButton("设置", new OnClickListener() {
              @Override
              public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS);
                startActivity(intent);
              }
            }).create();
        dialog.show();
      }
    } catch (Throwable e) {
      // TODO: handle exception
    }

    H5Log.d(getApplicationContext(), String.valueOf(Util.isDebugable(getApplicationContext())));
    final View view = View.inflate(this, R.layout.activity_welcome, null);

    setContentView(view);
    welcome = (ImageView) findViewById(R.id.welcome);
    AlphaAnimation aa = new AlphaAnimation(0.3f, 1.0f);
    aa.setDuration(5000);
    view.startAnimation(aa);

    aa.setAnimationListener(new AnimationListener() {
      @Override
      public void onAnimationEnd(Animation arg0) {
//        SharedPreferences preferences = getSharedPreferences(StaticVarUtil.USER_INFO, MODE_PRIVATE);
//        String acount = preferences.getString(StaticVarUtil.ACCOUNT, "");
//        String pass = preferences.getString(StaticVarUtil.PASSWORD, "");
//        String session = preferences.getString(acount + pass + StaticVarUtil.SESSION, "");
//        if (session != null && !session.isEmpty()) {
          System.out.println("autoLogin");
          autoLogin();
//        } else {
//          GetImageMsgAsytask getImageMsgAsytask = new GetImageMsgAsytask(false);
//          getImageMsgAsytask.execute();
//        }

      }

      @Override
      public void onAnimationRepeat(Animation animation) {
      }

      @Override
      public void onAnimationStart(Animation animation) {
        GetImageMsgAsytask getImageMsgAsytask = new GetImageMsgAsytask(true);
        getImageMsgAsytask.execute();
      }
    });
  }

  private Handler mHandler;
  @SuppressLint("NewApi")
  Runnable runnableUi = new Runnable() {
    @Override
    public void run() {
      if (StaticVarUtil.welcomeBitmap != null) {
        try {
          welcome.setBackground(new BitmapDrawable(StaticVarUtil.welcomeBitmap));
        } catch (NoSuchFieldError e) {
          // TODO: handle exception
        }

      }
    }
  };

  class GetImageMsgAsytask extends AsyncTask<String, String, String> {
    private boolean isWelcome;

    private GetImageMsgAsytask(boolean isWelcome) {
      // TODO Auto-generated constructor stub
      this.isWelcome = isWelcome;
    }

    @Override
    protected String doInBackground(String... params) {
      // TODO Auto-generated method stub
      return HttpUtilMc.IsReachIP()
          ? HttpUtilMc.queryStringForPost(HttpUtilMc.BASE_URL + "GetPollImageTimeIspoll")
          : HttpUtilMc.CONNECT_EXCEPTION;
    }

    @Override
    protected void onPostExecute(String result) {
      // TODO Auto-generated method stub
      super.onPostExecute(result);
      try {
        if (isWelcome) {
          mHandler = new Handler();
          result = !HttpUtilMc.CONNECT_EXCEPTION.equals(result) ? result : "0|0|0";
          String[] imageAndTime = result.split("\\|");
          final String imageTime = imageAndTime[0];
          String isPoll = imageAndTime[1];
          if (isPoll.equals("1")) {// 下拉下载
            final Animation anim = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.animated_remote);
            LinearInterpolator lir = new LinearInterpolator();
            anim.setInterpolator(lir);
            new Thread() {
              public void run() {
                try {
                  sleep(1500);
                } catch (InterruptedException e) {
                  // TODO Auto-generated catch block
                  e.printStackTrace();
                }
                StaticVarUtil.welcomeBitmap = Util
                    .getBitmap(HttpUtilMc.BASE_URL + "image/" + imageTime + ".jpg");
                mHandler.post(runnableUi);
              }
            }.start();
          } else {
          }

        } else {
          Intent i = new Intent();
          i.setClass(getApplicationContext(), LoginActivity.class);
          // 如果网络原因，则直接返回0|0
          i.putExtra("image", !HttpUtilMc.CONNECT_EXCEPTION.equals(result) ? result : "0|0|0");//
          startActivity(i);
          finish();
        }
      } catch (Exception e) {
        // TODO: handle exception
        Log.i("WelcomeActivity", e.toString());
      }
    }
  }

  private void autoLogin() {
    SharedPreferences preferences = getSharedPreferences(StaticVarUtil.USER_INFO, MODE_PRIVATE);
    String acountStr = preferences.getString(StaticVarUtil.ACCOUNT, "");
    String passStr = preferences.getString(StaticVarUtil.PASSWORD, "");
    String session = preferences.getString(acountStr + passStr + StaticVarUtil.SESSION, "");

    GetPicAsynctask getPicAsyntask = new GetPicAsynctask(WelcomeActivity.this, acountStr, passStr,
        null, new GetPicAsynctask.GetPic() {

          @Override
          public void onReturn(String result) {
            // TODO Auto-generated method stub

            if ("error".equals(result) || HttpUtilMc.CONNECT_EXCEPTION.equals(result)) {
              // password.setText("");
              // password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
              GetImageMsgAsytask getImageMsgAsytask = new GetImageMsgAsytask(false);
              getImageMsgAsytask.execute();
              return;
            }
          }
        });
    getPicAsyntask.execute();

    // Util.getAutoParmas(getApplicationContext(), session);
    // LoginAsynctask loginAsyntask = new LoginAsynctask(WelcomeActivity.this, acountStr, "", "",
    // new LoginAsynctask.LoginResult() {
    //
    // @Override
    // public void onLogin(String result) {
    // // TODO Auto-generated method stub
    //
    // if (result == null) {
    // GetImageMsgAsytask getImageMsgAsytask = new GetImageMsgAsytask(false);
    // getImageMsgAsytask.execute();
    // return;
    // }
    // try {
    // if (!HttpUtilMc.CONNECT_EXCEPTION.equals(result)) {
    // if (result.equals("error") || result.equals("errorReq")) {
    // ViewUtil.showToast(WelcomeActivity.this, "证书过期，请重新登录。");
    //// GetImageMsgAsytask getImageMsgAsytask = new GetImageMsgAsytask(false);
    //// getImageMsgAsytask.execute();
    // // progressDialog.cancel();
    // } else {
    // StaticVarUtil.listHerf = new ArrayList<HashMap<String, String>>();
    // JSONObject json = new JSONObject(result);
    // JSONArray jsonArray = (JSONArray) json.get("listHerf");
    // for (int i = 0; i < jsonArray.length(); i++) {
    // JSONObject o = (JSONObject) jsonArray.get(i);
    // HashMap<String, String> map = new HashMap<String, String>();
    // map.put("herf", o.getString("herf"));
    // map.put("tittle", o.getString("tittle"));
    // StaticVarUtil.listHerf.add(map);
    // }
    // SharedPreferences preferences = getSharedPreferences(StaticVarUtil.USER_INFO,
    // MODE_PRIVATE);
    // String accountStr = preferences.getString(StaticVarUtil.ACCOUNT, "");
    // String sessionStr = preferences.getString(accountStr + StaticVarUtil.SESSION, "");
    // String passwordStr = preferences.getString(StaticVarUtil.PASSWORD, "");
    //
    // StaticVarUtil.student.setAccount(accountStr);
    // StaticVarUtil.student.setPassword(passwordStr);
    // StaticVarUtil.session = sessionStr;
    // Intent intent = new Intent();
    // intent.setClass(WelcomeActivity.this, MainActivity.class);
    // // progressDialog.cancel();
    // startActivity(intent);
    // finish();
    // StaticVarUtil.quit();
    // }
    //
    // } else {
    // GetImageMsgAsytask getImageMsgAsytask = new GetImageMsgAsytask(false);
    // getImageMsgAsytask.execute();
    // }
    // } catch (Exception e) {
    // // TODO: handle exception
    // GetImageMsgAsytask getImageMsgAsytask = new GetImageMsgAsytask(false);
    // getImageMsgAsytask.execute();
    // Log.i("LoginActivity", e.toString());
    // }
    //
    // }
    // }, null, true);
    // loginAsyntask.execute();
  }
}
