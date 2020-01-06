package com.jawnho.util;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import java.util.Date;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.DurationFieldType;
import org.joda.time.Instant;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * 时间相关的工具类
 *
 * @author gyj
 * @date 2019/5/21
 */
public class TimeUtil {

  public static final int HALF_DAY = 12 * 60 * 60;
  /**
   * 线程安全的日期formatter
   */
  private static final DateTimeFormatter DATE_TIME_FORMATTER =
    DateTimeFormat.forPattern("yyyy-MM-dd")
      .withZone(DateTimeZone.getDefault());

  /**
   * 线程安全的仅由数字组成的详细时间日期formatter
   */
  private static final DateTimeFormatter ONLY_NUM_DATE_TIME_FORMATTER =
    DateTimeFormat.forPattern("yyyyMMddHHmmss")
      .withZone(DateTimeZone.getDefault());

  /**
   * 线程安全的仅由数字组成的详细时间日期formatter
   */
  private static final DateTimeFormatter ONLY_NUM_DATE_TIME_NO_HOUR_FORMATTER =
    DateTimeFormat.forPattern("yyyyMMdd")
      .withZone(DateTimeZone.getDefault());

  /**
   * 线程安全的详细时间日期formatter
   */
  private static final DateTimeFormatter SPECIFIC_DATE_TIME_FORMATTER =
    DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")
      .withZone(DateTimeZone.getDefault());

  /**
   * 线程安全的详细时间日期formatter
   */
  private static final DateTimeFormatter HOUR_MINUTE_FORMATTER =
    DateTimeFormat.forPattern("HH:mm")
      .withZone(DateTimeZone.getDefault());

  /**
   * 获得当前日期date
   */
  public static Date getCurDate() {
    return new Date();
  }

  /**
   * 获取今天的日期字符串
   */
  public static String getCurDateStr() {
    return DATE_TIME_FORMATTER.print(new Instant());
  }

  /**
   * 根据date， 获得"yyyy-MM-dd"格式的字符串
   */
  public static String formatToDateStr(Date date) {
    Preconditions.checkNotNull(date);

    DateTime dateTime = new DateTime(date);
    return dateTime.toString(DATE_TIME_FORMATTER);
  }

  /**
   * 获得第二天0点的日期
   */
  public static Date getTomorrowStartDate() {
    DateTime dateTime = new DateTime();
    dateTime = dateTime.plusDays(1);
    return new Date(dateTime.millisOfDay().withMinimumValue().getMillis());
  }

  /**
   * 获得今天天0点的日期
   */
  public static Date getTodayStartDate() {
    DateTime dateTime = new DateTime();
    return new Date(dateTime.millisOfDay().withMinimumValue().getMillis());
  }

  /**
   * 获得昨天的日期
   */
  public static String getYesterdayDateStr() {
    Instant yesterdayInstant = new DateTime()
      .withFieldAdded(DurationFieldType.days(), -1)
      .toInstant();

    return DATE_TIME_FORMATTER.print(yesterdayInstant);
  }

  /**
   * 是否早于今天
   */
  public static boolean isBeforeToday(String dateStr) {
    Preconditions.checkArgument(!Strings.isNullOrEmpty(dateStr));

    DateTime dateTime = DATE_TIME_FORMATTER.parseDateTime(dateStr);
    // 今天最早的时间
    DateTime todayStart = new DateTime().millisOfDay().withMinimumValue();
    return dateTime.isBefore(todayStart);
  }

  /**
   * 根据字符串，获取日期。dateStr格式：yyyy-MM-dd
   */
  public static Date formatToDate(String dateStr) {
    DateTime dateTime = DATE_TIME_FORMATTER.parseDateTime(dateStr);
    return dateTime.toDate();
  }

