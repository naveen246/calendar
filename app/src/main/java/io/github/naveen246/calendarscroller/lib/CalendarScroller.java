package io.github.naveen246.calendarscroller.lib;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.naveen246.calendarscroller.R;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import net.danlew.android.joda.JodaTimeAndroid;
import org.joda.time.LocalDateTime;

public class CalendarScroller extends FrameLayout implements CalendarScrollerDateAdapter.Listener {
  private Context context;
  private ScrollerViewHolder viewHolder;
  private LinearLayoutManager datesLayoutManager;
  private CalendarScrollerDateAdapter dateAdapter;
  private LocalDateTime initialDateTime;
  private AttributeSet attributeSet;
  private int daysContainerBackgroundColor;
  private int monthYearTextColor;

  public CalendarScroller(Context context) {
    super(context);
    init(context, null);
  }

  public CalendarScroller(Context context, AttributeSet attributeSet) {
    super(context, attributeSet);
    init(context, attributeSet);
  }

  public CalendarScroller(Context context, AttributeSet attributeSet, int defStyleAttr) {
    super(context, attributeSet, defStyleAttr);
    init(context, attributeSet);
  }

  private void init(Context context, AttributeSet attributeSet) {
    this.context = context;
    this.attributeSet = attributeSet;
    JodaTimeAndroid.init(context);
    View view = LayoutInflater.from(context).inflate(R.layout.layout_calendar_scroller, this, true);
    viewHolder = new ScrollerViewHolder(view);
    setDefaultColors();
    parseAttributes();
    initializeViews(attributeSet);
  }

  private void initializeViews(AttributeSet attributeSet) {
    datesLayoutManager = new LinearLayoutManager(context);
    datesLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
    viewHolder.dateRecyclerView.setLayoutManager(datesLayoutManager);
    dateAdapter = new CalendarScrollerDateAdapter(context, this, attributeSet);
    initialDateTime = new LocalDateTime();
    dateAdapter.addData(getMonthData(initialDateTime));
    viewHolder.dateRecyclerView.setAdapter(dateAdapter);
    viewHolder.dateRecyclerView.addOnScrollListener(
        new CalendarScrollerListener(datesLayoutManager) {
          @Override public void onLoadMore(int page, int totalItemsCount) {
            loadMoreData(page);
          }
        });
    viewHolder.dateRecyclerView.setBackgroundColor(daysContainerBackgroundColor);
    viewHolder.monthYearTextView.setTextColor(monthYearTextColor);
  }

  public void loadMoreData(int page) {
    LocalDateTime date = initialDateTime.withDayOfMonth(1).plusMonths(page);
    dateAdapter.addData(getMonthData(date));
  }

  private List<CalendarScrollerDate> getMonthData(LocalDateTime dateTime) {
    List<CalendarScrollerDate> dates = new ArrayList<>();
    LocalDateTime start = dateTime.dayOfMonth().withMinimumValue();
    LocalDateTime end = dateTime.dayOfMonth().withMaximumValue();
    for (LocalDateTime date = start; !date.isAfter(end); date = date.plusDays(1)) {
      String dayOfMonth = date.dayOfMonth().getAsText(getLocale());
      String dayOfWeek = date.dayOfWeek().getAsShortText(getLocale());
      String month = date.monthOfYear().getAsText(getLocale());
      String year = date.year().getAsText(getLocale());
      CalendarScrollerDate scrollerDate =
          new CalendarScrollerDate(dayOfMonth, dayOfWeek, month, year);
      dates.add(scrollerDate);
    }
    return dates;
  }

  private void parseAttributes() {
    if (attributeSet != null) {
      TypedArray a = context.getTheme()
          .obtainStyledAttributes(attributeSet, R.styleable.CalendarScroller, 0, 0);
      try {
        monthYearTextColor = a.getColor(R.styleable.CalendarScroller_monthYearTextColor,
            getColor(R.color.defaultDayTextColor));
        daysContainerBackgroundColor =
            a.getColor(R.styleable.CalendarScroller_daysContainerBackgroundColor,
                getColor(R.color.defaultDaysContainerBackgroundColor));
      } finally {
        a.recycle();
      }
    }
  }

  private void setDefaultColors() {
    monthYearTextColor = getColor(R.color.defaultDayTextColor);
    daysContainerBackgroundColor = getColor(R.color.defaultDaysContainerBackgroundColor);
  }

  private int getColor(int resId) {
    return context.getResources().getColor(resId);
  }

  private Locale getLocale() {
    return context.getResources().getConfiguration().locale;
  }

  public void changeMonth(CalendarScrollerDate date) {
    viewHolder.monthYearTextView.setText(date.month + " " + date.year);
  }

  @Override public void onDateSelected(CalendarScrollerDate date) {

  }

  class ScrollerViewHolder {
    @BindView(R.id.textview_month_year) TextView monthYearTextView;
    @BindView(R.id.recyclerview_date) RecyclerView dateRecyclerView;

    public ScrollerViewHolder(View view) {
      ButterKnife.bind(this, view);
    }
  }
}
