package ua.kiev.netmaster.netmasterqualitycontrol.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import ua.kiev.netmaster.netmasterqualitycontrol.R;
import ua.kiev.netmaster.netmasterqualitycontrol.activities.LoginActivity;
import ua.kiev.netmaster.netmasterqualitycontrol.activities.MainActivity;
import ua.kiev.netmaster.netmasterqualitycontrol.activities.MyApplication;
import ua.kiev.netmaster.netmasterqualitycontrol.domain.Employee;
import ua.kiev.netmaster.netmasterqualitycontrol.domain.MyDownTask;
import ua.kiev.netmaster.netmasterqualitycontrol.domain.Network;
import ua.kiev.netmaster.netmasterqualitycontrol.domain.Task;
import ua.kiev.netmaster.netmasterqualitycontrol.enums.DeteilsMode;
import ua.kiev.netmaster.netmasterqualitycontrol.loger.L;
import ua.kiev.netmaster.netmasterqualitycontrol.sequrity.MySecurity;


public class DetailsFragment extends Fragment implements View.OnClickListener {


    private MyApplication myApplication;
    private static Employee employee;
    private static Task task;
    private static Network network;
    private static DeteilsMode mode;

    private final String saveBtnTag="saveBtnTag", deleteBtnTag="deleteBtnTag", acceptBtnTag="acceptBtnTag", contactBtnTag="contactBtnTag";
    private String response;
    private EditText[] editTexts = new EditText[13];
    private FloatingActionButton fab;
    private TextView[] textViews = new TextView[15];
    private FloatingActionMenu faMenu;
    private Employee changedEmployee;
    private Task changedTask;
    private Network changedNetwork;
    //private Gson gson;
    private Map<String,String> params;

    //private static boolean isTaskMode;


    public DetailsFragment() {}


    public static DetailsFragment newInstance(Task task) {
        L.l("DetailsFragment.newInstance(Task task)");
        DetailsFragment f = new DetailsFragment();
        L.l("-----------------------"+String.valueOf(f.getId()));
        Bundle args = new Bundle();
        if(task!=null){
            args.putString("arg", "task");
            DetailsFragment.task  = task;
            mode = DeteilsMode.TaskMode;
        }
        f.setArguments(args);
        return f;
    }


    public static DetailsFragment newInstance(Employee employee){
        L.l("DetailsFragment.newInstance(Employee employee)");
        DetailsFragment f = new DetailsFragment();
        Bundle args = new Bundle();
        if (employee!=null){
            args.putString("arg", "employee");   //getArguments().getInt("arg", null);
            DetailsFragment.employee = employee;
            mode=DeteilsMode.EmployeeMode;
        }
        f.setArguments(args);
        return f;
    }

    public static DetailsFragment newInstance(Network network){
        L.l("DetailsFragment.newInstance(Employee employee)");
        DetailsFragment f = new DetailsFragment();
        Bundle args = new Bundle();
        if (network!=null){
            args.putString("arg", "network");   //getArguments().getInt("arg", null);
            DetailsFragment.network = network;
            mode = DeteilsMode.NetworkMode;
        }
        f.setArguments(args);
        return f;
    }

    private String getArg(){
       return getArguments().getString("arg");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        params = new HashMap<>();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_details, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        myFindViewById();
        initMyFab();
        myApplication = (MyApplication)getActivity().getApplication();
        //gson=myApplication.getGson();

    }

