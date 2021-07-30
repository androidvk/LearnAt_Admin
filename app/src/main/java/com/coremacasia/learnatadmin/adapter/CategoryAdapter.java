package com.coremacasia.learnatadmin.adapter;

import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.coremacasia.learnatadmin.commons.CommonDataModel;
import com.coremacasia.learnatadmin.databinding.FragmentCategoryBinding;
import com.coremacasia.learnatadmin.activities.CatDetails;
import com.coremacasia.learnatadmin.helpers.CategoryHelper;
import com.coremacasia.learnatadmin.utility.ImageSetterGlide;


import java.util.ArrayList;
import java.util.List;


public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    private Context context;

    public void setDataModel(CommonDataModel dataModel) {
        this.dataModel = dataModel;
        list = dataModel.getCategory();
    }

    private List<CategoryHelper> list = new ArrayList<>();

    private CommonDataModel dataModel;

    public CategoryAdapter(Context context) {
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ViewHolder(FragmentCategoryBinding.
                inflate(LayoutInflater.from(parent.getContext()), parent, false));

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        onBind(holder,position);
    }

    private void onBind(ViewHolder holder, int position) {
        CategoryHelper helper=list.get(position);
        holder.tName.setText("Name: \n"  +helper.getName());
        holder.tDescription.setText("Description: \n"+helper.getDescription());
        holder.tId.setText("ID: \n"+helper.getId());
        new ImageSetterGlide().defaultImg(holder.context,helper.getThumbnail(),holder.imageView);

        holder.mainView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.context.startActivity(new Intent(context, CatDetails.class)
                .putExtra("cat",helper.getId()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private static final String TAG = "ViewHolder";
        private TextView tName,tId,tDescription;
        private ImageView imageView;
        private Context context;
        private View mainView;
        public ViewHolder(FragmentCategoryBinding binding) {
            super(binding.getRoot());
            tName=binding.textView8;
            tId=binding.textView13;
            tDescription=binding.textView15;
            imageView=binding.imageView6;
            mainView=binding.mainView;
            context=binding.getRoot().getContext();

        }

    }
}