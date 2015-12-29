package ua.kiev.netmaster.netmasterqualitycontrol.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import ua.kiev.netmaster.netmasterqualitycontrol.R;
import ua.kiev.netmaster.netmasterqualitycontrol.activities.LoginActivity;
import ua.kiev.netmaster.netmasterqualitycontrol.domain.Employee;

/**
 * Created by ПК on 16.12.2015.
 */
public class EmplAdapter extends BaseAdapter{

    private Context ctx;
    private LayoutInflater lInflater;
    private List<Employee> objects;

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
            view = lInflater.inflate(R.layout.item, viewGroup, false);
        }
        Employee empl = getEmpl(i);
        Log.d(LoginActivity.LOG, "empl: "+empl);

        String[] datetine = empl.getLastOnline().toString().split(" ");

        ((TextView) view.findViewById(R.id.name_time)).setText(empl.getLogin());
        ((TextView) view.findViewById(R.id.priority_isbusy)).setText("Busy: "+empl.getIsBusy());
        ((TextView) view.findViewById(R.id.lastOnline_Adress)).setText(datetine[0]+" "+datetine[1]+" "+datetine[2]+" "+datetine[3]);
        ((TextView) view.findViewById(R.id.description)).setText(empl.getPhone());

        return view;
    }

    private Employee getEmpl(int position){
        return (Employee) getItem(position);
    }
}
