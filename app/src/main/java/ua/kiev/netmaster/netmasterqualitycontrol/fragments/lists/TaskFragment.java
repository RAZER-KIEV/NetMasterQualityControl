package ua.kiev.netmaster.netmasterqualitycontrol.fragments.lists;

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

import java.util.List;

import ua.kiev.netmaster.netmasterqualitycontrol.R;
import ua.kiev.netmaster.netmasterqualitycontrol.activities.LoginActivity;
import ua.kiev.netmaster.netmasterqualitycontrol.activities.MyApplication;
import ua.kiev.netmaster.netmasterqualitycontrol.adapters.HolderTaskAdapter;
import ua.kiev.netmaster.netmasterqualitycontrol.domain.Task;
import ua.kiev.netmaster.netmasterqualitycontrol.fragments.details.TaskDetailsFragment;
import ua.kiev.netmaster.netmasterqualitycontrol.fragments.dialogs.CreateTaskDialog;
import ua.kiev.netmaster.netmasterqualitycontrol.loger.L;

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
        L.l("TaskFragment. onAttach()");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        L.l("TaskFragment. onCreate()");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        L.l("TaskFragment. onCreateView()");
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_task, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        L.l("TaskFragment. onActivityCreated()");
       // MainActivity.commitFragment(new NetworkDetailsFragment(), getFragmentManager());
        //tokenTask =  new TypeToken<List<Task>>(){};
        myApplication = (MyApplication) getActivity().getApplication();
        myApplication.setToolbarTitle("Tasks", getActivity());
        listView = (ListView)getActivity().findViewById(R.id.taskListView);
        fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        fab.setOnClickListener(this);
        taskList = ((MyApplication)getActivity().getApplication()).updateTaskList(getActivity());
        taskAdapter = new HolderTaskAdapter(getActivity(), taskList);
        listView.setAdapter(taskAdapter);
        listView.setOnItemClickListener(this);


    }

    @Override
    public void onStart() {
        super.onStart();
        L.l("TaskFragment. onStart()");
       // ListView taskList = (ListView)getActivity().findViewById(R.id.taskListView);
       // taskList.setOnItemClickListener(this);
        //initViews();
    }

    @Override
    public void onResume() {
        super.onResume();
        L.l("TaskFragment. onResume()");
        ((MyApplication)getActivity().getApplication()).setCurTask(null);
    }


    @Override
    public void onPause() {
        super.onPause();
        L.l("TaskFragment. onPause()");
    }

    @Override
    public void onStop() {
        super.onStop();
        L.l("TaskFragment. onStop()");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        L.l("TaskFragment. onDestroyView()");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        L.l("TaskFragment. onDestroy()");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        L.l("TaskFragment. onDetach()");
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        task = taskList.get(i);
        L.l("TaskFragment. onItemClick(), task: " + task);
        //myApplication.commitFragment(NetworkDetailsFragment.newInstance(task), getFragmentManager());
        myApplication.commitFragment(TaskDetailsFragment.newInstance(i),getFragmentManager());
    }

    @Override
    public void onClick(View view) {
        if(view.getId()!=R.id.fab) return;
        new CreateTaskDialog().show(getFragmentManager(), "AddTaskDialog");
    }

    public List<Task> getTaskList() {
        return taskList;
    }
}
