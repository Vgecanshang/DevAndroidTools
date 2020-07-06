package com.cy.devandroidtools.utils;

import android.text.TextUtils;
import android.widget.EditText;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 文本工具类
 *
 * @author cy
 * @date 2020/4/8.
 */
public class TextUtil {
    /** 纯数字*/
    private static final Pattern PATTERN_STR_NUMBER = Pattern.compile("[0-9]*");
    /** 正数、负数、和小数*/
    private static final Pattern PATTERN_STR_NUMBER_DECI = Pattern.compile("^(\\-|\\+)?\\d+(\\.\\d+)?$");

    public static boolean isEmpty(CharSequence str){
        return TextUtils.isEmpty(str);
    }

    public static boolean isNotEmpty(CharSequence str){
        return !TextUtils.isEmpty(str);
    }

    public static boolean isNotNull(CharSequence str){
        return !TextUtils.isEmpty(str)&&!str.equals("null");
    }

    public static boolean isNotNull(Object object){
        return  object != null;
    }

    public static String getEditText(EditText editText){
        if(editText == null){
            return "";
        }
        return editText.getText().toString().trim();
    }

    /**
     * 是否是纯数字字符串（不包含小数点）
     * @param str
     * @return
     */
    public static boolean isNumber(CharSequence str){
        if(!isNotEmpty(str)){
            return false;
        }
        Matcher isNum = PATTERN_STR_NUMBER.matcher(str);
        if( !isNum.matches() ){
            return false;
        }
        return true;
    }

    /**
     * 是否是数字 （含小数点、正负数）
     * @return
     */
    public static boolean isNumberDeci(CharSequence str){
        if(!isNotEmpty(str)){
            return false;
        }
        Matcher isNum = PATTERN_STR_NUMBER_DECI.matcher(str);
        if( !isNum.matches() ){
            return false;
        }
        return true;
    }

    public static boolean isJsonObject(String str){
        return isNotEmpty(str)&&str.startsWith("{")&&str.endsWith("}");
    }

    /**
     * 根据Unicode编码判断中文汉字和符号
     * @param c
     * @return
     */
    private static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION) {
            return true;
        }
        return false;
    }

    /**
     * 判断中文汉字和符号
     * @param strName
     * @return
     */
    public static boolean isChinese(String strName) {
        char[] ch = strName.toCharArray();
        for (int i = 0; i < ch.length; i++) {
            char c = ch[i];
            if (isChinese(c)) {
                return true;
            }
        }
        return false;
    }


    public static <T> boolean notEmpty(List<T> list) {
        return !isEmpty(list);
    }

    /**
     * 判断数组是否为空
     * @param list
     * @param <T>
     * @return
     */
    public static <T> boolean isEmpty(List<T> list) {
        if (list == null || list.size() == 0) {
            return true;
        }
        return false;
    }

}
