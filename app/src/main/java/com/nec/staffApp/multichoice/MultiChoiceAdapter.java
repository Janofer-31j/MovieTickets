package com.nec.staffApp.multichoice;

import android.Manifest;
import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.PermissionChecker;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Filter;

import com.nec.staffApp.Staff;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


public abstract class MultiChoiceAdapter<VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH>{

    private static final float DESELECTED_ALPHA = 1f;
    static final float SELECTED_ALPHA = 0.25f;
    private static final String EXTRA_ITEM_LIST = "EXTRA_ITEM_LIST";

    boolean mIsInMultiChoiceMode;
    boolean mIsInSingleClickMode;
    private final Object lock = new Object();
    public List<Staff> staffList;
    private Map<Integer, State> mItemList = new LinkedHashMap<>();
    private Listener mListener = null;
    private RecyclerView mRecyclerView;

    //region Public methods

    public MultiChoiceAdapter(List<Staff> list){
        this.staffList = list;
    }
    public abstract Filter getFilter();

    /**
     * Override this method to customize the active item
     *  @param view  the view to customize
     * @param state true if the state is active/selected
     * @param position
     */
    public void setActive(@NonNull View view, boolean state, int position) {
        if (state) {
            view.setAlpha(SELECTED_ALPHA);
        } else {
            view.setAlpha(DESELECTED_ALPHA);
        }
    }

    /**
     * Provide the default behaviour for the item click with multi choice mode disabled
     *
     * @return the onClick action to perform when multi choice selection is off
     */
    protected View.OnClickListener defaultItemViewClickListener(VH holder, int position) {
        return null;
    }

    protected boolean isSelectableInMultiChoiceMode(int position) {
        return true;
    }

    /**
     * Deselect all the selected items in the adapter
     */
    public void deselectAll() {
        performAll(Action.DESELECT);
    }

    /**
     * Select all the view in the adapter
     */
    public void selectAll() {
        performAll(Action.SELECT);
    }

    /**
     * Select an item from the adapter position
     *
     * @param position adapter position of ther view which will be selected
     * @return True if the view has been selected, False if the view is already selected or is not part of the item list
     */
    public boolean select(int position) {
            if (mItemList.containsKey(position) && mItemList.get(position) == State.INACTIVE) {
            perform(Action.SELECT, position, true, true);
            return true;
        }
        return false;
    }

    /**
     * Deselect an item from the adapter position
     *
     * @param position adapter position of the view which will be deselected
     * @return True if the view has been deselected, False if the view is already deselected or is not part of the item list
     */
    public boolean deselect(int position) {
        if (mItemList.containsKey(position) && mItemList.get(position) == State.ACTIVE) {
            perform(Action.DESELECT, position, true, true);
            return true;
        }
        return false;
    }

    /**
     * Set the selection of the RecyclerView to always single click (instead of first long click and then single click)
     *
     * @param set true if single click sctivated
     */
    public void setSingleClickMode(boolean set) {
        mIsInSingleClickMode = set;
        processNotifyDataSetChanged();
    }


    /**
     * Method to get the number of selected items
     *
     * @return number of selected items
     */
    public int getSelectedItemCount() {
        return getSelectedItemListInternal().size();
    }


    /**
     * Get the list of selected item
     *
     * @return Collection of all the selected position in the adapter
     */
    public List<Staff> getSelectedItemList() {
        return getSelectedItemListInternal();
    }

    public void setMultiChoiceSelectionListener(Listener listener) {
        this.mListener = listener;
    }

    /**
     * @return true if the single click mode is active
     */
    public boolean isInSingleClickMode() {
        return mIsInSingleClickMode;
    }

