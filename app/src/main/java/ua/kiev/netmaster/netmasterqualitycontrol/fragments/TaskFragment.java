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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;

import java.util.List;
import java.util.concurrent.ExecutionException;

import ua.kiev.netmaster.netmasterqualitycontrol.R;
import ua.kiev.netmaster.netmasterqualitycontrol.activities.LoginActivity;
import ua.kiev.netmaster.netmasterqualitycontrol.activities.MainActivity;
import ua.kiev.netmaster.netmasterqualitycontrol.activities.MyApplication;
import ua.kiev.netmaster.netmasterqualitycontrol.adapters.HolderTaskAdapter;
import ua.kiev.netmaster.netmasterqualitycontrol.domain.MyDownTask;
import ua.kiev.netmaster.netmasterqualitycontrol.domain.Task;

public class TaskFragment extends Fragment implements AdapterView.OnItemClickListener, View.OnClickListener {


    private MyApplication myApplication;
    private Task task;
    //private String result;
    private List<Task> taskList;
    private HolderTaskAdapter taskAdapter;
    //private TypeToken<List<Task>> tokenTask;
    private ListView listView;
    private FloatingActionButton fab;

    public static final String TAG = "TaskFragmentTag";

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d(LoginActivity.LOG, "TaskFragment. onAttach()");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(LoginActivity.LOG, "TaskFragment. onCreate()");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(LoginActivity.LOG, "TaskFragment. onCreateView()");
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_task, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(LoginActivity.LOG, "TaskFragment. onActivityCreated()");
       // MainActivity.commitFragment(new DetailsFragment(), getFragmentManager());
        //tokenTask =  new TypeToken<List<Task>>(){};
        myApplication = (MyApplication) getActivity().getApplication();
        listView = (ListView)getActivity().findViewById(R.id.taskListView);
        fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        fab.setOnClickListener(this);
        taskList = ((MyApplication)getActivity().getApplication()).updateTaskList();
        taskAdapter = new HolderTaskAdapter(getActivity(), taskList);
        listView.setAdapter(taskAdapter);
        listView.setOnItemClickListener(this);


    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(LoginActivity.LOG, "TaskFragment. onStart()");
       // ListView taskList = (ListView)getActivity().findViewById(R.id.taskListView);
       // taskList.setOnItemClickListener(this);
        //initViews();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(LoginActivity.LOG, "TaskFragment. onResume()");
        ((MyApplication)getActivity().getApplication()).setCurTask(null);
    }


    @Override
    public void onPause() {
        super.onPause();
        Log.d(LoginActivity.LOG, "TaskFragment. onPause()");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(LoginActivity.LOG, "TaskFragment. onStop()");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(LoginActivity.LOG, "TaskFragment. onDestroyView()");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(LoginActivity.LOG, "TaskFragment. onDestroy()");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(LoginActivity.LOG, "TaskFragment. onDetach()");
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        task = taskList.get(i);
        Log.d(LoginActivity.LOG,"TaskFragment. onItemClick(), task: "+task);
        myApplication.commitFragment(DetailsFragment.newInstance(task), getFragmentManager());
    }

    @Override
    public void onClick(View view) {
        if(view.getId()!=R.id.fab) return;
            new CreateTaskDialog().show(getFragmentManager(),"AddTaskDialog");
    }

    public List<Task> getTaskList() {
        return taskList;
    }
}
