package com.xy.fy.util;

import java.util.List;

import cn.bmob.im.bean.BmobChatUser;

public class Flag {

  public static boolean isChat = false;
  public static List<BmobChatUser> arg0;

  public static void clear() {
    isChat = false;
    arg0 = null;
  }
}
