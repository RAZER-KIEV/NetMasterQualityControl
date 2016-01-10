package ua.kiev.netmaster.netmasterqualitycontrol.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.reflect.TypeToken;

import java.util.List;
import java.util.concurrent.ExecutionException;

import ua.kiev.netmaster.netmasterqualitycontrol.R;
import ua.kiev.netmaster.netmasterqualitycontrol.activities.LoginActivity;
import ua.kiev.netmaster.netmasterqualitycontrol.activities.MainActivity;
import ua.kiev.netmaster.netmasterqualitycontrol.adapters.EmplAdapter;
import ua.kiev.netmaster.netmasterqualitycontrol.domain.Employee;
import ua.kiev.netmaster.netmasterqualitycontrol.domain.MyDownTask;
import ua.kiev.netmaster.netmasterqualitycontrol.loger.L;

/**
 * Created by ПК on 15.12.2015.
 */
public class EmloyeeFragment extends Fragment implements AdapterView.OnItemClickListener, View.OnClickListener {

    public static final String TAG = "EmloyeeFragmentTag";
    private String result;
    private static List<Employee> emplList;
    private EmplAdapter emplkAdapter;
    private TypeToken<List<Employee>> tokenEmpl;
    //private MyDownTask myDownTask;
    private ListView listView;
    private FloatingActionButton fab;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d(LoginActivity.LOG, "EmloyeeFragment. onAttach()");
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(LoginActivity.LOG, "EmloyeeFragment. onCreate()");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(LoginActivity.LOG, "EmloyeeFragment. onCreateView()");
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_task, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(LoginActivity.LOG, "EmloyeeFragment. onActivityCreated()");

        tokenEmpl =  new TypeToken<List<Employee>>(){};
        listView = (ListView)getActivity().findViewById(R.id.taskListView);

        fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        fab.setOnClickListener(this);


        try {
            result = new MyDownTask("employee/getAll", getActivity().getApplicationContext()).execute().get();
            emplList = LoginActivity.gson.fromJson(result, tokenEmpl.getType());
            emplkAdapter = new EmplAdapter(getActivity().getApplicationContext(), emplList);
            listView.setAdapter(emplkAdapter);
            listView.setOnItemClickListener(this);


        } catch (InterruptedException| ExecutionException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(LoginActivity.LOG, "EmloyeeFragment. onStart()");



    }



    @Override
    public void onResume() {
        super.onResume();
        Log.d(LoginActivity.LOG, "EmloyeeFragment. onResume()");
    }


    @Override
    public void onPause() {
        super.onPause();
        Log.d(LoginActivity.LOG, "EmloyeeFragment. onPause()");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(LoginActivity.LOG, "EmloyeeFragment. onStop()");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(LoginActivity.LOG, "EmloyeeFragment. onDestroyView()");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(LoginActivity.LOG, "EmloyeeFragment. onDestroy()");
    }


    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(LoginActivity.LOG, "EmloyeeFragment. onDetach()");
    }

    public static List<Employee> getEmplList() {
        return emplList;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
       L.l(this.getClass().toString() + "EmloyeeFragment. onItemClick()");
        MainActivity.setDetailsFragment(DetailsFragment.newInstance(emplList.get(i)));
        MainActivity.commitFragment(MainActivity.getDetailsFragment(), getFragmentManager());
    }

    @Override
    public void onClick(View view) {
        CreateRegisterDialog createRegisterDialog = CreateRegisterDialog.newInstance(getString(R.string.create_new_empl));
        createRegisterDialog.show(getActivity().getFragmentManager(), "tag");

    }
}
