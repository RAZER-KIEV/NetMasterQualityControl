package ua.kiev.netmaster.netmasterqualitycontrol.fragments;


import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import ua.kiev.netmaster.netmasterqualitycontrol.R;
import ua.kiev.netmaster.netmasterqualitycontrol.activities.MainActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class DeleteDialogFragment extends DialogFragment implements View.OnClickListener {
    private Button deleteDtn, cancel;
    private EditText loginEt, passwordEt;
    private DeleteDialogFragComunicator deleteDialogFragComunicator;
    private String loginStr, passwordStr;
    private TextView loginTv, passwordTv, dialogTitleTv;

    public DeleteDialogFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        deleteDialogFragComunicator = (DeleteDialogFragComunicator) activity;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //return super.onCreateDialog(savedInstanceState);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.addtask_dialog, null);
        deleteDtn = (Button)view.findViewById(R.id.create_dialog);
        deleteDtn.setText(R.string.delete);
        cancel = (Button)view.findViewById(R.id.cancel_dialog);
        deleteDtn.setOnClickListener(this);
        cancel.setOnClickListener(this);
        loginEt = (EditText)view.findViewById(R.id.etTitle);
        loginEt.setVisibility(View.GONE);
        passwordEt = (EditText) view.findViewById(R.id.etDescription);
        passwordEt.setVisibility(View.GONE);
        loginTv = (TextView) view.findViewById(R.id.titleTv);
        loginTv.setVisibility(View.GONE);
        passwordTv = (TextView) view.findViewById(R.id.descriptionTv);
        passwordTv.setVisibility(View.GONE);
        dialogTitleTv = (TextView) view.findViewById(R.id.dialogTitleTv);
        dialogTitleTv.setText(R.string.delete_Employee);
        AlertDialog.Builder builder  = new AlertDialog.Builder(getActivity());
        builder.setView(view);
        return builder.create();
    }

    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.create_dialog){
            deleteDialogFragComunicator.delete(view);
            MainActivity.commitFragment( new EmloyeeFragment(), getFragmentManager());
            dismiss();
        }else {
            dismiss();
        }
    }
    public interface DeleteDialogFragComunicator {
        void delete(View v);
    }
}
