package com.xy.fy.main;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
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

import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.baidu.mapapi.SDKInitializer;
import com.bmob.im.demo.CustomApplcation;
import com.bmob.im.demo.MyMessageReceiver;
import com.bmob.im.demo.bean.User;
import com.bmob.im.demo.config.BmobConstants;
import com.bmob.im.demo.config.Config;
import com.bmob.im.demo.ui.BaseActivity;
import com.bmob.im.demo.ui.NewFriendActivity;
import com.bmob.im.demo.ui.fragment.ContactFragment;
import com.bmob.im.demo.ui.fragment.RecentFragment;
import com.bmob.im.demo.ui.fragment.SettingsFragment;
import com.cardsui.example.MyPlayCard;
import com.fima.cardsui.objects.CardStack;
import com.fima.cardsui.views.CardUI;
import com.mc.db.DBConnection;
import com.mc.util.BadgeUtil;
import com.mc.util.CircleImageView;
import com.mc.util.CustomRankListView;
import com.mc.util.CustomRankListView.OnAddFootListener;
import com.mc.util.CustomRankListView.OnFootLoadingListener;
import com.mc.util.H5Log;
import com.mc.util.H5Toast;
import com.mc.util.HttpAssist;
import com.mc.util.HttpUtilMc;
import com.mc.util.LogcatHelper;
import com.mc.util.Passport;
import com.mc.util.RankUtils;
import com.mc.util.SIMCardInfo;
import com.mc.util.Util;
import com.mc.util.VersionUpdate;
import com.slidingmenu.lib.SlidingMenu;
import com.slidingmenu.lib.SlidingMenu.OnOpenListener;
import com.xy.fy.adapter.ChooseHistorySchoolExpandAdapter;
import com.xy.fy.adapter.ChooseSchoolExpandAdapter;
import com.xy.fy.util.BitmapUtil;
import com.xy.fy.util.ConnectionUtil;
import com.xy.fy.util.ShareUtil;
import com.xy.fy.util.ShortcutUtils;
import com.xy.fy.util.StaticVarUtil;
import com.xy.fy.util.TestArrayAdapter;
import com.xy.fy.util.ViewUtil;
import com.xy.fy.view.HistoryCollege;
import com.xy.fy.view.ToolClass;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
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
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
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
import cn.bmob.im.BmobChat;
import cn.bmob.im.BmobChatManager;
import cn.bmob.im.BmobNotifyManager;
import cn.bmob.im.bean.BmobInvitation;
import cn.bmob.im.bean.BmobMsg;
import cn.bmob.im.config.BmobConfig;
import cn.bmob.im.db.BmobDB;
import cn.bmob.im.inteface.EventListener;
import cn.bmob.im.util.BmobLog;
import cn.bmob.v3.BmobInstallation;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;
import cn.sharesdk.onekeyshare.OnekeyShare;

public class MainActivity extends BaseActivity implements EventListener {

  private final static String TAG = "MainActivity";
  public static HashMap<String, String> mapScoreOne = null;// xn =1
  public static HashMap<String, String> mapScoreTwo = null;// xn = 2
  private static int requestTimes = 0;
  private static OnekeyShare share;
  private static ShareUtil shareUtil;
  private static boolean isFirst = true;
  private static boolean is_show = false;
  private final static int DEFAULTITEMSUM = 100;
  private static int lsitItemSum = DEFAULTITEMSUM;
  private static final int PIC = 11;// ͼƬ
  private static final int PHO = 22;// ����
  private static final int RESULT = 33;// ���ؽ��
  public static TextView bukao_tip = null;
  private static boolean isFirstListView = true;

  public static SlidingMenu slidingMenu;
  private Button chooseCollege;
  private Button chooseMsgKind;
  private Button chooseMsgSort;
  private CardUI mCardView;
  private TextView nickname;
  private String name;//
  private CircleImageView headPhoto;
  private LinearLayout menuBang = null;
  private LinearLayout menuMyBukao = null;
  private LinearLayout menuMyPaiming = null;
  private LinearLayout menuMyCjTongji = null;
  private LinearLayout menuMyCET = null;
  private LinearLayout menuIdea_back = null;
  private LinearLayout menuSetting = null;// ����
  private LinearLayout menuAbout = null;// ����
  private Button check_version = null;

  ArrayList<HashMap<String, Object>> listItem;// json����֮����б�,���������еĳɼ�����
  private TextView ideaMsgText = null;
  private TextView phoneText = null;

  private CustomRankListView allRankList;
  private TextView rankText;
  private TextView nameText;
  private TextView rankScoreText;
  private HashMap<String, String> allRankMap = new HashMap<String, String>();// ����ѧ���ѧ�ڵĳɼ�
  private AutoCompleteTextView search_edittext;
  private SimpleAdapter simpleAdapter;
  private String score_json;// json����

  private ProgressDialog dialog = null;

  private Bitmap bitmap = null;// �޸�ͷ��
  private MyHandler mHandler;

  private ChooseSchoolExpandAdapter adapter = new ChooseSchoolExpandAdapter(MainActivity.this);

  @SuppressLint("ShowToast")
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    requestWindowFeature(Window.FEATURE_NO_TITLE);

    super.setContentView(R.layout.activity_main);
    mHandler = new MyHandler(this);
    // ������ݷ�ʽ
    final Intent launchIntent = getIntent();
    final String action = launchIntent.getAction();
    if (Intent.ACTION_CREATE_SHORTCUT.equals(action)) {
      Log.i(TAG, "create shortcut method one---------------- ");
      setResult(RESULT_OK, ShortcutUtils.getShortcutToDesktopIntent(MainActivity.this));

      finish();
    }

