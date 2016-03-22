package ua.kiev.netmaster.netmasterqualitycontrol.fragments.details;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;

import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;

import java.util.HashMap;
import java.util.Map;

import ua.kiev.netmaster.netmasterqualitycontrol.R;
import ua.kiev.netmaster.netmasterqualitycontrol.activities.MyApplication;
import ua.kiev.netmaster.netmasterqualitycontrol.domain.Employee;
import ua.kiev.netmaster.netmasterqualitycontrol.enums.EmplPossition;
import ua.kiev.netmaster.netmasterqualitycontrol.fragments.dialogs.DeleteDialogFragment;
import ua.kiev.netmaster.netmasterqualitycontrol.loger.L;
import ua.kiev.netmaster.netmasterqualitycontrol.sequrity.MySecurity;

/**
 * Created by RAZER on 04-Mar-16.
 */
public class EmplDetailsFragment extends Fragment  implements View.OnClickListener {

    private FloatingActionMenu faMenu;
    private View rootV;
    private Employee employee;
    private Spinner positionSpinner;
    private MyApplication myApplication;
    private TextView empl_id_tv, login_tv , empl_network_id_tv, empl_registered_tv, empl_last_online_tv, empl_is_busy_tv, cur_task_tv;
    private EditText empl_inn_et, empl_phone_et, empl_home_et, empl_password_et;
    private TableRow password_row;
    private Map<String,String> params;
    private String response;
    private boolean isMe;

    public static EmplDetailsFragment newInstance (int position){
        EmplDetailsFragment emplDetailsFragment = new EmplDetailsFragment();
        Bundle args = new Bundle();
        args.putInt("position", position);
        emplDetailsFragment.setArguments(args);
        return emplDetailsFragment;
    }

    private int getPosition(){
        if(getArguments()!=null)
        return getArguments().getInt("position",-1);
        else return -1;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        L.l("onCreateView()", this);
        rootV = inflater.inflate(R.layout.empl_details_fragment,container, false);
        return rootV;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        L.l("onActivityCreated", this);
        myApplication = (MyApplication)getActivity().getApplication();
        L.l(myApplication.getMe().toString(), this);
        if(getPosition()==-1)employee = myApplication.getMe();
        else employee = myApplication.getEmplList().get(getPosition());
        params = new HashMap<>();
        isMe = myApplication.getMe().equals(employee);
        L.l(myApplication.getMe().toString(), this);
        L.l("isMe "+isMe, this);
        initViews();
    }

    @Override
    public void onStart() {
        super.onStart();
        faMenu = myApplication.getFaMenu();
    }

    private void initViews() {
        myApplication.overrideFab(getActivity(), this);
        myApplication.setToolbarTitle(employee.getLogin(),getActivity());
        empl_id_tv = (TextView) rootV.findViewById(R.id.empl_id_tv);
            empl_id_tv.setText(String.valueOf(employee.getId()));
        login_tv = (TextView) rootV.findViewById(R.id.login_tv);
            login_tv.setText(employee.getLogin());
        empl_network_id_tv = (TextView) rootV.findViewById(R.id.empl_network_id_tv);
            empl_network_id_tv.setText(myApplication.getNetworkName(employee.getNetworkId()));
        empl_registered_tv = (TextView) rootV.findViewById(R.id.empl_registered_tv);
            empl_registered_tv.setText(myApplication.getFormat().format(employee.getRegdate()));
        empl_last_online_tv = (TextView) rootV.findViewById(R.id.empl_last_online_tv);
            empl_last_online_tv.setText(myApplication.getFormat().format(employee.getLastOnline()));
        empl_is_busy_tv = (TextView) rootV.findViewById(R.id.empl_is_busy_tv);
            empl_is_busy_tv.setText(String.valueOf(employee.getIsBusy()));
        cur_task_tv = (TextView) rootV.findViewById(R.id.cur_task_tv);
            cur_task_tv.setText(String.valueOf(employee.getCurrentTaskId()));


        if(isMe){
            password_row = (TableRow) rootV.findViewById(R.id.password_row);
            password_row.setVisibility(View.VISIBLE);
            empl_password_et = (EditText)rootV.findViewById(R.id.empl_password_et);
            empl_password_et.setText(employee.getPassword());
        }
        empl_inn_et = (EditText)rootV.findViewById(R.id.empl_inn_et);
            empl_inn_et.setText(employee.getInn());
        empl_phone_et = (EditText)rootV.findViewById(R.id.empl_phone_et);
            empl_phone_et.setText(employee.getPhone());
        empl_home_et = (EditText)rootV.findViewById(R.id.empl_home_et);
            empl_home_et.setText(employee.getHome());

        preparePositionSpinner();
    }

