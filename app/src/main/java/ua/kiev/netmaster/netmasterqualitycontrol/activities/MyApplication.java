package ua.kiev.netmaster.netmasterqualitycontrol.activities;

import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Base64;
import android.util.Log;

import com.facebook.Profile;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import ua.kiev.netmaster.netmasterqualitycontrol.R;
import ua.kiev.netmaster.netmasterqualitycontrol.domain.Employee;
import ua.kiev.netmaster.netmasterqualitycontrol.domain.MyEvent;
import ua.kiev.netmaster.netmasterqualitycontrol.domain.MyDownTask;
import ua.kiev.netmaster.netmasterqualitycontrol.domain.Network;
import ua.kiev.netmaster.netmasterqualitycontrol.domain.Task;
import ua.kiev.netmaster.netmasterqualitycontrol.loger.L;

/**
 * Created by ПК on 21.12.2015.
 */
public class MyApplication extends Application{

    private List<Employee> emplList;
    private List<Network> networkList;
    private List<Task> taskList;
    private List<MyEvent> myEventList;
    private Employee me;
    private Employee employee;
    private Task curTask;
    private Network curNetwork;
    private Profile fbProfile;
    private Gson gson;
    private String login, password;

    @Override
    public void onCreate() {
        super.onCreate();
        L.l("onCreate()", this);
        printHashKey();
        gson = new Gson();
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
                Log.d(LoginActivity.LOG, Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException|NoSuchAlgorithmException e){e.printStackTrace();}
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
        L.l("onTrimMemory()+ level = "+level, this);
        super.onTrimMemory(level);
    }


    public List<Employee> updateEmplList(){
        try {
            String result = new MyDownTask(getString(R.string.employee_getAll),getApplicationContext()).execute().get();
            TypeToken<List<Employee>> tokenEmpl = new TypeToken<List<Employee>>() {}; //// TODO: 2/16/2016 make not automatic variable;
            emplList = gson.fromJson(result, tokenEmpl.getType());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        for(Employee e: emplList){
            if(e.getId()==me.getId())me=e;
        }
        return emplList;
    }

    public List<Task> updateTaskList(){
        try {
            String result = new MyDownTask(getString(R.string.task_getAll),getApplicationContext()).execute().get();
            TypeToken<List<Task>> tokenEmpl = new TypeToken<List<Task>>() {};
            taskList = gson.fromJson(result, tokenEmpl.getType());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return taskList;
    }

    public List<Network> updateNetworkList(){
        try {
            String result = new MyDownTask(getString(R.string.network_getAll),getApplicationContext()).execute().get();
            TypeToken<List<Network>> tokenEmpl = new TypeToken<List<Network>>() {};
            networkList = gson.fromJson(result, tokenEmpl.getType());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return networkList;
    }

    public List<MyEvent> updateEventList() {
        try {
            String result = new MyDownTask(getString(R.string.event_getAllbyNetwork),getApplicationContext()).execute().get();
            TypeToken<List<MyEvent>> tokenEmpl = new TypeToken<List<MyEvent>>() {};
            myEventList = gson.fromJson(result, tokenEmpl.getType());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return myEventList;
    }


    public void commitFragment(Fragment fragment, FragmentManager fragmentManager) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        String str[]=fragment.getClass().toString().split("\\.");
        String fragmentName = str[str.length-1].trim();
        L.l("fragment.getClass().getCanonicalName() = " + fragment.getClass().getCanonicalName(),this);
        fragmentTransaction.replace(R.id.contentLayout, fragment, fragmentName);
        fragmentTransaction.addToBackStack("BackTag");
        fragmentTransaction.commit();
        fragmentManager.executePendingTransactions();
    }

    private Employee getMefromServer() {
        try {
            Map<String,String> params=new HashMap<>();
            params.put(getString(R.string.urlTail), getString(R.string.employee_getEmpl));
            String result = new MyDownTask(params, getApplicationContext()).execute().get();
            TypeToken<Employee> tokenEmpl = new TypeToken<Employee>() {};
            me = gson.fromJson(result, tokenEmpl.getType());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return me;
    }


    //     Getters  Setters
    public List<Employee> getEmplList() {
        return emplList;
    }

    public void setEmplList(List<Employee> emplList) {
        this.emplList = emplList;
    }

    public List<Network> getNetworkList() {
        return networkList;
    }

    public void setNetworkList(List<Network> networkList) {
        this.networkList = networkList;
    }

    public List<Task> getTaskList() {
        return taskList;
    }

    public void setTaskList(List<Task> taskList) {
        this.taskList = taskList;
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

    public void setCurNetwork(Network curNetwork) {
        this.curNetwork = curNetwork;
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

    public void setGson(Gson gson) {
        this.gson = gson;
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


}
