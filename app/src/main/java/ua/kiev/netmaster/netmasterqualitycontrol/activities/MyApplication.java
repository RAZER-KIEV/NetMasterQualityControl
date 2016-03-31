package ua.kiev.netmaster.netmasterqualitycontrol.activities;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.Profile;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import ua.kiev.netmaster.netmasterqualitycontrol.R;
import ua.kiev.netmaster.netmasterqualitycontrol.domain.Employee;
import ua.kiev.netmaster.netmasterqualitycontrol.domain.MyEvent;
import ua.kiev.netmaster.netmasterqualitycontrol.domain.MyDownTask;
import ua.kiev.netmaster.netmasterqualitycontrol.domain.Network;
import ua.kiev.netmaster.netmasterqualitycontrol.domain.Office;
import ua.kiev.netmaster.netmasterqualitycontrol.domain.Task;
import ua.kiev.netmaster.netmasterqualitycontrol.enums.TaskType;
import ua.kiev.netmaster.netmasterqualitycontrol.fragments.details.EmplDetailsFragment;
import ua.kiev.netmaster.netmasterqualitycontrol.fragments.details.NetworkDetailsFragment;
import ua.kiev.netmaster.netmasterqualitycontrol.fragments.details.TaskDetailsFragment;
import ua.kiev.netmaster.netmasterqualitycontrol.loger.L;

/**
 * Created by ПК on 21.12.2015.
 */
public class MyApplication extends Application{

    private Activity currentActivity;
    //Lists
    private List<Employee> emplList;
    private List<Network> networkList;
    private List<Task> taskList;
    private List<MyEvent> myEventList;
    private List<Office> myOfficeList;
    //Tokens
    private TypeToken<List<MyEvent>> myEventListTypeToken;
    private TypeToken<Employee> employeeTypeToken;
    private TypeToken<Task> taskTypeToken;
    private TypeToken<List<Office>> officeListTypeToken;
    private TypeToken<List<Network>> networkListToken;
    //My Objects
    private Employee me;
    private Employee employee;
    private Task curTask;
    private Network curNetwork;
    private Profile fbProfile;

    private Gson gson;
    private String login, password;
    private String response;
    private FloatingActionButton fab;
    private FloatingActionMenu faMenu;
    private SubActionButton saveBtn, deleteBtn, acceptBtn, contactBtn;
    private FloatingActionMenu.Builder builder;
    private SimpleDateFormat format;
    private ImageView saveImg, deleteImg, acceptImg, contactImg;
    private SharedPreferences sharedpreferences;
    private SharedPreferences.Editor editor;
    private Map<String,String> params;
    private boolean serviceStarted;
    private boolean needToFinish;

    @Override
    public void onCreate() {
        super.onCreate();
        L.l("onCreate()", this);
        printHashKey();
        gson = new Gson();
        format = (SimpleDateFormat) SimpleDateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT);
        sharedpreferences = getSharedPreferences("myShPrefs", Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();
        params = new HashMap<>();
        initTokens();
    }

    private void initTokens(){
        myEventListTypeToken = new TypeToken<List<MyEvent>>(){};
        employeeTypeToken = new TypeToken<Employee>(){};
        taskTypeToken = new TypeToken<Task>(){};
        officeListTypeToken = new TypeToken<List<Office>>() {};
        networkListToken = new TypeToken<List<Network>>(){};
    }

    public void printHashKey(){
        // Add code to print out the key hash
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "ua.kiev.netmaster.netmasterqualitycontrol",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                L.l(Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException|NoSuchAlgorithmException e){e.printStackTrace();}
    }

    public List<Employee> updateEmplList(Activity activity){
        getParams().put(getString(R.string.urlTail), getString(R.string.employee_getAll));
        try {
            String result = new MyDownTask(params,activity).execute().get();
            TypeToken<List<Employee>> tokenEmpl = new TypeToken<List<Employee>>() {};
            emplList = gson.fromJson(result, tokenEmpl.getType());
        } catch (Exception e){
            e.printStackTrace();
            emplList = new ArrayList<>();
        }
        if(emplList!=null && emplList.size()>0){
            for(Employee e: emplList){
                if(e.getId()==me.getId())me=e;
            }
        }
        return emplList;
    }

    public List<Task> updateTaskList(Activity activity){
        getParams().put(getString(R.string.urlTail), getString(R.string.task_getAll));
        currentActivity = activity;
            try {
                String result = new MyDownTask(params,activity).execute().get();
                TypeToken<List<Task>> tokenEmpl = new TypeToken<List<Task>>() {};
                taskList = gson.fromJson(result, tokenEmpl.getType());
            } catch (Exception e){
                e.printStackTrace();
                taskList = new ArrayList<>();
            }
        return taskList;
    }

    public List<Network> updateNetworkList(Activity activity){
        getParams().put(getString(R.string.urlTail), getString(R.string.network_getAll));
        try {
            String result = new MyDownTask(params,activity).execute().get();
            networkList = gson.fromJson(result, networkListToken.getType());
            setMyNetwork();
        } catch (Exception e){
            e.printStackTrace();
            networkList = new ArrayList<>();
        }
        return networkList;
    }