    public void onSaveInstanceState(@Nullable Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            savedInstanceState.putSerializable(EXTRA_ITEM_LIST, (Serializable) mItemList);
        }
    }

    public void onRestoreInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            mItemList = (Map<Integer, State>) savedInstanceState.getSerializable(EXTRA_ITEM_LIST);

            int selectedListSize = getSelectedItemListInternal().size();
            updateToolbarIfNeeded(selectedListSize);
            updateMultiChoiceMode(selectedListSize);
            processNotifyDataSetChanged();
        }
    }

    //endregion

    //region Private methods
    List<Staff> getSelectedItemListInternal() {
        List<Staff> list = new ArrayList<>();
        for(Staff staff : staffList){
            if(staff.selected){
             list.add(staff);
            }
        }
        return list;
    }

    private void processSingleClick(int position) {
        if (mIsInMultiChoiceMode || mIsInSingleClickMode) {
            processClick(position);
        }
    }

    private void processLongClick(int position) {
        if (!mIsInMultiChoiceMode && !mIsInSingleClickMode) {
            processClick(position);
        }
    }

    public void processUpdate(View view, int position) {
        synchronized (lock){
            if (staffList.get(position) != null) {
                if (staffList.get(position).selected) {
                    setActive(view, true,position);
                } else {
                    setActive(view, false,position);
                }
            }
        }
    }

    private void processClick(int position) {
        if (staffList.get(position)!= null) {
            if (staffList.get(position).selected) {
                perform(Action.DESELECT, position, true, true);
            } else {
                perform(Action.SELECT, position, true, true);
            }
        }
    }

    /**
     * Remember to call this method before selecting or deselection something otherwise it won't vibrate
     */
    private void performVibrate() {
        if (!mIsInMultiChoiceMode && !mIsInSingleClickMode && mRecyclerView != null) {
            if (ContextCompat.checkSelfPermission(mRecyclerView.getContext(), Manifest.permission.VIBRATE) == PermissionChecker.PERMISSION_GRANTED) {
                Vibrator v = (Vibrator) mRecyclerView.getContext().getSystemService(Context.VIBRATOR_SERVICE);
                v.vibrate(10);
            }
        }
    }

    private void perform(Action action, int position, boolean withCallback, boolean withVibration) {
        if (withVibration) {
            performVibrate();
        }

        if (action == Action.SELECT) {
            staffList.get(position).selected = true;
        } else {
            staffList.get(position).selected = false;
        }

        int selectedListSize = getSelectedItemListInternal().size();

        updateToolbarIfNeeded(selectedListSize);

        updateMultiChoiceMode(selectedListSize);

        processNotifyDataSetChanged();

        if (mListener != null && withCallback) {
            if (action == Action.SELECT) {
                mListener.OnItemSelected(position, selectedListSize, mItemList.size());
            } else {
                mListener.OnItemDeselected(position, selectedListSize, mItemList.size());
            }
        }
    }

    private void processNotifyDataSetChanged() {
        if (mRecyclerView != null) {
            notifyDataSetChanged();
        }
    }

    private void updateToolbarIfNeeded(int selectedListSize) {
        if ((mIsInMultiChoiceMode || mIsInSingleClickMode || selectedListSize > 0)) {
        }
    }

    private void updateMultiChoiceMode(int selectedListSize) {
        boolean somethingSelected = selectedListSize > 0;
        if (mIsInMultiChoiceMode != somethingSelected) {
            mIsInMultiChoiceMode = somethingSelected;
            processNotifyDataSetChanged();
        }
    }

    private void performAll(Action action) {
        performVibrate();

        int selectedItems;
        State state;
        if (action == Action.SELECT) {
            selectedItems = mItemList.size();
            state = State.ACTIVE;
        } else {
            selectedItems = 0;
            state = State.INACTIVE;
        }

        for (Integer i : mItemList.keySet()) {
            mItemList.put(i, state);
        }

        updateToolbarIfNeeded(selectedItems);
        updateMultiChoiceMode(selectedItems);

        processNotifyDataSetChanged();

        if (mListener != null) {
            if (action == Action.SELECT) {
                mListener.OnSelectAll(getSelectedItemListInternal().size(), mItemList.size());
            } else {
                mListener.OnDeselectAll(getSelectedItemListInternal().size(), mItemList.size());
            }
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        mRecyclerView = recyclerView;

        for (int i = 0; i < getItemCount(); i++) {
            mItemList.put(i, State.INACTIVE);

        }
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public void onBindViewHolder(final VH holder, final int position) {
        View mCurrentView = holder.itemView;
        if ((mIsInMultiChoiceMode || mIsInSingleClickMode) && isSelectableInMultiChoiceMode(position)) {
            mCurrentView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    processSingleClick(holder.getAdapterPosition());
                }
            });
        } else if (defaultItemViewClickListener(holder, position) != null) {
            mCurrentView.setOnClickListener(defaultItemViewClickListener(holder, position));
        }

        mCurrentView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                processLongClick(holder.getAdapterPosition());
                return true;
            }
        });
    }

    public void updateList(List<Staff> list){
       // this.staffList.clear();
       // this.staffList = list;

    }
    //endregion

    //region Package-Protected methods

    @VisibleForTesting
    void setItemList(LinkedHashMap<Integer, State> itemList) {
        mItemList = itemList;
    }

    //endregion

    private enum Action {
        SELECT,
        DESELECT
    }

    enum State {
        ACTIVE,
        INACTIVE
    }

    public interface Listener {

        void OnItemSelected(int selectedPosition, int itemSelectedCount, int allItemCount);

        void OnItemDeselected(int deselectedPosition, int itemSelectedCount, int allItemCount);

        void OnSelectAll(int itemSelectedCount, int allItemCount);

        void OnDeselectAll(int itemSelectedCount, int allItemCount);
    }
}
