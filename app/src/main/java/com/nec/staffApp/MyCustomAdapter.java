package com.nec.staffApp;

/**
 * Created by Javeeth Asraf on 11/7/2017.
 */


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Nihal on 1/24/2017.
 */

public class MyCustomAdapter extends ArrayAdapter{
    private Context context;
    private ArrayList<Staff> staff;

    public MyCustomAdapter(Context context, int textViewResourceId, ArrayList objects) {
        super(context,textViewResourceId, objects);

        this.context= context;
        staff=objects;

    }

    private class ViewHolder
    {
        TextView staffName;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ViewHolder holder=null;
        if (convertView == null)
        {
            LayoutInflater vi = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(R.layout.staff_details, null);

            holder = new ViewHolder();
            holder.staffName = (TextView) convertView.findViewById(R.id.staff_name);

            convertView.setTag(holder);

        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        Staff stf= staff.get(position);

        holder.staffName.setText(""+ stf.getName());

        return convertView;


    }
}
