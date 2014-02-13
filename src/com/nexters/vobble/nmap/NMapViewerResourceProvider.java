package com.nexters.vobble.nmap;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import android.widget.ListView;

import com.nexters.vobble.R;
import com.nhn.android.maps.NMapOverlayItem;
import com.nhn.android.maps.overlay.NMapPOIitem;
import com.nhn.android.mapviewer.overlay.NMapResourceProvider;

public class NMapViewerResourceProvider extends NMapResourceProvider {
	public NMapViewerResourceProvider(Context context) {
		super(context);
	}

    @Override
    protected int findResourceIdForMarker(int markerId, boolean focused) {
        return R.drawable.ic_pin_01;
    }

    @Override
	public Drawable getDrawableForInfoLayer(NMapOverlayItem item) {
		// not supported
        return null;
	}

	@Override
	public Drawable[] getLocationDot() {
        // not supported
        return new Drawable[0];
	}

	@Override
	public Drawable getDirectionArrow() {
        // not supported
        return null;
	}

	@Override
	protected Drawable getDrawableForMarker(int markerId, boolean focused, NMapOverlayItem item) {
        // not supported
        return null;
	}

	@Override
	public Drawable getCalloutBackground(NMapOverlayItem item) {
        // not supported
        return null;
	}

	@Override
	public String getCalloutRightButtonText(NMapOverlayItem item) {
        // not supported
        return null;
	}

	@Override
	public Drawable[] getCalloutRightButton(NMapOverlayItem item) {
        // not supported
        return new Drawable[0];
	}

	@Override
	public Drawable[] getCalloutRightAccessory(NMapOverlayItem item) {
		// not supported
		return new Drawable[0];
	}

	@Override
	public int[] getCalloutTextColors(NMapOverlayItem item) {
        // not supported
        return new int[0];
	}

	@Override
	public int getParentLayoutIdForOverlappedListView() {
		// not supported
		return 0;
	}

	@Override
	public int getOverlappedListViewId() {
		// not supported
		return 0;
	}

	@Override
	public int getLayoutIdForOverlappedListView() {
		// not supported
		return 0;
	}

	@Override
	public int getListItemLayoutIdForOverlappedListView() {
		// not supported
		return 0;
	}

	@Override
	public int getListItemTextViewId() {
		// not supported
		return 0;
	}

	@Override
	public int getListItemTailTextViewId() {
		// not supported
		return 0;
	}

	@Override
	public int getListItemImageViewId() {
		// not supported
		return 0;
	}

	@Override
	public int getListItemDividerId() {
		// not supported
		return 0;
	}

	@Override
	public void setOverlappedListViewLayout(ListView listView, int itemCount, int width, int height) {
		// not supported
	}

	@Override
	public void setOverlappedItemResource(NMapPOIitem poiItem, ImageView imageView) {
		// not supported
	}
}