    private void setMyNetwork(){
        for(Network network :networkList){
            if(getMe().getNetworkId().equals(network.getNetworkId()))curNetwork = network;
        }
    }

    public List<MyEvent> updateEventList(Activity activity) {
        getParams().put(getString(R.string.urlTail), getString(R.string.event_getAllbyNetwork));
        try {
            String result = new MyDownTask(params,activity).execute().get();
            myEventList = gson.fromJson(result, myEventListTypeToken.getType());
        }catch (Exception e){
            e.printStackTrace();
            myEventList = new ArrayList<>();
        }
        return myEventList;
    }

    public List<Office> updateOfficeList(Activity activity) {
        getParams().put(getString(R.string.urlTail), getString(R.string.office_getAll));
        try {
            String result = new MyDownTask(params,activity).execute().get();
            myOfficeList = gson.fromJson(result, officeListTypeToken.getType());
        }catch (Exception e){
            e.printStackTrace();
            myOfficeList = new ArrayList<>();
        }
        return myOfficeList;
    }

    public void commitFragment(Fragment fragment, FragmentManager fragmentManager) {

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        String str[]=fragment.getClass().toString().split("\\.");
        String fragmentName = str[str.length-1].trim();
        L.l(" MYAPPLICATION. commitFragment. - "+fragmentName);
        //L.l("fragment.getClass().getCanonicalName() = " + fragment.getClass().getCanonicalName(),this);
        fragmentTransaction.replace(R.id.contentLayout, fragment, fragmentName);
        fragmentTransaction.addToBackStack("BackTag");
        fragmentTransaction.commit();
        fragmentManager.executePendingTransactions();
    }

    public Employee getEmplFromServer(Long id) {
            getParams().put(getString(R.string.urlTail), getString(R.string.employee_getEmpl));
            params.put(getString(R.string.id), String.valueOf(id));
            String result = sendRequest(params); //new MyDownTask(params, getApplicationContext()).execute().get();
            TypeToken<Employee> tokenEmpl = new TypeToken<Employee>() {};
            me = gson.fromJson(result, tokenEmpl.getType());
            return me;
    }

    public String sendRequest(Map<String, String> params){
        try {
            response = new MyDownTask(params,currentActivity).execute().get();
        } catch (InterruptedException|ExecutionException e) {
            e.printStackTrace();
        }
        return response;
    }

    public void overrideFab(Activity activity, Fragment fragment){
        L.l("overrideFab()", this);
        buildMyFabMenu(activity);
        setListeners((View.OnClickListener) fragment);
        if(fragment instanceof TaskDetailsFragment){
            builder.addSubActionView(deleteBtn);
            builder.addSubActionView(saveBtn);
            builder.addSubActionView(acceptBtn);
        }else if(fragment instanceof EmplDetailsFragment){
            builder.addSubActionView(deleteBtn);
            builder.addSubActionView(saveBtn);
            builder.addSubActionView(contactBtn);
        } else if( fragment instanceof NetworkDetailsFragment){
            builder.addSubActionView(deleteBtn);
            builder.addSubActionView(saveBtn);
        }
        builder.attachTo(fab);
        faMenu = builder.build();
    }

    private void setListeners(View.OnClickListener fragment){
        deleteBtn.setOnClickListener(fragment);
        saveBtn.setOnClickListener(fragment);
        acceptBtn.setOnClickListener(fragment);
        contactBtn.setOnClickListener(fragment);
    }

    private void buildMyFabMenu(Activity activity){
        L.l("buildMyFabMenu()", this);
        fab = (FloatingActionButton)activity.findViewById(R.id.fab);
        createImgs();
        SubActionButton.Builder itemBuilder = new SubActionButton.Builder(activity);
        saveBtn = itemBuilder.setContentView(saveImg).build();
        saveBtn.setTag(getString(R.string.saveBtnTag));
        deleteBtn = itemBuilder.setContentView(deleteImg).build();
        deleteBtn.setTag(getString(R.string.deleteBtnTag));
        acceptBtn = itemBuilder.setContentView(acceptImg).build();
        acceptBtn.setTag(getString(R.string.acceptBtnTag));
        contactBtn = itemBuilder.setContentView(contactImg).build();
        contactBtn.setTag(getString(R.string.contactBtnTag));
        builder = new FloatingActionMenu.Builder(activity);
    }

    private void createImgs(){
        saveImg = new ImageView(this);
        saveImg.setImageResource(R.drawable.ic_content_save);
        deleteImg = new ImageView(this);
        deleteImg.setImageResource(R.drawable.ic_delete);
        acceptImg = new ImageView(this);
        acceptImg.setImageResource(R.drawable.ic_checkbox_marked_circle);
        contactImg = new ImageView(this);
        contactImg.setImageResource(R.drawable.ic_phone_outgoing);
    }

    public String getNetworkName(Long networkId){
        String networkName="";
        for(Network network: getNetworkList()){
            if(network.getNetworkId()==networkId)networkName = network.getName();
        }
        return networkName;
    }

