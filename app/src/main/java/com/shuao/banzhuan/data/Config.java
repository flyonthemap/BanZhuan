package com.shuao.banzhuan.data;

import java.net.URI;

/**
 * Created by flyonthemap on 16/8/8.
 * 专门的数据库配置表
 */
public class Config {
    public static final String TAG = "result";
    public static final String DEBUG = "result";
    public static final String CODE = "code"; // 返回json结果的key值
    // 宿舍BaseURL
//    public static final String BASE_URL = "http://192.168.31.130:8080/banzhuan/";
    public static final String BASE_URL = "http://192.168.1.126:8080/banzhuan/";
//  宾馆的BaseURL

    // 注册界面URL
    public static final String GET_CODE_URI = "user/authCode";
    // 图书馆的URL
//    public static final String BASE_URL = "http://10.177.102.210:8080/banzhuan/";

//    public static final String BASE_URL = "http://192.168.43.137:8080/banzhuan";
public static final String BASE_IMAGE_URL = BASE_URL+"image";
//    public static final String BASE_URL = "http://192.168.2.2:8080/banzhuan/";

    public static final String REGISTER_URI = "user/reg";
    // 注册界面提示码
    public static final int GET_CODE_PHONE_EXIST = 1003; // 手机号已经注册
    public static final int CODE_CONFIRM_FAIL = 1004;   //验证码验证失败
    public static final int GET_CODE_SUCCESS = 1005; //获取验证码成功
    public static final int CODE_CONFIRM_SUCCESS = 1006;   //验证码验证失败
    public static final int REGISTER_SUCCESS = 1007;       // 注册成功
    public static final int REGISTER_FAIL = 1008;       // 注册失败
    // 注册界面表单key值
    public static final String AUTH_CODE = "authCode"; // 验证码验证的表单参数
    public static final String PHONE = "phoneNum";
    public static final String CHECK_AUTH_CODE = "checkAuthCode";
    public static final String USER_NAME = "nickname";


    //  登录相关参数配置
    public static final String LOGIN_URI = "user/login";
    public static final int LOGIN_SUCCESS = 2000;
    public static final int LOGIN_ERROR = 2001;
    public static final int LOGIN_PHONE_UNREGISTER = 2002;
    public static final String IS_LOGIN = "isLogin";
    // 加载对话框的提示标志
    public static final int LOAD_SUCC = 0x001;
    public static final int LOAD_FAIL = 0x002;
    public static final int LOAD_FAIL_FINISH = 0x003;
    // 应用界面相关的key值
    public static  final String STR_PACK_NAME = "packageName";
    public static  final String STR_ICON_URL = "iconUrl";
    public static final String STR_ID = "id";
    public static  final String STR_DOWNLOAD_URL = "downloadUrl";
    public static final String STR_SIZE = "size";
    public static final String STR_SCREEN = "screen";
    public static final String STR_DESCRIPTION = "des";
    public static final String STR_NAME = "name";

    public static final String STR_REWARD = "reward";
    public static final String USER_ID = "";
    public static final boolean IS_MAN = false;
    public static final String STR_IS_MAN = "isMan";
    public static final String STR_NICKNAME = "nickname";
    public static final String STR_BIRTHDAY = "birthday";
    public static final String STR_PORTRAIT = "portrait" ;
    public static final String STR_PORTRAIT_PATH = "portraitpath";


    public static final String UPLOAD_IMAGE_URL = "http://172.16.1.118:7080/user/portrait";
    public static final String DOWNLOAD_PORTRAIT_URL = "http://172.16.1.118:7080/download/portrait?userID=c1e0c8773841409ebb9f";
    public static final String STR_INCOME = "income";
    public static final String HOME_URL = "http://172.16.1.118:7080/task/list?index=0" ;
    public static final int PER_PAGE_COUNT = 10;



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
    // http请求状态码标志
    public final static int STATAS_OK = 200;// 请求OK
    public final static int NO_RESPONSE = 400;// 请求无响应 找不到响应资源
    public final static int S_EXCEPTION = 500;// 服务器出错
    public final static int RESPONESE_EXCEPTION = 160;// 响应异常
    public final static int TIMEOUT = 101;// 请求超时
    public final static int NO_NETWORK = 102;// 没用可用网络
    public final static int NULLPARAMEXCEPTION = 103;// 参数为空异常
    public final static int RESPONSE_OK = 2000;
    public final static int RELOGIN = 4001;
    public final static int SERVER_EXCEPTION = 5001;
    public final static int NULLPARAM=4006;//参数值为空
    public final static int LOSEPARAM=4005;//缺少参数
    public final static String SAVAVERSIONCODE="savaVersionCode";//保存最后上一次运行用的版本
    // 访问的主机
    public final static String HOST = "http://api.qcbis.com";

    public final static String PROJECTAPI = HOST + "/Project.aspx";
    public final static String LOANAPI = HOST + "/Loan.aspx";

    public final static String DOWNLOADURL="http://api.qcbis.com/Download.aspx/";
    // 保存图片的文件夹
//    public final static String ROOT = "OrongImages";
    // 保存头像
    public final static String USERICFODER = ROOT + "/user";
    // 保存项目图片
    public final static String PROJECTIMG = ROOT + "/project";

    // sharedPeferences
    public final static String ORPREFERENCES = "orongConfig";
    public final static String ISSAVEPW = "isSavepassword";
    public final static String USERNAME = "userName";
    public final static String PASSWORD = "password";
    public final static String USERICONPATH = "usericonpath";// 用户图像保存的url  =qrcode+path;//用户的url+存储路径

    // 加密密码
    public final static String ENCODEPASSWORD = "AAAcom.orongaaa";
    //
    public final static int PAGESIZE = 12;

    public final static Long MAXFIESZISE=1048576L*6L; //最多可保存6M
    public final static int MAXFIESNUM=80;//最多允许保存80张图片
    public final static int DELFIESNUM=30;//每次删除的图片数量
    public static final String USER_REGISTER_URL = "http://172.16.1.118:7080/user/reg";
    public static final String DOWNLOAD_URL = "http://172.16.1.118:7080/download/";
    public static final int PAGE_AMOUNT = 10;

}
