package ua.kiev.netmaster.netmasterqualitycontrol.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;

import ua.kiev.netmaster.netmasterqualitycontrol.R;
import ua.kiev.netmaster.netmasterqualitycontrol.domain.MyEvent;
import ua.kiev.netmaster.netmasterqualitycontrol.loger.L;

/**
 * Created by RAZER on 2/20/2016.
 */
public class RecyclerViewEventAdapter extends RecyclerView.Adapter<RecyclerViewEventAdapter.ViewHolder> {

    private List<MyEvent> myEventList;

    public RecyclerViewEventAdapter(List<MyEvent> networkList) {
        this.myEventList = networkList;
    }

    @Override
    public RecyclerViewEventAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerViewEventAdapter.ViewHolder holder, int position) {
        L.l(myEventList.get(position).toString(), this);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMM hh:mm:ss");
        String sdf = simpleDateFormat.format(myEventList.get(position).getPublishDate());
        L.l("sdf = "+sdf+", myEventList.get(position).getMessage() = "+myEventList.get(position).getMessage(), this);
        holder.textView0.setText(sdf);
        holder.textView1.setText(myEventList.get(position).getMessage());
    }

    @Override
    public int getItemCount() {
        return myEventList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView textView0;
        TextView textView1;
        public ViewHolder(View itemView) {
            super(itemView);
            textView0 = (TextView)itemView.findViewById(R.id.event_item_date);
            textView1 = (TextView) itemView.findViewById(R.id.event_item_message);
        }
    }
}
