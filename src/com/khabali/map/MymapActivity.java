package com.khabali.map;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.Address;
//import android.location.Geocoder;
import android.os.Bundle;
//import android.util.Log;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.khabali.fft.GridFriends;
import com.khabali.fft.R;

public class MymapActivity extends MapActivity {

	List<Overlay> mapOverlays;
	Drawable drawable,homeDrawable;
	public MapView mapView;
	CustomItemizedOverlay<CustomOverlayItem> itemizedOverlay;
	
	String name;
	String home;
	String local;
	String birth;
	String relation;
	String id;
	
	
	public static double lat,lng, My_lat, My_lng;
	GeoPoint gPoint, gPoint2;
	public List<Address> adresses = new ArrayList<Address>();
	private MapController mc;
	
	GPSTracker gps;
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);
		
		Intent i = getIntent();
		id = i.getExtras().getString(GridFriends.TAG_ID);
		name = i.getExtras().getString(GridFriends.TAG_NAME);
		home = i.getExtras().getString(GridFriends.TAG_HOME);
		local = i.getExtras().getString(GridFriends.TAG_LOCATION);
		birth = i.getExtras().getString(GridFriends.TAG_BIRHT);
		relation = i.getExtras().getString(GridFriends.TAG_RELATION);
		
		
		
		mapView = (MapView) findViewById(R.id.mapview);
		mapView.setBuiltInZoomControls(true);
		//Geocoder coder = new Geocoder(getApplicationContext());
		mapOverlays = mapView.getOverlays();
		
//		if(Geocoder.isPresent()) {
//		
//		 int 	counter = 0;
//		 boolean goo = true;
//		 
//		while(counter < 10 && goo ) {
//		try {
//			
//			counter ++;
//			adresses = coder.getFromLocationName(home, 1);
//			
//			if(adresses.size() != 0 ) goo = false;
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		}
		
//			if(adresses.size() != 0  && adresses != null) {
//					
//					 lat = adresses.get(0).getLatitude();
//					 lng = adresses.get(0).getLongitude();
//				
//					gPoint = new GeoPoint((int) (lat * 1E6),(int) (lng * 1E6));
//				}
//				else  gPoint = new GeoPoint((int) (0 * 1E6),(int) (0 * 1E6));
//				
		
				gPoint = getGeoPoint(getLocationInfo(home.replaceAll(" ","%20")));
				
				homeDrawable = MymapActivity.this.getResources().getDrawable(R.drawable.home);
				itemizedOverlay = new CustomItemizedOverlay<CustomOverlayItem>(
						homeDrawable, MymapActivity.this, mapView);
				
				CustomOverlayItem overlayitem = new CustomOverlayItem(gPoint,
						name+"\n(This is my home !)",
						"birthday : "    + birth 
						+"\nStatut : "   + relation
						+"\nHomeTown : " + home 
						+"\nLocation : " + local
						+"\nPress to calculate distance between you & him !", 
						"http://graph.facebook.com/"+id+"/picture");

				itemizedOverlay.addOverlay(overlayitem);
				mapOverlays.add(itemizedOverlay);
			
		
		
		if(local != " ***** " && local != null) {
			
//			 counter = 0;
//			 goo = true;
//			 
//			while(counter < 10 && goo) {
//			try {
//				
//				counter++;
//				adresses = coder.getFromLocationName(local, 1);
//				if(adresses.size() != 0 ) goo = false;
//				
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//			}
//				if(adresses.size() != 0  && adresses != null) {
//						
//						 lat = adresses.get(0).getLatitude();
//						 lng = adresses.get(0).getLongitude();
//					
//						gPoint = new GeoPoint((int) (lat * 1E6),(int) (lng * 1E6));
//					}
//					else  gPoint = new GeoPoint((int) (0 * 1E6),(int) (0 * 1E6));
//					
			gPoint = getGeoPoint(getLocationInfo(local.replaceAll(" ","%20")));
			
			
					homeDrawable = MymapActivity.this.getResources().getDrawable(R.drawable.marker);
					itemizedOverlay = new CustomItemizedOverlay<CustomOverlayItem>(
							homeDrawable, MymapActivity.this, mapView);
					
					 overlayitem = new CustomOverlayItem(gPoint,
							name+"\n(I'm here !)",
							"birthday : "    + birth 
							+"\nStatut : "   + relation
							+"\nHomeTown : " + home 
							+"\nLocation : " + local
							+"\nPress to calculate distance between you & him !", 
							"http://graph.facebook.com/"+id+"/picture");

					itemizedOverlay.addOverlay(overlayitem);
					mapOverlays.add(itemizedOverlay);
				
		}
		
//		}else {
//			Log.d("GeoCoder", "Geocoder is not imlemented !");
//		}
		
		gps =  new GPSTracker(getApplicationContext());
		
		if(gps.canGetLocation()) {
			My_lat = gps.getLatitude();
			My_lng = gps.getLongitude();
			
			drawable = MymapActivity.this.getResources().getDrawable(R.drawable.me);
			itemizedOverlay = new CustomItemizedOverlay<CustomOverlayItem>(
					drawable, MymapActivity.this, mapView);
			gPoint2 = new GeoPoint((int) (My_lat * 1E6),(int) (My_lng * 1E6));
			  overlayitem = new CustomOverlayItem(gPoint2,
					"this is You !",
					"this is your last known location\n" +
					"Press to calculate distance between you & him !",
					"");

			itemizedOverlay.addOverlay(overlayitem);
			mapOverlays.add(itemizedOverlay);

		}else {
			Toast.makeText(getApplicationContext(), "Your GPS is disable", Toast.LENGTH_SHORT).show();
		}
		
		mc = mapView.getController();
		mc.animateTo(gPoint);
		mc.setZoom(8);
		mapView.postInvalidate();
		
		// new Draw().execute();
	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}
	
	

