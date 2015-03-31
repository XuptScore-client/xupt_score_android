package com.xy.fy.util;

import cn.sharesdk.onekeyshare.OnekeyShare;

import com.xy.fy.main.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.view.Gravity;
import android.widget.Toast;

/**
 * view������
 */
public class ViewUtil {

	/**
	 * ����ģ��
	 *//*
	public static void showShare(Context context) {
		// ShareSDK.initSDK(this);
		OnekeyShare oks = new OnekeyShare();
		// �ر�sso��Ȩ
		// oks.disableSSOWhenAuthorize();

		// ����ʱNotification��ͼ�������
		oks.setNotification(R.drawable.default_head_photo,
				context.getString(R.string.app_name));
		// title���⣬ӡ��ʼǡ����䡢��Ϣ��΢�š���������QQ�ռ�ʹ��
		oks.setTitle(context.getString(R.string.app_name));
		// titleUrl�Ǳ�����������ӣ�������������QQ�ռ�ʹ��
		// oks.setTitleUrl("http://222.24.63.101/manager/publicity.html");
		// text�Ƿ����ı�������ƽ̨����Ҫ����ֶ�
		oks.setText("�����ʳɼ���������������ѧ����ʵ���˲�ѯ�ɼ��������͸�����Ϣ�����Խ���ѧ�ź�����Ϊ��¼ƾ֤����������:http://www.xiyoumobile.com/wechat_app/xiyouscore/");
		// imagePath��ͼƬ�ı���·����Linked-In�����ƽ̨��֧�ִ˲���
		// oks.setImagePath("/sdcard/test.jpg");// ȷ��SDcard������ڴ���ͼƬ
		// url����΢�ţ��������Ѻ�����Ȧ����ʹ��
		oks.setImagePath("http://www.xiyoumobile.com/wechat_app/xiyouscore/");
		oks.setUrl("http://www.xiyoumobile.com/wechat_app/xiyouscore/");
		// comment���Ҷ�������������ۣ�������������QQ�ռ�ʹ��
		oks.setComment("�����ʳɼ���������,���ص�ַ:");
		// site�Ƿ�������ݵ���վ���ƣ�����QQ�ռ�ʹ��
		oks.setSite(context.getString(R.string.app_name));
		// siteUrl�Ƿ�������ݵ���վ��ַ������QQ�ռ�ʹ��
		oks.setSiteUrl("http://www.xiyoumobile.com/wechat_app/xiyouscore/");
		// ��������GUI
		oks.show(context);
	}*/

	/**
	 * Toast
	 */
	private static Toast mToast;

	public static void showToast(Context context, String text) {
		if (mToast == null) {
			mToast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
		} else {
			mToast.setText(text);
			mToast.setDuration(Toast.LENGTH_SHORT);
		}
		mToast.show();
	}

	public static void cancelToast() {
		if (mToast != null) {
			mToast.cancel();
		}
	}

	public static void toastLength(String string, Activity activity) {
		Toast toast = Toast.makeText(activity, string, Toast.LENGTH_LONG);
		toast.setGravity(Gravity.CENTER, 0, 0);
		// LayoutInflater inflater = LayoutInflater.from(activity);
		// View view = inflater.inflate(R.layout, null);
		// toast.setView(view);
		toast.show();
	}

	public static void showDialog(String string, String title,
			final Activity activity) {
		Builder builder = new AlertDialog.Builder(activity);
		builder.setTitle(title);
		builder.setMessage(string);
		builder.setPositiveButton("ȷ��", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});
		builder.setNegativeButton("ȡ��", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});
		Dialog dialog = builder.create();
		dialog.show();
	}

	public static ProgressDialog getProgressDialog(Activity activity,
			String title) {
		ProgressDialog progressDialog = new ProgressDialog(activity);// ʵ����
		progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);// ���ý�������񣬷��ΪԲ�Σ���ת��
		progressDialog.setTitle(title);// ����ProgressDialog ����
		progressDialog.setMessage("���Ժ�...");// ����ProgressDialog ��ʾ��Ϣ
		progressDialog.setIndeterminate(false);// ����ProgressDialog
		progressDialog.setCancelable(true);// �Ƿ���԰��˻ذ���ȡ��
		return progressDialog;
	}
}
