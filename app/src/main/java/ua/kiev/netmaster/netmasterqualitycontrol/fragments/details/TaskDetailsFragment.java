package ua.kiev.netmaster.netmasterqualitycontrol.fragments.details;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import ua.kiev.netmaster.netmasterqualitycontrol.R;
import ua.kiev.netmaster.netmasterqualitycontrol.activities.MyApplication;
import ua.kiev.netmaster.netmasterqualitycontrol.domain.Employee;
import ua.kiev.netmaster.netmasterqualitycontrol.domain.Task;
import ua.kiev.netmaster.netmasterqualitycontrol.enums.TaskPriority;
import ua.kiev.netmaster.netmasterqualitycontrol.enums.TaskStatus;
import ua.kiev.netmaster.netmasterqualitycontrol.enums.TaskType;
import ua.kiev.netmaster.netmasterqualitycontrol.fragments.lists.TaskFragment;
import ua.kiev.netmaster.netmasterqualitycontrol.loger.L;
import ua.kiev.netmaster.netmasterqualitycontrol.sequrity.MySecurity;

/**
 * Created by RAZER on 2/26/2016.
 */
public class TaskDetailsFragment extends Fragment implements View.OnClickListener {

    private FloatingActionMenu faMenu;
    private View rootV;
    private Task task;
    private Spinner typeSpinner;
    private Spinner prioritySpinner;
    private MyApplication myApplication;
    private TextView task_id_tv_value, executors_id_tv_value , published_tv_value, accepted_tv_value, done_tv_value, status_tv_value;
    private EditText description_et, address_et;
   // private SimpleDateFormat format;
    private FloatingActionButton fab;
    private Task changedTask;
    private Map<String,String> params;
    private String response;

    public static TaskDetailsFragment newInstance (int position){
        TaskDetailsFragment taskDetailsFragment = new TaskDetailsFragment();
        Bundle args = new Bundle();
        args.putInt("position", position);
        taskDetailsFragment.setArguments(args);
        return taskDetailsFragment;
    }

    int getPosition(){
        return getArguments().getInt("position",-1);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootV = inflater.inflate(R.layout.task_details_fragment,container, false);
        return rootV;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        L.l("onActivityCreated", this);
        myApplication = (MyApplication)getActivity().getApplication();
        task = myApplication.getTaskList().get(getPosition());
        myApplication.setCurTask(task);
        params = myApplication.getParams();
        initViews();
    }

    @Override
    public void onStart() {
        super.onStart();
        faMenu = myApplication.getFaMenu();
    }

    private void initViews() {
        myApplication.overrideFab(getActivity(),this);
        myApplication.setToolbarTitle(task.getTaskType().toString(), getActivity());
        description_et = (EditText) rootV.findViewById(R.id.description_et);
            description_et.setText(task.getDescription());
        address_et = (EditText) rootV.findViewById(R.id.address_et);
            address_et.setText(task.getAddress());
        task_id_tv_value = (TextView) rootV.findViewById(R.id.task_id_tv_value);
            task_id_tv_value.setText(task.getTaskId().toString());
        executors_id_tv_value = (TextView) rootV.findViewById(R.id.executors_id_tv_value);
            executors_id_tv_value.setText(myApplication.getExecutorsNames(task.getExecuterIds()));                                         //Arrays.toString(task.getExecuterIds()).replaceAll("]", "").replace("[", ""));
        published_tv_value = (TextView) rootV.findViewById(R.id.published_tv_value);
            published_tv_value.setText(myApplication.getFormat().format(task.getPublished()));
        accepted_tv_value = (TextView) rootV.findViewById(R.id.accepted_tv_value);
            if(task.getAccepted()!=null)accepted_tv_value.setText(myApplication.getFormat().format(task.getAccepted()));
        done_tv_value = (TextView) rootV.findViewById(R.id.done_tv_value);
            if(task.getDone()!=null) done_tv_value.setText(myApplication.getFormat().format(task.getDone()));
        status_tv_value = (TextView) rootV.findViewById(R.id.status_tv_value);
            status_tv_value.setText(task.getStatus().toString());
        prepareSpinners();
    }

    private void prepareSpinners(){
        prepareTaskType();
        prepareTaskPriority();
    }

