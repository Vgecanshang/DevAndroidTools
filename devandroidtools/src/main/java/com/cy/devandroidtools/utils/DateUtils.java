package com.cy.devandroidtools.utils;

import android.annotation.SuppressLint;
import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

@SuppressLint("SimpleDateFormat") public class DateUtils
{
	
    //Y_M_D_T_H_M_S_000
    public static final String Y_M_D_H_M_S = "yyyy-MM-dd HH:mm:ss";     // 年月日 时分秒
    public static final String Y_M_D_H_M = "yyyy-MM-dd HH:mm";          // 年月日 时分
    public static final String Y_M_D = "yyyy-MM-dd";                    // 年月日
	
	/***
	 * 获取当前时间的 年月日 时分秒
	 * 
	 * @author jy_jun<br>
	 * @return 2013-8-10 13:30:20
	 */
	public final static String getCurrentDateText()
	{
		Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(calendar.getTime());
	}

	/** 转换时间 */
	public static String toDateTime(String beginTime)
	{
		try
		{
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date date = dateFormat.parse(beginTime);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			return sdf.format(date);

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return beginTime;

	}

	/** 转换时间 */
	public static String toAllDateTime(String beginTime)
	{
		try
		{
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			Date date = dateFormat.parse(beginTime);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
			return sdf.format(date);

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return beginTime;

	}

	public static String StringData(String dateTime)
	{
		final Calendar c = Calendar.getInstance();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date date;
		try
		{
			date = dateFormat.parse(dateTime);
			c.setTime(date);
			c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
			String mYear = String.valueOf(c.get(Calendar.YEAR)); // 获取当前年份
			String mMonth = String.valueOf(c.get(Calendar.MONTH) + 1);// 获取当前月份
			String mDay = String.valueOf(c.get(Calendar.DAY_OF_MONTH));// 获取当前月份的日期号码
			String mWay = String.valueOf(c.get(Calendar.DAY_OF_WEEK));
			int hh = c.get(Calendar.HOUR_OF_DAY);
			int mm = c.get(Calendar.MINUTE);
			if ("1".equals(mWay))
			{
				mWay = "天";
			}
			else if ("2".equals(mWay))
			{
				mWay = "一";
			}
			else if ("3".equals(mWay))
			{
				mWay = "二";
			}
			else if ("4".equals(mWay))
			{
				mWay = "三";
			}
			else if ("5".equals(mWay))
			{
				mWay = "四";
			}
			else if ("6".equals(mWay))
			{
				mWay = "五";
			}
			else if ("7".equals(mWay))
			{
				mWay = "六";
			}
			return mYear + "年" + mMonth + "月" + mDay + "日" + " 星期" + mWay + " " + (hh < 9 ? "0" + hh : hh) + ":" + (mm < 9 ? "0" + mm : mm);
		}
		catch (ParseException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return dateTime;

	}
	
	/**
	 * 将字符串转位日期类型 yyyy-MM-dd HH:mm:ss
	 * 
	 * @param sdate
	 * @return
	 */
	public static Date toDate(final String sdate)
	{
		if (TextUtils.isEmpty(sdate)) {
			return null;
		}

		ThreadLocal<SimpleDateFormat> dateFormater = new ThreadLocal<SimpleDateFormat>()
		{
			@Override
			protected SimpleDateFormat initialValue()
			{
				if (sdate.length() == 10)
					return new SimpleDateFormat("yyyy-MM-dd");
				if (sdate.length() == 16)
					return new SimpleDateFormat("yyyy-MM-dd HH:mm");
				else
					return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			}
		};
		try
		{
			return dateFormater.get().parse(sdate);
		}
		catch (Exception e)
		{
			return null;
		}
	}
	
	/**
	 * 返回自定义的时间格式
	 * @param sdate
	 * @param pattern
	 * @return
	 */
	public static String toTime(String sdate, final String pattern){
		ThreadLocal<SimpleDateFormat> dateFormater = new ThreadLocal<SimpleDateFormat>() {
            @Override
            protected SimpleDateFormat initialValue() {
                return new SimpleDateFormat(pattern);
            }
        };
        if (TextUtils.isEmpty(sdate)) {
            return "";
        }
        Date time = toDate(sdate);
        if (time == null) {
            return "";
        }
        String paramDate = dateFormater.get().format(time);
		return paramDate;
	}
	
	/**
     * 得到以星期为类型的时间
     */
    private static String getWeekForTypeOfTime(Date date, String pattern) {
        SimpleDateFormat df = new SimpleDateFormat(pattern);
        return getWeekForCN2(date) + " " + df.format(date);
    }

    // 获取中文 星期/周 几
    private static SimpleDateFormat weekDateFormat1 = new SimpleDateFormat("E");        //  周一 - 周日
    private static SimpleDateFormat weekDateFormat2 = new SimpleDateFormat("EEEE");     //  星期一 - 星期天

    // 获取中文周几
    public static final String getWeekForCN1(Date date) {
        return weekDateFormat1.format(date);
    }

    // 获取中文周几
    public static final String getWeekForCN1(String time) {
        return getWeekForCN1(parseTime(time, Y_M_D));
    }

    // 获取中文星期几
    public static final String getWeekForCN2(Date date) {
        return weekDateFormat2.format(date);
    }

    // 获取中文星期几
    public static final String getWeekForCN2(String time) {
        return getWeekForCN2(parseTime(time, Y_M_D));
    }
    
    public static Date parseTime(String time, String pattern) {
        if (time == null) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        try {
            return sdf.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

}
