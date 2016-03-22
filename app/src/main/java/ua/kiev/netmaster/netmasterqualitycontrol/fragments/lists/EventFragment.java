package ua.kiev.netmaster.netmasterqualitycontrol.fragments.lists;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import ua.kiev.netmaster.netmasterqualitycontrol.R;
import ua.kiev.netmaster.netmasterqualitycontrol.activities.MyApplication;
import ua.kiev.netmaster.netmasterqualitycontrol.adapters.RecyclerViewEventAdapter;
import ua.kiev.netmaster.netmasterqualitycontrol.domain.MyEvent;
import ua.kiev.netmaster.netmasterqualitycontrol.loger.L;


public class EventFragment extends Fragment {

    private MyApplication myApplication;
    private RecyclerView mRecyclerView;
    private FloatingActionButton fab;
    private List<MyEvent> myEventList;
    private RecyclerViewEventAdapter recyclerViewEventAdapter;

    public EventFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        super.onActivityCreated(savedInstanceState);
        mRecyclerView = (RecyclerView) getActivity().findViewById(R.id.list1);
        fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        fab.setVisibility(View.GONE);
        myApplication = (MyApplication)getActivity().getApplication();
        myApplication.setToolbarTitle("Events", getActivity());
        myEventList = myApplication.updateEventList(getActivity());
        recyclerViewEventAdapter = new RecyclerViewEventAdapter(myEventList);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(recyclerViewEventAdapter);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        L.l("onDetach()",this);
        fab.setVisibility(View.VISIBLE);
    }

    @Override
    public void onPause() {
        super.onPause();
        L.l("onPause()",this);
        fab.setVisibility(View.VISIBLE);
    }
}
