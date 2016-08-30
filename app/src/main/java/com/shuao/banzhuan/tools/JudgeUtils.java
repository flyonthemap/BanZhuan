package com.shuao.banzhuan.tools;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by flyonthemap on 16/8/10.
 */
public class JudgeUtils {
    public static boolean isPhoneNumberValid(String phoneNumber){

        String regex = "^((13[0-9])|(15[0-9])|(18[0-9]))\\d{8}$";
        Pattern p = Pattern.compile(regex,Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(phoneNumber);
        return m.matches();

    }
}
