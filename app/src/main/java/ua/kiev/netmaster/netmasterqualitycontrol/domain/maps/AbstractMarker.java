package ua.kiev.netmaster.netmasterqualitycontrol.domain.maps;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterItem;

/**
 * Created by RAZER on 22-Mar-16.
 */
public class AbstractMarker implements ClusterItem {

    protected LatLng latLng;

    protected MarkerOptions marker;

    @Override
    public LatLng getPosition() {
        return latLng;
    }

    public AbstractMarker(LatLng latLng, MarkerOptions marker) {
        this.marker = marker;
        this.latLng = latLng;
    }

    public MarkerOptions getMarker() {
        return marker;
    }

    public void setMarker(MarkerOptions marker) {
        this.marker = marker;
    }

}
