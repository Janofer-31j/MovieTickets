package com.nec.staffApp;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;

public class SearchListAdapter extends RecyclerView.Adapter<SearchListAdapter.ViewHolder> implements Filterable {
    private ArrayList<Staff> mArrayList;
    private ArrayList<Staff> mFilteredList;

    public SearchListAdapter(ArrayList<Staff> arrayList) {
        mArrayList = arrayList;
        mFilteredList = arrayList;
    }

    @Override
    public SearchListAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.search_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SearchListAdapter.ViewHolder viewHolder, int i) {

        viewHolder.tv_name.setText(mFilteredList.get(i).name);
        viewHolder.tvDepartment.setText(mFilteredList.get(i).dept);
        viewHolder.tvDesignation.setText(mFilteredList.get(i).dsn);
    }

    @Override
    public int getItemCount() {
        return mFilteredList.size();
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

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView tv_name, tvDepartment,tvDesignation;
        public ViewHolder(View view) {
            super(view);

            tv_name = (TextView)view.findViewById(R.id.tv_name);
            tvDepartment = (TextView)view.findViewById(R.id.tvDept);
            tvDesignation = (TextView)view.findViewById(R.id.tvDesig);

        }
    }

}