  /**
   * 根据dateStr，返回加上days之后的日期
   */
  public static String plusDays(String dateStr, int days) {
    Preconditions.checkArgument(!Strings.isNullOrEmpty(dateStr));
    DateTime dateTime = DATE_TIME_FORMATTER.parseDateTime(dateStr);
    dateTime = dateTime.plusDays(days);
    return dateTime.toString(DATE_TIME_FORMATTER);
  }

  /**
   * 获得当前时间所处分钟0秒的时间字符串
   */
  public static String getCurMinuteStartStr() {
    DateTime dateTime = new DateTime();
    dateTime = dateTime.minusSeconds(dateTime.secondOfMinute().get());
    return dateTime.toString(ONLY_NUM_DATE_TIME_FORMATTER);
  }

  /**
   * 减一分钟
   */
  public static String minusMin(String minStr, int n) {
    DateTime dateTime = ONLY_NUM_DATE_TIME_FORMATTER.parseDateTime(minStr);
    dateTime = dateTime.minusMinutes(n);
    return dateTime.toString(ONLY_NUM_DATE_TIME_FORMATTER);
  }

  /**
   * 获取今天开始的时间戳
   */
  public static long getTodayStartTimestamp() {
    DateTime dateTime = new DateTime();
    return dateTime.millisOfDay().withMinimumValue().getMillis();
  }

  /**
   * 根据时间戳，获得"yyyy-MM-dd"格式的字符串
   */
  public static String formatToDateStr(long timestamp) {
    return DATE_TIME_FORMATTER.print(timestamp);
  }

  /**
   * 获取当前时间
   */
  public static long getCurTime() {
    return System.currentTimeMillis();
  }

  /**
   * 根据传进来的时间，计算该时间所属那天的中午时间
   */
  public static long getNoonTime(long curTime) {
    DateTime dateTime = new DateTime(curTime);
    return dateTime.millisOfDay()
      // 当天0点
      .withMinimumValue()
      // 增加12小时
      .plusHours(12)
      .getMillis();
  }

  /**
   * 获取当前所属的n分钟的开始时间
   */
  public static Date getMinuteDate(int n) {
    DateTime dateTime = new DateTime();
    dateTime = dateTime.minusMinutes(dateTime.getMinuteOfHour() % n)
      .minusSeconds(dateTime.getSecondOfMinute())
      .minusMillis(dateTime.getMillisOfSecond());
    return dateTime.toDate();
  }

  /**
   * 获取 date 所属的n分钟的开始时间
   */
  public static Date getMinuteDate(Date date, int n) {
    DateTime dateTime = new DateTime(date);
    dateTime = dateTime.minusMinutes(dateTime.getMinuteOfHour() % n)
      .minusSeconds(dateTime.getSecondOfMinute())
      .minusMillis(dateTime.getMillisOfSecond());
    return dateTime.toDate();
  }

  /**
   * 根据时间戳，获得"yyyy-MM-dd HH:mm:ss"格式的字符串
   */
  public static String specialFormatToDateStr(Date date) {
    DateTime dateTime = new DateTime(date);
    return dateTime.toString(SPECIFIC_DATE_TIME_FORMATTER);
  }

  /**
   * 根据时间戳，获得"yyyyMMdd"格式的字符串
   */
  public static String specialFormatToDateStrNoHour(Date date) {
    DateTime dateTime = new DateTime(date);
    return dateTime.toString(ONLY_NUM_DATE_TIME_NO_HOUR_FORMATTER);
  }

  /**
   * 计算当前日期减去days后的日期
   */
  public static String minusDays(int days) {
    Preconditions.checkArgument(days >= 0);

    DateTime dateTime = new DateTime();
    dateTime = dateTime.minusDays(days);
    return dateTime.toString(DATE_TIME_FORMATTER);
  }

  /**
   * 根据dateStr，计算当前日期减去days后的日期
   */
  public static String minusDays(String dateStr, int days) {
    Preconditions.checkArgument(!Strings.isNullOrEmpty(dateStr));

    DateTime dateTime = new DateTime(dateStr);
    dateTime = dateTime.minusDays(days);
    return dateTime.toString(DATE_TIME_FORMATTER);
  }

