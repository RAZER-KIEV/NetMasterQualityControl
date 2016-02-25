package ua.kiev.netmaster.netmasterqualitycontrol.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import ua.kiev.netmaster.netmasterqualitycontrol.R;
import ua.kiev.netmaster.netmasterqualitycontrol.domain.Network;

/**
 * Created by RAZER on 2/8/2016.
 */
public class RecyclerViewNetworkAdapter extends RecyclerView.Adapter<RecyclerViewNetworkAdapter.ViewHolder> {

    private List<Network> networkList;

    public RecyclerViewNetworkAdapter(List<Network> networkList) {
        this.networkList = networkList;
    }

    @Override
    public RecyclerViewNetworkAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.network_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerViewNetworkAdapter.ViewHolder holder, int position) {
        holder.textView.setText(networkList.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return networkList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView textView;
        public ViewHolder(View itemView) {
            super(itemView);
            textView = (TextView)itemView.findViewById(R.id.networkItemTv);
        }
    }
}
