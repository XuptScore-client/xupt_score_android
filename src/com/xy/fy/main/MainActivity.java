package com.xy.fy.main;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map.Entry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import cn.sharesdk.onekeyshare.OnekeyShare;

import com.cardsui.example.MyPlayCard;
import com.fima.cardsui.objects.CardStack;
import com.fima.cardsui.views.CardUI;
import com.mc.db.DBConnection;
import com.mc.util.CircleImageView;
import com.mc.util.CustomRankListView;
import com.mc.util.CustomRankListView.OnAddFootListener;
import com.mc.util.CustomRankListView.OnFootLoadingListener;
import com.mc.util.HttpAssist;
import com.mc.util.HttpUtilMc;
import com.mc.util.LogcatHelper;
import com.mc.util.Passport;
import com.mc.util.SIMCardInfo;
import com.mc.util.Util;
import com.mc.util.VersionUpdate;
import com.slidingmenu.lib.SlidingMenu;
import com.slidingmenu.lib.SlidingMenu.OnOpenListener;
import com.xy.fy.adapter.ChooseHistorySchoolExpandAdapter;
import com.xy.fy.adapter.ChooseSchoolExpandAdapter;
import com.xy.fy.util.BitmapUtil;
import com.xy.fy.util.ShareUtil;
import com.xy.fy.util.StaticVarUtil;
import com.xy.fy.util.TestArrayAdapter;
import com.xy.fy.util.ViewUtil;
import com.xy.fy.view.HistoryCollege;
import com.xy.fy.view.ToolClass;

/**
 * ��һ����������������� ��ѯ�ɼ�
 * 
 * @author Administrator 2014-7-21
 */
@SuppressLint({ "ShowToast", "InflateParams" })
public class MainActivity extends Activity {
	private static int requestTimes = 0;// �������
	// share
	private static OnekeyShare share;
	private static ShareUtil shareUtil;
	// ����ɼ���map
	public static HashMap<String, String> mapScoreOne = null;// xn =1
	public static HashMap<String, String> mapScoreTwo = null;// xn = 2
	private static boolean isFirst = true;
	private static boolean is_show = false;

	public static SlidingMenu slidingMenu;// �໬���
	private Button chooseCollege;// ѡ��ѧУ��ť
	private Button chooseMsgKind;// ѡ��˵˵����
	private Button chooseMsgSort;// ѡ��˵˵����ʽ
	private CardUI mCardView;
	private TextView nickname;// �û���
	private String name;//
	private CircleImageView headPhoto;// ͷ��
	private LinearLayout menuBang = null;// �ɼ���ѯ
	private LinearLayout menuMyBukao = null;// ��������
	private LinearLayout menuMyPaiming = null;// �ҵ�����
	private LinearLayout menuIdea_back = null;// �������
	private LinearLayout menuSetting = null;// ����
	private LinearLayout menuAbout = null;// ����
	private Button check_version = null;
	ArrayList<HashMap<String, Object>> listItem;// json����֮����б�,���������еĳɼ�����
	//�������
	private TextView ideaMsgText = null;
	private TextView phoneText = null;
	// ����
	private final static int DEFAULTITEMSUM = 100;
	private static int lsitItemSum = DEFAULTITEMSUM;// ͨ��������Ļ�߶ȣ����Ӧ����ʾ������������listview
	private CustomRankListView allRankList;
	private TextView rankText;
	private TextView nameText;
	private TextView rankScoreText;
	private HashMap<String, String> allRankMap = new HashMap<String, String>();// ����ѧ���ѧ�ڵĳɼ�
	private ArrayList<HashMap<String, Object>> allRankArrayList;// ���е�����
	private ArrayList<HashMap<String, Object>> showRankArrayList;// Ӧ����ʾ������
	private AutoCompleteTextView search_edittext;
	private SimpleAdapter simpleAdapter;
	private String score_json;// json����
	private static boolean isFirstListView = true;

	private ProgressDialog dialog = null;

	private Bitmap bitmap = null;// �޸�ͷ��

	private static final int PIC = 11;// ͼƬ
	private static final int PHO = 22;// ����
	private static final int RESULT = 33;// ���ؽ��

	private ChooseSchoolExpandAdapter adapter = new ChooseSchoolExpandAdapter(
			MainActivity.this);

	@SuppressLint("ShowToast")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.setContentView(R.layout.activity_main);
		shareUtil = new ShareUtil(getApplicationContext());
		share = shareUtil.showShare();
		softDeclare();// ������ ���� ����Ϊ������
		dialog = ViewUtil.getProgressDialog(MainActivity.this, "���ڲ�ѯ");
		// init map
		mapScoreOne = new HashMap<String, String>();// tm ָ��ͬһ���ڴ��� ��
		mapScoreTwo = new HashMap<String, String>();
		setMenuItemListener();

		// ��ǰActivity��ջ
		StaticVarUtil.activities.add(MainActivity.this);
		// �ҵ�ID
		slidingMenu = (SlidingMenu) findViewById(R.id.slidingMenuXyScore);
		// ��sliding�������
		slidingMenu.setOnOpenListener(new OnOpenListener() {
			@Override
			public void onOpen() {
				// ��ȡ��ǰ�˵���ѡ��
				int item = getCurrentMeunItem();
				if (item == 1) {
					setMenuItemState(menuBang, true, menuMyBukao, false,
							menuMyPaiming, false, menuIdea_back, false,
							menuSetting, false, menuAbout, false);
				} else if (item == 2) {
					setMenuItemState(menuBang, false, menuMyBukao, true,
							menuMyPaiming, false, menuIdea_back, false,
							menuSetting, false, menuAbout, false);
				} else if (item == 3) {
					setMenuItemState(menuBang, false, menuMyBukao, false,
							menuMyPaiming, true, menuIdea_back, false,
							menuSetting, false, menuAbout, false);
				} else if (item == 4) {
					setMenuItemState(menuBang, false, menuMyBukao, false,
							menuMyPaiming, false, menuIdea_back, true,
							menuSetting, false, menuAbout, false);
				} else if (item == 5) {
					setMenuItemState(menuBang, false, menuMyBukao, false,
							menuMyPaiming, false, menuIdea_back, false,
							menuSetting, true, menuAbout, false);
				} else if (item == 6) {
					setMenuItemState(menuBang, false, menuMyBukao, false,
							menuMyPaiming, false, menuIdea_back, false,
							menuSetting, false, menuAbout, true);
				}
			}
		});

