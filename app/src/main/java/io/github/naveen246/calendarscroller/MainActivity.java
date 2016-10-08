package io.github.naveen246.calendarscroller;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.naveen246.calendarscroller.lib.CalendarScroller;

public class MainActivity extends AppCompatActivity {
  @BindView(R.id.layout_calendar) CalendarScroller calendarScroller;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    ButterKnife.bind(this);
  }
}
