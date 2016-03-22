package ua.kiev.netmaster.netmasterqualitycontrol.fragments.lists;

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
import ua.kiev.netmaster.netmasterqualitycontrol.adapters.RecyclerViewNetworkAdapter;
import ua.kiev.netmaster.netmasterqualitycontrol.domain.Network;
import ua.kiev.netmaster.netmasterqualitycontrol.fragments.details.NetworkDetailsFragment;
import ua.kiev.netmaster.netmasterqualitycontrol.fragments.dialogs.CreateNetworkDialog;
import ua.kiev.netmaster.netmasterqualitycontrol.listeners.NetworkRecyclerItemClickListener;

/*
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */

public class NetworkFragment extends Fragment implements View.OnClickListener {

    private MyApplication myApplication;
    private RecyclerView mRecyclerView;
    private FloatingActionButton fab;
    private List<Network> networkList;
    private RecyclerViewNetworkAdapter recyclerViewNetworkAdapter;

    public NetworkFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mRecyclerView = (RecyclerView) getActivity().findViewById(R.id.list1);
        fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        fab.setOnClickListener(this);
        myApplication = (MyApplication)getActivity().getApplication();
        myApplication.setToolbarTitle("Networks", getActivity());
        networkList = myApplication.updateNetworkList(getActivity());
        recyclerViewNetworkAdapter = new RecyclerViewNetworkAdapter(networkList);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(recyclerViewNetworkAdapter);
        mRecyclerView.addOnItemTouchListener(
                new NetworkRecyclerItemClickListener(getActivity(), new NetworkRecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                       myApplication.commitFragment(NetworkDetailsFragment.newInstance(position), getFragmentManager());
                    }
                })
        );
    }

    @Override
    public void onClick(View v) {
        new CreateNetworkDialog().show(getFragmentManager(), "createNetworkDialog");
       // L.t("clicked",getContext());
    }

    public interface OnListFragmentInteractionListener {
    }

    public List<Network> getNetworkList() {
        return networkList;
    }
}
