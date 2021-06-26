package com.coremacasia.learnatadmin.menus.mentors;

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.coremacasia.learnatadmin.R;
import com.coremacasia.learnatadmin.commons.CommonDataModel;
import com.coremacasia.learnatadmin.databinding.FragmentMentorBinding;
import com.coremacasia.learnatadmin.menus.mentors.placeholder.PlaceholderContent.PlaceholderItem;
import com.coremacasia.learnatadmin.utility.ImageSetterGlide;


import java.util.ArrayList;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link PlaceholderItem}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MentorsAdapter extends RecyclerView.Adapter<MentorsAdapter.ViewHolder> {
    private ArrayList<MentorHelper> list = new ArrayList<>();
    private CommonDataModel dataModel;

    private Context activity;
    private CommonDataModel commonDataModel;

    public MentorsAdapter(Context activity) {

    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(FragmentMentorBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        MentorHelper helper=list.get(position);
        holder.tName.setText(helper.getName());
        new ImageSetterGlide().defaultImg(holder.itemView.getContext()
                ,helper.getImage(),holder.imageView);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setDataModel(CommonDataModel commonDataModel) {
        this.commonDataModel = commonDataModel;
        list=commonDataModel.getMentors();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private TextView tName;

        public ViewHolder(FragmentMentorBinding binding) {
            super(binding.getRoot());
            tName = itemView.findViewById(R.id.textView40);
            imageView = itemView.findViewById(R.id.imageView12);
        }

    }
}