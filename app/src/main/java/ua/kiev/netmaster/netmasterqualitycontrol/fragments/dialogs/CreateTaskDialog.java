package ua.kiev.netmaster.netmasterqualitycontrol.fragments.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.HashMap;
import java.util.Map;

import ua.kiev.netmaster.netmasterqualitycontrol.R;
import ua.kiev.netmaster.netmasterqualitycontrol.activities.MyApplication;
import ua.kiev.netmaster.netmasterqualitycontrol.domain.Task;
import ua.kiev.netmaster.netmasterqualitycontrol.enums.TaskType;
import ua.kiev.netmaster.netmasterqualitycontrol.loger.L;

/**
 * Created by ПК on 18.12.2015.
 */
public class CreateTaskDialog extends DialogFragment implements View.OnClickListener {
    //// TODO: 18.12.2015
    private Button create, cancel;
    private EditText title, descripton;
    private AddTaskDialogCommunicator addTaskDialogCommunicator;
    private String  addressStr;
    private Spinner taskTypeSpinner;
    private TaskType taskType;
    private MyApplication myApplication;

    @Override
    public void onAttach(Activity activity) {
        L.l("onAttach()",this);
        super.onAttach(activity);
        addTaskDialogCommunicator = (AddTaskDialogCommunicator) activity;
        myApplication = (MyApplication) activity.getApplication();
    }



    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        L.l("onCreateDialog()",this);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.addtask_dialog, null);
        create = (Button)view.findViewById(R.id.create_dialog);
        cancel = (Button)view.findViewById(R.id.cancel_dialog);
        create.setOnClickListener(this);
        cancel.setOnClickListener(this);
        title = (EditText)view.findViewById(R.id.etTitle);
        title.setVisibility(View.GONE);
        descripton = (EditText) view.findViewById(R.id.etDescription);
        taskTypeSpinner = (Spinner)view.findViewById(R.id.task_type_spinner_dialog);
        prepareSpinner();

       AlertDialog.Builder builder  = new AlertDialog.Builder(getActivity());
        //builder.setTitle(R.string.create_new_Task);
        builder.setView(view);
        return builder.create();
    }

    private void prepareSpinner(){
        L.l("prepareSpinner()",this);
        ArrayAdapter<TaskType> adapter = new ArrayAdapter<>(getContext(), R.layout.sppiner_item, TaskType.values());
        adapter.setDropDownViewResource(R.layout.sppiner_item);
        taskTypeSpinner.setAdapter(adapter);
        taskTypeSpinner.setVisibility(View.VISIBLE);
        taskTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                taskType = TaskType.values()[position];
                L.l("onItemSelected()"+taskType.toString(), this);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                L.l("onNothingSelected()",taskType.toString());
            }
        });
    }

    @Override
    public void onClick(View view) {
        L.l("onClick()",this);
        if(view.getId()==R.id.create_dialog){
            L.l("Create Button clicked()",this);
            //titleStr = title.getText().toString();
            addressStr = descripton.getText().toString();
            addTaskDialogCommunicator.onAddTaskDialogData(taskType, addressStr);
            Map<String, String> params = new HashMap<>();
            params.put(getString(R.string.urlTail), "task/getTask");
            params.put(getString(R.string.id),"1");
            Task task = myApplication.getGson().fromJson(myApplication.sendRequest(params), Task.class);
            myApplication.setCurTask(task);
            dismiss();
        }else if(view.getId()==R.id.cancel_dialog){
            L.l("Cancel Button clicked()",this);
            dismiss();
        }
    }
    public interface  AddTaskDialogCommunicator
    {
        Long onAddTaskDialogData(TaskType type, String address);
    }
}
