package com.bmob.im.demo.ui;

import java.io.File;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import cn.bmob.im.BmobChat;
import cn.bmob.im.BmobUserManager;
import cn.bmob.im.util.BmobLog;
import cn.bmob.v3.BmobInstallation;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;

import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.baidu.mapapi.SDKInitializer;
import com.bmob.im.demo.CustomApplcation;
import com.bmob.im.demo.bean.User;
import com.bmob.im.demo.config.BmobConstants;
import com.bmob.im.demo.config.Config;
import com.xy.fy.main.R;
import com.xy.fy.util.StaticVarUtil;

/**
 * ����ҳ
 * 
 * @ClassName: SplashActivity
 * @Description: TODO
 * @author smile
 * @date 2014-6-4 ����9:45:43
 */
public class SplashActivity extends BaseActivity {

  private static final int GO_HOME = 100;
  private static final int GO_LOGIN = 200;

  // ��λ��ȡ��ǰ�û��ĵ���λ��
  private LocationClient mLocationClient;

  private BaiduReceiver mReceiver;// ע��㲥�����������ڼ��������Լ���֤key

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    // TODO Auto-generated method stub
    super.onCreate(savedInstanceState);

    setContentView(R.layout.activity_splash);
    // BmobIM SDK��ʼ��--ֻ��Ҫ��һ�δ��뼴����ɳ�ʼ��
    // �뵽Bmob����(http://www.bmob.cn/)����ApplicationId,�����ַ:http://docs.bmob.cn/android/faststart/index.html?menukey=fast_start&key=start_android
    BmobChat.getInstance(this).init(Config.applicationId);
    // ������λ
    initLocClient();
    // ע���ͼ SDK �㲥������
    IntentFilter iFilter = new IntentFilter();
    iFilter.addAction(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR);
    iFilter.addAction(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR);
    mReceiver = new BaiduReceiver();
    registerReceiver(mReceiver, iFilter);

