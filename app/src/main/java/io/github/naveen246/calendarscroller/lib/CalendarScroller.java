package io.github.naveen246.calendarscroller.lib;

import android.content.Context;
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
import net.danlew.android.joda.JodaTimeAndroid;
import org.joda.time.LocalDateTime;

public class CalendarScroller extends FrameLayout {
  private Context context;
  private ScrollerViewHolder viewHolder;
  private LinearLayoutManager datesLayoutManager;
  private CalendarScrollerDateAdapter dateAdapter;
  private LocalDateTime initialDateTime;

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
    JodaTimeAndroid.init(context);
    View view = LayoutInflater.from(context).inflate(R.layout.layout_calendar_scroller, this, true);
    viewHolder = new ScrollerViewHolder(view);
    initializeViews();
  }

  private void initializeViews() {
    datesLayoutManager = new LinearLayoutManager(context);
    datesLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
    viewHolder.dateRecyclerView.setLayoutManager(datesLayoutManager);
    dateAdapter = new CalendarScrollerDateAdapter(this);
    initialDateTime = new LocalDateTime();
    dateAdapter.addData(getMonthData(initialDateTime));
    viewHolder.dateRecyclerView.setAdapter(dateAdapter);
    viewHolder.dateRecyclerView.addOnScrollListener(
        new CalendarScrollerListener(datesLayoutManager) {
          @Override public void onLoadMore(int page, int totalItemsCount) {
            loadMoreData(page);
          }
        });
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
      String dayOfMonth = Integer.toString(date.getDayOfMonth());
      String dayOfWeek = date.dayOfWeek().getAsShortText();
      String month = date.monthOfYear().getAsText();
      String year = date.year().getAsText();
      CalendarScrollerDate scrollerDate =
          new CalendarScrollerDate(dayOfMonth, dayOfWeek, month, year);
      dates.add(scrollerDate);
    }
    return dates;
  }

  public void changeMonth(CalendarScrollerDate date) {
    viewHolder.monthYearTextView.setText(date.month + " " + date.year);
  }

  public void changeLayoutDirection(String direction) {
    datesLayoutManager.setReverseLayout(true);
    viewHolder.dateRecyclerView.setLayoutManager(datesLayoutManager);
  }

  class ScrollerViewHolder {
    @BindView(R.id.textview_month_year) TextView monthYearTextView;
    @BindView(R.id.recyclerview_date) RecyclerView dateRecyclerView;

    public ScrollerViewHolder(View view) {
      ButterKnife.bind(this, view);
    }
  }
}
