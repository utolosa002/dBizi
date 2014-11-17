package net.sareweb.android.dBizi.activity;


import java.util.List;

import net.sareweb.android.dBizi.R;
import net.sareweb.android.dBizi.model.City;
import net.sareweb.android.dBizi.model.Station;
import net.sareweb.android.dBizi.util.CityUtil;
import net.sareweb.android.dBizi.util.ConnectionUtil;
import net.sareweb.android.dBizi.util.DBiziConstants;
import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
@EActivity
public class StationMapActivity extends Activity {
	
    private static String TAG = "StationMapActivity";
    private GoogleMap mMap;

    @SuppressLint("NewApi")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(!ConnectionUtil.isOnline(this)){
    		setContentView(R.layout.not_connected);
    	}else{
            setContentView(R.layout.station_map);

            mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
    				.getMap();
    		mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(DBiziConstants.BDIZI_DEFAULT_LAT,
    				DBiziConstants.BDIZI_DEFAULT_LNG), DBiziConstants.BDIZI_DEFAULT_ZOOM));
    		
    	    loadStationsInMap();
    	}
    }
    
	@Background
	public void loadStationsInMap() {
		userPrefs = getSharedPreferences(DBiziConstants.USER_PREFS,
				MODE_PRIVATE);
		String idioma = userPrefs.getString(DBiziConstants.USER_PREFS_LANG,
				DBiziConstants.USER_PREF_LANG_EU);
		city = CityUtil.initCity(idioma);
		finishedBackgroundThread(0);
	}

	@UiThread
	void finishedBackgroundThread(int result) {

		if (mMap != null) {
			List<Station> stations = city.getStations();
			for (int i = 0; i < stations.size(); i++) {
				Double lat = stations.get(i).getLatitud();
				Double lng = stations.get(i).getLongitud();
				mMap.addMarker(new MarkerOptions()
						.position(new LatLng(lat, lng))
						.snippet(
								stations.get(i).getBicisDisponibles()
										+ " bizi / "
										+ stations.get(i).getPlazasTotales())
						.title(stations.get(i).getNombre()));
			}
		}
	}
	
	City city;
	SharedPreferences userPrefs;

}

