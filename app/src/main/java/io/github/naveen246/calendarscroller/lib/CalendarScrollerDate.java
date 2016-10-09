package io.github.naveen246.calendarscroller.lib;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import org.joda.time.LocalDateTime;

public class CalendarScrollerDate {
  private static final String YEAR_FORMAT = "yyyy";
  private static final String DATE_FORMAT = "d";
  String dayOfMonth;
  String dayOfWeek;
  String month;
  String year;
  LocalDateTime dateTime;

  public CalendarScrollerDate(LocalDateTime dateTime, Locale locale) {
    dayOfMonth = new SimpleDateFormat(DATE_FORMAT, locale).format(dateTime.toDate());
    dayOfWeek = dateTime.dayOfWeek().getAsShortText(locale);
    month = dateTime.monthOfYear().getAsText(locale);
    year = new SimpleDateFormat(YEAR_FORMAT, locale).format(dateTime.toDate());
    this.dateTime = dateTime;
  }

  public Date toDate() {
    return dateTime.toDate();
  }
}
