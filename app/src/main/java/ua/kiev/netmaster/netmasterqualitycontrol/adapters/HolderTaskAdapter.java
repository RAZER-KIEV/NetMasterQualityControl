package ua.kiev.netmaster.netmasterqualitycontrol.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.List;

import ua.kiev.netmaster.netmasterqualitycontrol.R;
import ua.kiev.netmaster.netmasterqualitycontrol.domain.Task;

/**
 * Created by ПК on 21.01.2016.
 */
public class HolderTaskAdapter  extends BaseAdapter{
    private Context ctx;
    private SimpleDateFormat format;
    private List<Task> objects = Collections.emptyList();

    public HolderTaskAdapter(Context ctx, List<Task> objects) {
        this.ctx = ctx;
        this.objects = objects;
        format = (SimpleDateFormat) SimpleDateFormat.getDateTimeInstance();
    }

    @Override
    public int getCount() {
        if(objects!=null) return objects.size();
        else return 0;
    }

    @Override
    public Object getItem(int position) {
        return objects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        MyViewHolder myViewHolder;
        Task task = getTask(i);
        String[] datetine = task.getPublished().toString().split(" ");
        if(convertView ==null){
            LayoutInflater inflater = (LayoutInflater) ctx
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item2, viewGroup, false);
            myViewHolder = new MyViewHolder();
            myViewHolder.holderTV1 = (TextView) convertView.findViewById(R.id.name_time2);
            myViewHolder.holderTV2 = (TextView) convertView.findViewById(R.id.lastOnline_Adress2);
            myViewHolder.holderTV3 = (TextView) convertView.findViewById(R.id.isbusy_priority2);
            myViewHolder.holderTV4 = (TextView) convertView.findViewById(R.id.tel_description2);
            convertView.setTag(myViewHolder);
        }else {
            myViewHolder = (MyViewHolder)convertView.getTag();
        }
        myViewHolder.holderTV1.setText(task.getTaskType().toString());//datetine[0] + " "+datetine[1]+" "+datetine[2]+" "+datetine[3]);
        myViewHolder.holderTV2.setText("priority: "+task.getPriority());
        myViewHolder.holderTV3.setText(task.getAddress());
        myViewHolder.holderTV4.setText(format.format(task.getPublished()));

        return convertView;
    }
    static class MyViewHolder {
        TextView holderTV1, holderTV2, holderTV3, holderTV4;

    }

    private Task getTask(int position){
        return (Task) getItem(position);
    }
}
