package com.coremacasia.learnatadmin.menus.subjects;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.coremacasia.learnatadmin.databinding.FragmentSubjectsBinding;
import com.coremacasia.learnatadmin.menus.subjects.placeholder.PlaceholderContent.PlaceholderItem;


import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link PlaceholderItem}.
 * TODO: Replace the implementation with code for your data type.
 */
public class SubjectsAdapter extends RecyclerView.Adapter<SubjectsAdapter.ViewHolder> {

    private final List<PlaceholderItem> mValues;

    public SubjectsAdapter(List<PlaceholderItem> items) {
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ViewHolder(FragmentSubjectsBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        public ViewHolder(FragmentSubjectsBinding binding) {
            super(binding.getRoot());

        }

       }
}