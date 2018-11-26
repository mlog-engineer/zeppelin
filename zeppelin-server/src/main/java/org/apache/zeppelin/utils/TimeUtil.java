package org.apache.zeppelin.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.Hours;
import org.joda.time.Minutes;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.joda.time.Seconds;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

/**
 * TimeUtil .
 */
public class TimeUtil {

  public static int hoursBetween(Date d1, Date d2) {
    DateTime dateTime1 = new DateTime(d1);
    DateTime dateTime2 = new DateTime(d2);
    return Hours.hoursBetween(dateTime2, dateTime1).getHours();
  }

  public static int minutesBetween(Date d1, Date d2) {
    DateTime dateTime1 = new DateTime(d1);
    DateTime dateTime2 = new DateTime(d2);
    return Minutes.minutesBetween(dateTime2, dateTime1).getMinutes();
  }

  public static int secondsBetween(Date d1, Date d2) {
    DateTime dateTime1 = new DateTime(d1);
    DateTime dateTime2 = new DateTime(d2);
    return Seconds.secondsBetween(dateTime2, dateTime1).getSeconds();
  }

  public static Duration duration(Date d1, Date d2) {
    DateTime dateTime1 = new DateTime(d1);
    DateTime dateTime2 = new DateTime(d2);
    Duration duration = new Duration(dateTime1, dateTime2);
    return duration;
  }

  public static String toReadableDuration(Date startDate, Date endDate) {
    Period period = new Period(new DateTime(startDate), new DateTime(endDate),
        PeriodType.dayTime());
    PeriodFormatterBuilder builder = new PeriodFormatterBuilder();
    if (period.getDays() > 0) {
      builder.appendHours().appendSeparator("d ");
    }
    if (period.getHours() > 0) {
      builder.appendHours().appendSeparator("h ");
    }
    if (period.getMinutes() > 0) {
      builder.appendMinutes().appendSeparator("m ");
    }
    builder.appendSeconds().appendSuffix("s");
    PeriodFormatter periodFormatter = builder.toFormatter();
    return periodFormatter.print(period);
  }

  public static String format(Date date, String format) {
    SimpleDateFormat sdf = new SimpleDateFormat(format);
    return sdf.format(date);
  }
}

