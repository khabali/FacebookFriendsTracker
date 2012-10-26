package com.khabali.map;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;
import com.readystatesoftware.mapviewballoons.BalloonItemizedOverlay;
import com.readystatesoftware.mapviewballoons.BalloonOverlayView;

public class CustomItemizedOverlay<Item extends OverlayItem> extends
		BalloonItemizedOverlay<CustomOverlayItem> {

	private ArrayList<CustomOverlayItem> mapOverlays = new ArrayList<CustomOverlayItem>();

	private Context mContext;
	Location src = new Location("");
	Location dest = new Location("");
	GeoPoint p;

	public CustomItemizedOverlay(Drawable defaultMarker, MapView mapView) {
		super(boundCenterBottom(defaultMarker), mapView);
	}

	public CustomItemizedOverlay(Drawable defaultMarker, Context context,
			MapView mapView) {
		this(defaultMarker, mapView);
		setmContext(context);
	}

	@Override
	protected CustomOverlayItem createItem(int i) {
		return mapOverlays.get(i);
	}

	@Override
	public int size() {
		return mapOverlays.size();
	}

	@Override
	protected boolean onBalloonTap(int index, CustomOverlayItem item) {
		
		src.setLatitude(MymapActivity.My_lat);
		src.setLongitude(MymapActivity.My_lng);
		
		p = item.getPoint();
		dest.setLatitude(p.getLatitudeE6() / 1E6);
		dest.setLongitude(p.getLongitudeE6() / 1E6);
		
		Toast.makeText(mContext, "The distance :" + ( src.distanceTo(dest) / 1000 ) +" KM" ,
				Toast.LENGTH_LONG).show();
		return true;
	}

	/*
	 * @Override protected boolean onTap(int index) { OverlayItem item =
	 * mapOverlays.get(index); AlertDialog.Builder dialog = new
	 * AlertDialog.Builder(mContext); dialog.setTitle(item.getTitle());
	 * dialog.setMessage(item.getSnippet()); dialog.show(); return true; }
	 */

	public void addOverlay(CustomOverlayItem overlay) {
		mapOverlays.add(overlay);
		this.populate();
	}

	@Override
	protected BalloonOverlayView<CustomOverlayItem> createBalloonOverlayView() {
		// use our custom balloon view with our custom overlay item type:
		return new CustomBalloonView<CustomOverlayItem>(getMapView()
				.getContext(), getBalloonBottomOffset());
	}

	public Context getmContext() {
		return mContext;
	}

	public void setmContext(Context mContext) {
		this.mContext = mContext;
	}

}