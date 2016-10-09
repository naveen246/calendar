package io.github.naveen246.calendarscroller.lib;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.naveen246.calendarscroller.R;
import java.util.ArrayList;
import java.util.List;

public class CalendarScrollerDateAdapter
    extends RecyclerView.Adapter<CalendarScrollerDateAdapter.DateViewHolder> {
  private List<CalendarScrollerDate> calendarScrollerDates = new ArrayList<>();
  private Listener listener;
  private Context context;
  private int dayTextColor;
  private int selectedDayTextColor;
  private int selectedDayBackgroundColor;
  private boolean displayDayOfWeek = true;
  private int selectedPosition = -1;

  public CalendarScrollerDateAdapter(Context context, Listener listener,
      AttributeSet attributeSet) {
    this.listener = listener;
    this.context = context;
    setDefaultColors();
    parseAttributes(attributeSet);
  }

  @Override public DateViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.item_calendar_scroller, parent, false);
    DateViewHolder viewHolder = new DateViewHolder(view);
    return viewHolder;
  }

  @Override public void onBindViewHolder(final DateViewHolder holder, final int position) {
    final CalendarScrollerDate date = calendarScrollerDates.get(position);
    listener.setMonth(date);
    holder.dayOfMonthTextView.setText(date.dayOfMonth);
    holder.dayOfWeekTextView.setText(date.dayOfWeek);
    if (!displayDayOfWeek) {
      holder.dayOfWeekTextView.setVisibility(View.INVISIBLE);
    }
    if (position == selectedPosition) {
      highlightSelectedDate(holder);
    } else {
      holder.dayOfMonthTextView.setTextColor(dayTextColor);
      holder.dayOfWeekTextView.setTextColor(dayTextColor);
      holder.itemView.setBackgroundColor(getColor(R.color.defaultDayBackgroundColor));
    }
    holder.itemView.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        selectedPosition = position;
        highlightSelectedDate(holder);
        listener.onDateSelected(date);
      }
    });
  }

  @Override public int getItemCount() {
    return calendarScrollerDates.size();
  }

  public void addData(List<CalendarScrollerDate> dates) {
    calendarScrollerDates.addAll(dates);
    notifyDataSetChanged();
  }

  private void highlightSelectedDate(DateViewHolder holder) {
    holder.dayOfMonthTextView.setTextColor(selectedDayTextColor);
    holder.dayOfWeekTextView.setTextColor(selectedDayTextColor);
    holder.itemView.setBackgroundColor(selectedDayBackgroundColor);
  }

  private void setDefaultColors() {
    dayTextColor = getColor(R.color.defaultDayTextColor);
    selectedDayTextColor = getColor(R.color.defaultSelectedDayTextColor);
    selectedDayBackgroundColor = getColor(R.color.defaultSelectedDayBackgroundColor);
  }

  private void parseAttributes(AttributeSet attributeSet) {
    if (attributeSet != null) {
      TypedArray a = context.getTheme()
          .obtainStyledAttributes(attributeSet, R.styleable.CalendarScroller, 0, 0);
      try {
        dayTextColor = a.getColor(R.styleable.CalendarScroller_dayTextColor,
            getColor(R.color.defaultDayTextColor));
        selectedDayTextColor = a.getColor(R.styleable.CalendarScroller_selectedDayTextColor,
            getColor(R.color.defaultSelectedDayTextColor));
        selectedDayBackgroundColor =
            a.getColor(R.styleable.CalendarScroller_selectedDayBackgroundColor,
                getColor(R.color.defaultSelectedDayBackgroundColor));
        displayDayOfWeek = a.getBoolean(R.styleable.CalendarScroller_displayDayOfWeek, true);
      } finally {
        a.recycle();
      }
    }
  }

  private int getColor(int resId) {
    return context.getResources().getColor(resId);
  }

  public interface Listener {
    void setMonth(CalendarScrollerDate date);

    void onDateSelected(CalendarScrollerDate date);
  }

  public class DateViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.textview_day_of_month) TextView dayOfMonthTextView;
    @BindView(R.id.textview_day_of_week) TextView dayOfWeekTextView;

    public DateViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }
  }
}
