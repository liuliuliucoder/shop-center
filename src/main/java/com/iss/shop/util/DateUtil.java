package com.iss.shop.util;//package com.iss.shop.util;
//
//import org.apache.commons.lang.StringUtils;
//import org.apache.commons.lang.time.DateUtils;
//
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.*;
//
//public class DateUtil extends DateUtils {
//    public static final String SHORT_DATE = "yyyy-MM-dd";
//    public static final String INDENT = "yyyyMMdd";
//    public static final String SHORT_MONTH = "yyyy-MM";
//    public static final String LONG_DATETTIME = "yyyy-MM-dd HH:mm:ss";
//    public static final String LONG_DATETTIMEWITHMS = "yyyy-MM-dd HH:mm:ss.SSS";
//    public static final String LONG_DATETTIMEWITHOUTSECOND = "yyyy-MM-dd HH:mm";
//    public static final String DATETTIME_ZERO = "yyyy-MM-dd 00:00:00";
//    public static final String DATETTIME_END = "yyyy-MM-dd 23:59:59";
//    public static final String SHORT_TIME = "HH:mm:ss";
//    public static final String LONG_DATETTIME_WITH_T = "yyyy-MM-dd'T'HH:mm:ss";
//
//    public DateUtil() {
//    }
//
//    public static Date createDate(String dateString) {
//        return StringUtils.isBlank(dateString) ? null : createDate(dateString, "yyyy-MM-dd");
//    }
//
//    public static Date createDate(String dateString, String format) {
//        if (!StringUtils.isBlank(dateString) && !StringUtils.isBlank(format)) {
//            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
//
//            try {
//                Date date = simpleDateFormat.parse(dateString);
//                return date;
//            } catch (ParseException var4) {
//                return null;
//            }
//        } else {
//            return null;
//        }
//    }
//
//    public static String format(Date currDate, String format) {
//        if (currDate != null && !StringUtils.isBlank(format)) {
//            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
//            return simpleDateFormat.format(currDate);
//        } else {
//            return null;
//        }
//    }
//}