    public void setToolbarTitle(String title,Activity activity ){
        Toolbar toolbar = (Toolbar) activity.findViewById(R.id.toolbar);
        toolbar.setTitle(title);
    }


    public void toastNoPermissions(){
        L.t(getString(R.string.no_permissions), this);
    }

    public void saveLoginPasswordToShPref(String login, String password){
        L.l("saveLoginPasswordToShPref() login = "+login+ ", password = "+password, this);
        editor.putString(getString(R.string.login), login);
        editor.putString(getString(R.string.password), password);
        editor.putBoolean("saved", true);
        editor.commit();
    }

    public String[] readLoginPasswordFromShPref(){
        login=sharedpreferences.getString(getString(R.string.login), null);
        password = sharedpreferences.getString(getString(R.string.password), null);
        L.l("readLoginPasswordFromShPref() login = " + login + ", password = " + password, this);
        return new String[]{ login, password };
    }

    public void clearLogPassShpref(){
        L.l("clearLogPassShpref()", this);
        editor.clear();
        editor.commit();
    }

    public Long onAddTaskDialogData(TaskType type, String address) {
        L.l("MainActivity. onAddTaskDialogData()");
        getParams().put("type", getGson().toJson(type));
        params.put("address", address);
        String res = sendRequest(params);
        Toast.makeText(currentActivity, "Task created = " + res, Toast.LENGTH_LONG).show();
        getParams().put(getString(R.string.urlTail), "task/getTask");
        params.put(getString(R.string.taskId), res);
        try{
            setCurTask(getGson().fromJson(sendRequest(params), Task.class));
        }catch (Exception e){
            e.printStackTrace();
        }
        return Long.valueOf(res);
    }

    public String getExecutorsNames(Long[] executors){
        String names="";
        for(Long id: executors){
            for(Employee e : getEmplList()){
                if(e.getId()==id)names+=e.getLogin()+" ";
            }
        }
        return names;
    }

    public String getOfficeNames(Long[] ids){
        String names="";
        if(ids!=null){
        for(Long id: ids){
                for(Office office  : updateOfficeList(currentActivity)){
                    if(office.getOfficeId()==id)names+=office.getName()+" ";
                }
            }
        }
        return names;
    }

    public Long[] longArrayPlusOne(Long[] array, Long id){
        if(array!=null){
            Long[] newArray = Arrays.copyOf(array, array.length+1);
            newArray[newArray.length-1] = id;
            return newArray;
        }else  return new Long[]{id};
    }

    public Task getTaskFromServer(Long id){
        getParams().put(getString(R.string.urlTail), getString(R.string.task_getTask));
        params.put(getString(R.string.id), String.valueOf(id));
        return getGson().fromJson(sendRequest(params), Task.class);
    }

    @Override
    public void onLowMemory() {
        L.l("onLowMemory()", this);
        super.onLowMemory();
    }

    @Override
    public void onTerminate() {
        L.l("onTerminate", this);
        super.onTerminate();
    }

    @Override
    public void onTrimMemory(int level) {
        L.l("onTrimMemory()+ level = " + level, this);
        super.onTrimMemory(level);
    }


    //     Getters  Setters


    public TypeToken<List<MyEvent>> getMyEventListTypeToken() {
        return myEventListTypeToken;
    }

    public TypeToken<Employee> getEmployeeTypeToken() {
        return employeeTypeToken;
    }

    public TypeToken<Task> getTaskTypeToken() {
        return taskTypeToken;
    }

    public TypeToken<List<Office>> getOfficeListTypeToken() {
        return officeListTypeToken;
    }

    public TypeToken<List<Network>> getNetworkListToken() {
        return networkListToken;
    }

    public Map<String, String> getParams() {
        params.clear();
        return params;
    }

    public List<Employee> getEmplList() {
        return emplList;
    }

    public List<Network> getNetworkList() {
        return networkList;
    }

    public List<Task> getTaskList() {
        return taskList;
    }

    public Employee getMe() {
        return me;
    }

    public void setMe(Employee me) {
        this.me = me;
    }

    public Task getCurTask() {
        return curTask;
    }

    public void setCurTask(Task curTask) {
        this.curTask = curTask;
    }

    public Network getCurNetwork() {
        return curNetwork;
    }

    public Profile getFbProfile() {
        return fbProfile;
    }

    public void setFbProfile(Profile fbProfile) {
        this.fbProfile = fbProfile;
    }

    public Gson getGson() {
        return gson;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public FloatingActionMenu getFaMenu() {
        return faMenu;
    }

    public SimpleDateFormat getFormat() {
        return format;
    }

    public void setFormat(SimpleDateFormat format) {
        this.format = format;
    }

    public void setCurrentActivity(Activity currentActivity) {
        this.currentActivity = currentActivity;
    }

    public boolean isServiceStarted() {
        return serviceStarted;
    }

    public void setServiceStarted(boolean serviceStarted) {
        this.serviceStarted = serviceStarted;
    }

    public boolean isNeedToFinish() {
        return needToFinish;
    }

    public void setNeedToFinish(boolean needToFinish) {
        this.needToFinish = needToFinish;
    }
}
