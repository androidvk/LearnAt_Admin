package com.coremacasia.learnatadmin.adapter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.coremacasia.learnatadmin.R;
import com.coremacasia.learnatadmin.commons.all_courses.CourseModel;
import com.coremacasia.learnatadmin.databinding.FragmentCoursesBinding;
import com.coremacasia.learnatadmin.helpers.CourseHelper;
import com.coremacasia.learnatadmin.dialogs.Dialog_Add_Course;
import com.coremacasia.learnatadmin.helpers.MentorHelper;
import com.coremacasia.learnatadmin.utility.ImageSetterGlide;
import com.coremacasia.learnatadmin.utility.MyStore;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CoursesAdapter extends RecyclerView.Adapter<CoursesAdapter.ViewHolder> {


    private List<CourseHelper> list = new ArrayList<>();
    private CourseModel courseModel;
    private Context context;
    private String imageLink;

    public void setDataModel(CourseModel courseModel) {
        list = courseModel.getAll_courses();
        this.courseModel = courseModel;
    }

    public CoursesAdapter(Context context) {

        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ViewHolder(FragmentCoursesBinding.inflate
                (LayoutInflater.from(parent.getContext()), parent, false));

    }

    public OnCourseSelectedListener listener;

    public interface OnCourseSelectedListener {
        void onCourseClick(CourseHelper helper);
    }

    public void onCourseClick(OnCourseSelectedListener listener) {
        this.listener = listener;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        CourseHelper helper = list.get(position);
        holder.tTitle.setText(helper.getTitle());
        if (helper.isIs_live()) {
            holder.tLive.setVisibility(View.VISIBLE);
            if (helper.getStart_date() != null) {
                holder.tStartDate.setVisibility(View.VISIBLE);
                String myFormat = "dd-MMMM"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                holder.tStartDate.setText("Starting from: " + sdf.format(helper.getStart_date()));
            } else holder.tStartDate.setVisibility(View.GONE);
        } else {
            holder.tLive.setVisibility(View.GONE);
            holder.tStartDate.setVisibility(View.GONE);
        }
        if (helper.getCourse_price() != null) {
            holder.tPrice.setText("Rs." + helper.getCourse_price());
        }
        holder.tDescription.setText(helper.getDesc());
        new ImageSetterGlide().defaultImg(holder.context, helper.getThumbnail(),
                holder.imageView);

        ArrayList<MentorHelper> mentorList = MyStore.getCommonData().getMentors();
        for (MentorHelper helper1 : mentorList) {
            if (helper.getMentor_id().equals(helper1.getMentor_id())) {
                holder.tMentorName.setText(helper1.getName());
                imageLink = helper1.getImage();
            }
        }

        holder.mainView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // listener.onCourseClick(helper);
            }
        });

        holder.bEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = ((AppCompatActivity) context)
                        .getSupportFragmentManager();
                Dialog_Add_Course dialog_add_course = Dialog_Add_Course.newInstance(helper.getCategory_id()
                        , helper);
                dialog_add_course.show(manager, Dialog_Add_Course.TAG);

            }
        });

        String wallpaper = "https://learnat.in/wp-content/uploads/2021/08/17879-scaled-e1628793009125.jpg";
        new ImageSetterGlide().defaultImg(holder.itemView.getContext(), wallpaper,
                holder.imageView);

        new ImageSetterGlide().defaultImg(holder.itemView.getContext(), imageLink,
                holder.teacherPng);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tTitle, tMentorName, tLive;
        private ImageView imageView, teacherPng;
        private Context context;
        private View mainView;
        private Button bEdit;
        private TextView tStartDate, tDescription, tPrice;

        public ViewHolder(FragmentCoursesBinding binding) {
            super(binding.getRoot());
            context = binding.getRoot().getContext();
            tTitle = itemView.findViewById(R.id.textView24);
            tMentorName = itemView.findViewById(R.id.textView45);
            tLive = itemView.findViewById(R.id.textView39);
            imageView = itemView.findViewById(R.id.imageView9);
            bEdit = binding.button9;
            mainView = binding.mainView;
            tStartDate = binding.textview101;
            tDescription = binding.textView43;
            teacherPng = itemView.findViewById(R.id.imageView15);
            tPrice = binding.textView57;
        }

    }
}