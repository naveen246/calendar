package io.github.naveen246.calendarscroller.lib;

import java.util.Date;
import java.util.Locale;
import org.joda.time.LocalDateTime;

public class CalendarScrollerDate {
  String dayOfMonth;
  String dayOfWeek;
  String month;
  String year;
  LocalDateTime dateTime;

  public CalendarScrollerDate(LocalDateTime date, Locale locale) {
    dayOfMonth = date.dayOfMonth().getAsText(locale);
    dayOfWeek = date.dayOfWeek().getAsShortText(locale);
    month = date.monthOfYear().getAsText(locale);
    year = date.year().getAsText(locale);
    dateTime = date;
  }

  public Date toDate() {
    return dateTime.toDate();
  }
}
