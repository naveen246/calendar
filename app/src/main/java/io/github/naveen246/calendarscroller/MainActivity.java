package io.github.naveen246.calendarscroller;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.naveen246.calendarscroller.lib.CalendarScroller;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
  @BindView(R.id.layout_calendar) CalendarScroller calendarScroller;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    String languageCode = "ar";
    setLocale(languageCode);
    setContentView(R.layout.activity_main);
    ButterKnife.bind(this);
  }

  public void setLocale(String languageCode) {
    Resources res = getResources();
    DisplayMetrics dm = res.getDisplayMetrics();
    Configuration config = res.getConfiguration();
    config.setLocale(new Locale(languageCode));
    res.updateConfiguration(config, dm);
  }
}
