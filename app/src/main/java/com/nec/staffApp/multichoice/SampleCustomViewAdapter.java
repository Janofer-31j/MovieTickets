package com.nec.staffApp.multichoice;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nec.staffApp.R;
import com.nec.staffApp.Results;
import com.nec.staffApp.Staff;

import java.util.ArrayList;
import java.util.HashMap;

public class SampleCustomViewAdapter extends MultiChoiceAdapter<SampleCustomViewAdapter.CustomViewHolder> {

    private final ArrayList<Staff> mArrayList;
    private ArrayList<Staff> mFilteredList;
    private ArrayList<Staff> selectedStaffList;
    private final Context mContext;
    public ArrayList<Object> viewArray = new ArrayList<Object>();
    HashMap<String, String> viewHashMap = new HashMap<String, String>();


    public SampleCustomViewAdapter(ArrayList<Staff> messageV0s, Context context) {
        this.mArrayList = messageV0s;
        this.mFilteredList = messageV0s;
        this. selectedStaffList= messageV0s;
        this.mContext = context;
        viewArray.clear();
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CustomViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sample_custom_view, parent, false));
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);

        Staff currentItem = mFilteredList.get(position);
        holder.itemView.setTag(currentItem.id);
        holder.tv_name.setText(currentItem.getName());

        holder.tvDepartment.setText(currentItem.getDept());

        holder.tvDesignation.setText(currentItem.getDsn());
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                String charString = charSequence.toString().toLowerCase();

                if (charString.isEmpty()) {

                    mFilteredList = mArrayList;
                } else {

                    ArrayList<Staff> filteredList = new ArrayList<>();

                    for (Staff searchedStaff : mArrayList) {

                        if ((!TextUtils.isEmpty(searchedStaff.name)) && searchedStaff.name.toLowerCase().contains(charString) ||
                                (!TextUtils.isEmpty(searchedStaff.dept)) && searchedStaff.dept.toLowerCase().contains(charString) ||
                                (!TextUtils.isEmpty(searchedStaff.dsn)) && searchedStaff.dsn.toLowerCase().contains(charString)) {
                            filteredList.add(searchedStaff);
                        }
                    }

                    mFilteredList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = mFilteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mFilteredList = (ArrayList<Staff>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    @Override
    public int getItemCount() {
        return mFilteredList.size();
    }

    @Override
    public void setActive(@NonNull View view, boolean state,int position) {
        Log.i("FunctionCheck",""+state);
        Log.i("FunctionCheck",""+view.getTag());
        Log.i("FunctionCheck",""+viewArray.size());

        LinearLayout relativeLayout = (LinearLayout) view.findViewById(R.id.container);
//        if(viewArray.size() != 0){
        View arr_view=null;
        if(viewArray.size() == 0){
            if(state){
                viewArray.add(view.getTag());
                relativeLayout.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorAccentOpac50));
            }else
                relativeLayout.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorAccentOpac80));
        }else  {

                if (viewArray.contains(view.getTag())) {
                    relativeLayout.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorAccentOpac50));
                } else if (relativeLayout.getBackground().equals(R.color.colorAccentOpac80) && !viewArray.contains(view.getTag())) {
                    viewArray.add(view.getTag());
                    relativeLayout.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorAccentOpac50));
                } else if (!state && viewArray.contains(view.getTag())) {
                    viewArray.remove(view.getTag());
                    relativeLayout.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorAccentOpac80));
                } else if (!state && !viewArray.contains(view.getTag())) {
                    relativeLayout.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorAccentOpac80));
                } else {
                    viewArray.add(view.getTag());
                    relativeLayout.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorAccentOpac50));
                }
            }
        }

//        }else{
//            if (state) {
//                viewArray.add(view);
//                relativeLayout.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorAccentOpac50));
//            } else {
//                relativeLayout.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorAccentOpac80));
//            }
//        }


    @Override
    protected View.OnClickListener defaultItemViewClickListener(CustomViewHolder holder, final int position) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("FunctionCheck","OnClickListener");
                Toast.makeText(mContext, "Click on item " + position, Toast.LENGTH_SHORT).show();
                Staff stf= mFilteredList.get(position);
                Intent intent = new Intent(mContext.getApplicationContext(), Results.class);
                intent.putExtra("code", stf.getId());
                mContext.startActivity(intent);
            }
        };
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {

        public TextView tv_name, tvDepartment, tvDesignation;


        CustomViewHolder(View itemView) {
            super(itemView);
            tv_name = (TextView)itemView.findViewById(R.id.tv_name);
            tvDepartment = (TextView)itemView.findViewById(R.id.tvDept);
            tvDesignation = (TextView)itemView.findViewById(R.id.tvDesig);
        }
    }
}
