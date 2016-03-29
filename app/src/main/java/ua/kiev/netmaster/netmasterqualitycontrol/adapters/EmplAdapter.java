package ua.kiev.netmaster.netmasterqualitycontrol.adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import ua.kiev.netmaster.netmasterqualitycontrol.R;
import ua.kiev.netmaster.netmasterqualitycontrol.activities.MyApplication;
import ua.kiev.netmaster.netmasterqualitycontrol.domain.Employee;

/**
 * Created by ПК on 16.12.2015.
 */
public class EmplAdapter extends BaseAdapter{

    private Context ctx;
    private LayoutInflater lInflater;
    private List<Employee> objects;
    private TextView right_top, left_bott, right_bott;
    private ImageView imageView_item2;
    private Employee empl;

    public EmplAdapter(Context ctx, List<Employee> objects) {
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
        empl = getEmpl(i);
        ((TextView) view.findViewById(R.id.left_top)).setText(empl.getLogin());
        right_top = ((TextView) view.findViewById(R.id.right_top));//.setText("Busy: "+empl.getIsBusy());
            right_top.setText("Busy: " + empl.getIsBusy());
        left_bott = ((TextView) view.findViewById(R.id.left_bott));//.setText(datetine[0]+" "+datetine[1]+" "+datetine[2]+" "+datetine[3]);
            left_bott.setText("Last online: "+((MyApplication)ctx.getApplicationContext()).getFormat().format(empl.getLastOnline()));
        right_bott = ((TextView) view.findViewById(R.id.right_bott));//.setText(empl.getPhone());
               right_bott.setText(empl.getPhone());
        imageView_item2 = ((ImageView) view.findViewById(R.id.imageView_item2));
        switchColors();
        choseImg();
        return view;
    }

    private void switchColors(){
        if (empl.getIsBusy()){
            right_top.setBackgroundColor(ContextCompat.getColor(ctx, R.color.color_light_red));
        } else right_top.setBackgroundColor(ContextCompat.getColor(ctx, R.color.color_light_green));
    }

    private void choseImg(){
        switch (empl.getPosition()){
            case TECHNICIAN: imageView_item2.setImageDrawable(ContextCompat.getDrawable(ctx,R.mipmap.tech_icon));
                break;
            case ADMIN: imageView_item2.setImageDrawable(ContextCompat.getDrawable(ctx,R.mipmap.admin_icon));
                break;
            case SUPERADMIN: imageView_item2.setImageDrawable(ContextCompat.getDrawable(ctx,R.mipmap.boss_icon));
                break;
        }
    }

    private Employee getEmpl(int position){
        return (Employee) getItem(position);
    }
}
