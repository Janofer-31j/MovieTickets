package com.nec.staffApp.multichoice;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nec.staffApp.R;
import com.nec.staffApp.Staff;

import java.util.ArrayList;

public class SampleCustomViewAdapter extends MultiChoiceAdapter<SampleCustomViewAdapter.SampleCustomViewHolder> {

    private final ArrayList<Staff> mArrayList;
    private ArrayList<Staff> mFilteredList;
    private final Context mContext;

    public SampleCustomViewAdapter(ArrayList<Staff> messageV0s, Context context) {
        this.mArrayList = messageV0s;
        this.mFilteredList = messageV0s;
        this.mContext = context;
    }

    @Override
    public SampleCustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SampleCustomViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sample_custom_view, parent, false));
    }

    @Override
    public void onBindViewHolder(SampleCustomViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);

        Staff currentItem = mFilteredList.get(position);

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
    public void setActive(@NonNull View view, boolean state) {

        LinearLayout relativeLayout = (LinearLayout) view.findViewById(R.id.container);
        if (state) {
            relativeLayout.setBackgroundColor(ContextCompat.getColor(mContext, R.color.btn_disabled));
        } else {
            //relativeLayout.setBackgroundColor(ContextCompat.getColor(mContext, android.R.color.transparent));
        }
    }

    @Override
    protected View.OnClickListener defaultItemViewClickListener(SampleCustomViewHolder holder, final int position) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "Click on item " + position, Toast.LENGTH_SHORT).show();
            }
        };
    }

    class SampleCustomViewHolder extends RecyclerView.ViewHolder {

        public TextView tv_name, tvDepartment, tvDesignation;


        SampleCustomViewHolder(View itemView) {
            super(itemView);
            tv_name = (TextView)itemView.findViewById(R.id.tv_name);
            tvDepartment = (TextView)itemView.findViewById(R.id.tvDept);
            tvDesignation = (TextView)itemView.findViewById(R.id.tvDesig);
        }
    }
}