    private void prepareTaskType() {
        typeSpinner = (Spinner) getActivity().findViewById(R.id.task_type_spinner);
        ArrayAdapter<TaskType> adapter = new ArrayAdapter<>(getContext(), R.layout.sppiner_item, TaskType.values());
        adapter.setDropDownViewResource(R.layout.sppiner_item);
        typeSpinner.setAdapter(adapter);
        typeSpinner.setSelection(getTypeIndex());
        typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                task.setTaskType(TaskType.values()[position]);
                myApplication.setCurTask(task);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private int getTypeIndex() {
        for(int y = 0; y<TaskType.values().length; y++ ){
            if(typeSpinner.getItemAtPosition(y).equals(task.getTaskType())) return y;
        }
        return 0;
    }

    private void prepareTaskPriority() {
        prioritySpinner = (Spinner) getActivity().findViewById(R.id.task_priority_spinner);
        ArrayAdapter<TaskPriority> adapter = new ArrayAdapter<>(getContext(), R.layout.sppiner_item, TaskPriority.values());
        adapter.setDropDownViewResource(R.layout.sppiner_item);
        prioritySpinner.setAdapter(adapter);
        prioritySpinner.setSelection(getPriorityIndex());
        prioritySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                task = myApplication.getCurTask();
                task.setPriority(TaskPriority.values()[position]);
                myApplication.setCurTask(task);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private int getPriorityIndex() {
        for(int y = 0; y<TaskPriority.values().length; y++ ){
            if(prioritySpinner.getItemAtPosition(y).equals(task.getPriority())) return y;
        }
        return 0;
    }

    @Override
    public void onStop() {
        super.onStop();
        L.l("onStop", this);
        if(faMenu.isOpen()) faMenu.close(true);
    }

    @Override
    public void onClick(View view) {
        compileChangedTask();
        L.l("onClick. isTaskMode");
        switch (view.getTag().toString()) {
            case "deleteBtnTag":
                taskOnClickDeleteParams();
                L.t("DELETED: " + response, getActivity());
                myApplication.commitFragment(new TaskFragment(), getFragmentManager());
                break;
            case "saveBtnTag":
                taskOnClickSave();
                L.t("saved: "+response, getActivity());
                break;
            case "acceptBtnTag":
                taskOnClicAcceptParams();
                L.t("accepted: " + response, getActivity());
                myApplication.commitFragment(TaskDetailsFragment.newInstance(getPosition()), getFragmentManager());
                break;
        }
    }

    private Task compileChangedTask(){
        changedTask = new Task(task.getTaskId(), task.getNetworkId(), task.getExecuterIds(), task.getTaskType(),description_et.getText().toString(),
                address_et.getText().toString(),task.getLatitude(), task.getLongitude(), task.getPublished(), task.getAccepted(), task.getDone(),
                task.getStatus(),task.getPriority());
        L.l("NetworkDetailsFragment. change Task" + changedTask.toString());
        return changedTask;
    }
    private void taskOnClickSave(){
        if(MySecurity.hasPermissionsToModify(changedTask, myApplication)){
            L.l("NetworkDetailsFragment. taskOnClick()");
            String gString = myApplication.getGson().toJson(compileChangedTask());
            L.l("NetworkDetailsFragment. gString: " + gString);
            getParams().put(getString(R.string.task), gString);
            response = myApplication.sendRequest(params);
        } else myApplication.toastNoPermissions();
    }

    private void taskOnClicAcceptParams(){
        if(MySecurity.hasPermissionsToAccept(changedTask, myApplication)) {
            L.l("taskOnClicAcceptParams", this);
            getParams().put(getString(R.string.urlTail), getString(R.string.task_acceptTask));
            params.put(getString(R.string.taskId), String.valueOf(changedTask.getTaskId()));
            response = myApplication.sendRequest(params);
        }else myApplication.toastNoPermissions();
    }

    private void taskOnClickDeleteParams(){
        if(MySecurity.hasPermissionsToModify(changedTask, myApplication)){
            L.l("taskOnClickDeleteParams", this);
            getParams().put(getString(R.string.taskId), changedTask.getTaskId().toString());
            response = myApplication.sendRequest(params);
        }else myApplication.toastNoPermissions();
    }

    private Map<String, String> getParams() {
        params.clear();
        return params;
    }
}