    BadgeUtil.resetBadgeCount(getApplicationContext());
    CheckVersionAsyntask checkVersionAsyntask = new CheckVersionAsyntask();
    checkVersionAsyntask.execute();
    shareUtil = new ShareUtil(getApplicationContext());
    share = shareUtil.showShare();
    softDeclare();// ������ ���� ����Ϊ������
    dialog = ViewUtil.getProgressDialog(MainActivity.this, "���ڲ�ѯ");
    dialog.setCancelable(false);
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
        if (item == StaticVarUtil.MENU_BANG) {
          setMenuItemState(menuBang, true, menuMyBukao, false, menuMyCjTongji, false, menuMyCET,
              false, menuMyPaiming, false, menuIdea_back, false, menuSetting, false, menuAbout,
              false);
        } else if (item == StaticVarUtil.MENU_BUKAO) {
          setMenuItemState(menuBang, false, menuMyBukao, true, menuMyCjTongji, false, menuMyCET,
              false, menuMyPaiming, false, menuIdea_back, false, menuSetting, false, menuAbout,
              false);
        } else if (item == StaticVarUtil.MENU_CJ_TJ) {
          setMenuItemState(menuBang, false, menuMyBukao, false, menuMyCjTongji, true, menuMyCET,
              false, menuMyPaiming, false, menuIdea_back, false, menuSetting, false, menuAbout,
              false);
        } else if (item == StaticVarUtil.MENU_PAIMING) {
          setMenuItemState(menuBang, false, menuMyBukao, false, menuMyCjTongji, false, menuMyCET,
              false, menuMyPaiming, true, menuIdea_back, false, menuSetting, false, menuAbout,
              false);
        } else if (item == StaticVarUtil.MENU_IDEA_BACK) {
          setMenuItemState(menuBang, false, menuMyBukao, false, menuMyCjTongji, false, menuMyCET,
              false, menuMyPaiming, false, menuIdea_back, true, menuSetting, false, menuAbout,
              false);
        } else if (item == StaticVarUtil.MENU_SETTING) {
          setMenuItemState(menuBang, false, menuMyBukao, false, menuMyCjTongji, false, menuMyCET,
              false, menuMyPaiming, false, menuIdea_back, false, menuSetting, true, menuAbout,
              false);
        } else if (item == StaticVarUtil.MENU_ABOUT) {
          setMenuItemState(menuBang, false, menuMyBukao, false, menuMyCjTongji, false, menuMyCET,
              false, menuMyPaiming, false, menuIdea_back, false, menuSetting, false, menuAbout,
              true);
        } else if (item == StaticVarUtil.MENU_CET) {
          setMenuItemState(menuBang, false, menuMyBukao, false, menuMyCjTongji, false, menuMyCET,
              true, menuMyPaiming, false, menuIdea_back, false, menuSetting, false, menuAbout,
              false);
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
      setMenuItemState(menuBang, false, menuMyBukao, false, menuMyCjTongji, false, menuMyCET, false,
          menuMyPaiming, false, menuIdea_back, false, menuSetting, true, menuAbout, false);
      setCurrentMenuItem(StaticVarUtil.MENU_SETTING);
      slidingMenu.toggle();// ҳ����ת
      slidingMenu.setContent(R.layout.activity_setting);
      menuSetting();
    } else {
      // request get score
      GetScoreAsyntask getScoreAsyntask = new GetScoreAsyntask();
      dialog.show();
      getScoreAsyntask.execute();

    }
  }

  private void softDeclare() {
    // TODO Auto-generated method stub

  }

  /*
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

  /*
   * ��ʾ��Ƭ
   */
  private boolean showCard(String xn, boolean isFirst) {
    String first_score = "";
    String second_score = "";

    if (isFirst || (mapScoreOne.isEmpty() && mapScoreTwo.isEmpty())) {
      first_score = getScore(xn, "1") == null ? "" : getScore(xn, "1").toString();
      second_score = getScore(xn, "2") == null ? "" : getScore(xn, "2").toString();
      mapScoreOne.put(xn, first_score);
      mapScoreTwo.put(xn, second_score);
    } else {
      first_score = mapScoreOne.get(xn) == null ? "" : mapScoreOne.get(xn);
      second_score = mapScoreTwo.get(xn) == null ? "" : mapScoreTwo.get(xn);
    }

    // add one card, and then add another one to the last stack.
    String xqs_str = "";
    if (!first_score.equals("")) {
      xqs_str += "��һѧ��,";
      CardStack stackPlay = new CardStack();
      stackPlay.setTitle(xn);
      mCardView.addStack(stackPlay);
      MyPlayCard _myPlayCard = new MyPlayCard("��һѧ��", first_score, "#33b6ea", "#33b6ea", true,
          false);
      String[][] first_score_array = getScoreToArray(first_score);
      _myPlayCard.setOnClickListener(
          new ScoreClass(first_score_array.length, first_score_array, xn + " ��һѧ��"));
      mCardView.addCard(_myPlayCard);
      // mCardView.addCardToLastStack(new
      // MyCard("By Androguide & GadgetCheck"));
    }

    if (!second_score.equals("")) {
      xqs_str += "�ڶ�ѧ��,";
      MyPlayCard myCard = new MyPlayCard("�ڶ�ѧ��", second_score, "#e00707", "#e00707", false, true);
      String[][] second_score_array = getScoreToArray(second_score);
      myCard.setOnClickListener(
          new ScoreClass(second_score_array.length, second_score_array, xn + " �ڶ�ѧ��"));
      mCardView.addCardToLastStack(myCard);
    }
    if (xqs_str.length() != 0) {
      xqs_str = xqs_str.substring(0, xqs_str.length() - 1);
      StaticVarUtil.list_Rank_xnAndXq.put(xn, xqs_str);
    }

    return true;

  }

  /*
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

  /*
   * �� �ɼ����ϳ� n��4�е����飬Ϊ�˿�����ʾ��tableҳ����
   * 
   * @param score
   * 
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

  /*
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
        JSONArray jsonArray = (JSONArray) jsonObject.get("liScoreModels");// ѧ��
        for (int i = 0; i < jsonArray.length(); i++) {
          JSONObject o = (JSONObject) jsonArray.get(i);
          if (o.get("xn").equals(xn)) {// ĳ��ѧ��
            JSONArray jsonArray2 = (JSONArray) o.get("list_xueKeScore");
            for (int j = 0; j < jsonArray2.length(); j++) {
              JSONObject jsonObject2 = (JSONObject) jsonArray2.get(j);
              if (jsonObject2.get("xq").equals(xq)) {
                // ��� �γ̴���->�γ���
                StaticVarUtil.kcdmList.put(
                    jsonObject2.get("kcmc") == null ? " " : jsonObject2.get("kcmc").toString(),
                    jsonObject2.get("kcdm").toString() + "|" + xn + "|" + xq);
                result.append(
                    jsonObject2.get("kcmc") == null ? " " : jsonObject2.get("kcmc").toString());// �γ�����
                result.append("--"
                    + jsonObject2.get("cj")// ���ճɼ�
                        .toString()
                    + (jsonObject2.get("bkcj").equals(" ") ? " "
                        : ("(" + jsonObject2.get("bkcj").toString() + ")"))// �������ɼ������ճɼ�ͬʱ��ʾ��
                );
                result.append(jsonObject2.get("pscj")// ƽʱ�ɼ�
                    .equals("") ? "/" : "--" + jsonObject2.get("pscj").toString());
                result.append(jsonObject2.get("qmcj")// ��ĩ�ɼ�
                    .equals("") ? "/" : "--" + jsonObject2.get("qmcj").toString());
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

  /*
   * ���õ�ǰMenuItem��״̬
   * 
   * @param item MenuItem�����flag�������״̬
   */
  private void setMenuItemState(LinearLayout item1, boolean flag1, LinearLayout item2,
      boolean flag2, LinearLayout item3, boolean flag3, LinearLayout item4, boolean flag4,
      LinearLayout item5, boolean flag5, LinearLayout item6, boolean flag6, LinearLayout item7,
      boolean flag7, LinearLayout item8, boolean flag8) {
    item1.setPressed(flag1);
    item2.setPressed(flag2);
    item3.setPressed(flag3);
    item4.setPressed(flag4);
    item5.setPressed(flag5);
    item6.setPressed(flag6);
    item7.setPressed(flag7);
    item8.setPressed(flag8);
  }

  /*
   * ����һЩmenuItem����
   */
  private void setMenuItemListener() {

    mCardView = null;
    nickname = (TextView) findViewById(R.id.nickname);// �û���
    headPhoto = (CircleImageView) findViewById(R.id.headphoto);// ͷ��
    menuBang = (LinearLayout) findViewById(R.id.menu_bang);// 1.�ɼ���ѯ
    menuMyBukao = (LinearLayout) findViewById(R.id.menu_my_bukao);// 2.������ѯ
    iv_bukao_tips = (ImageView) findViewById(R.id.iv_bukao_tips);

    menuMyCjTongji = (LinearLayout) findViewById(R.id.menu_my_cj_tongji);// �ɼ�ͳ��
    menuMyCjTongji.setVisibility(View.GONE);
    menuMyPaiming = (LinearLayout) findViewById(R.id.menu_my_paiming);// 3.�ҵ�����
    menuMyCET = (LinearLayout) findViewById(R.id.menu_my_cet);// CET���
    menuIdea_back = (LinearLayout) findViewById(R.id.idea_back);// 4.���ղص�
    menuSetting = (LinearLayout) findViewById(R.id.menu_setting);// 5.����
    menuAbout = (LinearLayout) findViewById(R.id.menu_about);
    bukao_tip = (TextView) findViewById(R.id.bukao_tip);

    LinearLayout menuQuit = (LinearLayout) findViewById(R.id.menu_quit);
    menuBang.setPressed(true);// ��ʼ��Ĭ���Ƿ��ư񱻰���
    setCurrentMenuItem(StaticVarUtil.MENU_BANG);// ��¼��ǰѡ��λ��

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
        setMenuItemState(menuBang, true, menuMyBukao, false, menuMyCjTongji, false, menuMyCET,
            false, menuMyPaiming, false, menuIdea_back, false, menuSetting, false, menuAbout,
            false);
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
            msg.what = StaticVarUtil.MENU_BANG;
            mHandler.sendMessage(msg);
          }
        }).start();
      }
    });

    // ��������
    menuMyBukao.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {

        setMenuItemState(menuBang, false, menuMyBukao, true, menuMyCjTongji, false, menuMyCET,
            false, menuMyPaiming, false, menuIdea_back, false, menuSetting, false, menuAbout,
            false);
        slidingMenu.toggle();// ҳ����ת
        // �ж����û��ͷ��Ļ�������ѡ��ͷ�񣬲���д�ǳ�
        // ������ת�������б�
        // showToast("����Գ������Ŭ�������У��������ע...");
        slidingMenu.setContent(R.layout.activity_chat_main);
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
            msg.what = StaticVarUtil.BMOB_CHAT;
            mHandler.sendMessage(msg);
          }
        }).start();

        /*
         * setMenuItemState(menuBang, false, menuMyBukao, true, menuMyPaiming, false, menuIdea_back,
         * false, menuSetting, false, menuAbout, false); slidingMenu.toggle();// ҳ����ת
         * slidingMenu.setContent(R.layout.activity_friend_list); new Thread(new Runnable() {
         * 
         * @Override public void run() { // TODO Auto-generated method stub try { Thread.sleep(300);
         * } catch (InterruptedException e) { // TODO Auto-generated catch block
         * e.printStackTrace(); } Message msg = new Message(); msg.what = 2;
         * mHandler.sendMessage(msg); } }).start();
         */
      }
    });

    menuMyPaiming.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        if (StaticVarUtil.list_Rank_xnAndXq.size() != 0) {

          setMenuItemState(menuBang, false, menuMyBukao, false, menuMyCjTongji, false, menuMyCET,
              false, menuMyPaiming, true, menuIdea_back, false, menuSetting, false, menuAbout,
              false);
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
              msg.what = StaticVarUtil.MENU_PAIMING;
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
            msg.what = StaticVarUtil.MENU_IDEA_BACK;
            mHandler.sendMessage(msg);
          }
        }).start();
      }
    });

    menuSetting.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        setMenuItemState(menuBang, false, menuMyBukao, false, menuMyCjTongji, false, menuMyCET,
            false, menuMyPaiming, false, menuIdea_back, false, menuSetting, true, menuAbout, false);
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
            msg.what = StaticVarUtil.MENU_SETTING;
            mHandler.sendMessage(msg);
          }
        }).start();

      }
    });

    menuMyCET.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        setMenuItemState(menuBang, false, menuMyBukao, false, menuMyCjTongji, false, menuMyCET,
            true, menuMyPaiming, false, menuIdea_back, false, menuSetting, false, menuAbout, false);
        slidingMenu.toggle();// ҳ����ת
        slidingMenu.setContent(R.layout.activity_cet);
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
            msg.what = StaticVarUtil.MENU_CET;
            mHandler.sendMessage(msg);
          }
        }).start();

      }
    });

    menuAbout.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        setMenuItemState(menuBang, false, menuMyBukao, false, menuMyCjTongji, false, menuMyCET,
            false, menuMyPaiming, false, menuIdea_back, false, menuSetting, false, menuAbout, true);

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
            msg.what = StaticVarUtil.MENU_ABOUT;
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

  // ��������

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
    TextView guanwang = (TextView) findViewById(R.id.ip);
    String ip = "http://www.xiyoumobile.com";
    CharSequence cs = Html.fromHtml(ip);
    guanwang.setText(cs);
    guanwang.setMovementMethod(LinkMovementMethod.getInstance());
    findViewById(R.id.email).setOnClickListener(new OnClickListener() {

      @Override
      public void onClick(View v) {
        // TODO Auto-generated method stub
        Intent emailIntent = new Intent(Intent.ACTION_SEND);

        emailIntent.putExtra(Intent.EXTRA_EMAIL, "ideaback_mc@126.com");
        emailIntent.putExtra(Intent.EXTRA_CC, "ideaback_mc@126.com");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "");
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "���ʳɼ�");
        emailIntent.setType("message/rfc822");
        startActivity(Intent.createChooser(emailIntent, "xiyouMc"));

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

  /*
   * �����ά��
   */
  private void showDialogSaveQrcode() {
    final CharSequence[] items = { "�����ά��" };
    AlertDialog.Builder builder = new AlertDialog.Builder(this);
    builder.setItems(items, new DialogInterface.OnClickListener() {
      public void onClick(DialogInterface dialog, int item) {
        if (!new File(StaticVarUtil.PATH + Util.QRCODE_FILENAME).exists()) {
          Bitmap bt = ((BitmapDrawable) getApplicationContext().getResources()
              .getDrawable(R.drawable.qrcode)).getBitmap();
          BitmapUtil.saveFileAndDB(getApplicationContext(), bt, Util.QRCODE_FILENAME);
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
    if (rankScoreText == null) {
      return;
    }
    rankScoreText.setOnClickListener(new OnClickListener() {

      @Override
      public void onClick(View v) {
        // TODO Auto-generated method stub
        if (!rankText.getText().equals("")) {
          allRankList.setSelection(Integer.parseInt(rankText.getText().toString()) - 1);
        }
      }
    });
    // search
    search_edittext.addTextChangedListener(new TextWatcher() {

      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {
        // TODO Auto-generated method stub

      }

      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        // TODO Auto-generated method stub

      }

      @Override
      public void afterTextChanged(Editable s) {
        // TODO Auto-generated method stub
        if (search_edittext.getText().toString().length() > 0) {
          // ��λ
          for (HashMap<String, Object> map : RankUtils.showRankArrayList) {
            if ((map.get("name").toString()).equals(search_edittext.getText().toString())) {
              search_edittext.clearFocus();
              closeInputMethod();
              allRankList.setSelection(Integer.parseInt(map.get("rankId").toString()) - 1);

            }
          }
        }
      }
    });
    search_edittext.setOnKeyListener(new OnKeyListener() {

      @Override
      public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (KeyEvent.KEYCODE_ENTER == keyCode && event.getAction() == KeyEvent.ACTION_DOWN) {
          boolean isSearch = false;
          for (HashMap<String, Object> map : RankUtils.showRankArrayList) {
            if ((map.get("name").toString()).equals(search_edittext.getText().toString())) {
              allRankList.setSelection(Integer.parseInt(map.get("rankId").toString()) - 1);
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
    xnSpinner.setLayoutParams(
        new LinearLayout.LayoutParams(width, LinearLayout.LayoutParams.WRAP_CONTENT));

    // xnSpinner.setDropDownWidth(width);
    xqSpinner = (Spinner) findViewById(R.id.xqSpinner);
    xqSpinner.setLayoutParams(
        new LinearLayout.LayoutParams(width, LinearLayout.LayoutParams.WRAP_CONTENT));
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
    xnAdapter = new TestArrayAdapter(R.layout.list_item, getApplicationContext(), xns, width);// ����Adapter
    String xq = StaticVarUtil.list_Rank_xnAndXq.get(xns[0]);// Ĭ�ϵ�һѧ����׸�ѧ������
    String[] xqs = xq.split("\\,");
    xqAdapter = new TestArrayAdapter(R.layout.list_item, getApplicationContext(), xqs, width);// ����Adapter
    xnAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // ѡ�������˵���ʽ
    xqAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // ѡ�������˵���ʽ
    xnSpinner.setAdapter(xnAdapter);
    xnSpinner.setSelection(0, false);// �������������̬
    xqSpinner.setAdapter(xqAdapter);
    xqSpinner.setSelection(0, false);
    listener(xns, width);
    String result = "";// �ɼ�������

    if (allRankMap.size() != 0) {
      for (String xnAndXq : allRankMap.keySet()) {
        // ������ menu���������Ա����ж�
        if (xnAndXq.equals(selectXn + selectXq)) {
          // ���������ֱ�� ����value
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

  /*
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

  // public void onBackPressed() {
  // ViewUtil.cancelToast();
  // super.onBackPressed();
  // }

  /*
   * ����ˢ��
   */
  @SuppressWarnings("unused")
  private void rankListViewListener() {
    // TODO Auto-generated method stub
    // ����FootView
    final View footer = View.inflate(getApplicationContext(), R.layout.rank_footer, null);
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
          protected ArrayList<HashMap<String, Object>> doInBackground(Void... params) {
            try {
              // ģ��ӷ�������ȡ���ݵĹ���
              Thread.sleep(2000);
            } catch (InterruptedException e) {
              e.printStackTrace();
            }
            // �ٴζ�ȡ10������
            ArrayList<HashMap<String, Object>> virtualData = new ArrayList<HashMap<String, Object>>();
            for (int i = lsitItemSum; i < RankUtils.allRankArrayList.size(); i++) {
              virtualData.add(RankUtils.allRankArrayList.get(i));
              lsitItemSum += 1;
            }
            // �����µĴ�С
            return virtualData;
          }

          // ��doInBackground����ִ��
          @Override
          protected void onPostExecute(ArrayList<HashMap<String, Object>> result) {
            RankUtils.showRankArrayList.addAll(result);// ����������������
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
    rankRequestParmas(StaticVarUtil.student.getAccount() + "|" + selectXn.split("\\-")[0] + "|"
        + (selectXq.equals("��һѧ��") ? "1" : "2"));
    String result = "";
    // ���Ȳ�ѯ�ڴ����Ƿ��и�ѧ�ڳɼ�
    for (String xnAndXq : allRankMap.keySet()) {
      if (xnAndXq.equals(selectXn + selectXq)) {
        // ���������ֱ�� ����value
        result = allRankMap.get(xnAndXq);
        refeshRank(result, isFirstListView);
        break;
      }
    }
    if (result.equals("")) {
      // �����ڣ�������
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
          .getSystemService(getApplicationContext().TELEPHONY_SERVICE)).getDeviceId();
      imei = Passport.jiami(imei, String.valueOf(time));
      realData = URLEncoder.encode(realData);
      time_s = URLEncoder.encode(time_s);
      StaticVarUtil.data = realData;
      StaticVarUtil.viewstate = time_s;
      StaticVarUtil.content = imei;
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

    xnSpinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {

      public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        // TODO Auto-generated method stub
        if (isTouchXNSpinner) {
          lsitItemSum = DEFAULTITEMSUM;// ����ΪĬ��
          // ��spinner�ϵ�ѡ�����ʾ��TextView����
          selectXn = xnAdapter.getItem(arg2);
          // �Զ����� ѧ��
          String xq = StaticVarUtil.list_Rank_xnAndXq.get(xns[arg2]);// Ĭ�ϵ�һѧ����׸�ѧ������
          String[] xqs = xq.split("\\,");
          xqAdapter = new TestArrayAdapter(R.layout.list_item, getApplicationContext(), xqs, width);// ����Adapter
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
    xqSpinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {

      public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
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

  // ������
  protected void cet() {
    // TODO Auto-generated method stub
    // �˵���ť
    Button menu = (Button) findViewById(R.id.butMenu);
    menu.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        slidingMenu.toggle();
      }
    });

    final EditText accout = (EditText) findViewById(R.id.cet_account);

    final EditText name = (EditText) findViewById(R.id.cet_name);

    Button query = (Button) findViewById(R.id.butQuery);
    query.setOnClickListener(new OnClickListener() {

      @Override
      public void onClick(View v) {
        // TODO Auto-generated method stub
        String accoutStr = accout.getText().toString();
        String nameStr = name.getText().toString();// 1435229674374
        if (accoutStr == null || "".equals(accoutStr) || nameStr == null || "".equals(nameStr)) {
          return;
        }
        StaticVarUtil.cet_account = accoutStr;
        long time = System.currentTimeMillis();
        String time_s = Passport.jiami(String.valueOf(time),
            String.valueOf(new char[] { 2, 4, 8, 8, 2, 2 }));
        StaticVarUtil.cet_data = Passport.jiami(accoutStr, String.valueOf(time));
        StaticVarUtil.cet_viewstate = time_s;
        GetCETAsyntask getCETAsyntask = new GetCETAsyntask();
        dialog.show();
        try {
          getCETAsyntask.execute(new String[] { URLEncoder.encode(
              URLEncoder.encode(nameStr.length() < 3 ? nameStr : nameStr.substring(0, 2), "utf-8"),
              "utf-8") });
        } catch (UnsupportedEncodingException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
      }
    });
    Button searchCet = (Button) findViewById(R.id.butForgetCet);
    searchCet.setOnClickListener(new OnClickListener() {

      @Override
      public void onClick(View arg0) {
        // TODO Auto-generated method stub
        Intent intent = new Intent(getApplicationContext(), ForgetCetActivity.class);
        startActivity(intent);
      }
    });
  }

  /*
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
        if (password1.equals("") && password2.equals("") && bitmap == null
            && password3.equals("")) {
          ViewUtil.showToast(getApplicationContext(), "��û����Ϣ��Ҫ�޸�");
          return;
        }

        // ����
        if (password1.equals("") && password2.equals("")) {
          // ������޸�

        } else {
          if (password1.equals(StaticVarUtil.student.getPassword())
              && password2.equals("") == false) {
            ;
          } else {
            ViewUtil.showToast(getApplicationContext(), "�����벻��ȷ���������벻��Ϊ��,�������");
            return;
          }
        }
        if (password2.equals(password3)) {
          if (!Util.hasDigitAndNum(password2)) {
            ViewUtil.showToast(getApplicationContext(), "�����б���������ֺ���ĸ");
          } else {
            if (password2.length() < 6) {
              // �����޸�������볬��6λ
              ViewUtil.showToast(getApplicationContext(), "������볬��6λ");
            } else {
              ChangePwAsyntask changePwAsyntask = new ChangePwAsyntask();
              changePwAsyntask.execute(new String[] { password1, password2 });
            }

          }

        } else {
          ViewUtil.showToast(getApplicationContext(), "�����벻��ȷ");

          return;
        }

      }
    });

  }

  /*
   * ѡ��ͷ��
   * 
   * @return
   */
  protected void chooseHeadPhoto() {
    String[] items = new String[] { "ѡ�񱾵�ͼƬ", "����" };
    new AlertDialog.Builder(this).setTitle("����ͷ��")
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
              Intent intent2 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
              Uri imageUri = Uri.fromFile(new File(StaticVarUtil.PATH, "temp.JPEG"));
              // ָ����Ƭ����·����SD������image.jpgΪһ����ʱ�ļ���ÿ�����պ����ͼƬ���ᱻ�滻
              intent2.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
              startActivityForResult(intent2, PHO);
              break;
            }
          }
        }).setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
          }
        }).show();
  }

  /*
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
          uploadFileAsytask.execute(new String[] {
              StaticVarUtil.PATH + "/" + StaticVarUtil.student.getAccount() + ".JPEG" });
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

  /*
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

  /*
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
      collegeId = Integer.parseInt(ToolClass.nameIdTreeMap.get(chooseCollege.getText().toString()));
    } catch (Exception e) {
      // ������-1
      System.out.println("û�������ѧ");
      collegeId = -1;
    }
    return collegeId;
  }

  /*
   * ѡ���У����У����ѡ����ʷ
   */
  @SuppressLint("InflateParams")
  protected void chooseCollege() {
    LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
    View view = inflater.inflate(R.layout.choose_school, null);

    // ��ʾ��ѧ��ʷadapterHistory.notifyDataSetChanged();
    ArrayList<CharSequence> allCollege = readHistory2();
    HistoryCollege.initData(allCollege);// ��ʼ������Դ
    ChooseHistorySchoolExpandAdapter adapterHistory = new ChooseHistorySchoolExpandAdapter(
        MainActivity.this);
    ExpandableListView expandHistory = (ExpandableListView) view.findViewById(R.id.expandHistory);
    expandHistory.setAdapter(adapterHistory);

    // �ҵ��ؼ�expandListView
    ExpandableListView expandListView = (ExpandableListView) view.findViewById(R.id.expandListView);
    expandListView.setAdapter(adapter);

    final Dialog dialog = new AlertDialog.Builder(MainActivity.this).setView(view).create();
    dialog.setCancelable(true);
    dialog.show();

    // ѡ���ѧ����
    expandListView.setOnChildClickListener(new OnChildClickListener() {
      public boolean onChildClick(ExpandableListView parent, View v, int groupPosition,
          int childPosition, long id) {
        String schoolName = ToolClass.schoolsList.get(groupPosition).get(childPosition).toString();
        dialog.cancel();
        chooseCollege.setText(schoolName);
        return true;
      }
    });

    // ѡ����ʷ����
    expandHistory.setOnChildClickListener(new OnChildClickListener() {
      public boolean onChildClick(ExpandableListView parent, View v, int groupPosition,
          int childPosition, long id) {
        String schoolName = HistoryCollege.allHistory.get(groupPosition).get(childPosition)
            .toString();
        dialog.cancel();
        chooseCollege.setText(schoolName);
        return true;
      }
    });
  }

  private String new_version;// ���°汾
  private String update_content;// ��������
  private static String apk_url;// ���ص�ַ

  /*
   * ˢ��
   * 
   * @param messageSort 0������ڹ�ע1�������չ�ע2������ư�
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

  /*
   * ���������ʷ
   */
  private void saveHistory() {
    SharedPreferences share = getSharedPreferences("history", MODE_PRIVATE);
    Editor editor = share.edit();
    editor.putString("msgKind", chooseMsgKind.getText().toString());// �����´ε�¼��ʾ
    editor.putString("msgSort", chooseMsgSort.getText().toString());// �����´ε�¼��ʾ
    editor.putString("theLastCollege", chooseCollege.getText().toString());// �����´ε�¼��ʾ

    // ����ѡ���ѧ��ʾ
    HashSet<String> set = (HashSet<String>) share.getStringSet("college", new HashSet<String>());
    if (judgeCollegeId() > 0) {
      if (!set.contains(chooseCollege.getText().toString())) {
        // ����������ͼ���
        set.add(chooseCollege.getText().toString());
      }
    }
    editor.putStringSet("college", set);// �����ȥ
    editor.commit();// ͬ������Ӳ������
  }

  /*
   * ��ȡ��ʷ��¼������ʾ����ѡ����Ĵ�ѧ
   */
  private ArrayList<CharSequence> readHistory2() {
    SharedPreferences share = getSharedPreferences("history", MODE_PRIVATE);
    // ��ʾ���һ�μ�¼,û����ʾĬ��
    HashSet<String> set = (HashSet<String>) share.getStringSet("college", null);
    ArrayList<CharSequence> allHistory = null;// Ҫ���ص�����
    if (set != null) {
      // ��Ϊ�յĻ����������ݵ���ʾ
      allHistory = new ArrayList<CharSequence>();
      for (String string : set) {
        allHistory.add(string);
      }
    }
    return allHistory;
  }

  /*
   * ���ֻ���ť�ļ���
   */
  @Override
  public boolean onKeyDown(int keyCode, KeyEvent event) {
    switch (keyCode) {
    case KeyEvent.KEYCODE_BACK:
      // ����Ƿ��ذ�ť,�˳�
      if (getCurrentMeunItem() != 1) {// ���ڵ�һ��ҳ��,���ص�һ��ҳ��
        menuBang.setPressed(true);// ��ʼ��Ĭ���Ƿ��ư񱻰���
        setCurrentMenuItem(StaticVarUtil.MENU_BANG);// ��¼��ǰѡ��λ��
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

  /*
   * ����ڴ���еĹ�������
   */
  private void deleteCatch() {
    StaticVarUtil.list_Rank_xnAndXq.clear();
    RankUtils.allRankArrayList = null;
    mapScoreOne = null;
    mapScoreTwo = null;
    CustomApplcation.getInstance().logout();
    StaticVarUtil.quit();
    isFirstListView = true;
    // ��ճɼ�����
    isFirst = true;
    fragments = null;
    try {
      unregisterReceiver(mReceiver);
    } catch (Exception e) {
      // TODO: handle exception
    }

    try {
      unregisterReceiver(newReceiver);
    } catch (Exception e) {
    }
    newReceiver = null;
    try {
      unregisterReceiver(userReceiver);
    } catch (Exception e) {
    }
    userReceiver = null;
    contactFragment = null;
    recentFragment = null;
    settingFragment = null;
    try {
      // BmobDB.create(this).clearAllDbCache();

    } catch (Exception e) {
      // TODO: handle exception
    }

  }

  @Override
  protected void onResume() {
    // TODO Auto-generated method stub
    super.onResume();

    if (!ConnectionUtil.isConn(getApplicationContext())) {
      ConnectionUtil.setNetworkMethod(MainActivity.this);
      return;
    }
    // СԲ����ʾ
    if (iv_recent_tips == null || iv_bukao_tips == null) {
      return;
    }
    if (BmobDB.create(this).hasUnReadMsg()) {
      iv_recent_tips.setVisibility(View.VISIBLE);
      iv_bukao_tips.setVisibility(View.VISIBLE);
    } else {
      iv_bukao_tips.setVisibility(View.GONE);
      iv_recent_tips.setVisibility(View.GONE);
    }
    if (BmobDB.create(this).hasNewInvite()) {
      iv_contact_tips.setVisibility(View.VISIBLE);
      iv_bukao_tips.setVisibility(View.VISIBLE);
    } else {
      iv_contact_tips.setVisibility(View.GONE);
      iv_bukao_tips.setVisibility(View.GONE);
    }
    MyMessageReceiver.ehList.add(this);// �������͵���Ϣ
    // ���
    MyMessageReceiver.mNewNum = 0;
  }

  @Override
  protected void onPause() {
    // TODO Auto-generated method stub
    super.onPause();
    MyMessageReceiver.ehList.remove(this);// ȡ���������͵���Ϣ
    allRankMap.clear();
  }

  /*
   * �˳�ģ��
   * 
   * @param logout �Ƿ�ע��
   */
  private void quit(final boolean logout) {
    Builder builder = new AlertDialog.Builder(MainActivity.this);

    if (logout) {
      builder.setMessage("��ȷ��Ҫע����");
    } else {
      // builder.setMessage("��ȷ��Ҫ�˳���");
      /*
       * dialog.cancel(); deleteCatch(); LogcatHelper.getInstance(MainActivity.this).stop();
       */
      moveTaskToBack(true);
      return;
    }

    builder.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
        dialog.cancel();
        BmobDB.create(getApplicationContext()).queryBmobInviteList().clear();
        deleteCatch();
        LogcatHelper.getInstance(MainActivity.this).stop();
        // ȡ����ʱ������
        BmobChat.getInstance(getApplicationContext()).stopPollService();

        userManager.logout();
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

  /*
   * ��¼���õ�ǰMenuItem��λ�ã�1��2��3��4��5�ֱ����ɼ���ѯ��������ѯ���ҵ����������ղصģ�ѡ��
   * 
   * @param menuItem �˵���ѡ��
   */
  private void setCurrentMenuItem(int menuItem) {
    SharedPreferences preferences = getSharedPreferences("currentMenuItem", MODE_PRIVATE);
    Editor editor = preferences.edit();
    editor.putInt("item", menuItem);
    editor.commit();
  }

  /*
   * ȡ�õ�ǰMenuItem��λ��
   * 
   * @return ��ǰ��menu�Ĳ˵��� 1��2��3��4��5�ֱ����ɼ���ѯ��������ѯ���ҵ����������ղصģ�ѡ��,0����û�����
   */
  private int getCurrentMeunItem() {
    SharedPreferences preferences = getSharedPreferences("currentMenuItem", MODE_PRIVATE);
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
        url = HttpUtilMc.BASE_URL + "xscjcx.aspx?session=" + StaticVarUtil.session + "&url="
            + url_str + "&xm="
            + URLEncoder.encode(URLEncoder.encode(xm.split("=")[1], "utf8"), "utf8") + "&" + gnmkdm;
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
      StaticVarUtil.student.setName(name);
      try {
        if (!HttpUtilMc.CONNECT_EXCEPTION.equals(result)) {
          if (!result.equals("error")) {
            /*
             * ���ַ��� д��xml�ļ���
             */
            if (!result.equals("no_evaluation")) {
              requestTimes = 0;
              score_json = result;
              listItem = new ArrayList<HashMap<String, Object>>();
              JSONObject jsonObject = new JSONObject(result);
              JSONArray jsonArray = (JSONArray) jsonObject.get("liScoreModels");// ������array
              for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject o = (JSONObject) jsonArray.get(i);
                HashMap<String, Object> map = new HashMap<String, Object>();
                map.put("xn", o.get("xn"));
                map.put("list_xueKeScore", o.get("list_xueKeScore"));
                listItem.add(map);
              }
            } else {
              Builder builder = new AlertDialog.Builder(MainActivity.this);

              builder.setMessage("�뵽�������н�ʦ���ۺ��ѯ�ɼ���");

              builder.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                  quit(true);
                }
              });
              builder.create().show();
            }
            menu1();
            /*
             * new Thread(new Runnable() {
             * 
             * @Override public void run() { // TODO Auto-generated method stub try {
             * Thread.sleep(1300); } catch (InterruptedException e) { // TODO Auto-generated catch
             * block e.printStackTrace(); } Message msg = new Message(); msg.what = 10;
             * mHandler.sendMessage(msg); } }).start();
             */
          } else {
            ViewUtil.showToast(getApplicationContext(), "��ѯʧ��");
          }

        } else {
          if (requestTimes < 5) {
            requestTimes++;
            GetScoreAsyntask getScoreAsyntask = new GetScoreAsyntask();
            getScoreAsyntask.execute();
          } else {
            ViewUtil.showToast(getApplicationContext(), HttpUtilMc.CONNECT_EXCEPTION);
          }
        }
      } catch (Exception e) {
        // TODO: handle exception
        e.printStackTrace();
        Log.i("LoginActivity", e.toString());
      }

    }

  }

  /*
   * ��ȡ �ɼ�����
   * 
   * @author Administrator 2015-2-8
   */
  class GetRankAsyntask extends AsyncTask<String, String, String> {

    @Override
    protected String doInBackground(String... params) {
      // TODO Auto-generated method stub
      return HttpUtilMc
          .queryStringForPost(HttpUtilMc.BASE_URL + "RankServlet.jsp?data=" + StaticVarUtil.data
              + "&viewstate=" + StaticVarUtil.viewstate + "&content=" + StaticVarUtil.content);
    }

    @Override
    protected void onPostExecute(String result) {
      super.onPostExecute(result);

      nickname.setText(name);
      try {
        if (!HttpUtilMc.CONNECT_EXCEPTION.equals(result)) {
          if (!result.equals("error")) {
            /*
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
            dialog.cancel();
            ViewUtil.showToast(getApplicationContext(), HttpUtilMc.CONNECT_EXCEPTION);
          }
        }
      } catch (Exception e) {
        e.printStackTrace();
        dialog.cancel();
        Log.i("LoginActivity", e.toString());
      }

    }

  }

  class GetCETAsyntask extends AsyncTask<String, String, String> {

    @Override
    protected String doInBackground(String... params) {
      // TODO Auto-generated method stub
      return HttpUtilMc.queryStringForPost(HttpUtilMc.XIYOUMC_BASE_IP + "servlet/CetServlet?ticket="
          + StaticVarUtil.cet_data + "&time=" + StaticVarUtil.cet_viewstate + "&name=" + params[0]);
    }

    @Override
    protected void onPostExecute(String result) {
      super.onPostExecute(result);

      dialog.cancel();
      try {
        if (!HttpUtilMc.CONNECT_EXCEPTION.equals(result)) {
          if (!result.equals("error")) {
            if (result.equals("2")) {
              ViewUtil.showToast(getApplicationContext(), "׼��֤�������");
              return;
            }
            if (result.equals("4")) {
              ViewUtil.showToast(getApplicationContext(), "�����������");
              return;
            }
            if (result.length() < 2) {
              return;
            }
            Bundle bundle = new Bundle();
            bundle.putString("data", result);
            Intent intent = new Intent(getApplicationContext(), CETActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);
          } else {
            ViewUtil.showToast(getApplicationContext(), "��ѯʧ��");
          }
        }
      } catch (Exception e) {
        e.printStackTrace();
        Log.i("LoginActivity", e.toString());
      }

    }

  }

  /*
   * 
   * @param result �ɼ��ַ���
   * 
   * @param isFirst �Ƿ��ǵ�һ��
   */
  private void refeshRank(String result, boolean isFirst) {
    try {
      if (RankUtils.allRankArrayList == null || RankUtils.showRankArrayList == null) {
        RankUtils.allRankArrayList = new ArrayList<HashMap<String, Object>>();
        RankUtils.showRankArrayList = new ArrayList<HashMap<String, Object>>();
      } else {
        RankUtils.allRankArrayList.clear();
        RankUtils.showRankArrayList.clear();
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
        RankUtils.allRankArrayList.add(map);
        if (String.valueOf(rankId).equals(rank)) {
          rankScoreText.setText(o.get("score").toString());// ��ʾ�ɼ�

          Animation animation = AnimationUtils.loadAnimation(getApplicationContext(),
              R.anim.textscore_translate);
          rankScoreText.setAnimation(animation);

        }
      }
      // ��ȡ ֮ǰ��õĹ̶� ������item�� ��ֹ������̫�󣬶����µĽ��������ݶ���ʾ������
      for (int i = 0; i < (lsitItemSum > RankUtils.allRankArrayList.size()
          ? RankUtils.allRankArrayList.size() : lsitItemSum); i++) {
        RankUtils.showRankArrayList.add(RankUtils.allRankArrayList.get(i));
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
        final String data = ideaMsgText.getText().toString().trim() + "|"
            + ("".equals(phoneText.getText().toString().trim()) ? ""
                : phoneText.getText().toString().trim())
            + "|" + StaticVarUtil.student.getAccount() + "|"
            + Util.getVersion(getApplicationContext()) + "|" + number;
        new Thread(new Runnable() {
          @Override
          public void run() {
            Util.sendMail(data);
            Message msg = new Message();
            msg.what = StaticVarUtil.IDEA_BACK_TOAST;
            mHandler.sendMessage(msg);
          }
        }).start();
      }
    });
  }

  class MyHandler extends Handler {

    WeakReference<Activity> mActivityReference;

    MyHandler(Activity activity) {
      mActivityReference = new WeakReference<Activity>(activity);
    }

    @Override
    public void handleMessage(Message msg) {
      // TODO Auto-generated method stub
      final Activity activity = mActivityReference.get();
      if (activity != null) {
        switch (msg.what) {
        case StaticVarUtil.MENU_BANG:
          menu1();
          setCurrentMenuItem(StaticVarUtil.MENU_BANG);// ��¼��ǰѡ��λ��
          return;
        case StaticVarUtil.MENU_ABOUT:
          aboutListener();
          setCurrentMenuItem(StaticVarUtil.MENU_ABOUT);// ��¼��ǰѡ��λ��
          return;
        }
        if (!ConnectionUtil.isConn(getApplicationContext())) {
          ConnectionUtil.setNetworkMethod(MainActivity.this);
          menuBang.setPressed(true);// ��ʼ��Ĭ���Ƿ��ư񱻰���
          setCurrentMenuItem(StaticVarUtil.MENU_BANG);// ��¼��ǰѡ��λ��
          slidingMenu.setContent(R.layout.card_main);
          menu1();
          return;
        }
        switch (msg.what) {
        case StaticVarUtil.MENU_BUKAO:
          friend_list();
          setCurrentMenuItem(StaticVarUtil.MENU_BUKAO);// ��¼��ǰѡ��λ��
          break;
        case StaticVarUtil.MENU_PAIMING:
          rank();
          setCurrentMenuItem(StaticVarUtil.MENU_PAIMING);// ��¼��ǰѡ��λ��
          break;
        case StaticVarUtil.MENU_IDEA_BACK:
          menuIdeaBack();
          setCurrentMenuItem(StaticVarUtil.MENU_IDEA_BACK);// ��¼��ǰѡ��λ��
          break;
        case StaticVarUtil.MENU_SETTING:
          menuSetting();
          setCurrentMenuItem(StaticVarUtil.MENU_SETTING);// ��¼��ǰѡ��λ��
          break;

        case StaticVarUtil.SHARE:
          showShareQrcodeDialog();
          break;
        case StaticVarUtil.MENU_CET:
          cet();
          setCurrentMenuItem(StaticVarUtil.MENU_CET);// ��¼��ǰѡ��λ��
          break;
        case StaticVarUtil.IDEA_BACK_TOAST:
          ViewUtil.showToast(getApplicationContext(), "��л����");
          break;

        case StaticVarUtil.BMOB_CHAT:
          try {
            chat();
          } catch (ClassCastException e) {
            // TODO: handle exception
            e.printStackTrace();
          }

          setCurrentMenuItem(StaticVarUtil.MENU_BUKAO);// ��¼��ǰѡ��λ��
          break;
        case StaticVarUtil.CHECK_VERSION:
          CheckVersionAsyntask checkVersionAsyntask = new CheckVersionAsyntask();
          checkVersionAsyntask.execute();
          break;
        }
      }
    }

  }

  private static final int GO_HOME = 100;
  private static final int GO_LOGIN = 200;

  // ��λ��ȡ��ǰ�û��ĵ���λ��
  private LocationClient mLocationClient;

  private BaiduReceiver mReceiver;// ע��㲥�����������ڼ��������Լ���֤key

  private void chat() {
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
    dialog.show();
    if (userManager.getCurrentUser() != null) {
      // ÿ���Զ���½��ʱ�����Ҫ�����µ�ǰλ�úͺ��ѵ����ϣ���Ϊ���ѵ�ͷ���ǳ�ɶ���Ǿ����䶯��
      File file = new File(StaticVarUtil.PATH, StaticVarUtil.student.getAccount() + ".JPEG");
      if (file.exists()) {
        uploadAvatar(StaticVarUtil.PATH + "/" + StaticVarUtil.student.getAccount() + ".JPEG");
      }

      updateUserInfos();
      chatHandler.sendEmptyMessageDelayed(GO_HOME, 0);
    } else {
      chatHandler.sendEmptyMessageDelayed(GO_LOGIN, 2000);
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

  private Button[] mTabs;
  private ContactFragment contactFragment;
  private RecentFragment recentFragment;
  private SettingsFragment settingFragment;
  private Fragment[] fragments;
  private int index;
  private int currentTabIndex;
  ImageView iv_recent_tips, iv_contact_tips, iv_bukao_tips;// ��Ϣ��ʾ

  private void initView() {
    mTabs = new Button[3];
    mTabs[0] = (Button) findViewById(R.id.btn_message);
    mTabs[1] = (Button) findViewById(R.id.btn_contract);
    mTabs[2] = (Button) findViewById(R.id.btn_set);
    iv_recent_tips = (ImageView) findViewById(R.id.iv_recent_tips);
    iv_contact_tips = (ImageView) findViewById(R.id.iv_contact_tips);

    if (iv_recent_tips == null) {
      return;
    }
    if (BmobDB.create(this).hasUnReadMsg()) {
      iv_recent_tips.setVisibility(View.VISIBLE);
      iv_bukao_tips.setVisibility(View.VISIBLE);
    } else {
      iv_recent_tips.setVisibility(View.GONE);
      iv_bukao_tips.setVisibility(View.GONE);
    }
    if (BmobDB.create(this).hasNewInvite()) {
      iv_contact_tips.setVisibility(View.VISIBLE);
      iv_bukao_tips.setVisibility(View.VISIBLE);
    } else {
      iv_contact_tips.setVisibility(View.GONE);
      iv_bukao_tips.setVisibility(View.GONE);
    }
    MyMessageReceiver.ehList.add(this);// �������͵���Ϣ
    // ���
    MyMessageReceiver.mNewNum = 0;
    // �ѵ�һ��tab��Ϊѡ��״̬
    mTabs[0].setSelected(true);
  }
  
  private void initTab() {
    contactFragment = new ContactFragment();
    recentFragment = new RecentFragment();
    settingFragment = new SettingsFragment();
    fragments = new Fragment[] { recentFragment, contactFragment, settingFragment };
    // �����ʾ��һ��fragment
    try {
      if (!contactFragment.isAdded()) {
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, recentFragment)
        .add(R.id.fragment_container, contactFragment).hide(contactFragment).show(recentFragment)
        .commitAllowingStateLoss();
      }
    } catch (Exception e) {
      // TODO: handle exception
    }
//    while (contactFragment.isAdded()) {
      dialog.dismiss();
//      break;
//    }
  }

  /**
   * button����¼�
   * 
   * @param view
   */
  public void onTabSelect(View view) {
    if (mTabs == null) {
      return;
    }
    switch (view.getId()) {
    case R.id.btn_message:
      index = 0;
      break;
    case R.id.btn_contract:
      index = 1;
      break;
    case R.id.btn_set:
      index = 2;
      break;
    }
    if (currentTabIndex != index) {
      FragmentTransaction trx = getSupportFragmentManager().beginTransaction();
      trx.hide(fragments[currentTabIndex]);
      if (!fragments[index].isAdded()&&index != 1) {
          trx.add(R.id.fragment_container, fragments[index]);
      }
      trx.show(fragments[index]).commit();
    }
    mTabs[currentTabIndex].setSelected(false);
    // �ѵ�ǰtab��Ϊѡ��״̬
    mTabs[index].setSelected(true);
    currentTabIndex = index;
  }

  /**
   * ˢ�½���
   * 
   * @Title: refreshNewMsg @Description: TODO @param @param message @return void @throws
   */
  private void refreshNewMsg(BmobMsg message) {
    // ������ʾ
    boolean isAllow = CustomApplcation.getInstance().getSpUtil().isAllowVoice();
    if (isAllow) {
      CustomApplcation.getInstance().getMediaPlayer().start();
    }
    iv_recent_tips.setVisibility(View.VISIBLE);
    iv_bukao_tips.setVisibility(View.VISIBLE);
    // ҲҪ�洢����
    if (message != null) {
      BmobChatManager.getInstance(MainActivity.this).saveReceiveMessage(true, message);
    }
    if (currentTabIndex == 0) {
      // ��ǰҳ�����Ϊ�Ựҳ�棬ˢ�´�ҳ��
      if (recentFragment != null) {
        recentFragment.refresh();
      }
    }
  }

  NewBroadcastReceiver newReceiver;

  private void initNewMessageBroadCast() {
    // ע�������Ϣ�㲥
    newReceiver = new NewBroadcastReceiver();
    IntentFilter intentFilter = new IntentFilter(BmobConfig.BROADCAST_NEW_MESSAGE);
    // ���ȼ�Ҫ����ChatActivity
    intentFilter.setPriority(3);
    registerReceiver(newReceiver, intentFilter);
  }

  /**
   * ����Ϣ�㲥������
   * 
   */
  private class NewBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
      // ˢ�½���
      refreshNewMsg(null);
      // �ǵðѹ㲥���ս��
      abortBroadcast();
    }
  }

  TagBroadcastReceiver userReceiver;

  private void initTagMessageBroadCast() {
    // ע�������Ϣ�㲥
    userReceiver = new TagBroadcastReceiver();
    IntentFilter intentFilter = new IntentFilter(BmobConfig.BROADCAST_ADD_USER_MESSAGE);
    // ���ȼ�Ҫ����ChatActivity
    intentFilter.setPriority(3);
    registerReceiver(userReceiver, intentFilter);
  }

  /**
   * ��ǩ��Ϣ�㲥������
   */
  private class TagBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
      BmobInvitation message = (BmobInvitation) intent.getSerializableExtra("invite");
      refreshInvite(message);
      // �ǵðѹ㲥���ս��
      abortBroadcast();
    }
  }

  @Override
  public void onNetChange(boolean isNetConnected) {
    // TODO Auto-generated method stub
    if (isNetConnected) {
      ShowToast(R.string.network_tips);
    }
  }

  @Override
  public void onAddUser(BmobInvitation message) {
    // TODO Auto-generated method stub
    refreshInvite(message);
  }

  /**
   * ˢ�º�������
   * 
   * @Title: notifyAddUser @Description: TODO @param @param message @return void @throws
   */
  private void refreshInvite(BmobInvitation message) {
    boolean isAllow = CustomApplcation.getInstance().getSpUtil().isAllowVoice();
    if (isAllow) {
      CustomApplcation.getInstance().getMediaPlayer().start();
    }
    iv_contact_tips.setVisibility(View.VISIBLE);
    iv_bukao_tips.setVisibility(View.VISIBLE);
    if (currentTabIndex == 1) {
      if (contactFragment != null) {
        contactFragment.refresh();
      }
    } else {
      // ͬʱ����֪ͨ
      String tickerText = message.getFromname() + "������Ӻ���";
      boolean isAllowVibrate = CustomApplcation.getInstance().getSpUtil().isAllowVibrate();
      BmobNotifyManager.getInstance(this).showNotify(isAllow, isAllowVibrate,
          R.drawable.ic_launcher, tickerText, message.getFromname(), tickerText.toString(),
          NewFriendActivity.class);
    }
  }

  @Override
  public void onOffline() {
    // TODO Auto-generated method stub
    showOfflineDialog(this);
    finish();
  }

  @Override
  public void onReaded(String conversionId, String msgTime) {
    // TODO Auto-generated method stub
  }

  /**
   * ���������η��ؼ����˳�
   */
  @Override
  public void onBackPressed() {
    // TODO Auto-generated method stub
    // if (firstTime + 2000 > System.currentTimeMillis()) {
    ViewUtil.cancelToast();
    super.onBackPressed();

    /*
     * } else { ShowToast("�ٰ�һ���˳�����"); } firstTime = System.currentTimeMillis();
     */
  }

  private void startChatActivity() {
    BmobConstants.IS_STARTED = 1;
    // ������ʱ�����񣨵�λΪ�룩-���������̨�Ƿ���δ������Ϣ���еĻ���ȡ����
    BmobChat.getInstance(getApplicationContext()).startPollService(30);
    // �����㲥������
    initNewMessageBroadCast();
    initTagMessageBroadCast();
    initView();
    initTab();
  }

  @SuppressLint("HandlerLeak")
  private Handler chatHandler = new Handler() {
    @Override
    public void handleMessage(Message msg) {
      switch (msg.what) {
      case GO_HOME:
        // startAnimActivity(com.bmob.im.demo.ui.MainActivity.class);
        H5Log.d(getApplicationContext(), "go home");
        startChatActivity();
        break;
      case GO_LOGIN:
        H5Log.d(getApplicationContext(), "go login");
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
                  // Intent intent = new Intent(getApplicationContext(),
                  // com.bmob.im.demo.ui.MainActivity.class);
                  // startActivity(intent);
                  startChatActivity();
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
            dialog.cancel();
            if (arg1 == null) {
              return;
            }
            BmobLog.i(arg1);

            if (arg1.split("'").length < 3) {
              H5Toast.showToast(getApplicationContext(), "ҵ��æ�����Ժ����ԣ�");
              menuBang.setPressed(true);// ��ʼ��Ĭ���Ƿ��ư񱻰���
              setCurrentMenuItem(StaticVarUtil.MENU_BANG);// ��¼��ǰѡ��λ��
              slidingMenu.setContent(R.layout.card_main);
              menu1();
              return;
            }
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
                  // Intent intent = new Intent(getApplicationContext(),
                  // com.bmob.im.demo.ui.MainActivity.class);
                  // startActivity(intent);
                  startChatActivity();
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
        // Intent intent = new Intent(getApplicationContext(),
        // com.bmob.im.demo.ui.MainActivity.class);
        // startActivity(intent);
        startChatActivity();
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
    try {
      CustomApplcation.getInstance().logout();
      unregisterReceiver(mReceiver);
    } catch (Exception e) {
      // TODO: handle exception
    }

    try {
      unregisterReceiver(newReceiver);
    } catch (Exception e) {
    }
    try {
      unregisterReceiver(userReceiver);
    } catch (Exception e) {
    }
    // ȡ����ʱ������
    BmobChat.getInstance(this).stopPollService();
    super.onDestroy();
  }

  // ��ʾ ������listview
  private void setListView() {
    // TODO Auto-generated method stub

    simpleAdapter = new SimpleAdapter(getApplicationContext(), RankUtils.showRankArrayList,
        R.layout.allrank_listitem, new String[] { "rankId", "name", "score" },
        new int[] { R.id.rankId, R.id.name, R.id.score });
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
      return HttpUtilMc
          .queryStringForPost(HttpUtilMc.BASE_URL + "changepw.jsp?session=" + StaticVarUtil.session
              + "&url=" + canshu + "&old_password=" + params[0] + "&new_password=" + params[1]);

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
          ViewUtil.showToast(getApplicationContext(), HttpUtilMc.CONNECT_EXCEPTION);
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
      return HttpUtilMc.queryStringForPost(
          HttpUtilMc.BASE_URL + "getuserphoto.jsp?username=" + StaticVarUtil.student.getAccount());
    }

    @Override
    protected void onPostExecute(String result) {
      // TODO Auto-generated method stub
      super.onPostExecute(result);
      if (result == null) {
        return;
      }
      if ("no_photo".equals(result) || result.split("/").length < 2) {
        return;
      }
      StaticVarUtil.PHOTOFILENAME = result.split("/")[1];
      // �ж� ͷ���ļ������Ƿ���� ���û���ͷ��
      if (DBConnection.getPhotoName(StaticVarUtil.student.getAccount(), getApplicationContext())
          .equals(StaticVarUtil.PHOTOFILENAME)
          && new File(StaticVarUtil.PATH + "/" + StaticVarUtil.student.getAccount() + ".JPEG")
              .exists()) {
        // �������
        Bitmap bitmap = Util.convertToBitmap(
            StaticVarUtil.PATH + "/" + StaticVarUtil.student.getAccount() + ".JPEG", 240, 240);
        if (bitmap != null) {
          headPhoto.setImageBitmap(bitmap);
        }
      } else {
        // ����ļ����в��������ͷ��
        GetPicture getPicture = new GetPicture();
        getPicture.execute(
            new String[] { HttpUtilMc.BASE_URL + "user_photo/" + StaticVarUtil.PHOTOFILENAME });
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
      if (bitmap == null) {
        return;
      }

      if (Util.isExternalStorageWritable()) {
        Util.saveBitmap2file(bitmap, StaticVarUtil.PHOTOFILENAME, getApplicationContext());
      }
      headPhoto.setImageBitmap(Util.convertToBitmap(
          StaticVarUtil.PATH + "/" + StaticVarUtil.student.getAccount() + ".JPEG", 240, 240));// ��ʾͼƬ
      bitmap.recycle();
    }

  }

  // �첽���汾
  class CheckVersionAsyntask extends AsyncTask<String, String, String> {

    @Override
    protected String doInBackground(String... params) {
      // TODO Auto-generated method stub
      return HttpUtilMc.queryStringForPost(HttpUtilMc.BASE_URL + "checkversion.jsp?version="
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
          if (!result.equals("no")) {
            // �����°汾
            String[] str = result.split("\\|");
            apk_url = str[0];
            new_version = str[1];
            update_content = str[2];
            VersionUpdate versionUpdate = new VersionUpdate(MainActivity.this);
            versionUpdate.apkUrl = HttpUtilMc.IP + apk_url;
            versionUpdate.updateMsg = new_version + "\n\n" + update_content;
            versionUpdate.checkUpdateInfo();
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

  /*
   * �ϴ�ͷ��
   * 
   * @author mc 2014-4-28
   */
  class UploadFileAsytask extends AsyncTask<String, String, String> {
    @Override
    protected String doInBackground(String... params) {
      // TODO Auto-generated method stub
      return HttpAssist.uploadFile(new File(params[0]), StaticVarUtil.student.getAccount());
    }

    @Override
    protected void onPostExecute(String result) {
      super.onPostExecute(result);
      try {
        if (result.equals("error")) {
          return;
        }
        if (Util.isExternalStorageWritable()) {
          Util.saveBitmap2file(bitmap, result, getApplicationContext());
          bitmap.recycle();
        }
        ViewUtil.showToast(getApplicationContext(), !HttpUtilMc.CONNECT_EXCEPTION.equals(result)
            ? !result.equals("error") ? "�޸ĳɹ�" : "�޸�ʧ��" : HttpUtilMc.CONNECT_EXCEPTION);
      } catch (Exception e) {
        Log.i("LoginActivity", e.toString());
      }
    }
  }

  @Override
  public void onMessage(BmobMsg message) {
    // TODO Auto-generated method stub
    refreshNewMsg(message);
  }
}
