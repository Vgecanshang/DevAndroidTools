package com.cy.devandroidtools.log;

import android.text.TextUtils;
import android.util.Log;



/**
 * 日志打印类 DEBUG 用来判断是否打印日志
 * 
 * @author cy
 * 
 */
public class CLog
{
	private static boolean DEBUG = false;

	public static void setDebug(boolean debug){
        DEBUG = debug;
    }

	public static void v(String tag, String message)
	{
		if (DEBUG)
		{
			if (!TextUtils.isEmpty(message))
			{
				Log.v(tag, message);
			}
		}
	}

	public static void d(String tag, String message)
	{
		if (DEBUG)
		{
			if (!TextUtils.isEmpty(message))
			{
				Log.d(tag, message);

			}
		}
	}

	public static void i(String tag, String message)
	{
		if (DEBUG)
		{
			if (!TextUtils.isEmpty(message))
			{
				Log.i(tag, message);
			}
		}
	}

	public static void w(String tag, String message)
	{
		if (DEBUG)
		{
			if (!TextUtils.isEmpty(message))
			{
				Log.w(tag, message);
			}
		}
	}

	public static void e(String tag, String message)
	{
		if (DEBUG)
		{
			if (!TextUtils.isEmpty(message))
			{
				Log.e(tag, message);
			}
		}
	}

	public static void println(String message)
	{
		if (DEBUG)
		{
			if (!TextUtils.isEmpty(message))
			{
				System.out.println(message);
			}
		}
	}
	
	public static void log(String tag, String str) {
        int index = 0; // 当前位置
        int max = 3800;// 需要截取的最大长度,别用4000
        String sub;    // 进行截取操作的string
        while (index < str.length()) { 
            if (str.length() < max) { // 如果长度比最大长度小
                max = str.length();   // 最大长度设为length,全部截取完成.
                sub = str.substring(index, max);
            } else {
                sub = str.substring(index, max);
            } 
            Log.i(tag, sub);         // 进行输出
            index = max;
            max += 3800;
        } 
    }
	
}
