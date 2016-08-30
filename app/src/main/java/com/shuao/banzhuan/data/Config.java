package com.shuao.banzhuan.data;

import java.net.URI;

/**
 * Created by flyonthemap on 16/8/8.
 *
 */
public class Config {

    public static final String URL = "http://172.16.1.118:7080/";
    public static final String REGISTER_URL = "http://172.16.1.118:7080/user/smsCode";
    public static final String DEBUG = "home";
    public static final String DOWNLOAD_URL = "http://172.16.1.118:7080/download/app?appID=app2";
    public static final int PAGE_AMOUNT = 10;
    public static final String USER_REGISTER_URL = "http://172.16.1.118:7080/user/reg";
    public static final String LOGIN_URL = "http://172.16.1.118:7080/user/login";
    public static final String USER_NAME = "userName";
    public static final String MOBILE = "mobile";
    public static final String PASSWORD = "password";
    public static final String SMS_CODE = "smsCode";
    public static final String CODE = "code";
    public static final int REGISTER_SUCCESS = 1000;
    public static final int REGISTER_CODE_ERROR = 1001;
    // 计数
    public static final int GET_CODE_COUNT = 1004;
    public static final int GET_CODE_SUCCESS = 1005;
    public static final int  REGISTER_USER_EXIST= 1002;
    public static final int GET_CODE_PHONE_EXIST = 1003;

    public static final int LOGIN_SUCCESS = 2000;
    public static final int LOGIN_ERROR = 2001;
    public static final int LOGIN_UNREGISTER = 2002;
    public static final String IS_LOGIN = "isLogin";
    public static final String USER_ID = "";
    public static final boolean IS_MAN = false;
    public static final String STR_IS_MAN = "isMan";
    public static final String STR_NICKNAME = "nickname";
    public static final String STR_BIRTHDAY = "birthday";
    public static final String STR_PORTRAIT = "portrait" ;
    public static final String STR_PORTRAIT_PATH = "portraitpath";
    public static final String LOGOUT_URL = "http://172.16.1.118:7080/user/logout?userID=c1e0c8773841409ebb9f";
    public static final String ICON_URL = "http://img1.imgtn.bdimg.com/it/u=2299165671,2554860548&fm=21&gp=0.jpg";
    public static final String UPLOAD_IMAGE_URL = "http://172.16.1.118:7080/user/portrait";
//    public static final String DOWNLOAD_PORTRAIT_URL = "http://172.16.1.118:7080/home/banbo/banzhuan/user/c1e0c8773841409ebb9f/portrait/2016-08/logo-square.jpg";
    public static final String DOWNLOAD_PORTRAIT_URL = "http://172.16.1.118:7080/download/portrait?userID=c1e0c8773841409ebb9f";
//    public static final String DOWNLOAD_PORTRAIT_URL = "http://imgsrc.baidu.com/forum/w%3D580/sign=71a4d2199f3df8dca63d8f99fd1072bf/27110924ab18972bd56cdc24e6cd7b899f510a95.jpg";


    public static final String STR_INCOME = "income";
    public static final String HOME_URL = "http://172.16.1.118:7080/task/list?index=0" ;
    public static long income ;
    // 表格条目的头部宽度
    public static int HEAD_WIDTH = 0;
    public static String PORTRAIT_PATH = "";
    public static boolean isRegister = false;
    public static boolean isMan = true;
    public static String nickName = "";
    public static String birthday = "";
    public static int count = 30;
    public static final String CACHE = "cache";

    public static final String ICON = "icon";
    // 根目录
    public static final String ROOT = "banzhuan";
    //存放下载的文件夹
    public static final String DOWNLOAD = "download";

}
