package ua.kiev.netmaster.netmasterqualitycontrol.domain.maps;

import android.content.Context;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;

import ua.kiev.netmaster.netmasterqualitycontrol.domain.maps.AbstractMarker;

/**
 * Created by RAZER on 22-Mar-16.
 */
public class OwnIconRendered extends DefaultClusterRenderer<AbstractMarker> {

    public OwnIconRendered(Context context, GoogleMap map, ClusterManager<AbstractMarker> clusterManager) {
        super(context, map, clusterManager);
    }

    @Override
    protected void onBeforeClusterItemRendered(AbstractMarker item, MarkerOptions markerOptions) {
        markerOptions.icon(item.getMarker().getIcon());
    }
}