    @Override
    public void onStart() {
        super.onStart();
        if(getArg().equals("task")){
            initTaskViews();
        }else if(getArg().equals("employee")){
            initEmplViews();
        }else  if(getArg().equals("network")){
            initNetworkViews();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        L.l("onDetach ", this);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    private Task compileChangedTask(){
        changedTask = new Task(task.getTaskId(), task.getNetworkId(), fromStringToLongArr(editTexts[0].getText().toString()), editTexts[1].getText().toString(), editTexts[2].getText().toString(),
                editTexts[3].getText().toString(),task.getLatitude(), task.getLongitude(), task.getPublished(), task.getAccepted(), task.getDone(),
                task.getStatus(),task.getPriority());
        Log.d(LoginActivity.LOG, "DetailsFragment. change Task" + changedTask.toString());
        return changedTask;
    }

    private Employee compileChangedEmpl(){
       /* changedEmployee = new Employee();
        changedEmployee.setId(employee.getId());
        changedEmployee.setNetworkId(employee.getNetworkId());
        changedEmployee.setPosition(employee.getPosition());
        changedEmployee.setRegdate(employee.getRegdate());
        changedEmployee.setLastLat(employee.getLastLat());
        changedEmployee.setLastLong(employee.getLastLong());
        changedEmployee.setLastOnline(employee.getLastOnline());
        //changedEmployee.setLogin();*/


        changedEmployee = new Employee(employee.getId(), employee.getNetworkId(), employee.getPosition(), editTexts[1].getText().toString(),editTexts[2].getText().toString(), editTexts[4].getText().toString(),
                editTexts[5].getText().toString(), editTexts[6].getText().toString(), employee.getRegdate(), Integer.valueOf(editTexts[8].getText().toString()),
                Integer.valueOf(editTexts[9].getText().toString()), Boolean.valueOf(editTexts[10].getText().toString()), Boolean.valueOf(editTexts[6].getText().toString()),
                employee.getLastLat(),employee.getLastLong(), employee.getLastOnline());
        Log.d(LoginActivity.LOG, "DetailsFragment. change Empl" + changedEmployee.toString());
        return changedEmployee;
    }

    private Network compileChangedNetwork(){
       changedNetwork = new Network(network.getNetworkId(), editTexts[0].getText().toString(),fromStringToLongArr(editTexts[1].getText().toString()), fromStringToLongArr(editTexts[2].getText().toString()),
               fromStringToLongArr(editTexts[3].getText().toString()), fromStringToLongArr(editTexts[4].getText().toString()), editTexts[5].getText().toString());
        L.l("changedNetwork = " + changedNetwork, this);
        return changedNetwork;
    }

    private Long[] fromStringToLongArr(String string){
        String[] strArr = string.split(",");
        Long[] longArr = new Long[strArr.length];
        for(int i=0; i<strArr.length; i++){
            if((strArr[i]!=null)&!(strArr[i].equals("null"))) longArr[i]=Long.parseLong(strArr[i].trim()); else longArr[i]=0l;
        }
        return longArr;
    }

    private void taskOnClickSave(){
        if(MySecurity.hasPermissionsToModify(changedTask,myApplication)){
        Log.d(LoginActivity.LOG, "DetailsFragment. taskOnClick()");
        String gString = myApplication.getGson().toJson(compileChangedTask());
        Log.d(LoginActivity.LOG, "DetailsFragment. gString: " + gString);
        params.put(getString(R.string.task), gString);
        sendRequest(params);
        } else toastNoPermissins();
    }

    private void networkOnClickSave(){
        if(MySecurity.hasPermissionsToModify(compileChangedNetwork(), myApplication)){
        String gString = myApplication.getGson().toJson(compileChangedNetwork());
        L.l("networkOnClickSave. gString = "+gString, this);
        params.put(getString(R.string.networkgson), gString);
        sendRequest(params);
        } else toastNoPermissins();
    }

    private void taskOnClicAcceptParams(){
        if(MySecurity.hasPermissionsToAccept(changedTask, myApplication)) {
            L.l("taskOnClicAcceptParams", this);
            if (changedTask.getAccepted() == null) {
                changedTask.setAccepted(new Date());
            } else if (changedTask.getDone() == null) changedTask.setDone(new Date());
            String gString = myApplication.getGson().toJson(changedTask);
            params.put(getString(R.string.task), gString);
            sendRequest(params);
        }else toastNoPermissins();
    }

    private void taskOnClickDeleteParams(){
        if(MySecurity.hasPermissionsToModify(changedTask, myApplication)){
        L.l("taskOnClickDeleteParams", this);
        params.put(getString(R.string.taskId), changedTask.getTaskId().toString());
        sendRequest(params);
        }else toastNoPermissins();
    }

    private void networkOnClickDeleteParams(){
        if(MySecurity.hasPermissionsToModify(changedNetwork,myApplication)){
        params.put(getString(R.string.networkid),changedNetwork.getNetworkId().toString());
        sendRequest(params);
        }else toastNoPermissins();
    }
    private void emplOnClickSaveParams(){
        if(MySecurity.hasPermissionsToModify(changedEmployee, myApplication)){
        Log.d(LoginActivity.LOG, "DetailsFragment. emplOnClickSaveParams()");
        String gString = myApplication.getGson().toJson(changedEmployee);
        Log.d(LoginActivity.LOG, "DetailsFragment. gString: " + gString);
        params.put(getString(R.string.employee), gString);
        sendRequest(params);
        }else toastNoPermissins();
    }

    public void emplOnClickDeleteParams(){
        if(MySecurity.hasPermissionsToModify(changedEmployee, myApplication)){
        L.l("emplOnClickAcceptParams()", this);
        params.put(getString(R.string.emlpId), changedEmployee.getId().toString());
        sendRequest(params);
        }else toastNoPermissins();
    }

    private String sendRequest(Map<String, String> params){
        try {
            response = new MyDownTask(params, getActivity().getApplicationContext()).execute().get();
        } catch (InterruptedException|ExecutionException e) {
            e.printStackTrace();
        }
        return response;
    }

    @Override
    public void onStop() {
        super.onStop();
        L.l("onStop", this);
        if(faMenu.isOpen()) faMenu.close(true);
        fab=null;
    }

    @Override
    public void onClick(View view) {
        params.clear();
        if(mode==DeteilsMode.EmployeeMode){
            L.l("DetailsFragment. onClick() !isTaskMode");
            compileChangedEmpl();
            switch (view.getTag().toString()){
                case  "deleteBtnTag" :
                    new DeleteDialogFragment().show(getFragmentManager(), "deleteEmplTag");
                    //L.t("deleted "+response, getActivity());
                    return;
                case "saveBtnTag" :
                    emplOnClickSaveParams();
                    L.t("saved. "+response, getActivity());
                    break;
                case  "contactBtnTag" :
                    callEmpl();
                    L.t("contactBtnTag pressed. TODO", getActivity());
                    break;
            }
        }
        if(mode==DeteilsMode.TaskMode){
            compileChangedTask();
            L.l("onClick. isTaskMode");
            switch (view.getTag().toString()){
                case  "deleteBtnTag" :
                    taskOnClickDeleteParams();
                    L.t("DELETED", getActivity());
                    myApplication.commitFragment(new TaskFragment(),getFragmentManager());
                    break;
                case "saveBtnTag" :
                    taskOnClickSave();
                    L.t( response, getActivity());
                    break;
                case  "acceptBtnTag" :
                    taskOnClicAcceptParams();
                    L.t("accepted " + response, getActivity()); // TODO: 29.12.2015 migrate data setting to server!
                    myApplication.commitFragment( new TaskFragment(), getFragmentManager());
                    break;
            }
        }if(mode ==DeteilsMode.NetworkMode){
            compileChangedNetwork();
            switch (view.getTag().toString()){
                case  "deleteBtnTag" :
                    //taskOnClickDeleteParams();
                    networkOnClickDeleteParams();
                    L.t("DELETED", getActivity());
                    myApplication.commitFragment(new NetworkFragment(), getFragmentManager());
                    break;
                case "saveBtnTag" :
                    networkOnClickSave();
                    L.t( response, getActivity());
                    break;
            }
        }
    }

    private void callEmpl(){
        Intent intent =new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + employee.getPhone()));
        startActivity(intent);
    }

