package net.sareweb.android.dBizi.activity;


import net.sareweb.android.dBizi.R;
import android.app.Activity;
import android.os.Bundle;

import com.googlecode.androidannotations.annotations.EActivity;

@EActivity
public class StationMapActivity extends Activity {
	
    private static String TAG = "StationListActivity";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.station_map);
    }

}
