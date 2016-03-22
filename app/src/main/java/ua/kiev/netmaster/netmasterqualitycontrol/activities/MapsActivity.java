package ua.kiev.netmaster.netmasterqualitycontrol.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import ua.kiev.netmaster.netmasterqualitycontrol.R;
import ua.kiev.netmaster.netmasterqualitycontrol.domain.Employee;
import ua.kiev.netmaster.netmasterqualitycontrol.domain.maps.OwnIconRendered;
import ua.kiev.netmaster.netmasterqualitycontrol.domain.maps.AbstractMarker;
import ua.kiev.netmaster.netmasterqualitycontrol.domain.Task;
import ua.kiev.netmaster.netmasterqualitycontrol.enums.EmplPossition;
import ua.kiev.netmaster.netmasterqualitycontrol.enums.TaskType;
import ua.kiev.netmaster.netmasterqualitycontrol.fragments.dialogs.CreateTaskDialog;
import ua.kiev.netmaster.netmasterqualitycontrol.loger.L;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener, CreateTaskDialog.AddTaskDialogCommunicator,
        GoogleMap.InfoWindowAdapter, GoogleMap.OnInfoWindowClickListener, ClusterManager.OnClusterClickListener<AbstractMarker>,
        ClusterManager.OnClusterInfoWindowClickListener<AbstractMarker>, ClusterManager.OnClusterItemClickListener<AbstractMarker>,ClusterManager.OnClusterItemInfoWindowClickListener<AbstractMarker> {

    private MyApplication myApplication;
    private List<Task> taskList;
    private List<Employee> employeeList;
    private Double minLat = 90.0, minLong= 180.0, maxLat=-90.0, maxLong=-180.0;
    private GoogleMap mMap;
    private UiSettings uiSettings;
    private LatLngBounds bounds;
    private DateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm", Locale.ENGLISH);
    private LatLng latLng;
    private Task curTask;
    private int taskPosition;
    private ClusterManager<AbstractMarker> mClusterManager;
    private AbstractMarker chosenMarker;
    private Cluster<AbstractMarker> chosenCluster;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        L.l("onCreate()", this);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        //SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
          //      .findFragmentById(R.id.map);
        //mMap = mapFragment.getMap();
        setUpMap();
        //mapFragment.getMapAsync(this);
        myApplication = (MyApplication) getApplication();
        myApplication.setCurrentActivity(this);
        taskList = myApplication.updateTaskList(this);
        employeeList = myApplication.updateEmplList(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMap();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        L.l("MapsActivity. onMapReady()");
        mMap = googleMap;
        setUpClusters();
        mMap.setInfoWindowAdapter(mClusterManager.getMarkerManager());
        mMap.setOnMarkerClickListener(mClusterManager);
        uiSettings = mMap.getUiSettings();
        mMap.setOnMapLongClickListener(this);
        mMap.setOnInfoWindowClickListener(this);
        uiSettings.setZoomControlsEnabled(true);
        uiSettings.setMapToolbarEnabled(false);
        makeMarkers();
        //mClusterManager.getMarkerCollection().setOnInfoWindowAdapter(new MarkerInfoWindowAdapter());
        mClusterManager.getMarkerCollection().setOnInfoWindowAdapter(new MarkerInfoWindowAdapter());
        animateCamera();
    }

    private void setUpMap() {
        ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMapAsync(this);
    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        L.l("MapsActivity. onMapLongClick()");
        this.latLng = latLng;
        new CreateTaskDialog().show(getSupportFragmentManager(), null);
    }

    private void setUpClusters() {
        mClusterManager = new ClusterManager<AbstractMarker>(this.getApplicationContext(), mMap);

        mMap.setOnCameraChangeListener(mClusterManager);
        mClusterManager.setRenderer(new OwnIconRendered(getApplicationContext(), mMap, mClusterManager));
        mClusterManager.setOnClusterItemClickListener(this);
        mClusterManager.setOnClusterClickListener(this);

        //mMap.setOnMarkerClickListener(mClusterManager);
        // Add cluster items (markers) to the cluster manager.
    }


    private void animateCamera() {
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


    private LatLng getLatLng(Double curLat, Double curLng){
        if (curLat < minLat) minLat = curLat;
        if (curLng < minLong) minLong = curLng;
        if (curLat > maxLat) maxLat = curLat;
        if (curLng > maxLong) maxLong = curLng;
        return new LatLng(curLat, curLng);
    }

    
    private void makeMarkers(){
       addEmployeesToMap();
       addTasksToMap();
       addOfficesToMap();
       bounds = new LatLngBounds(new LatLng(minLat, minLong), new LatLng(maxLat, maxLong));
    }

    private void addOfficesToMap() {
    }

    private void addTasksToMap() {
        for(Task task: taskList){
            try {
                if(task.getLatitude()==null) continue;
                MarkerOptions marker = new MarkerOptions().position(getLatLng(task.getLatitude(), task.getLongitude())).icon(getIcon(task));
               // marker.snippet("Priority - "+task.getPriority()+";\n Description - "+task.getDescription());
                //mMap.addMarker(marker);
                mClusterManager.addItem(new AbstractMarker(marker.getPosition(), marker));
            }catch (Exception e){
                e.printStackTrace();
                L.l(e.toString());
            }
        }
    }

    private void addEmployeesToMap(){
        for(Employee employee: employeeList){
            try {
                MarkerOptions marker = new MarkerOptions().position(getLatLng(employee.getLastLat(), employee.getLastLong())).title(employee.getLogin() + " "
                        + format.format(employee.getLastOnline())).icon(getIcon(employee));
                //mMap.addMarker(marker);
                mClusterManager.addItem(new AbstractMarker(marker.getPosition(),marker));
            }catch (Exception e){
                e.printStackTrace();
                L.l(e.toString());
            }
        }
    }

    private BitmapDescriptor getIcon(Object o){
        if (o instanceof Employee){
            if(((Employee)o).getPosition()== EmplPossition.TECHNICIAN) return BitmapDescriptorFactory.fromResource(R.mipmap.tech_icon);
            else if(((Employee)o).getPosition()== EmplPossition.ADMIN) return BitmapDescriptorFactory.fromResource(R.mipmap.admin_icon);
            else if(((Employee)o).getPosition()== EmplPossition.SUPERADMIN) return BitmapDescriptorFactory.fromResource(R.mipmap.boss_icon);
        }else  if (o instanceof Task){
            if(((Task)o).getTaskType()== TaskType.REPAIR) return BitmapDescriptorFactory.fromResource(R.mipmap.task_icon);
            else if(((Task)o).getTaskType()== TaskType.USER_CONNECTING) return BitmapDescriptorFactory.fromResource(R.mipmap.userconn_icon);
            else if(((Task)o).getTaskType()== TaskType.CABLE_INSTALL) return BitmapDescriptorFactory.fromResource(R.mipmap.cable_install);
            else if(((Task)o).getTaskType()== TaskType.BOX_INSTALL) return BitmapDescriptorFactory.fromResource(R.mipmap.box_install);
            else if(((Task)o).getTaskType()== TaskType.OTHER) return BitmapDescriptorFactory.fromResource(R.mipmap.other_task);
        }
            return null;
    }

    @Override
    public Long onAddTaskDialogData(TaskType type, String address) {
        Long id = myApplication.onAddTaskDialogData(type,address);
        Task task = myApplication.getCurTask();
        task.setLatitude(latLng.latitude);
        task.setLongitude(latLng.longitude);
        Map<String,String> params = new HashMap<>();
        params.put(getString(R.string.task), myApplication.getGson().toJson(task));
        L.l("task updated "+myApplication.sendRequest(params));
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title(task.getTaskType().toString());
        markerOptions.snippet(myApplication.getFormat().format(task.getPublished()));
        markerOptions.icon(getIcon(task));
        mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.addMarker(markerOptions);
        return id;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        L.l("getInfoWindow()", this);
        defineTask(marker);
        if (curTask==null) return null;
        View window = getLayoutInflater().inflate(R.layout.info_window, null);
        //LatLng latLng = marker.getPosition();
        TextView tv0 = (TextView) window.findViewById(R.id.infowindow_tv0);
        tv0.setText(curTask.getTaskType() + " " + format.format(curTask.getPublished()));
        TextView tv1 = (TextView) window.findViewById(R.id.infowindow_tv1);
        tv1.setText("Priority: "+curTask.getPriority());
        TextView tv2 = (TextView) window.findViewById(R.id.infowindow_tv2);
        tv2.setText("Description: "+ curTask.getDescription());
        TextView tv3 = (TextView) window.findViewById(R.id.infowindow_tv3);
        tv3.setText("Executors: " + myApplication.getExecutorsNames(curTask.getExecuterIds()));
        return window;

    }

    @Override
    public View getInfoContents(Marker marker) {
        L.l("getInfoContents()", this);
        return null;
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("taskPosition",taskPosition);
        startActivity(intent);
        //myApplication.commitFragment(TaskDetailsFragment.newInstance(taskPosition), getSupportFragmentManager());
    }

    private int defineTask(Marker marker){
        int position=0;
        for(Task task : myApplication.updateTaskList(this)){
            if(task.getLongitude()!=null && (task.getLatitude()==marker.getPosition().latitude && task.getLongitude()==marker.getPosition().longitude)){
                curTask = task;
                taskPosition = position;
                return position;
            }
            position++;
        }
        curTask=null;
        return -1;
    }

    private class MarkerInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {
        @Override
        public View getInfoContents(Marker arg0) {
            return null;
        }

        @Override
        public View getInfoWindow(Marker marker) {
            defineTask(marker);
            if (curTask==null) return null;
            View window = getLayoutInflater().inflate(R.layout.info_window, null);
            //LatLng latLng = marker.getPosition();
            TextView tv0 = (TextView) window.findViewById(R.id.infowindow_tv0);
            tv0.setText(curTask.getTaskType() + " " + format.format(curTask.getPublished()));
            TextView tv1 = (TextView) window.findViewById(R.id.infowindow_tv1);
            tv1.setText("Priority: "+curTask.getPriority());
            TextView tv2 = (TextView) window.findViewById(R.id.infowindow_tv2);
            tv2.setText("Description: "+ curTask.getDescription());
            TextView tv3 = (TextView) window.findViewById(R.id.infowindow_tv3);
            tv3.setText("Executors: " + myApplication.getExecutorsNames(curTask.getExecuterIds()));
            return window;
        }
    }

    @Override
    public boolean onClusterClick(Cluster<AbstractMarker> cluster) {
        chosenCluster = cluster;
        return false;
    }

    @Override
    public void onClusterInfoWindowClick(Cluster<AbstractMarker> cluster) {

    }

    @Override
    public boolean onClusterItemClick(AbstractMarker myClusterItem) {
        chosenMarker = myClusterItem;
        return false;
    }

    @Override
    public void onClusterItemInfoWindowClick(AbstractMarker myClusterItem) {

    }
}