    private void myFindViewById(){
        L.l("myFindViewById()",this);
        fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);

        textViews[0] = (TextView) getActivity().findViewById(R.id.detTextView0);
        textViews[1] = (TextView) getActivity().findViewById(R.id.detTextView1);
        textViews[2] = (TextView) getActivity().findViewById(R.id.detTextView2);
        textViews[3] = (TextView) getActivity().findViewById(R.id.detTextView3);
        textViews[4] = (TextView) getActivity().findViewById(R.id.detTextView4);
        textViews[5] = (TextView) getActivity().findViewById(R.id.detTextView5);
        textViews[6] = (TextView) getActivity().findViewById(R.id.detTextView6);
        textViews[7] = (TextView) getActivity().findViewById(R.id.detTextView7);
        textViews[8] = (TextView) getActivity().findViewById(R.id.detTextView8);
        textViews[9] = (TextView) getActivity().findViewById(R.id.detTextView9);
        textViews[10] = (TextView) getActivity().findViewById(R.id.detTextView10);
        textViews[11] = (TextView) getActivity().findViewById(R.id.detTextView11);
        textViews[12] = (TextView) getActivity().findViewById(R.id.detTextView12);
        textViews[13] = (TextView) getActivity().findViewById(R.id.detTextView13);
        textViews[14] = (TextView) getActivity().findViewById(R.id.detTextView14);