  /**
   * 转化成由小时、分钟组成的时间字符串
   */
  public static String formatToHourMin(Date timestamp) {
    Preconditions.checkNotNull(timestamp);

    DateTime dateTime = new DateTime(timestamp);
    return dateTime.toString(HOUR_MINUTE_FORMATTER);
  }

  /**
   * 转化成由小时、分钟组成的时间字符串
   */
  public static String formatToHourMin(Date timestamp, int n) {
    Preconditions.checkNotNull(timestamp);

    DateTime dateTime = new DateTime(timestamp);
    dateTime = dateTime.minusMinutes(dateTime.getMinuteOfHour() % n)
      .minusSeconds(dateTime.getSecondOfMinute())
      .minusMillis(dateTime.getMillisOfSecond());
    return dateTime.toString(HOUR_MINUTE_FORMATTER);
  }

  /**
   * 根据一天内的分钟数，转化成相应的时间
   */
  public static Date formatToHourMin(String dateStr, int minutes) {
    Preconditions.checkArgument(!Strings.isNullOrEmpty(dateStr));
    Preconditions.checkArgument(minutes >= 0);

    DateTime dateTime = new DateTime(dateStr).millisOfDay().withMinimumValue();
    dateTime = dateTime.plusMinutes(minutes);
    return dateTime.toDate();
  }

  /**
   * 获得到目前为止的分钟数
   */
  public static int getMinutes() {
    DateTime dateTime = new DateTime();
    return dateTime.getMinuteOfDay();
  }

  /**
   * 在date的基础上，减去n分钟
   */
  public static Date minusMinutes(Date date, int n) {
    Preconditions.checkNotNull(date);

    DateTime dateTime = new DateTime(date);
    dateTime = dateTime.minusMinutes(n);
    return dateTime.toDate();
  }

  /**
   * 在date的基础上，加上n分钟
   */
  public static Date plusMinutes(Date date, int n) {
    Preconditions.checkNotNull(date);

    DateTime dateTime = new DateTime(date);
    dateTime = dateTime.plusMinutes(n);
    return dateTime.toDate();
  }

  /**
   * 获得昨天开始的时间
   */
  public static Date getYesterdayStartDate() {
    DateTime dateTime = new DateTime();
    dateTime = dateTime.minusDays(1);
    return new Date(dateTime.millisOfDay().withMinimumValue().getMillis());
  }

  /**
   * 获得昨天结束的时间
   */
  public static long getYesterdayEndTimestamp() {
    DateTime dateTime = new DateTime();
    dateTime = dateTime.minusDays(1).millisOfDay().withMaximumValue();
    return dateTime.getMillis();
  }

  /**
   * 判断dateStr1是否等于dateStr2或者在dateStr2之前
   */
  public static boolean isBeforeOrEqual(String dateStr1, String dateStr2) {
    Preconditions.checkArgument(!Strings.isNullOrEmpty(dateStr1));
    Preconditions.checkArgument(!Strings.isNullOrEmpty(dateStr2));

    DateTime dateTime1 = DATE_TIME_FORMATTER.parseDateTime(dateStr1);
    DateTime dateTime2 = DATE_TIME_FORMATTER.parseDateTime(dateStr2);

    return dateTime1.isBefore(dateTime2) || dateTime1.equals(dateTime2);
  }

  /**
   * 获取当前所属的小时的开始时间
   */
  public static Date getHourStartDate() {
    // 获取当前时间
    DateTime dateTime = new DateTime();
    // 减去分钟、秒、毫秒
    dateTime = dateTime.minusMinutes(dateTime.getMinuteOfHour())
      .minusSeconds(dateTime.getSecondOfMinute())
      .minusMillis(dateTime.getMillisOfSecond());
    return dateTime.toDate();
  }

