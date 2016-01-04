package ua.kiev.netmaster.netmasterqualitycontrol.activities;

import android.content.Intent;
import android.os.Bundle;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.net.CookieHandler;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import ua.kiev.netmaster.netmasterqualitycontrol.R;
import ua.kiev.netmaster.netmasterqualitycontrol.domain.Employee;
import ua.kiev.netmaster.netmasterqualitycontrol.domain.MyDownTask;
import ua.kiev.netmaster.netmasterqualitycontrol.domain.Task;
import ua.kiev.netmaster.netmasterqualitycontrol.fragments.CreateRegisterDialog;
import ua.kiev.netmaster.netmasterqualitycontrol.fragments.CreateTaskDialog;
import ua.kiev.netmaster.netmasterqualitycontrol.fragments.DeleteDialogFragment;
import ua.kiev.netmaster.netmasterqualitycontrol.fragments.DetailsFragment;
import ua.kiev.netmaster.netmasterqualitycontrol.fragments.EmloyeeFragment;
import ua.kiev.netmaster.netmasterqualitycontrol.fragments.LogoutDialog;
import ua.kiev.netmaster.netmasterqualitycontrol.fragments.TaskFragment;
import ua.kiev.netmaster.netmasterqualitycontrol.loger.L;
import ua.kiev.netmaster.netmasterqualitycontrol.service.MyService;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, CreateTaskDialog.AddTaskDialogCommunicator, DeleteDialogFragment.DeleteDialogFragComunicator, LogoutDialog.LogoutDialogCommunicator,
        CreateRegisterDialog.RegisterDialogCommunicator {

    //private final String saveBtnTag="saveBtnTag", deleteBtnTag="deleteBtnTag", acceptBtnTag="acceptBtnTag", contactBtnTag="contactBtnTag";
    private static Employee employee;
    private static Task task;
    private TextView profileNameTv, profileNameTv1, profileNameTv2;
    private ImageView userIconView;
    DrawerLayout drawer;
    NavigationView navigationView;
    private static DetailsFragment detailsFragment;
    private boolean serviceStarted;
    //private com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton cirkMenuFab;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(LoginActivity.LOG, "MainActivity. onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        //initFragments();
        CookieHandler.getDefault();
        this.employee = LoginActivity.getEmployee();
        Log.d(LoginActivity.LOG, "MainActivity. onCreate() this.employee ="+employee);
    }

    private void setFbProfile() {
        Log.d(LoginActivity.LOG, "MainActivity. setFbProfile()");
        // TODO: 24.12.2015
        //NavigationView l = (NavigationView) findViewById(R.id.nav_view);
        //LinearLayout ll = (LinearLayout) findViewById(R.id.linear_nav_header);  //not work. ll==null;
        //profileNameTv1 = (TextView) drawer.findViewById(R.id.profileNameTv);   //null
        //profileNameTv1 = (TextView) navigationView.findViewById(R.id.profileNameTv); //null
        //profileNameTv1 = (TextView) findViewById(R.id.profileNameTv); //null
        /*
       Thanks, bro!
One question: how to setText on the TextView which lays in the header of drawler. (Set name or email)?
The problem is how to get access to this View.
It is not accessable by using findViewById simply. I tried in different combinations and always get NullPointerEx... And i also find nothing on stackoverflov.com.
Can you give me few lines of code here, how it must looks like.
        * */
        profileNameTv1 = (TextView) findViewById(R.id.profileNameTv);
            if(profileNameTv1==null) {
            Log.d(LoginActivity.LOG, "MainActivity. setFbProfile()  profileNameTv11==null; profileNameTv1 = "+profileNameTv1);
            return;
        }
        if(LoginActivity.getProfile()!=null){
            Log.d(LoginActivity.LOG, "MainActivity. setFbProfile()  LoginActivity.getProfile()!=null");
            profileNameTv1.setText("Wtf?");//LoginActivity.getProfile().getFirstName()+" "+LoginActivity.getProfile().getLastName());// TODO: 23.12.2015  
        }else if (MainActivity.getEmployee()!=null){
            Log.d(LoginActivity.LOG, "MainActivity. setFbProfile()  MainActivity.getEmployee()!=null");
            profileNameTv1.setText(employee.getLogin()+" "+employee.getPosition());
        }
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
        Log.d(LoginActivity.LOG, "MainActivity. onResume()");
        //setFbProfile();
    }

    @Override
    public void onBackPressed() {
        Log.d(LoginActivity.LOG, "MainActivity. onBackPressed()");
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
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        Log.d(LoginActivity.LOG, "MainActivity. onNavigationItemSelected()");
        int id = item.getItemId();

        if (id == R.id.nav_tasks) {
            commitFragment(new TaskFragment(), getSupportFragmentManager());
        } else if (id == R.id.nav_employees) {

            commitFragment(new EmloyeeFragment(), getSupportFragmentManager());

        } else if (id == R.id.nav_map) {
            Intent intent = new Intent(this, MapsActivity.class);
            startActivity(intent);


        } else if (id == R.id.manage_my_prof) {
            commitFragment(detailsFragment = DetailsFragment.newInstance(LoginActivity.getEmployee()),getSupportFragmentManager());

        } else if (id == R.id.nav_send) {
            if(!serviceStarted){
                startService(new Intent(this, MyService.class));
            }
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public static void commitFragment(Fragment fragment, FragmentManager fragmentManager) {
        //if(fragment instanceof DetailsFragment)  .setVisibility(View.VISIBLE);
        Log.d(LoginActivity.LOG, "MainActivity. commitFragment() "+fragment.getClass());
        fragmentManager.popBackStack();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction.replace(R.id.contentLayout, fragment);
        fragmentTransaction.addToBackStack("BackTag");
        fragmentTransaction.commit();
    }





    @Override
    public void onAddTaskDialogData(String title, String description) {
        Log.d(LoginActivity.LOG, "MainActivity. onAddTaskDialogData()");
        Map<String,String> params = new HashMap<>();
        params.put(getString(R.string.title),title);
        params.put(getString(R.string.description), description);
        //params.put(getString(R.string.urlStr), "");
        String res = "";
        try {
            res = new MyDownTask(params,this).execute().get();
        } catch (InterruptedException|ExecutionException e) {
            e.printStackTrace();
        }
        Toast.makeText(this,"Task created = " +res, Toast.LENGTH_LONG).show();
        commitFragment(new TaskFragment(),getSupportFragmentManager());
    }

    public static Employee getEmployee() {
        return employee;
    }

    public static void setEmployee(Employee employee) {
        MainActivity.employee = employee;
    }

    public static Task getTask() {
        return task;
    }

    public static void setTask(Task task) {
        MainActivity.task = task;
    }

    public static DetailsFragment getDetailsFragment() {
        return detailsFragment;
    }

    public static void setDetailsFragment(DetailsFragment detailsFragment) {
        MainActivity.detailsFragment = detailsFragment;
    }

    @Override
    public void delete(View v) {
            if(v.getId()==R.id.create_dialog)
           detailsFragment.emplOnClickDeleteParams();// TODO: 04.01.2016
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
        L.l("goToLoginActivity()", this);
        startActivity(new Intent(this, LoginActivity.class));
    }

    @Override
    public void registerDialogData(String login, String password) {
        Map<String,String> params = new HashMap<>();
        params.put(getString(R.string.urlTail), getString(R.string.addEmpl));
        params.put(getString(R.string.login),login);
        params.put(getString(R.string.password), password);
        try {
            String res = new MyDownTask(params,this).execute().get();
            Toast.makeText(this,"User created: " +res, Toast.LENGTH_LONG).show();
            commitFragment(new EmloyeeFragment(),getSupportFragmentManager());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