        editTexts[0] = (EditText) getActivity().findViewById(R.id.detEditText2);
        editTexts[1] = (EditText) getActivity().findViewById(R.id.detEditText3);
        editTexts[2] = (EditText) getActivity().findViewById(R.id.detEditText4);
        editTexts[3] = (EditText) getActivity().findViewById(R.id.detEditText5);
        editTexts[4] = (EditText) getActivity().findViewById(R.id.detEditText6);
        editTexts[5] = (EditText) getActivity().findViewById(R.id.detEditText7);
        editTexts[6] = (EditText) getActivity().findViewById(R.id.detEditText8);
        editTexts[7] = (EditText) getActivity().findViewById(R.id.detEditText9);
        editTexts[8] = (EditText) getActivity().findViewById(R.id.detEditText10);
        editTexts[9] = (EditText) getActivity().findViewById(R.id.detEditText11);
        editTexts[10] = (EditText) getActivity().findViewById(R.id.detEditText12);
        editTexts[11] = (EditText) getActivity().findViewById(R.id.detEditText13);
        editTexts[12] = (EditText) getActivity().findViewById(R.id.detEditText14);
    }
    private void initMyFab(){
        L.l("initMyFab()", this);
        SubActionButton.Builder itemBuilder = new SubActionButton.Builder(getActivity());

        ImageView saveImg = new ImageView(getActivity());
        saveImg.setImageResource(R.drawable.ic_content_save);

        ImageView deleteImg = new ImageView(getActivity());
        deleteImg.setImageResource(R.drawable.ic_delete);

        ImageView acceptImg = new ImageView(getActivity());
        acceptImg.setImageResource(R.drawable.ic_checkbox_marked_circle);

        ImageView contactImg = new ImageView(getActivity());
        contactImg.setImageResource(R.drawable.ic_phone_outgoing);

        SubActionButton saveBtn = itemBuilder.setContentView(saveImg).build();
        saveBtn.setOnClickListener(this);
        saveBtn.setTag(saveBtnTag);
        SubActionButton deleteBtn = itemBuilder.setContentView(deleteImg).build();
        deleteBtn.setOnClickListener(this);
        deleteBtn.setTag(deleteBtnTag);
        SubActionButton acceptBtn = itemBuilder.setContentView(acceptImg).build();
        acceptBtn.setOnClickListener(this);
        acceptBtn.setTag(acceptBtnTag);
        SubActionButton contactBtn = itemBuilder.setContentView(contactImg).build();
        contactBtn.setOnClickListener(this);
        contactBtn.setTag(contactBtnTag);
        L.l("initMyFab(). isTaskMode=" + mode, this);
        FloatingActionMenu.Builder builder = new FloatingActionMenu.Builder(getActivity())
                .addSubActionView(deleteBtn)
                .addSubActionView(saveBtn);
        if(mode==DeteilsMode.EmployeeMode){
            L.l("contactBtn added", this);
            builder.addSubActionView(contactBtn);
        }else if(mode==DeteilsMode.TaskMode) {
            L.l("acceptBtn added", this);
            builder.addSubActionView(acceptBtn);
        }
        builder.attachTo(fab);
        L.l(fab.toString());
        L.l(saveBtn.toString());
        L.l(deleteBtn.toString());
        L.l(acceptBtn.toString());
        faMenu = builder.build();
    }

    private void initTaskViews() {
        L.l("initTaskViews()", this);
        textViews[0].setText("ID");
        textViews[1].setText(task.getTaskId().toString());
        textViews[2].setText("Executors");
        textViews[3].setText("Title");
        textViews[4].setText("Description");
        textViews[5].setText("Address");
        textViews[6].setText("Published");
        textViews[7].setText("Accepted");
        textViews[8].setText("Done");
        textViews[9].setText("Status");
        textViews[10].setText("Priority");

        editTexts[0].setText(Arrays.toString(task.getExecuterIds()).replaceAll("]", "").replace("[", ""));
        editTexts[1].setText(task.getTitle());
        editTexts[2].setText(task.getDescription());
        editTexts[3].setText(task.getAddress());
        editTexts[4].setText(("" + task.getPublished()).toString().substring(0, 20).trim());
        if(task.getAccepted()!=null){
                 editTexts[5].setText(("" + task.getAccepted()).toString().substring(0, 20).trim());
        }else    editTexts[5].setText("" + task.getAccepted());
        if(task.getDone()!=null){
                 editTexts[6].setText(("" + task.getDone()).toString().substring(0, 20).trim());
        }else    editTexts[6].setText(""+task.getDone());
        editTexts[7].setText(task.getStatus().toString());
        editTexts[8].setText(task.getPriority().toString());

        for(int i=0; i<11; i++){
            textViews[i].setVisibility(View.VISIBLE);
        }
        for(int i=0; i<9; i++){
            editTexts[i].setVisibility(View.VISIBLE);
        }
    }
    private void initEmplViews() {
        L.l("initEmplViews()", this);
        textViews[0].setText("ID");
        textViews[1].setText(employee.getId().toString());
        textViews[2].setText("Network");
        textViews[3].setText("Login");
        textViews[4].setText("Password");
        textViews[5].setText("Position");
        textViews[6].setText("INN");
        textViews[7].setText("Phone");
        textViews[8].setText("Home");
        textViews[9].setText("Is Busy");
        textViews[10].setText("WrongPass");
        textViews[11].setText("BonusSumm");
        textViews[12].setText("isBlocked");
        textViews[13].setText("Reg. date");
        textViews[14].setText("last Online");


        String networkName="";
        for(Network network: myApplication.getNetworkList()){
            if(network.getNetworkId()==employee.getNetworkId())networkName = network.getName();
        }
        editTexts[0].setText(networkName);
        editTexts[1].setText(employee.getLogin());
        editTexts[2].setText(employee.getPassword()); editTexts[2].setTransformationMethod(PasswordTransformationMethod.getInstance()); // TODO: 26.12.2015
        editTexts[3].setText(employee.getPosition().toString());
        editTexts[4].setText(employee.getInn());
        editTexts[5].setText(employee.getPhone());
        editTexts[6].setText(employee.getHome());
        editTexts[7].setText(employee.getIsBusy().toString());
        editTexts[8].setText(employee.getWrongPass().toString());
        editTexts[9].setText(employee.getBonusSumm().toString());
        editTexts[10].setText(employee.getIsBlocked().toString());
        editTexts[11].setText("" + employee.getRegdate().toString().substring(0, 20).trim());
        editTexts[12].setText("" + employee.getLastOnline().toString().substring(0, 20).trim());

        for (int i = 0; i < 14; i++) {
            textViews[i].setVisibility(View.VISIBLE);
        }
        for (int i = 0; i < 12; i++) {
            editTexts[i].setVisibility(View.VISIBLE);
        }
    }

    private void initNetworkViews(){
        L.l("initNetworkViews()", this);
        textViews[0].setText("ID");
        textViews[1].setText(network.getNetworkId().toString());
        textViews[2].setText("Name");
        textViews[3].setText("Owners");
        textViews[4].setText("Employees");
        textViews[5].setText("Tasks");
        textViews[6].setText("Partners");
        textViews[7].setText("Contacts");
        //editTexts[0].setText(Arrays.toString(task.getExecuterIds()).replaceAll("]", "").replace("[", ""));
        editTexts[0].setText(network.getName());
        editTexts[1].setText(Arrays.toString(network.getOwners()).replaceAll("]","").replace("[", ""));
        editTexts[2].setText(Arrays.toString(network.getEmployees()).replaceAll("]","").replace("[",""));
        editTexts[3].setText(Arrays.toString(network.getTasks()).replaceAll("]", "").replace("[", ""));
        editTexts[4].setText(Arrays.toString(network.getFriendlyNetworks()).replaceAll("]", "").replace("[", ""));
        editTexts[5].setText(network.getContacts());

        for (int i = 0; i < 7; i++) {
            textViews[i].setVisibility(View.VISIBLE);
        }
        for (int i = 0; i < 5; i++) {
            editTexts[i].setVisibility(View.VISIBLE);
        }

    }

    private void toastNoPermissins(){
        L.t(myApplication.getString(R.string.no_permissions),getActivity());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        L.l("onDestroy", this);
    }
}