  /**
   * 获取当前所属的小时的开始时间
   */
  public static String getHourStartDateStr() {
    // 获取当前时间
    DateTime dateTime = new DateTime();
    // 减去分钟、秒、毫秒
    dateTime = dateTime.minusMinutes(dateTime.getMinuteOfHour())
      .minusSeconds(dateTime.getSecondOfMinute())
      .minusMillis(dateTime.getMillisOfSecond());
    return dateTime.toString(SPECIFIC_DATE_TIME_FORMATTER);
  }

  /**
   * 当前时间减去num小时
   */
  public static Date minusHours(Date date, int num) {
    Preconditions.checkNotNull(date);

    DateTime dateTime = new DateTime(date);
    dateTime = dateTime.minusHours(num);
    return dateTime.toDate();
  }

  /**
   * 获得该date的hour值
   */
  public static int getHour(Date date) {
    Preconditions.checkNotNull(date);

    DateTime dateTime = new DateTime(date);
    return dateTime.getHourOfDay();
  }

  /**
   * 获得dateStr这一天开始的时间
   */
  public static Date getStartDateByDateStr(String dateStr) {
    Preconditions.checkArgument(!Strings.isNullOrEmpty(dateStr));

    DateTime dateTime = new DateTime(dateStr);
    return dateTime.millisOfDay().withMinimumValue().toDate();
  }

  /**
   * 将date转化为long
   */
  public static long formatToTimeMillis(Date date) {
    Preconditions.checkNotNull(date);

    DateTime dateTime = new DateTime(date);
    return dateTime.getMillis();
  }

  /**
   * 根据date，获取所属日期
   */
  public static String getDateStrByDate(Date date) {
    Preconditions.checkNotNull(date);

    DateTime dateTime = new DateTime(date);
    return dateTime.toString(DATE_TIME_FORMATTER);
  }

  //region getSecondsToEndOfDay

  /**
   * 获取现在到当天结束的时间
   */
  public static int getSecondsToEndOfDay() {
    return getSecondsToEndOfDay(new Date());
  }

  /**
   * 根据date，获取现在到当天结束的时间
   */
  public static int getSecondsToEndOfDay(Date date) {
    Preconditions.checkNotNull(date);

    DateTime dateTime = new DateTime(date);
    long curTime = dateTime.getMillis();
    long endOfDayTime = dateTime.millisOfDay().withMaximumValue().getMillis();
    return (int) ((endOfDayTime - curTime) / 1000);
  }

  //endregion

  /**
   * 获取date所属日期的开始时间
   */
  public static Date getStartDateByDate(Date date) {
    Preconditions.checkNotNull(date);

    DateTime dateTime = new DateTime(date);
    return dateTime.millisOfDay().withMinimumValue().toDate();
  }

  /**
   * 根据dateStr，获取当天最开始的时间戳
   */
  public static long getStartTimestampByDateStr(String dateStr) {
    Preconditions.checkArgument(!Strings.isNullOrEmpty(dateStr));

    DateTime dateTime = new DateTime(dateStr);
    return dateTime.millisOfDay().withMinimumValue().getMillis();
  }

  /**
   * 根据dateStr，获取当天结束的时间戳
   */
  public static long getEndTimestampByDateStr(String dateStr) {
    Preconditions.checkArgument(!Strings.isNullOrEmpty(dateStr));

    DateTime dateTime = new DateTime(dateStr);
    return dateTime.millisOfDay().withMaximumValue().getMillis();
  }

  /**
   * 根据timestamp，获取当天最开始的时间戳
   */
  public static long getStartTimestampByTimestamp(long timestamp) {
    Preconditions.checkArgument(timestamp > 0);

    DateTime dateTime = new DateTime(timestamp);
    return dateTime.millisOfDay().withMinimumValue().getMillis();
  }