    if (userManager.getCurrentUser() != null) {
      // ÿ���Զ���½��ʱ�����Ҫ�����µ�ǰλ�úͺ��ѵ����ϣ���Ϊ���ѵ�ͷ���ǳ�ɶ���Ǿ����䶯��
      updateUserInfos();
      mHandler.sendEmptyMessageDelayed(GO_HOME, 100);
    } else {
      mHandler.sendEmptyMessageDelayed(GO_LOGIN, 2000);
    }
  }

  /**
   * ������λ�����µ�ǰ�û��ľ�γ������
   * 
   * @Title: initLocClient @Description: TODO @param @return void @throws
   */
  private void initLocClient() {
    mLocationClient = CustomApplcation.getInstance().mLocationClient;
    LocationClientOption option = new LocationClientOption();
    option.setLocationMode(LocationMode.Hight_Accuracy);// ���ö�λģʽ:�߾���ģʽ
    option.setCoorType("bd09ll"); // ������������:�ٶȾ�γ��
    option.setScanSpan(1000);// ���÷���λ����ļ��ʱ��Ϊ1000ms:����1000Ϊ�ֶ���λһ�Σ����ڻ����1000��Ϊ��ʱ��λ
    option.setIsNeedAddress(false);// ����Ҫ������ַ��Ϣ
    mLocationClient.setLocOption(option);
    mLocationClient.start();
  }

  @SuppressLint("HandlerLeak")
  private Handler mHandler = new Handler() {
    @Override
    public void handleMessage(Message msg) {
      switch (msg.what) {
      case GO_HOME:
        startAnimActivity(MainActivity.class);
        finish();
        break;
      case GO_LOGIN:
        // ����ÿ��Ӧ�õ�ע����������϶���һ������IM sdkδ�ṩע�᷽�����û��ɰ���bmod SDK��ע�᷽ʽ����ע�ᡣ
        // ע���ʱ����Ҫע�����㣺1��User���а��豸id��type��2���豸���а�username�ֶ�
        final User bu = new User();
        bu.setUsername(StaticVarUtil.student.getName() + "-" + StaticVarUtil.student.getAccount());
        bu.setPassword(StaticVarUtil.student.getPassword());
        // ��user���豸id���а�
        bu.setDeviceType("android");
        bu.setInstallId(BmobInstallation.getInstallationId(getApplicationContext()));
        bu.signUp(getApplicationContext(), new SaveListener() {

          @Override
          public void onSuccess() {
            // TODO Auto-generated method stub
            // ShowToast("ע��ɹ�");
            // ���豸��username���а�
            userManager.bindInstallationForRegister(bu.getUsername());
            // ���µ���λ����Ϣ
            updateUserLocation();
            // ���㲥֪ͨ��½ҳ���˳�
            sendBroadcast(new Intent(BmobConstants.ACTION_REGISTER_SUCCESS_FINISH));
            updateInfo(StaticVarUtil.student.getName());
          }

          private void updateInfo(String nick) {
            final User user = userManager.getCurrentUser(User.class);
            user.setNick(nick);
            user.update(getApplicationContext(), new UpdateListener() {
              @Override
              public void onSuccess() {
                // TODO Auto-generated method stub
                File file = new File(StaticVarUtil.PATH,
                    StaticVarUtil.student.getAccount() + ".JPEG");
                if (file.exists()) {
                  uploadAvatar(
                      StaticVarUtil.PATH + "/" + StaticVarUtil.student.getAccount() + ".JPEG");
                } else {
                  // ������ҳ
                  Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                  startActivity(intent);
                  finish();
                }
              }

              @Override
              public void onFailure(int arg0, String arg1) {
                // TODO Auto-generated method stub
                ShowToast("onFailure:" + arg1);
              }
            });
          }

          @Override
          public void onFailure(int arg0, String arg1) {
            // TODO Auto-generated method stub
            BmobLog.i(arg1);
            if ("already taken.".equals(arg1.split("'")[2].trim())) {// �Ѿ�ע���
              userManager.login(
                  StaticVarUtil.student.getName() + "-" + StaticVarUtil.student.getAccount(),
                  StaticVarUtil.student.getPassword(), new SaveListener() {

                @Override
                public void onSuccess() {
                  // TODO Auto-generated method stub
                  runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                      // TODO Auto-generated method stub
                    }
                  });
                  // �����û��ĵ���λ���Լ����ѵ�����
                  updateUserInfos();
                  Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                  startActivity(intent);
                  finish();
                }

                @Override
                public void onFailure(int errorcode, String arg0) {
                  // TODO Auto-generated method stub
                  BmobLog.i(arg0);
                  ShowToast(arg0);
                }
              });

            }
            // ShowToast("ע��ʧ��:" + arg1);
          }
        });
        /*
         * startAnimActivity(LoginActivity.class); finish();
         */break;
      }
    }
  };

  // �����û�ͷ��
  private void uploadAvatar(String path) {
    final BmobFile bmobFile = new BmobFile(new File(path));
    bmobFile.upload(this, new UploadFileListener() {

      @Override
      public void onSuccess() {
        // TODO Auto-generated method stub
        String url = bmobFile.getFileUrl(getApplicationContext());
        // ����BmobUser����
        updateUserAvatar(url);
      }

      @Override
      public void onProgress(Integer arg0) {
        // TODO Auto-generated method stub

      }

      @Override
      public void onFailure(int arg0, String msg) {
        // TODO Auto-generated method stub
        ShowToast("ͷ���ϴ�ʧ�ܣ�" + msg);
      }
    });
  }

  // �����û���Ϣ
  private void updateUserAvatar(final String url) {
    User user = (User) userManager.getCurrentUser(User.class);
    user.setAvatar(url);
    user.update(this, new UpdateListener() {
      @Override
      public void onSuccess() {
        // TODO Auto-generated method stub
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();
        // ����ͷ��
      }

      @Override
      public void onFailure(int code, String msg) {
        // TODO Auto-generated method stub
        ShowToast("ͷ�����ʧ�ܣ�" + msg);
      }
    });
  }

  /**
   * ����㲥�����࣬���� SDK key ��֤�Լ������쳣�㲥
   */
  public class BaiduReceiver extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
      String s = intent.getAction();
      if (s.equals(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR)) {
        ShowToast("key ��֤����! ���� AndroidManifest.xml �ļ��м�� key ����");
      } else if (s.equals(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR)) {
        ShowToast("��ǰ�������Ӳ��ȶ�������������������!");
      }
    }
  }

  @Override
  protected void onDestroy() {
    // �˳�ʱ���ٶ�λ
    if (mLocationClient != null && mLocationClient.isStarted()) {
      mLocationClient.stop();
    }
    unregisterReceiver(mReceiver);
    super.onDestroy();
  }

}
