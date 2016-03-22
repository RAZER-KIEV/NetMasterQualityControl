package ua.kiev.netmaster.netmasterqualitycontrol.fragments.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import ua.kiev.netmaster.netmasterqualitycontrol.R;
import ua.kiev.netmaster.netmasterqualitycontrol.activities.MyApplication;
import ua.kiev.netmaster.netmasterqualitycontrol.loger.L;

/**
 * Created by ПК on 22.12.2015.
 */
public class CreateRegisterDialog extends DialogFragment implements View.OnClickListener{
    private Button registerBt, cancel;
    private EditText loginEt, passwordEt;
    private RegisterDialogCommunicator registerDialogCommunicator;
    private String loginStr, passwordStr;
    private static String title;
    private TextView loginTv, passwordTv, dialogTitleTv;
    private View view;
    private MyApplication myApplication;

    public CreateRegisterDialog() {
    }


    public static CreateRegisterDialog newInstance(String title) {
        L.l("NetworkDetailsFragment.newInstance(Task task)");
        CreateRegisterDialog f = new CreateRegisterDialog();
        L.l("-----------------------" + String.valueOf(f.getId()));
        Bundle args = new Bundle();
        if (title != null) {
            args.putString("title", title);
            CreateRegisterDialog.title = title;
        }
        f.setArguments(args);
        return f;
    }

    private void initViews() {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.addtask_dialog, null);
        registerBt = (Button)view.findViewById(R.id.create_dialog);
        registerBt.setText(R.string.register);
        cancel = (Button)view.findViewById(R.id.cancel_dialog);
        registerBt.setOnClickListener(this);
        cancel.setOnClickListener(this);
        loginEt = (EditText)view.findViewById(R.id.etTitle);
        loginEt.setText(myApplication.getLogin());//LoginActivity.getLogin());
        passwordEt = (EditText) view.findViewById(R.id.etDescription);
        passwordEt.setText(myApplication.getPassword());
        loginTv = (TextView) view.findViewById(R.id.titleTv);
        passwordTv = (TextView) view.findViewById(R.id.descriptionTv);
        passwordTv.setText(R.string.password);
        dialogTitleTv = (TextView) view.findViewById(R.id.dialogTitleTv);

        if(title!=null)
            setTitle(title);
        else {
            dialogTitleTv.setText(R.string.register_dialog_title);
            loginTv.setText(R.string.login);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        registerDialogCommunicator = (RegisterDialogCommunicator) activity;
        myApplication = (MyApplication) activity.getApplication();
        initViews();
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder  = new AlertDialog.Builder(getActivity());
        builder.setView(view);
        return builder.create();
    }

    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.create_dialog){
            loginStr = loginEt.getText().toString();
            passwordStr = passwordEt.getText().toString();
            if(passwordStr.length()>0) {
                registerDialogCommunicator.registerDialogData(loginStr, passwordStr);
                //addTaskDialogCommunicator.onAddTaskDialogData(loginStr, passwordStr);
            }else
                registerDialogCommunicator.addEmplToMyNetwork(Long.valueOf(loginStr));
            dismiss();
        }else {
            dismiss();
        }
    }

    public interface  RegisterDialogCommunicator
    {
        void registerDialogData(String login, String password);
        void addEmplToMyNetwork(Long emplId);
    }

    public void setTitle(String title){
        dialogTitleTv.setText(title);
        loginEt.setText("");
        passwordEt.setText("");
        passwordEt.setHint("only if new");
        loginTv.setText("login or ID");
    }
}
