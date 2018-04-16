package com.cloudk.algo;

/**
 * Created by dong on 2018/1/29.
 * 判断字符串是否为空
 */

public class Str {

    public static String delBeginSpace(String s) {
        for(int i = 0; i<s.length();i++){
            if(s.charAt(i) != ' ') {
                continue;
            }else {
                return s.substring(i);
            }
        }
        return s;
    }

    public static boolean isEmpty(String s) {
        for(int i=0;i<s.length();i++) {
            if(s.charAt(i)!=' ') {
                return false;
            }
        }
        return true;
    }

    public static boolean isRightFormat(String s) {
        for(int i = 0; i < s.length(); i++) {
            if(' ' == s.charAt(i)) {
                return false;
            }
        }
        return true;
    }

}
