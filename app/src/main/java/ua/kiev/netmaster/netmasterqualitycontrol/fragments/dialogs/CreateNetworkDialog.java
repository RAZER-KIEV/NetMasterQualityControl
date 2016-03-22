package ua.kiev.netmaster.netmasterqualitycontrol.fragments.dialogs;

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
import android.widget.TextView;

import ua.kiev.netmaster.netmasterqualitycontrol.R;

/**
 * Created by RAZER on 2/9/2016.
 */
public class CreateNetworkDialog extends DialogFragment implements View.OnClickListener {
    //// TODO: 18.12.2015
    private Button create, cancel;
    private EditText title, descripton;
    //private AddTaskDialogCommunicator addTaskDialogCommunicator;
    private CreateNetworkDialogCommunicator createNetworkDialogCommunicator;
    private String titleStr;//, descriptionStr;
    private TextView descriptionTV, dialogTitle;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        //addTaskDialogCommunicator = (AddTaskDialogCommunicator) activity;
        createNetworkDialogCommunicator = (CreateNetworkDialogCommunicator)activity;
    }



    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.addtask_dialog, null);
        dialogTitle = (TextView) view.findViewById(R.id.dialogTitleTv);
        dialogTitle.setText(getString(R.string.createNetwork));

        create = (Button)view.findViewById(R.id.create_dialog);
        cancel = (Button)view.findViewById(R.id.cancel_dialog);
        create.setOnClickListener(this);
        cancel.setOnClickListener(this);

        descriptionTV = (TextView) view.findViewById(R.id.descriptionTv);
        descriptionTV.setVisibility(View.GONE);

        title = (EditText)view.findViewById(R.id.etTitle);
        descripton = (EditText) view.findViewById(R.id.etDescription);
        descripton.setVisibility(View.GONE);


        AlertDialog.Builder builder  = new AlertDialog.Builder(getActivity());
        //builder.setTitle(R.string.create_new_Task);
        builder.setView(view);
        return builder.create();
    }

    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.create_dialog){
            titleStr = title.getText().toString();
            createNetworkDialogCommunicator.onCreateNetworkDialogData(titleStr);
            dismiss();
        }else {
            dismiss();
        }
    }
    public interface  CreateNetworkDialogCommunicator
    {
        void onCreateNetworkDialogData(String title);
    }
}
