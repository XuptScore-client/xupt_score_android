package com.xy.fy.main;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;

import com.mc.util.CrashHandler;

public class WelcomeActivity extends Activity {


	/*// ����״̬��
		NotificationManager notificationManager = null;
		Notification notification = null;
		PendingIntent pendingIntent = null;// �������͵��¼�
*/	@SuppressLint("ShowToast")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		CrashHandler crashHandler = CrashHandler.getInstance();
		crashHandler.init(this);
		final View view = View.inflate(this, R.layout.activity_welcome, null);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(view);
	
		AlphaAnimation aa = new AlphaAnimation(0.3f, 1.0f);
		aa.setDuration(1000);
		view.startAnimation(aa);

		aa.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationEnd(Animation arg0) {
				Intent i = new Intent();
				i.setClass(getApplicationContext(), LoginActivity.class);
				startActivity(i);
				finish();
				
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationStart(Animation animation) {
			
				//i = System.currentTimeMillis();
				// ������ʼ��ʱ����з��͹㲥���������ݵĸ���
//					Toast.makeText(getApplicationContext(), "��ӭ", 1000).show();
					
				
			}
		});
	}
	

	

}