    private void preparePositionSpinner() {
        positionSpinner = (Spinner) getActivity().findViewById(R.id.empl_position_spinner);
        ArrayAdapter<EmplPossition> adapter = new ArrayAdapter<>(getContext(), R.layout.sppiner_item, EmplPossition.values());
        adapter.setDropDownViewResource(R.layout.sppiner_item);
        positionSpinner.setAdapter(adapter);
        positionSpinner.setSelection(getTypeIndex());
        positionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                L.l("amIOwnerOfCurNetwork() = "+amIOwnerOfCurNetwork());
                if (myApplication.getMe().getPosition().equals(EmplPossition.SUPERADMIN) || amIOwnerOfCurNetwork()) {
                    employee.setPosition(EmplPossition.values()[position]);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private int getTypeIndex() {
        for(int i = 0; i<positionSpinner.getCount(); i++ ){
            if(positionSpinner.getItemAtPosition(i).equals(employee.getPosition())) return i;
        }
        return 0;
    }

    private boolean amIOwnerOfCurNetwork(){
        for(Long id :myApplication.getCurNetwork().getOwners()){
            if(id.equals(myApplication.getMe().getId())) return true;
        }
        return false;
    }

    @Override
    public void onStop() {
        super.onStop();
        L.l("onStop", this);
        if(faMenu.isOpen()) faMenu.close(true);
    }

    @Override
    public void onClick(View view) {
        compileChangedEmpl();
        switch (view.getTag().toString()){
            case  "deleteBtnTag" :
                new DeleteDialogFragment().show(getFragmentManager(), "deleteEmplTag");
                break;
            case "saveBtnTag" :
                emplOnClickSaveParams();
                break;
            case  "contactBtnTag" :
                callEmpl();
                break;
        }
    }

    private Employee compileChangedEmpl(){
       if(isMe) employee.setPassword(empl_password_et.getText().toString());
        employee.setInn(empl_inn_et.getText().toString());
        employee.setPhone(empl_phone_et.getText().toString());
        employee.setHome(empl_home_et.getText().toString());
        L.l("NetworkDetailsFragment. changed Empl" + employee.toString());
        return employee;
    }

    public void emplOnClickDeleteParams(){
        if(MySecurity.hasPermissionsToModify(employee, myApplication)){
            L.l("emplOnClickDeleteParams()", this);
            params.put(getString(R.string.emlpId), employee.getId().toString());
            response = myApplication.sendRequest(params);
            L.t("deleted: "+response, getActivity());
        }else myApplication.toastNoPermissions();
    }

    private void emplOnClickSaveParams(){
        if(MySecurity.hasPermissionsToModify(employee, myApplication)){
            L.l("emplOnClickSaveParams()", this);
            String gString = myApplication.getGson().toJson(employee);
            params.put(getString(R.string.employee), gString);
            response = myApplication.sendRequest(params);
            L.t("saved: "+response, getActivity());
        } else myApplication.toastNoPermissions();
    }

    private void callEmpl(){
        Intent intent =new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + employee.getPhone()));
        startActivity(intent);
    }
}
