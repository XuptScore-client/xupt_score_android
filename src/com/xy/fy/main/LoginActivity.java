package com.xy.fy.main;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.mc.util.HttpUtilMc;
import com.xy.fy.util.ConnectionUtil;
import com.xy.fy.util.HttpUtil;
import com.xy.fy.util.StaticVarUtil;
import com.xy.fy.util.ViewUtil;
import com.xy.fy.view.ToolClass;

@SuppressLint("HandlerLeak")
public class LoginActivity extends Activity {

	private EditText account;// 账号
	private EditText password;// 密码
	private Button forgetPassWord;// 忘记密码
	private CheckBox rememberPassword;// 记住密码
	private Button login;// 登陆
	private ProgressDialog progressDialog;
	private String session;// session
    //保存所有herf
	private List<HashMap<String, String>> listHerf;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.setContentView(R.layout.activity_login);

		Intent i = getIntent();
		session = i.getStringExtra("session");
		StaticVarUtil.session =session;
		this.findViewById();

		if (!this.initData()) {// 初始化一些必须的数据
			ViewUtil.toastLength("内存卡有问题", LoginActivity.this);
		}

		this.isRemember();// 是否是记住密码的

		ToolClass.map();// 初始化映射关系，防止以后用到

		// 忘记密码按钮
		this.forgetPassWord.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				/*
				 * Intent intent = new Intent();
				 * intent.setClass(getApplicationContext(),
				 * ForgetPasswordActivity.class); startActivity(intent);
				 */
				Toast.makeText(getApplicationContext(), "暂不可用，请持续关注。。。", 1000)
						.show();
			}
		});
		// 登陆按钮
		this.login.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String strAccount = account.getText().toString();
				String strPassword = password.getText().toString();
				try {
					Integer.parseInt(strAccount);
				} catch (Exception e) {
					ViewUtil.toastShort("账号必须为十位以内的数字！", LoginActivity.this);
					return;
				}
				if (strAccount == null || strAccount.equals("")
						|| strPassword.equals("") || strPassword == null) {
					ViewUtil.toastShort("账号密码不能为空", LoginActivity.this);
					return;
				}
				if (!ConnectionUtil.isConn(getApplicationContext())) {
					ConnectionUtil.setNetworkMethod(LoginActivity.this);
					return;
				}
				rememberPassword(strAccount, strPassword);
				login();
			}
		});
	}

	/**
	 * 初始化一些必须的数据
	 */
	private boolean initData() {
		boolean isSDcardExist = Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
		if (isSDcardExist) {
			File sdDir = Environment.getExternalStorageDirectory();// 获取根路径
			StaticVarUtil.PATH = sdDir.toString() + HttpUtil.FENGYUN;
			// 创建应用专用路径
			File file = new File(StaticVarUtil.PATH);
			if (!file.exists()) {
				try {
					file.mkdirs();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			// 创建fileCache文件夹
			file = new File(StaticVarUtil.PATH + "/fileCache");
			if (!file.exists()) {
				try {
					file.mkdirs();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			// 创建save文件夹
			file = new File(StaticVarUtil.PATH + "/save");
			if (!file.exists()) {
				try {
					file.mkdirs();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			// 创建jsonCache文件夹
			file = new File(StaticVarUtil.PATH + "/jsonCache");
			if (!file.exists()) {
				try {
					file.mkdirs();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			// 创建temp.png文件
			file = new File(StaticVarUtil.PATH + "/temp.JPEG");
			if (!file.exists()) {
				try {
					file.createNewFile();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			// 存放上传图片
			file = new File(StaticVarUtil.PATH + "/upload.JPEG");
			if (!file.exists()) {
				try {
					file.createNewFile();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 是否是记住密码
	 */
	private void isRemember() {
		SharedPreferences preferences = getSharedPreferences(
				StaticVarUtil.USER_INFO, MODE_PRIVATE);
		String account = preferences.getString(StaticVarUtil.ACCOUNT, "");
		String password = preferences.getString(StaticVarUtil.PASSWORD, "");
		boolean isRemember = preferences.getBoolean(StaticVarUtil.IS_REMEMBER,
				false);
		if (isRemember == true) {
			this.rememberPassword.setChecked(true);
			this.account.setText(account);
			this.password.setText(password);
		} else {
			this.rememberPassword.setChecked(false);
			this.account.setText(account);
			this.password.setText("");
		}

	}

	/**
	 * 记住密码
	 */
	private void rememberPassword(String account, String password) {
		SharedPreferences preferences = getSharedPreferences(
				StaticVarUtil.USER_INFO, MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putString(StaticVarUtil.ACCOUNT, account);
		if (rememberPassword.isChecked() == true) {
			editor.putString(StaticVarUtil.PASSWORD, password);
			editor.putBoolean(StaticVarUtil.IS_REMEMBER, true);// 记住密码
		} else {
			editor.putString(StaticVarUtil.PASSWORD, "");
			editor.putBoolean(StaticVarUtil.IS_REMEMBER, false);// 不记住密码
		}
		editor.commit();
	}

	/**
	 * 开启新线程登陆
	 */
	private void login() {

		LoginAsyntask loginAsyntask = new LoginAsyntask();
		loginAsyntask.execute();
	}

	/**
	 * 找到控件ID
	 */
	private void findViewById() {
		this.account = (EditText) findViewById(R.id.etAccount);
		this.password = (EditText) findViewById(R.id.etPassword);
		this.forgetPassWord = (Button) findViewById(R.id.butForgetPassword);
		this.rememberPassword = (CheckBox) findViewById(R.id.butRememberPassword);
		this.login = (Button) findViewById(R.id.butLogin);
		this.progressDialog = ViewUtil.getProgressDialog(LoginActivity.this,
				"正在登录");
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (!ConnectionUtil.isConn(getApplicationContext())) {
			ConnectionUtil.setNetworkMethod(LoginActivity.this);
			return;
		}
	}

	// 异步加载登录
	class LoginAsyntask extends AsyncTask<Object, String, String> {

		@Override
		protected String doInBackground(Object... params) {
			// TODO Auto-generated method stub
			String url;
			url = HttpUtilMc.BASE_URL + "login.jsp?username="
					+ account.getText().toString().trim() + "&password="
					+ password.getText().toString().trim() + "&session=" + session;
			System.out.println("url" + url);
			// 查询返回结果
			String result = HttpUtilMc.queryStringForPost(url);
			System.out.println("=========================  " + result);
			return result;

		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			// progress.cancel();
			try {
				if (!HttpUtilMc.CONNECT_EXCEPTION.equals(result)) {

					if (!result.equals("error")) {

						listHerf = new ArrayList<HashMap<String,String>>();
						JSONObject json = new JSONObject(result);
						JSONArray jsonArray = (JSONArray) json.get("listHerf");
                        for (int i = 0; i < jsonArray.length(); i++) {
							JSONObject o = (JSONObject) jsonArray.get(i);
							HashMap<String,String> map = new HashMap<String, String>();
							map.put("herf", o.getString("herf"));
							map.put("tittle", o.getString("tittle"));
							listHerf.add(map);
						}
                        StaticVarUtil.listHerf  = listHerf;//设置为静态
                        Intent intent = new Intent();
    					intent.setClass(LoginActivity.this, MainActivity.class);
    					startActivity(intent);
    					StaticVarUtil.student.setAccount(Integer.valueOf(account.getText().toString().trim()));
    					StaticVarUtil.student.setPassword( password.getText().toString().trim());
    					finish();
					} else {
						Toast.makeText(getApplicationContext(), "登录失败", 1)
								.show();
					}

				} else {
					Toast.makeText(getApplicationContext(),
							HttpUtilMc.CONNECT_EXCEPTION, 1).show();
					// progress.cancel();
				}
			} catch (Exception e) {
				// TODO: handle exception
				Log.i("LoginActivity", e.toString());
			}

		}

	}
}
