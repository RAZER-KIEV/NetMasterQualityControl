package ua.kiev.netmaster.netmasterqualitycontrol.activities;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import ua.kiev.netmaster.netmasterqualitycontrol.R;
import ua.kiev.netmaster.netmasterqualitycontrol.domain.Employee;
import ua.kiev.netmaster.netmasterqualitycontrol.domain.MyDownTask;
import ua.kiev.netmaster.netmasterqualitycontrol.domain.Task;
import ua.kiev.netmaster.netmasterqualitycontrol.fragments.CreateNetworkDialog;
import ua.kiev.netmaster.netmasterqualitycontrol.fragments.CreateRegisterDialog;
import ua.kiev.netmaster.netmasterqualitycontrol.fragments.CreateTaskDialog;
import ua.kiev.netmaster.netmasterqualitycontrol.fragments.DeleteDialogFragment;
import ua.kiev.netmaster.netmasterqualitycontrol.fragments.DetailsFragment;
import ua.kiev.netmaster.netmasterqualitycontrol.fragments.EmloyeeFragment;
import ua.kiev.netmaster.netmasterqualitycontrol.fragments.EventFragment;
import ua.kiev.netmaster.netmasterqualitycontrol.fragments.LogoutDialog;
import ua.kiev.netmaster.netmasterqualitycontrol.fragments.NetworkFragment;
import ua.kiev.netmaster.netmasterqualitycontrol.fragments.TaskFragment;
import ua.kiev.netmaster.netmasterqualitycontrol.loger.L;
import ua.kiev.netmaster.netmasterqualitycontrol.service.MyService;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, CreateTaskDialog.AddTaskDialogCommunicator, DeleteDialogFragment.DeleteDialogFragComunicator, LogoutDialog.LogoutDialogCommunicator,
        CreateRegisterDialog.RegisterDialogCommunicator, CreateNetworkDialog.CreateNetworkDialogCommunicator {

    //private final String saveBtnTag="saveBtnTag", deleteBtnTag="deleteBtnTag", acceptBtnTag="acceptBtnTag", contactBtnTag="contactBtnTag";
    private Employee me;
    private Task task;
    private List<Employee> emplList;
    private TextView profileNameTv, profileNameTv1, profileNameTv2;
    private ImageView userIconView;
    private Map<String,String> params;
    DrawerLayout drawer;
    NavigationView navigationView;

    //// TODO: 2/16/2016 make nonstatic
    private DetailsFragment detailsFragment;

    private boolean serviceStarted, bound;
    private ServiceConnection sConn;
    private MyService myService;
    private Map<Employee, Location> employeeLocationMap;
    private Intent intent;
    private String result;
    private TypeToken<List<Employee>> tokenEmpl;
    private MyApplication myApplication;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(LoginActivity.LOG, "MainActivity. onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        CookieHandler.getDefault();
        initViews();
        myApplication = (MyApplication) getApplication();
        myApplication.updateEmplList();
        myApplication.updateNetworkList();
        myApplication.updateTaskList();
        tokenEmpl = new TypeToken<List<Employee>>() {};
        this.me = ((MyApplication)getApplication()).getMe(); //LoginActivity.getMe();
        servicePrepare();
        setFbProfile();
        Log.d(LoginActivity.LOG, "MainActivity. onCreate() this.me =" + me);
        onClickStartStop();
    }

    private void setFbProfile() {
        Log.d(LoginActivity.LOG, "MainActivity. setFbProfile()");
        // TODO: 24.12.2015
        //NavigationView l = (NavigationView) findViewById(R.id.nav_view);
        //LinearLayout ll = (LinearLayout) findViewById(R.id.linear_nav_header);  //not work. ll==null;
       // profileNameTv1 = (TextView) drawer.findViewById(R.id.profileNameTv);   //null
       // profileNameTv1 = (TextView) navigationView.findViewById(R.id.profileNameTv); //null
        //profileNameTv1 = (TextView) findViewById(R.id.profileNameTv); //null
        //profileNameTv1 = (TextView) findViewById(R.id.profileNameTv);
        View  headerview = navigationView.inflateHeaderView(R.layout.nav_header_main);
        //profileNameTv = (TextView)headerview.findViewById(R.id.profileNameTv);
        LinearLayout linearLayout = (LinearLayout)headerview.findViewById(R.id.linear_nav_header);
        profileNameTv = new TextView(this);
        profileNameTv.setGravity(Gravity.BOTTOM);
        profileNameTv.setTextColor(Color.WHITE);
       // profileNameTv.setText(LoginActivity.getProfile().getFirstName()+" "+LoginActivity.getProfile().getLastName());
        if(myApplication.getFbProfile()!=null){
            Log.d(LoginActivity.LOG, "MainActivity. setFbProfile()  LoginActivity.getProfile()!=null");
            profileNameTv.setText(myApplication.getFbProfile().getFirstName()+" "+myApplication.getFbProfile().getLastName());// TODO: 23.12.2015
        }else if (myApplication.getMe()!=null){
            Log.d(LoginActivity.LOG, "MainActivity. setFbProfile()  MainActivity.getMe()!=null");
            profileNameTv.setText(me.getLogin() + " " + me.getPosition());
        }
        linearLayout.addView(profileNameTv);
    }

    @Override
    protected void onStart() {
        super.onStart();
        L.l("MainActivity. onStart()");
        //intent = new Intent(this, MyService.class);
        bindService(new Intent(this, MyService.class), sConn, 0);
    }

    @Override
    protected void onStop() {
        super.onStop();
        L.l("MainActivity. onStop()");
        if (!bound) return;
        unbindService(sConn);
        bound = false;
    }

    private void servicePrepare(){
        L.l("MainActivity. servicePrepare()");
        //Service prepare.
        sConn = new ServiceConnection() {
            public void onServiceConnected(ComponentName name, IBinder binder) {
                L.l("MainActivity onServiceConnected");
                myService = ((MyService.MyBinder) binder).getService();
                bound = true;
            }
            public void onServiceDisconnected(ComponentName name) {
                L.l("MainActivity onServiceDisconnected");
                bound = false;
            }
        };
    }

    public void onClickStartStop() {
        L.l("MainActivity.  onClickStart()");
        if(!serviceStarted){
            startService(intent=new Intent(this, MyService.class));
            serviceStarted=true;
        }else {
            stopService(intent);
            serviceStarted=false;
        }
        L.t("Service works = " + serviceStarted, this);
    }

    private void initViews(){
        Log.d(LoginActivity.LOG, "MainActivity. initViews()");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        L.l("MainActivity. onResume()");
    }

    @Override
    public void onBackPressed() {
        L.l("MainActivity. onBackPressed()");
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            //getSupportFragmentManager().
            if(getSupportFragmentManager().getBackStackEntryCount()==0){
                new LogoutDialog().show(getSupportFragmentManager(), "tag");
            } else super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main_drawer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
       /* int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);*/
        onNavigationItemSelected(item);
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        L.l("MainActivity. onNavigationItemSelected()");
        int id = item.getItemId();
        if (id == R.id.nav_tasks) {
            myApplication.commitFragment(new TaskFragment(), getSupportFragmentManager());
        } else if (id == R.id.nav_employees) {
            myApplication.commitFragment(new EmloyeeFragment(), getSupportFragmentManager());
        } else if (id == R.id.nav_map) {
            Intent intent = new Intent(this, MapsActivity.class);
            startActivity(intent);
        } else if (id == R.id.manage_my_prof) {
            myApplication.commitFragment(detailsFragment = DetailsFragment.newInstance(myApplication.getMe()), getSupportFragmentManager());
        } else if (id == R.id.nav_service) {
            onClickStartStop();
        } else if (id == R.id.nav_logOut){
            goToLoginActivity();
        }else  if(id == R.id.nav_networks){
            myApplication.commitFragment(new NetworkFragment(), getSupportFragmentManager());
        }else  if(id ==R.id.nav_events){
            myApplication.commitFragment(new EventFragment(),getSupportFragmentManager());
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onAddTaskDialogData(String title, String description) {
        L.l("MainActivity. onAddTaskDialogData()");
        Map<String,String> params = new HashMap<>();
        params.put(getString(R.string.title), title);
        params.put(getString(R.string.description), description);
        //params.put(getString(R.string.urlStr), "");
        String res = "";
        try {
            res = new MyDownTask(params,this).execute().get();
        } catch (InterruptedException|ExecutionException e) {
            e.printStackTrace();
        }
        Toast.makeText(this,"Task created = " +res, Toast.LENGTH_LONG).show();
        myApplication.commitFragment(new TaskFragment(), getSupportFragmentManager());
    }

    public Task getTask() {
        return task;
    }

    @Override
    public void delete(View v) {
            if(v.getId()==R.id.create_dialog){
                ((DetailsFragment)getSupportFragmentManager().findFragmentByTag(getString(R.string.detailsFragment))).emplOnClickDeleteParams();
            //detailsFragment.emplOnClickDeleteParams();// TODO: 04.01.2016
            }
    }

    @Override
    protected void onPause() {
        super.onPause();
        L.l("onPause", this);
        // TODO: 04.01.2016 Exit dialog. ?
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        L.l("onDestroy", this);
    }

    @Override
    public void goToLoginActivity() {
        L.l("goToLoginActivity", this);
        getSupportFragmentManager().popBackStackImmediate();
        myApplication.setMe(null);
        myApplication.setFbProfile(null);
        super.onBackPressed();
        //super.onBackPressed();
    }

    @Override
    public void registerDialogData(String login, String password) {
        params = new HashMap<>();
        params.put(getString(R.string.urlTail), getString(R.string.addEmpl));
        params.put(getString(R.string.login),login);
        params.put(getString(R.string.password), password);
        try {
            String res = new MyDownTask(params,this).execute().get();
            Toast.makeText(this,"User created: " +res, Toast.LENGTH_LONG).show();
            myApplication.commitFragment(new EmloyeeFragment(), getSupportFragmentManager());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addEmplToMyNetwork(Long emplId) {
        params = new HashMap<>();
        params.put(getString(R.string.urlTail),getString(R.string.network_addEmployeeToMyNetwork));
        params.put(getString(R.string.emlpId), String.valueOf(emplId));
        try {
            String res = new MyDownTask(params,this).execute().get();
            Toast.makeText(this, res, Toast.LENGTH_LONG).show();
            myApplication.commitFragment(new EmloyeeFragment(), getSupportFragmentManager());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onCreateNetworkDialogData(String title) {
        params = new HashMap<>();
        params.put(getString(R.string.networkname),title);
        params.put(getString(R.string.owners), String.valueOf(myApplication.getMe().getId()));
        try {
            String res = new MyDownTask(params, this).execute().get();
            L.t("Network created: "+res,this);
            myApplication.commitFragment(new NetworkFragment(), getSupportFragmentManager());
        }catch (Exception e){
            e.printStackTrace();
            L.t("Creation faild! "+e,this);
        }
    }

    public List<Employee> getEmplList() {
        return emplList;
    }
}