  /**
   * 根据date，获取昨天的时间字符串
   */
  public static String getYesterdayDateStrByDate(Date date) {
    Preconditions.checkNotNull(date);

    DateTime dateTime = new DateTime(date);
    dateTime = dateTime.minusDays(1);
    return dateTime.toString(DATE_TIME_FORMATTER);
  }

  /**
   * 将"yyyy-MM-dd HH:mm:ss"格式的字符串，获得"yyyy-MM-dd"格式的字符串
   */
  public static String formatToDateStr(String specialDateStr) {
    Preconditions.checkArgument(!Strings.isNullOrEmpty(specialDateStr));

    DateTime dateTime = SPECIFIC_DATE_TIME_FORMATTER.parseDateTime(specialDateStr);
    return dateTime.toString(DATE_TIME_FORMATTER);
  }

  /**
   * 根据字符串，获取日期。dateStr格式：yyyy-MM-dd HH:mm:ss
   */
  public static Date formatSpecialToDate(String dateStrSpecial) {
    DateTime dateTime = SPECIFIC_DATE_TIME_FORMATTER.parseDateTime(dateStrSpecial);
    return dateTime.toDate();
  }

  /**
   * 根据date，获取所属的n分钟的开始时间
   */
  public static String getDateMinuteStr(Date date, int n) {
    Preconditions.checkNotNull(date);
    Preconditions.checkArgument(n > 0);

    DateTime dateTime = new DateTime(date);
    dateTime = dateTime.minusMinutes(dateTime.getMinuteOfHour() % n)
      .minusSeconds(dateTime.getSecondOfMinute())
      .minusMillis(dateTime.getMillisOfSecond());
    return dateTime.toString(SPECIFIC_DATE_TIME_FORMATTER);
  }

  /**
   * 根据date，获取所属的n分钟的结束时间
   */
  public static String getDateMinuteEndStr(Date date, int n) {
    Preconditions.checkNotNull(date);
    Preconditions.checkArgument(n > 0);

    DateTime dateTime = new DateTime(date);
    dateTime = dateTime.plusMinutes(n - dateTime.getMinuteOfHour() % n)
      .minusSeconds(dateTime.getSecondOfMinute())
      .minusMillis(dateTime.getMillisOfSecond());
    return dateTime.toString(SPECIFIC_DATE_TIME_FORMATTER);
  }

  /**
   * 计算两个时间戳相差的天数
   */
  public static int calDaysBetweenTwoTimestamp(long startTimestamp, long endTimestamp) {
    Preconditions.checkArgument(startTimestamp <= endTimestamp);

    DateTime startDateTime = new DateTime(startTimestamp);
    startDateTime = new DateTime(startDateTime.toString(DATE_TIME_FORMATTER));

    DateTime endDateTime = new DateTime(endTimestamp);
    endDateTime = new DateTime(endDateTime.toString(DATE_TIME_FORMATTER));

    return (int) ((endDateTime.getMillis() - startDateTime.getMillis()) / (1000 * 60 * 60
      * 24));
  }

  /**
   * 根据date，取millis的开始时间，再减去millis
   */
  public static Date getMillisDate(Date date, int millis) {
    Preconditions.checkNotNull(date);
    Preconditions.checkArgument(millis > 0);

    DateTime dateTime = new DateTime(date);
    dateTime = dateTime.minusMillis(dateTime.getMillisOfDay() % millis)
      .minusMillis(millis);

    return dateTime.toDate();
  }

  /**
   * 格式转化 yyyyMMddHHmmss -> HH:mm:ss
   */
  public static String formatMinStr(String minStr) {
    Preconditions.checkArgument(!Strings.isNullOrEmpty(minStr));
    DateTime dateTime = ONLY_NUM_DATE_TIME_FORMATTER.parseDateTime(minStr);
    return dateTime.toString(HOUR_MINUTE_FORMATTER);
  }
}
