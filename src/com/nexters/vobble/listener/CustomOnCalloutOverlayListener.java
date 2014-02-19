package com.nexters.vobble.listener;

import android.graphics.Rect;
import com.nexters.vobble.nmap.NMapCalloutBasicOverlay;
import com.nhn.android.maps.NMapOverlay;
import com.nhn.android.maps.NMapOverlayItem;
import com.nhn.android.mapviewer.overlay.NMapCalloutOverlay;
import com.nhn.android.mapviewer.overlay.NMapOverlayManager;

public class CustomOnCalloutOverlayListener implements NMapOverlayManager.OnCalloutOverlayListener {
    @Override
    public NMapCalloutOverlay onCreateCalloutOverlay(NMapOverlay itemOverlay, NMapOverlayItem overlayItem, Rect itemBounds) {
        return new NMapCalloutBasicOverlay(itemOverlay, overlayItem, itemBounds);
    }
}
