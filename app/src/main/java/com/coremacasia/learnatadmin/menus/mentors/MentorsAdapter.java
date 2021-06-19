package com.coremacasia.learnatadmin.menus.mentors;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.coremacasia.learnatadmin.databinding.FragmentMentorBinding;
import com.coremacasia.learnatadmin.menus.mentors.placeholder.PlaceholderContent.PlaceholderItem;


import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link PlaceholderItem}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MentorsAdapter extends RecyclerView.Adapter<MentorsAdapter.ViewHolder> {

    private final List<PlaceholderItem> mValues;

    public MentorsAdapter(List<PlaceholderItem> items) {
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ViewHolder(FragmentMentorBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        public ViewHolder(FragmentMentorBinding binding) {
            super(binding.getRoot());

        }

       }
}