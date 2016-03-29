package ua.kiev.netmaster.netmasterqualitycontrol.adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
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
    private Task task;
    private MyViewHolder myViewHolder;

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

        task = getTask(i);
        String[] datetine = task.getPublished().toString().split(" ");
        if(convertView ==null){
            LayoutInflater inflater = (LayoutInflater) ctx
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item2, viewGroup, false);
            myViewHolder = new MyViewHolder();
            myViewHolder.holderTV1 = (TextView) convertView.findViewById(R.id.left_top);
            myViewHolder.holderTV2 = (TextView) convertView.findViewById(R.id.right_top);
            myViewHolder.holderTV3 = (TextView) convertView.findViewById(R.id.left_bott);
            myViewHolder.holderTV4 = (TextView) convertView.findViewById(R.id.right_bott);
            myViewHolder.imageView = (ImageView) convertView.findViewById(R.id.imageView_item2);
            convertView.setTag(myViewHolder);
        }else {
            myViewHolder = (MyViewHolder)convertView.getTag();
        }
        myViewHolder.holderTV1.setText(task.getAddress());//datetine[0] + " "+datetine[1]+" "+datetine[2]+" "+datetine[3]);
        myViewHolder.holderTV2.setText("Priority: "+task.getPriority());
        myViewHolder.holderTV3.setText(task.getDescription());
        myViewHolder.holderTV4.setText("Status: " + task.getStatus());
        switchColors();
        switchImages();
        return convertView;
    }

    private void switchColors(){
        switch (task.getStatus()){
            case NEW: myViewHolder.holderTV4.setBackgroundColor(ContextCompat.getColor(ctx, R.color.color_light_red));  //ctx.getResources().getColor(R.color.color_light_red));
                break;
            case INPROCESS: myViewHolder.holderTV4.setBackgroundColor(ContextCompat.getColor(ctx, R.color.color_light_yellow));
                break;
            case DONE: myViewHolder.holderTV4.setBackgroundColor(ContextCompat.getColor(ctx, R.color.color_light_green));
                break;
        }
    }

    private void switchImages(){
        switch (task.getTaskType()){
            case USER_CONNECTING:
                myViewHolder.imageView.setBackground(ContextCompat.getDrawable(ctx, R.mipmap.userconn_icon));
                break;
            case BOX_INSTALL:
                myViewHolder.imageView.setBackground(ContextCompat.getDrawable(ctx, R.mipmap.box_install));
                break;
            case REPAIR:
                myViewHolder.imageView.setBackground(ContextCompat.getDrawable(ctx, R.mipmap.task_icon));
                break;
            case OTHER:
                myViewHolder.imageView.setBackground(ContextCompat.getDrawable(ctx, R.mipmap.userconn_icon));
                break;
            case CABLE_INSTALL:
                myViewHolder.imageView.setBackground(ContextCompat.getDrawable(ctx, R.mipmap.cable_install));
                break;
        }
    }

    static class MyViewHolder {
        TextView holderTV1, holderTV2, holderTV3, holderTV4;
        ImageView imageView;

    }

    private Task getTask(int position){
        return (Task) getItem(position);
    }
}
