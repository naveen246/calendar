package io.github.naveen246.calendarscroller.lib;

import android.support.v7.widget.RecyclerView;
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

  public CalendarScrollerDateAdapter(Listener listener) {
    this.listener = listener;
  }

  @Override public DateViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view =
        LayoutInflater.from(parent.getContext()).inflate(R.layout.item_date_list, parent, false);
    DateViewHolder viewHolder = new DateViewHolder(view);
    return viewHolder;
  }

  @Override public void onBindViewHolder(DateViewHolder holder, int position) {
    final CalendarScrollerDate date = calendarScrollerDates.get(position);
    listener.changeMonth(date);
    holder.dateTextView.setText(date.dayOfMonth);
    holder.dayTextView.setText(date.dayOfWeek);
    holder.itemView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
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

  public class DateViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.textview_date) TextView dateTextView;
    @BindView(R.id.textview_day) TextView dayTextView;

    public DateViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }
  }

  public interface Listener {
    void changeMonth(CalendarScrollerDate date);
    void onDateSelected(CalendarScrollerDate date);
  }
}
