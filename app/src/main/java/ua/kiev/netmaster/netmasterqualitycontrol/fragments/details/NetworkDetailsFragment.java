package ua.kiev.netmaster.netmasterqualitycontrol.fragments.details;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import ua.kiev.netmaster.netmasterqualitycontrol.R;
import ua.kiev.netmaster.netmasterqualitycontrol.activities.MyApplication;
import ua.kiev.netmaster.netmasterqualitycontrol.domain.Employee;
import ua.kiev.netmaster.netmasterqualitycontrol.domain.Network;
import ua.kiev.netmaster.netmasterqualitycontrol.fragments.lists.NetworkFragment;
import ua.kiev.netmaster.netmasterqualitycontrol.loger.L;
import ua.kiev.netmaster.netmasterqualitycontrol.sequrity.MySecurity;


public class NetworkDetailsFragment extends Fragment implements View.OnClickListener {


    private MyApplication myApplication;
    private String response;
    private FloatingActionButton fab;
    private FloatingActionMenu faMenu;
    private Map<String,String> params;
    private Network network;
    private View rootV;
    private TextView network_id_tv, network_owners_tv, network_employees_tv, network_partners_tv, offices_tv;
    private EditText network_name_et, network_contacts_et;
    private Button owners_plus_btn, empl_plus_btn, partners_plus_btn, offices_plus_btn;


    public NetworkDetailsFragment() {}


    public static NetworkDetailsFragment newInstance(int networkPoss){
        NetworkDetailsFragment networkDetailsFragment = new NetworkDetailsFragment();
        Bundle args = new Bundle();
        args.putInt("position", networkPoss);
        networkDetailsFragment.setArguments(args);
        return networkDetailsFragment;
    }

    private int getPosition(){
        int pos=0;
        if(getArguments()!=null) {
            pos = getArguments().getInt("position", -1);
            return pos;
        } else {
            for(Network ntwrk :myApplication.getNetworkList()){
                if(network.getNetworkId().equals(myApplication.getMe().getNetworkId())) return pos;
                pos++;
            }
        }
        return pos;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        params = new HashMap<>();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootV = inflater.inflate(R.layout.network_datails_fragment, container, false);
        return rootV;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        myApplication = (MyApplication)getActivity().getApplication();
        network = myApplication.getNetworkList().get(getPosition());
        params = new HashMap<>();
        initViews();
    }

    private void initViews() {
        myApplication.overrideFab(getActivity(),this);
        myApplication.setToolbarTitle(network.getName(), getActivity());
        network_id_tv = (TextView) rootV.findViewById(R.id.network_id_tv);
            network_id_tv.setText(network.getNetworkId().toString());
        network_owners_tv = (TextView) rootV.findViewById(R.id.network_owners_tv);
            network_owners_tv.setText(fromLongArrayToEmployeeNames(network.getOwners()));
        network_employees_tv = (TextView) rootV.findViewById(R.id.network_employees_tv);
            network_employees_tv.setText(fromLongArrayToEmployeeNames(network.getEmployees()));
        network_partners_tv = (TextView) rootV.findViewById(R.id.network_partners_tv);
            network_partners_tv.setText(fromLongArrayToNetworkNames(network.getFriendlyNetworks()));
        offices_tv = (TextView) rootV.findViewById(R.id.offices_tv);
            offices_tv.setText(Arrays.toString(network.getOffices()));
        network_name_et = (EditText) rootV.findViewById(R.id.network_name_et);
            network_name_et.setText(network.getName());
        network_contacts_et = (EditText) rootV.findViewById(R.id.network_contacts_et);
            network_contacts_et.setText(network.getContacts());
        initButtons();
    }

