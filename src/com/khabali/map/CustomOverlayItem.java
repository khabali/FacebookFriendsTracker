package com.khabali.map;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.OverlayItem;

public class CustomOverlayItem extends OverlayItem {

	String mImageUrl;

	public CustomOverlayItem(GeoPoint point, String title, String snippet,
			String imageUrl) {
		super(point, title, snippet);
		mImageUrl = imageUrl;
	}

	public String getmImageUrl() {
		return mImageUrl;
	}

	public void setmImageUrl(String mImageUrl) {
		this.mImageUrl = mImageUrl;
	}

}
