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
import ua.kiev.netmaster.netmasterqualitycontrol.loger.L;

/**
 * Created by ПК on 04.01.2016.
 */
public class LogoutDialog extends DialogFragment implements View.OnClickListener {
    private Button ExitBtn, cancel;
    private EditText loginEt, passwordEt;
    private LogoutDialogCommunicator logoutDialogCommunicator;
    private TextView loginTv, passwordTv, dialogTitleTv;
    private static boolean exit=false;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        logoutDialogCommunicator = (LogoutDialogCommunicator) activity;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //return super.onCreateDialog(savedInstanceState);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.addtask_dialog, null);
        ExitBtn = (Button)view.findViewById(R.id.create_dialog);
        ExitBtn.setText(R.string.logout);
        cancel = (Button)view.findViewById(R.id.cancel_dialog);
        ExitBtn.setOnClickListener(this);
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
        dialogTitleTv.setText(R.string.do_logout);
        AlertDialog.Builder builder  = new AlertDialog.Builder(getActivity());
        builder.setView(view);
        return builder.create();
    }

    @Override
    public void onClick(View view) {
        L.l("onClick", this);
        if(view.getId()==R.id.create_dialog){
            logoutDialogCommunicator.goToLoginActivity();
            dismiss();
        }else {
            dismiss();
        }
    }

    public interface LogoutDialogCommunicator{
        void goToLoginActivity();
    }

}
