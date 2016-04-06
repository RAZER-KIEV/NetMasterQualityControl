package ua.kiev.netmaster.netmasterqualitycontrol.activities;

import android.content.Intent;
import android.graphics.Color;
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
import com.google.android.gms.maps.model.PolylineOptions;
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
import ua.kiev.netmaster.netmasterqualitycontrol.domain.Office;
import ua.kiev.netmaster.netmasterqualitycontrol.domain.maps.OwnIconRendered;
import ua.kiev.netmaster.netmasterqualitycontrol.domain.maps.AbstractMarker;
import ua.kiev.netmaster.netmasterqualitycontrol.domain.Task;
import ua.kiev.netmaster.netmasterqualitycontrol.enums.EmplPossition;
import ua.kiev.netmaster.netmasterqualitycontrol.enums.TaskStatus;
import ua.kiev.netmaster.netmasterqualitycontrol.enums.TaskType;
import ua.kiev.netmaster.netmasterqualitycontrol.fragments.dialogs.CreateOfficeDialog;
import ua.kiev.netmaster.netmasterqualitycontrol.fragments.dialogs.CreateTaskDialog;
import ua.kiev.netmaster.netmasterqualitycontrol.loger.L;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener, CreateTaskDialog.AddTaskDialogCommunicator, CreateOfficeDialog.CreateOfficeDialogCommunicator,
        GoogleMap.OnInfoWindowClickListener, ClusterManager.OnClusterClickListener<AbstractMarker>,
        ClusterManager.OnClusterInfoWindowClickListener<AbstractMarker>, ClusterManager.OnClusterItemClickListener<AbstractMarker>,ClusterManager.OnClusterItemInfoWindowClickListener<AbstractMarker> {

    private boolean addOfficeMode;
    private Map<String, String > params;
    private MyApplication myApplication;
    private List<Task> taskList;
    private List<Employee> employeeList;
    private List<Office> officeList;
    private Double minLat = 90.0, minLong= 180.0, maxLat=-90.0, maxLong=-180.0;
    private GoogleMap mMap;
    private UiSettings uiSettings;
    private LatLngBounds bounds;
    private DateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm", Locale.ENGLISH);
    private LatLng latLng;
    private Task curTask;
    private Employee curEmployee;
    private Office curOffice;
    private int position;
    private ClusterManager<AbstractMarker> mClusterManager;
    private AbstractMarker chosenMarker;                // TODO: 22-Mar-16 make infowindow for Clusters.
    private Cluster<AbstractMarker> chosenCluster;       // TODO: 22-Mar-16 make infowindow for Clusters.
    private Map<LatLng, LatLng> empl_task;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        L.l("onCreate()", this);
        setContentView(R.layout.activity_maps);
        addOfficeMode = getIntent().getBooleanExtra("isAddOfficeMode", false);
        myApplication = (MyApplication) getApplication();

        setUpMap();
        myApplication.setCurrentActivity(this);
        params = new HashMap<>();
        taskList = myApplication.updateTaskList(this);
        employeeList = myApplication.updateEmplList(this);
        officeList = myApplication.updateOfficeList(this);
        empl_task = new HashMap<>();
    }

    @Override
    protected void onResume() {
        super.onResume();
        L.l("onResume()", this);
        L.l("myApplication.isNeedToFinish() = "+myApplication.isNeedToFinish(), this);
        if(myApplication.isNeedToFinish()) finish();
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
        if(!empl_task.isEmpty()) makePolylines();
        mClusterManager.getMarkerCollection().setOnInfoWindowAdapter(new MarkerInfoWindowAdapter());
        animateCamera();
        if(addOfficeMode){
            L.t("Find location of your office -> Long click!", this);
        }
    }

    private void makePolylines() {
        L.l("makePolylines()", this);
        for(LatLng latLng : empl_task.keySet()){
            PolylineOptions polylineOptions = new PolylineOptions().add(latLng).add(empl_task.get(latLng)).color(Color.BLUE);
            mMap.addPolyline(polylineOptions);
        }
    }

    private void setUpMap() {
        ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMapAsync(this);
    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        L.l("MapsActivity. onMapLongClick()");
        this.latLng = latLng;
        if(addOfficeMode) new CreateOfficeDialog().show(getSupportFragmentManager(),null);
        else {
            new CreateTaskDialog().show(getSupportFragmentManager(), null);
        }
    }

    private void setUpClusters() {
        mClusterManager = new ClusterManager<AbstractMarker>(this.getApplicationContext(), mMap);
        mMap.setOnCameraChangeListener(mClusterManager);
        mClusterManager.setRenderer(new OwnIconRendered(getApplicationContext(), mMap, mClusterManager));
        mClusterManager.setOnClusterItemClickListener(this);
        mClusterManager.setOnClusterClickListener(this);
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
        for(Office office: officeList){
            try {
                if(office.getLatitude()==null) continue;
                MarkerOptions marker = new MarkerOptions().position(getLatLng(office.getLatitude(), office.getLongitude())).icon(getIcon(office));
                mClusterManager.addItem(new AbstractMarker(marker.getPosition(), marker));
            }catch (Exception e){
                e.printStackTrace();
                L.l(e.toString());
            }
        }
    }

    private void addTasksToMap() {
        for(Task task: taskList){
            try {
                if(task.getLatitude()==null||task.getStatus()== TaskStatus.DONE) continue;
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
            if(employee.getIsBusy()){
                for(Task t:taskList){
                    if(t.getTaskId()==employee.getCurrentTaskId()) empl_task.put(new LatLng(t.getLatitude(), t.getLongitude()),new LatLng(employee.getLastLat(),employee.getLastLong()));
                }
            }
            try {
                MarkerOptions marker = new MarkerOptions().position(getLatLng(employee.getLastLat(), employee.getLastLong())).icon(getIcon(employee));
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
        }else if(o instanceof  Office){
            return BitmapDescriptorFactory.fromResource(R.mipmap.office_icon);
        }
            return null;
    }

    @Override
    public Long onAddTaskDialogData(TaskType type, String address) {
        Long id = myApplication.onAddTaskDialogData(type,address);
        Task task = myApplication.getCurTask();
        task.setLatitude(latLng.latitude);
        task.setLongitude(latLng.longitude);
        params.clear();
        params.put(getString(R.string.task), myApplication.getGson().toJson(task));
        L.l("task updated " + myApplication.sendRequest(params));
        createNewMarker(task, latLng);
        return id;
    }

    @Override
    public void onCreateOfficeDialogData(String officeName) {
        L.l("onCreateOfficeDialogData()", this);
        params.clear();
        params.put("longitude", String.valueOf(latLng.longitude));
        params.put("latitude", String.valueOf(latLng.latitude));
        params.put("officeName", officeName);
        String id = myApplication.sendRequest(params);
        L.l("myApplication.sendRequest(params)" + id, this);
        createNewMarker(myApplication.getTaskFromServer(Long.valueOf(id)), latLng);
    }

    private void createNewMarker(Object o, LatLng latLng){
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.icon(getIcon(o));
        if(o instanceof Office){
            markerOptions.title(((Office) o).getName());
            markerOptions.snippet(((Office) o).getDescription());
        }else if(o instanceof Task){
            markerOptions.title(((Task) o).getTaskType().toString()+" "+format.format(((Task) o).getPublished()));
            markerOptions.snippet(((Task) o).getDescription());
        }
        mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.addMarker(markerOptions);
    }

    private int defineEmpl(Marker marker) {
        int position=0;
        for(Employee employee : myApplication.updateEmplList(this)){
            if(employee.getLastLat()!=null && (employee.getLastLat()==marker.getPosition().latitude && employee.getLastLong()==marker.getPosition().longitude)){
                curEmployee = employee;
                this.position = position;
                return position;
            }
            position++;
        }
        curEmployee = null;
        return -1;
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        Intent intent = new Intent(this, MainActivity.class);
        if(curTask!=null) intent.putExtra("task", position);
        if(curEmployee!=null) intent.putExtra("employee", position);
        if(curOffice!=null){
            L.t("Not implemented yet", this);
            return;
        }                                                                          //intent.putExtra("office", position);
        startActivity(intent);
    }

    private int defineTask(Marker marker){
        int position=0;
        for(Task task : myApplication.updateTaskList(this)){
            if(task.getLongitude()!=null && (task.getLatitude()==marker.getPosition().latitude && task.getLongitude()==marker.getPosition().longitude)){
                curTask = task;
                this.position = position;
                return position;
            }
            position++;
        }
        curTask = null;
        return -1;
    }

    private int defineOffice(Marker marker){
        int position=0;
        for(Office office : myApplication.updateOfficeList(this)){
            if(office.getLongitude()!=null && (office.getLatitude()==marker.getPosition().latitude && office.getLongitude()==marker.getPosition().longitude)){
                curOffice = office;
                this.position = position;
                return position;
            }
            position++;
        }
        curOffice = null;
        return -1;
    }



    private class MarkerInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {
        @Override
        public View getInfoContents(Marker arg0) {
            return null;
        }

        @Override
        public View getInfoWindow(Marker marker) {
            L.l("getInfoWindow()", this);
            View window = getLayoutInflater().inflate(R.layout.my_info_window, null);
            TextView tv0 = (TextView) window.findViewById(R.id.infowindow_tv0);
            curTask=null;
            curEmployee=null;
            curOffice=null;
            defineTask(marker);
            if (curTask!=null){
                tv0.setText(curTask.getTaskType() + " " + format.format(curTask.getPublished())+"\nPriority: "+curTask.getPriority()+"\nAddress: "+ curTask.getAddress()+"\nDescription: "+
                        curTask.getDescription()+"\nExecutors: " + myApplication.getExecutorsNames(curTask.getExecuterIds())+"\nStatus: "+curTask.getStatus());
                return window;
            } else {
                defineEmpl(marker);
                if(curEmployee!=null){
                    tv0.setText(curEmployee.getLogin() + " "
                            + format.format(curEmployee.getLastOnline()));
                    return window;
                }else {
                    defineOffice(marker);
                    if(curOffice!=null){
                        tv0.setText(curOffice.getName() + " "
                                + curOffice.getDescription());
                        return window;
                    }
                }
            }
            return null;
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
