package ua.kiev.netmaster.netmasterqualitycontrol.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import ua.kiev.netmaster.netmasterqualitycontrol.R;
import ua.kiev.netmaster.netmasterqualitycontrol.domain.Task;

/**
 * Created by ПК on 15.12.2015.
 */
public class TaskAdapter extends BaseAdapter {

    private Context ctx;
    private LayoutInflater lInflater;
    private List<Task> objects;

    public TaskAdapter(Context ctx, List<Task> objects) {
        this.ctx = ctx;
        this.objects = objects;
        lInflater = (LayoutInflater)ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return objects.size();
    }

    @Override
    public Object getItem(int i) {
        return objects.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = lInflater.inflate(R.layout.item2, viewGroup, false);
        }
        Task task = getTask(i);

        String[] datetine = task.getPublished().toString().split(" ");

        ((TextView) view.findViewById(R.id.name_time2)).setText(datetine[0]+" "+datetine[1]+" "+datetine[2]+" "+datetine[3]);
        ((TextView) view.findViewById(R.id.isbusy_priority2)).setText("priority: "+task.getPriority());
        ((TextView) view.findViewById(R.id.lastOnline_Adress2)).setText(task.getAddress());
        ((TextView) view.findViewById(R.id.tel_description2)).setText(task.getDescription());


        return view;
    }

    private Task getTask(int position){
        return (Task) getItem(position);
    }
}
