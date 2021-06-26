package com.coremacasia.learnatadmin.menus.courses;

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.coremacasia.learnatadmin.commons.CommonDataModel;
import com.coremacasia.learnatadmin.databinding.FragmentCoursesBinding;


import java.util.ArrayList;
import java.util.List;

public class CoursesAdapter extends RecyclerView.Adapter<CoursesAdapter.ViewHolder> {

    private CommonDataModel commonDataModel;
    private List<CourseHelper> list=new ArrayList<>();

    public void setDataModel(CommonDataModel commonDataModel) {
        this.commonDataModel = commonDataModel;
        list=commonDataModel.getAll_courses();
    }

    public CoursesAdapter(Context context) {

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ViewHolder(FragmentCoursesBinding.inflate
                (LayoutInflater.from(parent.getContext()), parent, false));

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(FragmentCoursesBinding binding) {
            super(binding.getRoot());

        }

    }
}