package ua.kiev.netmaster.netmasterqualitycontrol.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import ua.kiev.netmaster.netmasterqualitycontrol.R;
import ua.kiev.netmaster.netmasterqualitycontrol.domain.Employee;
import ua.kiev.netmaster.netmasterqualitycontrol.domain.Task;
import ua.kiev.netmaster.netmasterqualitycontrol.fragments.EmloyeeFragment;
import ua.kiev.netmaster.netmasterqualitycontrol.fragments.TaskFragment;
import ua.kiev.netmaster.netmasterqualitycontrol.loger.L;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    
    private List<Task> taskList;
    private List<Employee> employeeList;
    
    private GoogleMap mMap;
    private LatLngBounds bounds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        L.l("onCreate()", this);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        
        taskList= TaskFragment.getTaskList();
        updeteEmplList();
    }

    private void updeteEmplList(){
        L.l("updeteEmplList()",this);
       employeeList = MainActivity.getEmloyeeFragment().updateEmplList();
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d(LoginActivity.LOG, "MapsActivity. onMapReady()");
        mMap = googleMap;
        Double minLat = 90.0, minLong= 180.0, maxLat=-90.0, maxLong=-180.0;
        for(Employee employee: employeeList){
            L.l("Arrays.toString(employee.getLastLatLong())"+employee.getLastLat()+ " "+ employee.getLastLong(), this);
            try {
                Double curLat = employee.getLastLat();
                Double curLng = employee.getLastLong();
                if (curLat < minLat) minLat = curLat;
                if (curLng < minLong) minLong = curLng;
                if (curLat > maxLat) maxLat = curLat;
                if (curLng > maxLong) maxLong = curLng;
                LatLng curLatLong = new LatLng(employee.getLastLat(),employee.getLastLong());
                //String title = ""+employee.getLogin()+"\n"+employee.getLastOnline().toString();
                //L.l("title= " +title,this);
                DateFormat format = new SimpleDateFormat("yyyy-mm-dd hh:mm", Locale.ENGLISH);
                format.format(employee.getLastOnline());
                mMap.addMarker(new MarkerOptions().position(curLatLong).title(employee.getLogin()+"    "+format.format(employee.getLastOnline())));

            }catch (Exception e){
                e.printStackTrace();
                L.l(e.toString());
            }
        }
        LatLng min = new LatLng(minLat, minLong);
        LatLng max = new LatLng(maxLat, maxLong);
        bounds =new LatLngBounds(min, max);


        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, 150);
        L.l("minLat= " + minLat + ", minLong= " + minLong + ", maxLat= " + maxLat + ", maxLong= " + maxLong);
        try{
            mMap.animateCamera(cameraUpdate);
        }catch (Exception e){
            L.l("onMapReady(). catch!",this);
            e.printStackTrace();
            mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
                @Override
                public void onMapLoaded() {
                    mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 150));
                }
            });
        }
    }
    
    private void makeMarkers(){
        L.l("makeMarkers()", this);
        for (Task task:taskList){
            if(task.getLatitude()!=null&task.getLongitude()!=null){
                LatLng ll= new LatLng(Double.parseDouble(task.getLatitude()),Double.parseDouble(task.getLongitude()));
                mMap.addMarker( new MarkerOptions().position(ll).title(task.getTitle()));
            }
        }
        for(Employee employee: employeeList){
           // if(employee.getLastLatLong()!=null)
                // TODO: 05.01.2016  LongLat -> LatLong! 

        }
    }
}