		if (Util.isNull(StaticVarUtil.student.getAccount())) {
			deleteCatch();
			LogcatHelper.getInstance(MainActivity.this).stop();
			Intent i = new Intent();
			i.setClass(getApplicationContext(), LoginActivity.class);
			startActivity(i);
			return;
		}
		if (!Util.checkPWD(StaticVarUtil.student.getPassword())) {
			ViewUtil.showToast(getApplicationContext(), "���벻��ȫ����������������");
			setMenuItemState(menuBang, false, menuMyBukao, false,
					menuMyPaiming, false, menuIdea_back, false, menuSetting,
					true, menuAbout, false);
			setCurrentMenuItem(5);// ��¼��ǰѡ��λ�ã�������ת
			slidingMenu.toggle();// ҳ����ת
			slidingMenu.setContent(R.layout.activity_setting);
			menuSetting();
		} else {
			// ���� ��ȡ �ɼ�
			GetScoreAsyntask getScoreAsyntask = new GetScoreAsyntask();
			dialog.show();
			getScoreAsyntask.execute();
		}
	}

	private void softDeclare() {
		// TODO Auto-generated method stub

	}

	/**
	 * ��һ���˵���
	 */
	private void menu1() {
		// �˵���ť
		Button menu = (Button) findViewById(R.id.butMenu);
		menu.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				slidingMenu.toggle();
			}
		});
		if (mCardView != null) {
			isFirst = false;
		}
		mCardView = (CardUI) findViewById(R.id.cardsview);
		mCardView.setSwipeable(true);
		ShowCardAsyntask showCardAsyntask = new ShowCardAsyntask();
		dialog.show();
		showCardAsyntask.execute();

	}

	class ShowCardAsyntask extends AsyncTask<String, String, Boolean> {
		@Override
		protected Boolean doInBackground(String... params) {
			Resources resources = getResources();
			String[] xn = resources.getStringArray(R.array.xn);
			boolean result = false;
			for (int i = 0; i < xn.length; i++) {
				result = showCard(xn[i], isFirst);// �����ӳ�̫�󣬿��Կ���ֱ�Ӵ��ڴ�ȡ
													// ����ÿ�μ��㡣(�Ѿ��޸�Ϊ���ڴ���ȡֵ)
			}
			return result;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			if (result) {
				mCardView.refresh();
			}
			if (!Util.isRecordLoginMessage(getApplicationContext())) {// �Ƿ��Ѿ��ϴ����ֻ���Ϣ
				// δ�ϴ�,�򱣴��ֻ���Ϣ������
				Util.saveDeviceInfo(getApplicationContext());
				// �ϴ�������Ϣ
				Util.uploadDevInfos(getApplicationContext());
			}
			dialog.cancel();
		}

	}

	/**
	 * ��ʾ��Ƭ
	 */
	private boolean showCard(String xn, boolean isFirst) {
		String first_score = "";
		String second_score = "";

		if (isFirst || (mapScoreOne.isEmpty() && mapScoreTwo.isEmpty())) {
			first_score = getScore(xn, "1") == null ? "" : getScore(xn, "1")
					.toString();
			second_score = getScore(xn, "2") == null ? "" : getScore(xn, "2")
					.toString();
			mapScoreOne.put(xn, first_score);
			mapScoreTwo.put(xn, second_score);
		} else {
			first_score = mapScoreOne.get(xn) == null ? "" : mapScoreOne
					.get(xn);
			second_score = mapScoreTwo.get(xn) == null ? "" : mapScoreTwo
					.get(xn);
		}

		// add one card, and then add another one to the last stack.
		String xqs_str = "";
		if (!first_score.equals("")) {
			xqs_str += "��һѧ��,";
			CardStack stackPlay = new CardStack();
			stackPlay.setTitle(xn);
			mCardView.addStack(stackPlay);
			MyPlayCard _myPlayCard = new MyPlayCard("��һѧ��", first_score,
					"#33b6ea", "#33b6ea", true, false);
			String[][] first_score_array = getScoreToArray(first_score);
			_myPlayCard.setOnClickListener(new ScoreClass(
					first_score_array.length, first_score_array, xn + " ��һѧ��"));
			mCardView.addCard(_myPlayCard);
			// mCardView.addCardToLastStack(new
			// MyCard("By Androguide & GadgetCheck"));
		}

		if (!second_score.equals("")) {
			xqs_str += "�ڶ�ѧ��,";
			MyPlayCard myCard = new MyPlayCard("�ڶ�ѧ��", second_score, "#e00707",
					"#e00707", false, true);
			String[][] second_score_array = getScoreToArray(second_score);
			myCard.setOnClickListener(new ScoreClass(second_score_array.length,
					second_score_array, xn + " �ڶ�ѧ��"));
			mCardView.addCardToLastStack(myCard);
		}
		if (xqs_str.length() != 0) {
			xqs_str = xqs_str.substring(0, xqs_str.length() - 1);
			StaticVarUtil.list_Rank_xnAndXq.put(xn, xqs_str);
		}

		return true;

	}

	/**
	 * �����Ƭ��ת
	 * 
	 * @author Administrator 2014-7-23
	 */
	class ScoreClass implements OnClickListener {
		int col;// �ɼ������к�
		String[][] score;// �����гɼ�����Ϊ��ά����
		String xn;

		public ScoreClass(int col, String[][] score, String xn) {
			this.col = col;
			this.score = score;
			this.xn = xn;
		}

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if (Util.isFastDoubleClick()) {
				return;
			}
			Intent i = new Intent();
			i.setClass(getApplicationContext(), ShowScoreActivity.class);
			Bundle b = new Bundle();
			b.putString("col", String.valueOf(col));
			for (int j = 0; j < score.length; j++) {
				b.putStringArray("score" + j, score[j]);
			}
			b.putString("xn_and_xq", xn);
			i.putExtras(b);
			startActivity(i);
		}

	}

	/**
	 * �� �ɼ����ϳ� n��4�е����飬Ϊ�˿�����ʾ��tableҳ����
	 * 
	 * @param score
	 * @return
	 */
	private String[][] getScoreToArray(String score) {
		String[] s = score.split("\n");
		String[][] result = new String[s.length][4];// n�� 4�е�����
		for (int i = 0; i < result.length; i++) {
			result[i] = s[i].split("--");
		}
		return result;
	}

	/**
	 * ���� ��ȡ�̶�ѧ�� �̶�ѧ�ڵĳɼ�
	 */
	private StringBuilder getScore(String xn, String xq) {
		StringBuilder result = null;
		if (listItem != null) {
			result = new StringBuilder();
			// ����json
			JSONObject jsonObject;
			try {
				jsonObject = new JSONObject(score_json);
				JSONArray jsonArray = (JSONArray) jsonObject
						.get("liScoreModels");// ѧ��
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject o = (JSONObject) jsonArray.get(i);
					if (o.get("xn").equals(xn)) {// ĳ��ѧ��
						JSONArray jsonArray2 = (JSONArray) o
								.get("list_xueKeScore");
						for (int j = 0; j < jsonArray2.length(); j++) {
							JSONObject jsonObject2 = (JSONObject) jsonArray2
									.get(j);
							if (jsonObject2.get("xq").equals(xq)) {
								// ��� �γ̴���->�γ���
								StaticVarUtil.kcdmList.put(jsonObject2
										.get("kcmc") == null ? " "
										: jsonObject2.get("kcmc").toString(),
										jsonObject2.get("kcdm").toString()
												+ "|" + xn + "|" + xq);
								result.append(jsonObject2.get("kcmc") == null ? " "
										: jsonObject2.get("kcmc").toString());// �γ�����
								result.append("--"
										+ jsonObject2.get("cj")// ���ճɼ�
												.toString()
										+ (jsonObject2.get("bkcj").equals(" ") ? " "
												: ("("
														+ jsonObject2.get(
																"bkcj")
																.toString() + ")"))// �������ɼ������ճɼ�ͬʱ��ʾ��
								);
								result.append(jsonObject2.get("pscj")// ƽʱ�ɼ�
										.equals("") ? "/" : "--"
										+ jsonObject2.get("pscj").toString());
								result.append(jsonObject2.get("qmcj")// ��ĩ�ɼ�
										.equals("") ? "/" : "--"
										+ jsonObject2.get("qmcj").toString());
								result.append("\n");
							}
						}
					}
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return result;
	}

	/**
	 * ���õ�ǰMenuItem��״̬
	 * 
	 * @param item
	 *            MenuItem�����flag�������״̬
	 */
	private void setMenuItemState(LinearLayout item1, boolean flag1,
			LinearLayout item2, boolean flag2, LinearLayout item3,
			boolean flag3, LinearLayout item4, boolean flag4,
			LinearLayout item5, boolean flag5, LinearLayout item6, boolean flag6) {
		item1.setPressed(flag1);
		item2.setPressed(flag2);
		item3.setPressed(flag3);
		item4.setPressed(flag4);
		item5.setPressed(flag5);
		item6.setPressed(flag6);
	}

	/**
	 * ����һЩmenuItem����
	 */
	private void setMenuItemListener() {

		mCardView = null;
		nickname = (TextView) findViewById(R.id.nickname);// �û���
		headPhoto = (CircleImageView) findViewById(R.id.headphoto);// ͷ��
		menuBang = (LinearLayout) findViewById(R.id.menu_bang);// 1.�ɼ���ѯ
		menuMyBukao = (LinearLayout) findViewById(R.id.menu_my_bukao);// 2.������ѯ
		menuMyPaiming = (LinearLayout) findViewById(R.id.menu_my_paiming);// 3.�ҵ�����
		menuIdea_back = (LinearLayout) findViewById(R.id.idea_back);// 4.���ղص�
		menuSetting = (LinearLayout) findViewById(R.id.menu_setting);// 5.����
		menuAbout = (LinearLayout) findViewById(R.id.menu_about);

		LinearLayout menuQuit = (LinearLayout) findViewById(R.id.menu_quit);
		menuBang.setPressed(true);// ��ʼ��Ĭ���Ƿ��ư񱻰���
		setCurrentMenuItem(1);// ��¼��ǰѡ��λ��

		// �����������ȡͷ��id �����жϱ����Ƿ�������ļ�
		GetPhotoID getPhotoID = new GetPhotoID();
		getPhotoID.execute();

		headPhoto.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated methodstub

				File file = new File(StaticVarUtil.PATH);
				if (!file.exists()) {
					file.mkdirs();// �����ļ�
				}
				chooseHeadPhoto();// �ı�ͷ��
			}
		});

		menuBang.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				setMenuItemState(menuBang, true, menuMyBukao, false,
						menuMyPaiming, false, menuIdea_back, false,
						menuSetting, false, menuAbout, false);
				slidingMenu.toggle();// ҳ����ת
				slidingMenu.setContent(R.layout.card_main);
				new Thread(new Runnable() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
						try {
							Thread.sleep(300);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						Message msg = new Message();
						msg.what = 1;
						mHandler.sendMessage(msg);
					}
				}).start();
			}
		});

		// ��������
		menuMyBukao.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// �ж����û��ͷ��Ļ�������ѡ��ͷ�񣬲���д�ǳ�
				// ������ת�������б�
				// showToast("����Գ������Ŭ�������У��������ע...");

				setMenuItemState(menuBang, false, menuMyBukao, true,
						menuMyPaiming, false, menuIdea_back, false,
						menuSetting, false, menuAbout, false);
				slidingMenu.toggle();// ҳ����ת
				slidingMenu.setContent(R.layout.activity_friend_list);
				new Thread(new Runnable() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
						try {
							Thread.sleep(300);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						Message msg = new Message();
						msg.what = 2;
						mHandler.sendMessage(msg);
					}
				}).start();
			}
		});

		menuMyPaiming.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (StaticVarUtil.list_Rank_xnAndXq.size() != 0) {

					setMenuItemState(menuBang, false, menuMyBukao, false,
							menuMyPaiming, true, menuIdea_back, false,
							menuSetting, false, menuAbout, false);
					slidingMenu.toggle();// ҳ����ת
					slidingMenu.setContent(R.layout.activity_rank);
					new Thread(new Runnable() {
						@Override
						public void run() {
							// TODO Auto-generated method stub
							try {
								Thread.sleep(300);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							Message msg = new Message();
							msg.what = 3;
							mHandler.sendMessage(msg);
						}
					}).start();
				} else {
					ViewUtil.showToast(getApplicationContext(), "���粻�ȶ������Ժ��ѯ");
				}
			}
		});

		menuIdea_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				slidingMenu.toggle();// ҳ����ת
				slidingMenu.setContent(R.layout.activity_ideaback);
				new Thread(new Runnable() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
						try {
							Thread.sleep(300);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						Message msg = new Message();
						msg.what = 4;
						mHandler.sendMessage(msg);
					}
				}).start();
			}
		});

		menuSetting.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				setMenuItemState(menuBang, false, menuMyBukao, false,
						menuMyPaiming, false, menuIdea_back, false,
						menuSetting, true, menuAbout, false);
				slidingMenu.toggle();// ҳ����ת
				slidingMenu.setContent(R.layout.activity_setting);
				new Thread(new Runnable() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
						try {
							Thread.sleep(300);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						Message msg = new Message();
						msg.what = 5;
						mHandler.sendMessage(msg);
					}
				}).start();

			}
		});

		menuAbout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				setMenuItemState(menuBang, false, menuMyBukao, false,
						menuMyPaiming, false, menuIdea_back, false,
						menuSetting, false, menuAbout, true);

				slidingMenu.toggle();// ҳ����ת
				slidingMenu.setContent(R.layout.activity_about);

				new Thread(new Runnable() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
						try {
							Thread.sleep(300);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						Message msg = new Message();
						msg.what = 6;
						mHandler.sendMessage(msg);
					}
				}).start();
			}
		});

		menuQuit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				quit(true);
			}
		});

	}

	private void aboutListener() {
		if (Util.getAndroidSDKVersion() > 10) {
			findViewById(R.id.newTip).setAlpha(100);
		}
		// �˵���ť
		Button menu = (Button) findViewById(R.id.butMenu);
		menu.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				slidingMenu.toggle();
			}
		});

		ImageView qrcode_imageview = (ImageView) findViewById(R.id.code);
		qrcode_imageview.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showDialogSaveQrcode();

			}
		});
		findViewById(R.id.guanwang).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Uri uri = Uri.parse("http://www.xiyoumobile.com");
				Intent it = new Intent(Intent.ACTION_VIEW, uri);
				startActivity(it);
			}
		});
		findViewById(R.id.email).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent data = new Intent(Intent.ACTION_SENDTO);
				data.setData(Uri.parse("69449212@qq.com"));
				data.putExtra(Intent.EXTRA_SUBJECT, "�����ʳɼ�������");
				data.putExtra(Intent.EXTRA_TEXT, " ");
				startActivity(data);
			}
		});
		check_version = (Button) findViewById(R.id.checkversion);// ����°汾��ť
		TextView version = (TextView) findViewById(R.id.version);
		version.setText(Util.getVersion(getApplicationContext()));
		check_version.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				CheckVersionAsyntask checkVersionAsyntask = new CheckVersionAsyntask();
				is_show = true;
				dialog.show();
				checkVersionAsyntask.execute();
			}
		});
	}

	/**
	 * �����ά��
	 */
	private void showDialogSaveQrcode() {
		final CharSequence[] items = { "�����ά��" };
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setItems(items, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int item) {
				if (!new File(StaticVarUtil.PATH + Util.QRCODE_FILENAME)
						.exists()) {
					Bitmap bt = ((BitmapDrawable) getApplicationContext()
							.getResources().getDrawable(R.drawable.qrcode))
							.getBitmap();
					BitmapUtil.saveFileAndDB(getApplicationContext(), bt,
							Util.QRCODE_FILENAME);
					bt.recycle();
				}
				ViewUtil.showToast(getApplicationContext(), "��ά���ѱ��棬�뽫������ͬѧ��");

			}
		});
		AlertDialog alert = builder.create();
		alert.show();
	}

	String selectXn;
	String selectXq;
	// TODO Auto-generated method stub
	Spinner xnSpinner;
	Spinner xqSpinner;
	ArrayAdapter<String> xnAdapter;
	ArrayAdapter<String> xqAdapter;

	private void rank() {

		// menu���� �ж�Ϊ��һ�� Ϊ�˳�ʼ�� listview
		isFirstListView = true;
		findviewById();
		rankScoreText.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (!rankText.getText().equals("")) {
					allRankList.setSelection(Integer.parseInt(rankText
							.getText().toString()) - 1);
				}
			}
		});
		// search
		search_edittext.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				if (search_edittext.getText().toString().length() > 0) {
					// ��λ
					for (HashMap<String, Object> map : showRankArrayList) {
						if ((map.get("name").toString()).equals(search_edittext
								.getText().toString())) {
							search_edittext.clearFocus();
							closeInputMethod();
							allRankList.setSelection(Integer.parseInt(map.get(
									"rankId").toString()) - 1);

						}
					}
				}
			}
		});
		search_edittext.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (KeyEvent.KEYCODE_ENTER == keyCode
						&& event.getAction() == KeyEvent.ACTION_DOWN) {
					boolean isSearch = false;
					for (HashMap<String, Object> map : showRankArrayList) {
						if ((map.get("name").toString()).equals(search_edittext
								.getText().toString())) {
							allRankList.setSelection(Integer.parseInt(map.get(
									"rankId").toString()) - 1);
							isSearch = true;
						}
					}
					if (!isSearch) {
						ViewUtil.showToast(getApplicationContext(), "û�и�ѧ����Ϣ");
					}
					search_edittext.clearFocus();
					closeInputMethod();
					return true;
				}
				return false;

			}
		});

		// �˵���ť
		Button menu = (Button) findViewById(R.id.butMenu);
		menu.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				slidingMenu.toggle();
			}
		});
		// ����ť
		Button share = (Button) findViewById(R.id.share);
		share.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// ViewUtil.showShare(getApplicationContext());
				shareUtil.showShareUI(MainActivity.share);
			}
		});
		// rankListViewListener();

		nameText.setText(name);
		WindowManager wm = this.getWindowManager();
		@SuppressWarnings("deprecation")
		int width = wm.getDefaultDisplay().getWidth() / 4 + 10;
		xnSpinner = (Spinner) findViewById(R.id.xnSpinner);
		xnSpinner.setLayoutParams(new LinearLayout.LayoutParams(width,
				LinearLayout.LayoutParams.WRAP_CONTENT));

		// xnSpinner.setDropDownWidth(width);
		xqSpinner = (Spinner) findViewById(R.id.xqSpinner);
		xqSpinner.setLayoutParams(new LinearLayout.LayoutParams(width,
				LinearLayout.LayoutParams.WRAP_CONTENT));
		String[] xns = new String[StaticVarUtil.list_Rank_xnAndXq.size()];
		int i = 0;

		Iterator<?> it = StaticVarUtil.list_Rank_xnAndXq.entrySet().iterator();
		while (it.hasNext()) {
			Entry<?, ?> entry = (Entry<?, ?>) it.next();
			xns[i] = String.valueOf(entry.getKey());// ����������Ӧ�ļ�
			i++;
			// entry.getValue() ����������Ӧ��ֵ
		}
		// ����dropDownItem ���
		xnAdapter = new TestArrayAdapter(R.layout.list_item,
				getApplicationContext(), xns, width);// ����Adapter
		String xq = StaticVarUtil.list_Rank_xnAndXq.get(xns[0]);// Ĭ�ϵ�һѧ����׸�ѧ������
		String[] xqs = xq.split("\\,");
		xqAdapter = new TestArrayAdapter(R.layout.list_item,
				getApplicationContext(), xqs, width);// ����Adapter
		xnAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // ѡ�������˵���ʽ
		xqAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // ѡ�������˵���ʽ
		xnSpinner.setAdapter(xnAdapter);
		xnSpinner.setSelection(0, false);// �������������̬
		xqSpinner.setAdapter(xqAdapter);
		xqSpinner.setSelection(0, false);
		listener(xns, width);
		String result = "";// �ɼ�������

		if (allRankMap.size() != 0) {
			for (String xnAndXq : allRankMap.keySet()) {// ������ menu���������Ա����ж�
				if (xnAndXq.equals(selectXn + selectXq)) {// ���������ֱ�� ����value
					result = allRankMap.get(xnAndXq);
					refeshRank(result, isFirstListView);
					break;
				}
			}
		} else {
			requestRankAsyntask();
		}
	}

	private void findviewById() {
		// TODO Auto-generated method stub
		allRankList = (CustomRankListView) findViewById(R.id.allRank);
		rankScoreText = (TextView) findViewById(R.id.score);
		search_edittext = (AutoCompleteTextView) findViewById(R.id.search);
		rankText = (TextView) findViewById(R.id.rank);
		nameText = (TextView) findViewById(R.id.name);
	}

	/**
	 * ȡ�������
	 */
	private void closeInputMethod() {
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		boolean isOpen = imm.isActive();
		if (isOpen) {
			imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);// û����ʾ����ʾ
			// imm.hideSoftInputFromWindow(mobile_topup_num.getWindowToken(),
			// InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}

	public void onBackPressed() {
		ViewUtil.cancelToast();
		super.onBackPressed();
	}

	/**
	 * ����ˢ��
	 */
	@SuppressWarnings("unused")
	private void rankListViewListener() {
		// TODO Auto-generated method stub
		// ����FootView
		final View footer = View.inflate(getApplicationContext(),
				R.layout.rank_footer, null);
		allRankList.setOnAddFootListener(new OnAddFootListener() {
			@Override
			public void addFoot() {
				allRankList.addFooterView(footer);
			}
		});
		allRankList.setOnFootLoadingListener(new OnFootLoadingListener() {
			@Override
			public void onFootLoading() {
				new AsyncTask<Void, Void, ArrayList<HashMap<String, Object>>>() {
					@Override
					protected ArrayList<HashMap<String, Object>> doInBackground(
							Void... params) {
						try {
							// ģ��ӷ�������ȡ���ݵĹ���
							Thread.sleep(2000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						// �ٴζ�ȡ10������
						ArrayList<HashMap<String, Object>> virtualData = new ArrayList<HashMap<String, Object>>();
						for (int i = lsitItemSum; i < allRankArrayList.size(); i++) {
							virtualData.add(allRankArrayList.get(i));
							lsitItemSum += 1;
						}
						// �����µĴ�С
						return virtualData;
					}

					// ��doInBackground����ִ��
					@Override
					protected void onPostExecute(
							ArrayList<HashMap<String, Object>> result) {
						showRankArrayList.addAll(result);// ����������������
						simpleAdapter.notifyDataSetChanged();
						allRankList.onFootLoadingComplete();// �������ˢ��,���ǵײ��������,�������Ҫ����
						// �Ƴ�footer,�������������
						allRankList.removeFooterView(footer);
						super.onPostExecute(result);
					}
				}.execute();
			}
		});
	}

	private void requestRankAsyntask() {
		// Ĭ��
		selectXn = xnSpinner.getSelectedItem().toString();
		selectXq = xqSpinner.getSelectedItem().toString();
		rankRequestParmas(StaticVarUtil.student.getAccount() + "|"
				+ selectXn.split("\\-")[0] + "|"
				+ (selectXq.equals("��һѧ��") ? "1" : "2"));
		String result = "";
		// ���Ȳ�ѯ�ڴ����Ƿ��и�ѧ�ڳɼ�
		for (String xnAndXq : allRankMap.keySet()) {
			if (xnAndXq.equals(selectXn + selectXq)) {// ���������ֱ�� ����value
				result = allRankMap.get(xnAndXq);
				refeshRank(result, isFirstListView);
				break;
			}
		}
		if (result.equals("")) {// �����ڣ�������
			GetRankAsyntask getRankAsyntask = new GetRankAsyntask();
			dialog.show();
			getRankAsyntask.execute();
		}

	}

	@SuppressWarnings("deprecation")
	private void rankRequestParmas(String data) {
		long time = System.currentTimeMillis();
		// String s = new char[]{3,2,3,4,3,8,3,8,3,2,3,2}.toString();
		try {
			String time_s = Passport.jiami(String.valueOf(time),
					String.valueOf(new char[] { 2, 4, 8, 8, 2, 2 }));
			String realData = Passport.jiami(data, String.valueOf(time));
			@SuppressWarnings("static-access")
			String imei = ((TelephonyManager) getApplicationContext()
					.getSystemService(getApplicationContext().TELEPHONY_SERVICE))
					.getDeviceId();
			imei = Passport.jiami(imei, String.valueOf(time));
			realData = URLEncoder.encode(realData);
			time_s = URLEncoder.encode(time_s);
			StaticVarUtil.data = realData;
			StaticVarUtil.viewstate = time_s;
			StaticVarUtil.content = imei;
			// ��֤data ����Ϊ�������г��ִ���
			String checkData = Util.checkRankRequestData(realData, time_s);
			if (!checkData.equals(data)) {
				rankRequestParmas(data);// �ݹ��ٴμ��㣬ֱ���������ȷ��
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	boolean isTouchXNSpinner = false;
	boolean isTouchXQSpinner = false;

	@SuppressLint("ClickableViewAccessibility")
	private void listener(final String[] xns, final int width) {
		// TODO Auto-generated method stub

		xnSpinner
				.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {

					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						// TODO Auto-generated method stub
						if (isTouchXNSpinner) {
							lsitItemSum = DEFAULTITEMSUM;// ����ΪĬ��
							// ��spinner�ϵ�ѡ�����ʾ��TextView����
							selectXn = xnAdapter.getItem(arg2);
							// �Զ����� ѧ��
							String xq = StaticVarUtil.list_Rank_xnAndXq
									.get(xns[arg2]);// Ĭ�ϵ�һѧ����׸�ѧ������
							String[] xqs = xq.split("\\,");
							xqAdapter = new TestArrayAdapter(
									R.layout.list_item,
									getApplicationContext(), xqs, width);// ����Adapter
							xqSpinner.setAdapter(xqAdapter);
							requestRankAsyntask();
							isTouchXNSpinner = false;
						}
					}

					public void onNothingSelected(AdapterView<?> arg0) {
						// TODO Auto-generated method stub
					}
				});
		xnSpinner.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				isTouchXNSpinner = true;
				return false;
			}
		});
		xqSpinner
				.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {

					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						// TODO Auto-generated method stub
						if (isTouchXQSpinner) {
							lsitItemSum = DEFAULTITEMSUM;// ����ΪĬ��
							// ��spinner�ϵ�ѡ�����ʾ��TextView����
							selectXq = xqAdapter.getItem(arg2);
							requestRankAsyntask();
							isTouchXQSpinner = false;
						}
					}

					public void onNothingSelected(AdapterView<?> arg0) {
						// TODO Auto-generated method stub
					}

				});
		xqSpinner.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub

				isTouchXQSpinner = true;

				return false;
			}
		});
	}

	// �����б�
	protected void friend_list() {
		// TODO Auto-generated method stub
		// �˵���ť
		Button menu = (Button) findViewById(R.id.butMenu);
		menu.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				slidingMenu.toggle();
			}
		});

		// ��Ӻ��Ѱ�ť
		Button add_friend = (Button) findViewById(R.id.add_friend);
		add_friend.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent();
				i.setClass(MainActivity.this, AddFriendListActivity.class);
				startActivity(i);
			}
		});

	}

	/**
	 * �޸ĸ�����Ϣ��ֻ���޸��ǳƣ����룬ͷ��
	 */
	protected void menuSetting() {

		// �˵���ť
		Button menu = (Button) findViewById(R.id.butMenu);
		menu.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				slidingMenu.toggle();
			}
		});

		if (StaticVarUtil.student == null) {
			ViewUtil.showToast(getApplicationContext(), "�Բ��𣬲鿴ѡ�����ȵ�¼");
			return;
		}
		EditText etAccount = (EditText) findViewById(R.id.etAccount);
		etAccount.setText(StaticVarUtil.student.getAccount() + "");// �޸Ķ���ʾһ��0������
		etAccount.setEnabled(false);// ������
		final EditText etPassword1 = (EditText) findViewById(R.id.etPassword1);
		final EditText etPassword2 = (EditText) findViewById(R.id.etPassword2);
		final EditText cofPassword2 = (EditText) findViewById(R.id.corfimPassword2);// ȷ������
		// �޸�
		Button butAlter = (Button) findViewById(R.id.butAlter);
		butAlter.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// �ؼ�ֵ
				String password1 = etPassword1.getText().toString();
				String password2 = etPassword2.getText().toString().trim();
				String password3 = cofPassword2.getText().toString().trim();
				if (password1.equals("") && password2.equals("")
						&& bitmap == null && password3.equals("")) {
					ViewUtil.showToast(getApplicationContext(), "��û����Ϣ��Ҫ�޸�");
					return;
				}

				// ����
				if (password1.equals("") && password2.equals("")) {// ������޸�

				} else {
					if (password1.equals(StaticVarUtil.student.getPassword())
							&& password2.equals("") == false) {
					} else {
						ViewUtil.showToast(getApplicationContext(),
								"�����벻��ȷ���������벻��Ϊ��,�������");
						return;
					}
				}
				if (password2.equals(password3)) {
					if (!Util.hasDigitAndNum(password2)) {
						ViewUtil.showToast(getApplicationContext(),
								"�����б���������ֺ���ĸ");
					} else {
						if (password2.length() < 6) {// �����޸�������볬��6λ
							ViewUtil.showToast(getApplicationContext(),
									"������볬��6λ");
						} else {
							ChangePwAsyntask changePwAsyntask = new ChangePwAsyntask();
							changePwAsyntask.execute(new String[] { password1,
									password2 });
						}

					}

				} else {
					ViewUtil.showToast(getApplicationContext(), "�����벻��ȷ");

					return;
				}

			}
		});

	}

	/**
	 * ѡ��ͷ��
	 * 
	 * @return
	 */
	protected void chooseHeadPhoto() {
		String[] items = new String[] { "ѡ�񱾵�ͼƬ", "����" };
		new AlertDialog.Builder(this)
				.setTitle("����ͷ��")
				.setItems(items, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						switch (which) {
						case 0:// ѡ�񱾵�ͼƬ
							Intent intent = new Intent();
							intent.setType("image/*");
							intent.setAction(Intent.ACTION_GET_CONTENT);
							startActivityForResult(intent, PIC);
							break;
						case 1:// ����
							Intent intent2 = new Intent(
									MediaStore.ACTION_IMAGE_CAPTURE);
							Uri imageUri = Uri.fromFile(new File(
									StaticVarUtil.PATH, "temp.JPEG"));
							// ָ����Ƭ����·����SD������image.jpgΪһ����ʱ�ļ���ÿ�����պ����ͼƬ���ᱻ�滻
							intent2.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
							startActivityForResult(intent2, PHO);
							break;
						}
					}
				})
				.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				}).show();
	}

	/**
	 * ȡ�ûش�������
	 */
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// �������벻��ȡ����ʱ��
		if (resultCode != RESULT_CANCELED) {
			switch (requestCode) {
			case PHO:
				File tempFile = new File(StaticVarUtil.PATH + "/temp.JPEG");
				startPhotoZoom(Uri.fromFile(tempFile));
				break;
			case PIC:
				// ��Ƭ��ԭʼ��Դ��ַ
				Uri originalUri = data.getData();
				startPhotoZoom(originalUri);
				break;
			case RESULT:
				if (data != null) {
					Bundle extras = data.getExtras();
					if (extras != null) {
						bitmap = extras.getParcelable("data");
					}
					bitmap = BitmapUtil.resizeBitmapWidth(bitmap, 240);// ������Ϊ240���ص�ͼƬ
					BitmapUtil.saveBitmapToFile(bitmap, StaticVarUtil.PATH,
							StaticVarUtil.student.getAccount() + ".JPEG");
					headPhoto.setImageBitmap(bitmap);
					// �ϴ�ͷ��
					UploadFileAsytask uploadFileAsytask = new UploadFileAsytask();
					uploadFileAsytask.execute(new String[] { StaticVarUtil.PATH
							+ "/" + StaticVarUtil.student.getAccount()
							+ ".JPEG" });
				}
				break;
			default:
				break;
			}
		} else {
			bitmap = null;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	/**
	 * �ü�ͼƬ����ʵ��
	 * 
	 * @param uri
	 */
	public void startPhotoZoom(Uri uri) {

		Intent intent = new Intent("com.android.camera.action.CROP");// ����ϵͳ�Ľ�ͼ���ܡ�
		intent.setDataAndType(uri, "image/*");
		// ���òü�
		intent.putExtra("crop", "true");
		// aspectX aspectY �ǿ�ߵı���
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// outputX outputY �ǲü�ͼƬ���
		intent.putExtra("outputX", 320);
		intent.putExtra("outputY", 320);
		intent.putExtra("scale", true);// �ڱ�
		intent.putExtra("scaleUpIfNeeded", true);// �ڱ�
		intent.putExtra("return-data", true);
		startActivityForResult(intent, RESULT);
	}

	/**
	 * �ж��Ƿ�����ȷѡ���ѧ��-1�������0�������У��������ִ���ͬ��ѧ
	 */
	private int judgeCollegeId() {
		int collegeId = -1;
		try {
			// ��������д�ѧ��ֱ�ӷ���0
			if (chooseCollege.getText().toString().equals("���д�ѧ")) {
				return 0;// ֱ�ӷ������д�ѧ
			}
			// �����ĳһ��ѧ������Id
			collegeId = Integer.parseInt(ToolClass.nameIdTreeMap
					.get(chooseCollege.getText().toString()));
		} catch (Exception e) {
			// ������-1
			System.out.println("û�������ѧ");
			collegeId = -1;
		}
		return collegeId;
	}

	/**
	 * ѡ���У����У����ѡ����ʷ
	 */
	protected void chooseCollege() {
		LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
		View view = inflater.inflate(R.layout.choose_school, null);

		// ��ʾ��ѧ��ʷadapterHistory.notifyDataSetChanged();
		ArrayList<CharSequence> allCollege = readHistory2();
		HistoryCollege.initData(allCollege);// ��ʼ������Դ
		ChooseHistorySchoolExpandAdapter adapterHistory = new ChooseHistorySchoolExpandAdapter(
				MainActivity.this);
		ExpandableListView expandHistory = (ExpandableListView) view
				.findViewById(R.id.expandHistory);
		expandHistory.setAdapter(adapterHistory);

		// �ҵ��ؼ�expandListView
		ExpandableListView expandListView = (ExpandableListView) view
				.findViewById(R.id.expandListView);
		expandListView.setAdapter(adapter);

		final Dialog dialog = new AlertDialog.Builder(MainActivity.this)
				.setView(view).create();
		dialog.setCancelable(true);
		dialog.show();

		// ѡ���ѧ����
		expandListView.setOnChildClickListener(new OnChildClickListener() {
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
				String schoolName = ToolClass.schoolsList.get(groupPosition)
						.get(childPosition).toString();
				dialog.cancel();
				chooseCollege.setText(schoolName);
				return true;
			}
		});

		// ѡ����ʷ����
		expandHistory.setOnChildClickListener(new OnChildClickListener() {
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
				String schoolName = HistoryCollege.allHistory
						.get(groupPosition).get(childPosition).toString();
				dialog.cancel();
				chooseCollege.setText(schoolName);
				return true;
			}
		});
	}

	private String new_version;// ���°汾
	private String update_content;// ��������
	private static String apk_url;// ���ص�ַ
	private Button download_version;// ���ذ汾
	private Button cancle_check;// ȡ��

	/**
	 * ˢ��
	 * 
	 * @param messageSort
	 *            0������ڹ�ע1�������չ�ע2������ư�
	 */
	@SuppressLint("SimpleDateFormat")
	private void refresh(int collegeId, int messageKind, int messageSort) {
		// ����������ʷ������������ѧУ��ÿ�ζ����棩�����࣬����ʽ��ֻ���浱ǰ��Σ�Ϊ���ٴν���ʱ��ֱ����ʾ��
		saveHistory();
		// �������߳�
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE) + 6);
		StaticVarUtil.fileName = "jsonCache.txt";// ���ñ����ļ�������
	}

	/**
	 * ���������ʷ
	 */
	private void saveHistory() {
		SharedPreferences share = getSharedPreferences("history", MODE_PRIVATE);
		Editor editor = share.edit();
		editor.putString("msgKind", chooseMsgKind.getText().toString());// �����´ε�¼��ʾ
		editor.putString("msgSort", chooseMsgSort.getText().toString());// �����´ε�¼��ʾ
		editor.putString("theLastCollege", chooseCollege.getText().toString());// �����´ε�¼��ʾ

		// ����ѡ���ѧ��ʾ
		HashSet<String> set = (HashSet<String>) share.getStringSet("college",
				new HashSet<String>());
		if (judgeCollegeId() > 0) {
			if (!set.contains(chooseCollege.getText().toString())) {// ����������ͼ���
				set.add(chooseCollege.getText().toString());
			}
		}
		editor.putStringSet("college", set);// �����ȥ
		editor.commit();// ͬ������Ӳ������
	}

	/**
	 * ��ȡ��ʷ��¼������ʾ����ѡ����Ĵ�ѧ
	 */
	private ArrayList<CharSequence> readHistory2() {
		SharedPreferences share = getSharedPreferences("history", MODE_PRIVATE);
		// ��ʾ���һ�μ�¼,û����ʾĬ��
		HashSet<String> set = (HashSet<String>) share.getStringSet("college",
				null);
		ArrayList<CharSequence> allHistory = null;// Ҫ���ص�����
		if (set != null) {// ��Ϊ�յĻ����������ݵ���ʾ
			allHistory = new ArrayList<CharSequence>();
			for (String string : set) {
				allHistory.add(string);
			}
		}
		return allHistory;
	}

	/**
	 * ���ֻ���ť�ļ���
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:// ����Ƿ��ذ�ť,�˳�
			if (getCurrentMeunItem() != 1) {// ���ڵ�һ��ҳ��,���ص�һ��ҳ��
				menuBang.setPressed(true);// ��ʼ��Ĭ���Ƿ��ư񱻰���
				setCurrentMenuItem(1);// ��¼��ǰѡ��λ��
				slidingMenu.setContent(R.layout.card_main);
				menu1();
			} else
				quit(false);

			break;
		case KeyEvent.KEYCODE_MENU:// ����ǲ˵���ť
			slidingMenu.toggle();
			break;
		default:
			break;
		}
		return true;
	}

	/**
	 * ����ڴ���еĹ�������
	 */
	private void deleteCatch() {
		StaticVarUtil.list_Rank_xnAndXq.clear();
		allRankArrayList = null;
		mapScoreOne = null;
		mapScoreTwo = null;
		StaticVarUtil.quit();
		isFirstListView = true;
		// ��ճɼ�����
		isFirst = true;
	}

	/**
	 * �˳�ģ��
	 * 
	 * @param logout
	 *            �Ƿ�ע��
	 */
	private void quit(final boolean logout) {
		Builder builder = new AlertDialog.Builder(MainActivity.this);

		if (logout) {
			builder.setMessage("��ȷ��Ҫע����");
		} else {
			builder.setMessage("��ȷ��Ҫ�˳���");
		}
		builder.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
				deleteCatch();
				LogcatHelper.getInstance(MainActivity.this).stop();
				if (logout) {
					Intent i = new Intent();
					i.setClass(getApplicationContext(), LoginActivity.class);
					startActivity(i);
				}
			}
		});
		builder.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});
		Dialog dialog = builder.create();
		dialog.show();
	}

	/**
	 * ��¼���õ�ǰMenuItem��λ�ã�1��2��3��4��5�ֱ����ɼ���ѯ��������ѯ���ҵ����������ղصģ�ѡ��
	 * 
	 * @param menuItem
	 *            �˵���ѡ��
	 */
	private void setCurrentMenuItem(int menuItem) {
		SharedPreferences preferences = getSharedPreferences("currentMenuItem",
				MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putInt("item", menuItem);
		editor.commit();
	}

	/**
	 * ȡ�õ�ǰMenuItem��λ��
	 * 
	 * @return ��ǰ��menu�Ĳ˵��� 1��2��3��4��5�ֱ����ɼ���ѯ��������ѯ���ҵ����������ղصģ�ѡ��,0����û�����
	 */
	private int getCurrentMeunItem() {
		SharedPreferences preferences = getSharedPreferences("currentMenuItem",
				MODE_PRIVATE);
		int flag = preferences.getInt("item", 0);
		return flag;
	}

	// �첽���ػ�ȡ�ɼ�
	class GetScoreAsyntask extends AsyncTask<Object, String, String> {

		@Override
		protected String doInBackground(Object... params) {
			// TODO Auto-generated method stub
			String url = "";
			String canshu = Util.getURL(StaticVarUtil.QUERY_SCORE);
			String[] can = canshu.split("&");
			String url_str = can[0];
			String xm = can[1];
			name = xm.split("=")[1];
			String gnmkdm = can[2];
			try {
				url = HttpUtilMc.BASE_URL
						+ "xscjcx.aspx?session="
						+ StaticVarUtil.session
						+ "&url="
						+ url_str
						+ "&xm="
						+ URLEncoder.encode(
								URLEncoder.encode(xm.split("=")[1], "utf8"),
								"utf8") + "&" + gnmkdm;
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// ��ѯ���ؽ��
			String result = HttpUtilMc.queryStringForPost(url);
			return result;

		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			// ��ʾ�û���
			nickname.setText(name);
			try {
				if (!HttpUtilMc.CONNECT_EXCEPTION.equals(result)) {
					if (!result.equals("error")) {
						/**
						 * ���ַ��� д��xml�ļ���
						 */
						if (!result.equals("no_evaluation")) {
							requestTimes = 0;
							score_json = result;
							listItem = new ArrayList<HashMap<String, Object>>();
							JSONObject jsonObject = new JSONObject(result);
							JSONArray jsonArray = (JSONArray) jsonObject
									.get("liScoreModels");// ������array
							for (int i = 0; i < jsonArray.length(); i++) {
								JSONObject o = (JSONObject) jsonArray.get(i);
								HashMap<String, Object> map = new HashMap<String, Object>();
								map.put("xn", o.get("xn"));
								map.put("list_xueKeScore",
										o.get("list_xueKeScore"));
								listItem.add(map);
							}
						} else {
							Builder builder = new AlertDialog.Builder(
									MainActivity.this);

							builder.setMessage("�뵽�������н�ʦ���ۺ��ѯ�ɼ���");

							builder.setPositiveButton("ȷ��",
									new DialogInterface.OnClickListener() {
										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											quit(true);
										}
									});
							builder.create().show();
						}
						menu1();

					} else {
						ViewUtil.showToast(getApplicationContext(), "��ѯʧ��");
					}

				} else {
					if (requestTimes < 5) {
						requestTimes++;
						GetScoreAsyntask getScoreAsyntask = new GetScoreAsyntask();
						getScoreAsyntask.execute();
					} else
						ViewUtil.showToast(getApplicationContext(),
								HttpUtilMc.CONNECT_EXCEPTION);
				}
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				Log.i("LoginActivity", e.toString());
			}

		}

	}

	/**
	 * ��ȡ �ɼ�����
	 * 
	 * @author Administrator 2015-2-8
	 */
	class GetRankAsyntask extends AsyncTask<String, String, String> {

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			return HttpUtilMc.queryStringForPost(HttpUtilMc.BASE_URL
					+ "RankServlet.jsp?data=" + StaticVarUtil.data
					+ "&viewstate=" + StaticVarUtil.viewstate + "&content="
					+ StaticVarUtil.content);
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);

			nickname.setText(name);
			try {
				if (!HttpUtilMc.CONNECT_EXCEPTION.equals(result)) {
					if (!result.equals("error")) {
						/**
						 * ���ַ��� д��xml�ļ���
						 */
						if (!result.equals("")) {
							dialog.cancel();
							requestTimes = 0;
							refeshRank(result, isFirstListView);
							allRankMap.put(selectXn + selectXq, result);// �����ݱ��浽�ڴ��У��´ξͲ����ظ���ȡ��
						}
					} else {
						ViewUtil.showToast(getApplicationContext(), "��ѯʧ��");
					}
				} else {
					if (requestTimes < 4) {
						requestTimes++;
						GetRankAsyntask getRankAsyntask = new GetRankAsyntask();
						getRankAsyntask.execute();
					} else {
						ViewUtil.showToast(getApplicationContext(),
								HttpUtilMc.CONNECT_EXCEPTION);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				Log.i("LoginActivity", e.toString());
			}

		}

	}

	/**
	 * 
	 * @param result
	 *            �ɼ��ַ���
	 * @param isFirst
	 *            �Ƿ��ǵ�һ��
	 */
	private void refeshRank(String result, boolean isFirst) {
		try {
			if (allRankArrayList == null & showRankArrayList == null) {
				allRankArrayList = new ArrayList<HashMap<String, Object>>();
				showRankArrayList = new ArrayList<HashMap<String, Object>>();
			} else {
				allRankArrayList.clear();
				showRankArrayList.clear();
			}

			JSONObject jsonObject = new JSONObject(result);
			String rank = String.valueOf(jsonObject.getString("rank"));
			rankText.setText(rank);

			JSONArray jsonArray = (JSONArray) jsonObject.get("rankList");
			int rankId = 0;
			for (int i = jsonArray.length() - 1; i >= 0; i--) {
				JSONObject o = (JSONObject) jsonArray.get(i);
				HashMap<String, Object> map = new HashMap<String, Object>();
				rankId += 1;
				map.put("rankId", String.valueOf(rankId));
				map.put("name", o.get("name"));
				map.put("xh", o.get("xh"));
				map.put("score", o.get("score"));
				allRankArrayList.add(map);
				if (String.valueOf(rankId).equals(rank)) {
					rankScoreText.setText(o.get("score").toString());// ��ʾ�ɼ�
					Animation animation = AnimationUtils
							.loadAnimation(getApplicationContext(),
									R.anim.textscore_translate);
					rankScoreText.setAnimation(animation);
				}
			}
			// ��ȡ ֮ǰ��õĹ̶� ������item�� ��ֹ������̫�󣬶����µĽ��������ݶ���ʾ������
			for (int i = 0; i < (lsitItemSum > allRankArrayList.size() ? allRankArrayList
					.size() : lsitItemSum); i++) {
				showRankArrayList.add(allRankArrayList.get(i));
			}
			if (isFirst) {
				setListView();
				isFirstListView = false;
			} else {
				simpleAdapter.notifyDataSetChanged();
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void menuIdeaBack() {
		Button menu = (Button) findViewById(R.id.butMenu);
		menu.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				slidingMenu.toggle();
			}
		});
		ideaMsgText = (TextView) findViewById(R.id.text);
		phoneText = (TextView) findViewById(R.id.phone);
		findViewById(R.id.send).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				SIMCardInfo siminfo = new SIMCardInfo(getApplicationContext());
				final String number = siminfo.getNativePhoneNumber();
				final String data = ideaMsgText.getText().toString().trim()
						+ "|"
						+ ("".equals(phoneText.getText().toString().trim()) ? ""
								: phoneText.getText().toString().trim()) + "|"
						+ StaticVarUtil.student.getAccount() + "|"
						+ Util.getVersion(getApplicationContext()) + "|"
						+ number;
				new Thread(new Runnable() {
					@Override
					public void run() {
						Util.sendMail(data);
						Message msg = new Message();
						msg.what = 8;
						mHandler.sendMessage(msg);
					}
				}).start();
			}
		});
	}

	private Handler mHandler = new Handler() {

		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				menu1();
				setCurrentMenuItem(1);// ��¼��ǰѡ��λ��
				break;
			case 2:
				friend_list();
				setCurrentMenuItem(2);// ��¼��ǰѡ��λ��
				break;
			case 3:
				rank();
				setCurrentMenuItem(3);// ��¼��ǰѡ��λ��
				break;
			case 4:
				menuIdeaBack();
				setCurrentMenuItem(4);// ��¼��ǰѡ��λ��
				break;
			case 5:
				menuSetting();
				setCurrentMenuItem(5);// ��¼��ǰѡ��λ��
				break;
			case 6:
				aboutListener();
				setCurrentMenuItem(6);// ��¼��ǰѡ��λ��
				break;
			case 7:
				showShareQrcodeDialog();
				break;
			case 8:
				ViewUtil.showToast(getApplicationContext(), "��л����");
				break;
			// ��ȡ���ݵ�����
			// Bundle data = msg.getData();
			// int count = data.getInt("COUNT");
			// ����UI���µȲ���
			}
		};
	};

	// ��ʾ ������listview
	private void setListView() {
		// TODO Auto-generated method stub
		if (showRankArrayList.size() < DEFAULTITEMSUM) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					try {
						Thread.sleep(300);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					Message msg = new Message();
					msg.what = 7;
					mHandler.sendMessage(msg);
				}
			}).start();

		}
		simpleAdapter = new SimpleAdapter(getApplicationContext(),
				showRankArrayList, R.layout.allrank_listitem, new String[] {
						"rankId", "name", "score" }, new int[] { R.id.rankId,
						R.id.name, R.id.score });
		allRankList.setAdapter(simpleAdapter);

	}

	private void showShareQrcodeDialog() {
		Builder builder = new AlertDialog.Builder(MainActivity.this);

		builder.setMessage("���ڱ�רҵʹ����������,�������������\n�����ѯ׼ȷ������������ͬѧ���ظ������");

		builder.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// ViewUtil.showShare(getApplicationContext());
				shareUtil.showShareUI(MainActivity.share);
			}
		});
		builder.create().show();
	}

	// �첽�ı�����
	class ChangePwAsyntask extends AsyncTask<String, String, String> {

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			String canshu = Util.getURL(StaticVarUtil.CHANGE_PW);
			return HttpUtilMc.queryStringForPost(HttpUtilMc.BASE_URL
					+ "changepw.jsp?session=" + StaticVarUtil.session + "&url="
					+ canshu + "&old_password=" + params[0] + "&new_password="
					+ params[1]);

		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			// progress.cancel();
			// ��ʾ�û���
			nickname.setText(name);
			try {
				if (HttpUtilMc.CONNECT_EXCEPTION.equals(result)) {
					ViewUtil.showToast(getApplicationContext(),
							HttpUtilMc.CONNECT_EXCEPTION);
					return;
				}
				ViewUtil.showToast(getApplicationContext(),
						!result.equals("error") ? "�޸ĳɹ�,�����µ�¼" : "�޸Ĳ��ɹ�");
				if (!result.equals("error")) {
					quit(true);// ע�����µ�¼
				}
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				Log.i("LoginActivity", e.toString());
			}

		}

	}

	// �������ȡͷ��
	class GetPhotoID extends AsyncTask<String, String, String> {

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			return HttpUtilMc.queryStringForPost(HttpUtilMc.BASE_URL
					+ "getuserphoto.jsp?username="
					+ StaticVarUtil.student.getAccount());
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if ("no_photo".equals(result) || result.split("/").length < 2) {
				return;
			}
			StaticVarUtil.PHOTOFILENAME = result.split("/")[1];
			// �ж� ͷ���ļ������Ƿ���� ���û���ͷ��
			if (DBConnection.getPhotoName(StaticVarUtil.student.getAccount(),
					getApplicationContext())
					.equals(StaticVarUtil.PHOTOFILENAME)
					&& new File(StaticVarUtil.PATH + "/"
							+ StaticVarUtil.student.getAccount() + ".JPEG")
							.exists()) {// �������
				Bitmap bitmap = Util.convertToBitmap(StaticVarUtil.PATH + "/"
						+ StaticVarUtil.student.getAccount() + ".JPEG", 240,
						240);
				if (bitmap != null) {
					headPhoto.setImageBitmap(bitmap);
				}
			} else {// ����ļ����в��������ͷ��
				GetPicture getPicture = new GetPicture();
				getPicture.execute(new String[] { HttpUtilMc.BASE_URL
						+ "user_photo/" + StaticVarUtil.PHOTOFILENAME });
			}
		}

	}

	// �������ȡͷ��
	class GetPicture extends AsyncTask<String, Bitmap, Bitmap> {

		@Override
		protected Bitmap doInBackground(String... params) {
			// TODO Auto-generated method stub
			return Util.getBitmap(params[0]);
		}

		@Override
		protected void onPostExecute(Bitmap bitmap) {
			// TODO Auto-generated method stub
			super.onPostExecute(bitmap);
			if (bitmap == null)
				return;

			if (Util.isExternalStorageWritable()) {
				Util.saveBitmap2file(bitmap, StaticVarUtil.PHOTOFILENAME,
						getApplicationContext());
			}
			headPhoto.setImageBitmap(Util.convertToBitmap(StaticVarUtil.PATH
					+ "/" + StaticVarUtil.student.getAccount() + ".JPEG", 240,
					240));// ��ʾͼƬ
			bitmap.recycle();
		}

	}

	// �첽���汾
	class CheckVersionAsyntask extends AsyncTask<String, String, String> {

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			return HttpUtilMc.queryStringForPost(HttpUtilMc.BASE_URL
					+ "checkversion.jsp?version="
					+ Util.getVersion(getApplicationContext()));
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			if (is_show) {
				dialog.cancel();
			}
			try {
				if (!HttpUtilMc.CONNECT_EXCEPTION.equals(result)) {
					if (!result.equals("no")) {// �����°汾

						String[] str = result.split("\\|");
						apk_url = str[0];
						new_version = str[1];
						update_content = str[2];
						VersionUpdate versionUpdate = new VersionUpdate(
								MainActivity.this);
						versionUpdate.apkUrl = HttpUtilMc.IP + apk_url;
						versionUpdate.updateMsg = new_version + "\n\n"
								+ update_content;
						return;
					}
					ViewUtil.showToast(getApplicationContext(), "�Ѿ������°汾��");
				}
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				Log.i("LoginActivity", e.toString());
			}
		}
	}

	/**
	 * �ϴ�ͷ��
	 * 
	 * @author mc 2014-4-28
	 */
	class UploadFileAsytask extends AsyncTask<String, String, String> {
		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			return HttpAssist.uploadFile(new File(params[0]),
					StaticVarUtil.student.getAccount());
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			try {
				if (result.equals("error")) {
					return;
				}
				if (Util.isExternalStorageWritable()) {
					Util.saveBitmap2file(bitmap, result,
							getApplicationContext());
					bitmap.recycle();
				}
				ViewUtil.showToast(
						getApplicationContext(),
						!HttpUtilMc.CONNECT_EXCEPTION.equals(result) ? !result
								.equals("error") ? "�޸ĳɹ�" : "�޸�ʧ��"
								: HttpUtilMc.CONNECT_EXCEPTION);
			} catch (Exception e) {
				Log.i("LoginActivity", e.toString());
			}
		}
	}

}