public static JSONObject getLocationInfo(String address) {

		HttpGet httpGet = new HttpGet("http://maps.google."
				+ "com/maps/api/geocode/json?address=" + address
				+ "ka&sensor=false");
		HttpClient client = new DefaultHttpClient();
		HttpResponse response;
		StringBuilder stringBuilder = new StringBuilder();

		try {
			response = client.execute(httpGet);
			HttpEntity entity = response.getEntity();
			InputStream stream = entity.getContent();
			int b;
			while ((b = stream.read()) != -1) {
				stringBuilder.append((char) b);
			}
		} catch (ClientProtocolException e) {
		} catch (IOException e) {
		}

		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject = new JSONObject(stringBuilder.toString());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return jsonObject;
	}


public static GeoPoint getGeoPoint(JSONObject jsonObject) {

		Double lon = new Double(0);
		Double lat = new Double(0);

		try {

			lon = ((JSONArray)jsonObject.get("results")).getJSONObject(0)
				.getJSONObject("geometry").getJSONObject("location")
				.getDouble("lng");

			lat = ((JSONArray)jsonObject.get("results")).getJSONObject(0)
				.getJSONObject("geometry").getJSONObject("location")
				.getDouble("lat");

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return new GeoPoint((int) (lat * 1E6), (int) (lon * 1E6));

	}
/*	
	public class Draw extends AsyncTask<String, String, String> {

		@Override
		protected String doInBackground(String... params) {
			
			NavigationDataSet nv = MapService.calculateRoute(lat, lng, My_lat, My_lng, 0);
			
			drawPath(nv, Color.GREEN, mapView);
	
			return null;
		}
		
	}
	

*/


/*
public void drawPath(NavigationDataSet navSet, int color, MapView mMapView01) {

    Log.d("KKKKK", "map color before: " + color);        

    // color correction for dining, make it darker
    if (color == Color.parseColor("#add331")) color = Color.parseColor("#6C8715");
    Log.d("KKKK", "map color after: " + color);

    Collection overlaysToAddAgain = new ArrayList();
    for (Iterator iter = mMapView01.getOverlays().iterator(); iter.hasNext();) {
        Object o = iter.next();
        Log.d("KKKKKK", "overlay type: " + o.getClass().getName());
        if (!RouteOverlay.class.getName().equals(o.getClass().getName())) {
            // mMapView01.getOverlays().remove(o);
            overlaysToAddAgain.add(o);
        }
    }
    mMapView01.getOverlays().clear();
    mMapView01.getOverlays().addAll(overlaysToAddAgain);

    String path = navSet.getRoutePlacemark().getCoordinates();
    Log.d("KKKKK", "path=" + path);
    if (path != null && path.trim().length() > 0) {
        String[] pairs = path.trim().split(" ");

        Log.d("KKKKKK", "pairs.length=" + pairs.length);

        String[] lngLat = pairs[0].split(","); // lngLat[0]=longitude lngLat[1]=latitude lngLat[2]=height

        Log.d("KKKKKK", "lnglat =" + lngLat + ", length: " + lngLat.length);

        if (lngLat.length<3) lngLat = pairs[1].split(","); // if first pair is not transferred completely, take seconds pair //TODO 

        try {
            GeoPoint startGP = new GeoPoint((int) (Double.parseDouble(lngLat[1]) * 1E6), (int) (Double.parseDouble(lngLat[0]) * 1E6));
            mMapView01.getOverlays().add(new RouteOverlay(startGP, startGP, 1));
            GeoPoint gp1;
            GeoPoint gp2 = startGP;

            for (int i = 1; i < pairs.length; i++) // the last one would be crash
            {
                lngLat = pairs[i].split(",");

                gp1 = gp2;

                if (lngLat.length >= 2 && gp1.getLatitudeE6() != 0 && gp1.getLongitudeE6() != 0
                        && gp2.getLatitudeE6() != 0 && gp2.getLongitudeE6() != 0) {

                    // for GeoPoint, first:latitude, second:longitude
                    gp2 = new GeoPoint((int) (Double.parseDouble(lngLat[1]) * 1E6), (int) (Double.parseDouble(lngLat[0]) * 1E6));

                    if (gp2.getLatitudeE6() != 22200000) { 
                        mMapView01.getOverlays().add(new RouteOverlay(gp1, gp2, 2, color));
                        Log.d("KKKKK", "draw:" + gp1.getLatitudeE6() + "/" + gp1.getLongitudeE6() + " TO " + gp2.getLatitudeE6() + "/" + gp2.getLongitudeE6());
                    }
                }
                // Log.d(myapp.APP,"pair:" + pairs[i]);
            }
            //routeOverlays.add(new RouteOverlay(gp2,gp2, 3));
            mMapView01.getOverlays().add(new RouteOverlay(gp2, gp2, 3));
        } catch (NumberFormatException e) {
            Log.e("KKKKKK", "Cannot draw route.", e);
        }
    }
    // mMapView01.getOverlays().addAll(routeOverlays); // use the default color
    mMapView01.setEnabled(true);
}
*/	
}
