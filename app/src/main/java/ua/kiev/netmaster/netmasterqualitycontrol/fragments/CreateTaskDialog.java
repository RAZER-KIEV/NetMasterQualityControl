package ua.kiev.netmaster.netmasterqualitycontrol.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import ua.kiev.netmaster.netmasterqualitycontrol.R;

/**
 * Created by ПК on 18.12.2015.
 */
public class CreateTaskDialog extends DialogFragment implements View.OnClickListener {
    //// TODO: 18.12.2015
    private Button create, cancel;
    private EditText title, descripton;
    private AddTaskDialogCommunicator addTaskDialogCommunicator;
    private String titleStr, descriptionStr;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        addTaskDialogCommunicator = (AddTaskDialogCommunicator) activity;
    }



    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.addtask_dialog, null);
        create = (Button)view.findViewById(R.id.create_dialog);
        cancel = (Button)view.findViewById(R.id.cancel_dialog);
        create.setOnClickListener(this);
        cancel.setOnClickListener(this);
        title = (EditText)view.findViewById(R.id.etTitle);
        descripton = (EditText) view.findViewById(R.id.etDescription);

       AlertDialog.Builder builder  = new AlertDialog.Builder(getActivity());
        //builder.setTitle(R.string.create_new_Task);
        builder.setView(view);
        return builder.create();
    }

    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.create_dialog){
            titleStr = title.getText().toString();
            descriptionStr = descripton.getText().toString();
            addTaskDialogCommunicator.onAddTaskDialogData(titleStr, descriptionStr);
            dismiss();
        }else {
            dismiss();
        }
    }
    public interface  AddTaskDialogCommunicator
    {
        void onAddTaskDialogData(String title, String description);
    }
}