    private void initButtons(){
        if(MySecurity.hasPermissionsToModify(network,myApplication)){
            owners_plus_btn = (Button) rootV.findViewById(R.id.owners_plus_btn);
                owners_plus_btn.setVisibility(View.VISIBLE);
                owners_plus_btn.setOnClickListener(this);
            empl_plus_btn = (Button) rootV.findViewById(R.id.empl_plus_btn);
                empl_plus_btn.setVisibility(View.VISIBLE);
                empl_plus_btn.setOnClickListener(this);
            partners_plus_btn = (Button) rootV.findViewById(R.id.partners_plus_btn);
                partners_plus_btn.setVisibility(View.VISIBLE);
                partners_plus_btn.setOnClickListener(this);
            offices_plus_btn = (Button) rootV.findViewById(R.id.offices_plus_btn);
                offices_plus_btn.setVisibility(View.VISIBLE);
                offices_plus_btn.setOnClickListener(this);
        }
    }

    private String fromLongArrayToNetworkNames(Long[] ids){
            if(ids==null || ids.length<1 ) return "";
            StringBuilder sb = new StringBuilder();
            for(Long id : ids){
                for(Network network : myApplication.getNetworkList()) {
                    if( network.getNetworkId()== id){
                     sb.append(network.getName()+" ");
                    }
                }
            }
        return sb.toString();
    }


    private String fromLongArrayToEmployeeNames(Long[] ids){
        if(ids==null || ids.length<1 ) return "";
        StringBuilder sb = new StringBuilder();
        for(Long id : ids){
            for(Employee employee : myApplication.getEmplList()) {
                if( employee.getId()== id){
                    sb.append(employee.getLogin()+" ");
                }
            }
        }
        return sb.toString();
    }


    @Override
    public void onStart() {
        super.onStart();
        faMenu = myApplication.getFaMenu();
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


    private Network compileChangedNetwork(){
        // TODO: 11-Mar-16
        network.setName(network_name_et.getText().toString());
        network.setContacts(network_contacts_et.getText().toString());
        return network;
    }

    private Long[] fromStringToLongArr(String string){
        String[] strArr = string.split(",");
        Long[] longArr = new Long[strArr.length];
        for(int i=0; i<strArr.length; i++){
            if((strArr[i]!=null)&!(strArr[i].equals("null"))) longArr[i]=Long.parseLong(strArr[i].trim()); else longArr[i]=0l;
        }
        return longArr;
    }

    private void networkOnClickSave(){
        if(MySecurity.hasPermissionsToModify(compileChangedNetwork(), myApplication)){
        String gString = myApplication.getGson().toJson(compileChangedNetwork());
        L.l("networkOnClickSave. gString = "+gString, this);
        params.put(getString(R.string.networkgson), gString);
        myApplication.sendRequest(params);
        } else myApplication.toastNoPermissions();
    }

    private void networkOnClickDeleteParams(){
        if(MySecurity.hasPermissionsToModify(network, myApplication)){
        params.put(getString(R.string.networkid),network.getNetworkId().toString());
        myApplication.sendRequest(params);
        }else myApplication.toastNoPermissions();
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
        String tag="";
        if((tag= (String) view.getTag())!=null){
            compileChangedNetwork();
            switch (tag){
                case  "deleteBtnTag" :
                    networkOnClickDeleteParams();
                    L.t("DELETED", getActivity());
                    myApplication.commitFragment(new NetworkFragment(), getFragmentManager());
                    break;
                case "saveBtnTag" :
                    networkOnClickSave();
                    L.t( response, getActivity());
                    break;
            }
        } else {
            switch(view.getId()){
                case R.id.owners_plus_btn :
                    //todo add empl
                    L.t("add owner", getActivity());
                    break;
                case R.id.empl_plus_btn :
                    L.t("add empl", getActivity());
                    // TODO: 14-Mar-16
                    break;
                case R.id.partners_plus_btn :
                    L.t("add partner", getActivity());
                    // TODO: 14-Mar-16
                    break;
                case R.id.offices_plus_btn :
                    L.t("add office", getActivity());
                    // TODO: 14-Mar-16
                    break;
            }
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        L.l("onDestroy", this);
    }
}
