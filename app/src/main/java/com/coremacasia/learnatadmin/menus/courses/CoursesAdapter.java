package com.coremacasia.learnatadmin.menus.courses;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
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
import com.coremacasia.learnatadmin.commons.CommonDataModel;
import com.coremacasia.learnatadmin.commons.all_courses.CourseModel;
import com.coremacasia.learnatadmin.databinding.FragmentCoursesBinding;
import com.coremacasia.learnatadmin.menus.category.CatDetail.DF_Add_Course;
import com.coremacasia.learnatadmin.menus.mentors.MentorHelper;
import com.coremacasia.learnatadmin.utility.ImageSetterGlide;
import com.coremacasia.learnatadmin.utility.MyStore;


import java.util.ArrayList;
import java.util.List;

public class CoursesAdapter extends RecyclerView.Adapter<CoursesAdapter.ViewHolder> {


    private List<CourseHelper> list=new ArrayList<>();
    private CourseModel courseModel;
    private Context context;

    public void setDataModel(CourseModel courseModel) {
        list=courseModel.getAll_courses();
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

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        CourseHelper helper=list.get(position);
        holder.tTitle.setText(helper.getTitle());
        if (helper.isIs_live()) {
            holder.tLive.setVisibility(View.VISIBLE);
        } else {
            holder.tLive.setVisibility(View.GONE);
        }
        new ImageSetterGlide().defaultImg(holder.context, helper.getThumbnail(),
                holder.imageView);

        ArrayList<MentorHelper> mentorList = MyStore.getCommonData().getMentors();
        for (MentorHelper helper1 : mentorList) {
            if (helper.getMentor_id().equals(helper1.getMentor_id())) {
                holder.tMentorName.setText(helper1.getName());
            }
        }

        holder.mainView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        holder.bEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = ((AppCompatActivity) context)
                        .getSupportFragmentManager();
                DF_Add_Course df_add_course= DF_Add_Course.newInstance(helper.getCategory_id()
                        ,helper);
                df_add_course.show(manager,DF_Add_Course.TAG);

            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tTitle, tMentorName, tLive;
        private ImageView imageView;
        private Context context;
        private View mainView;
        private Button bEdit;
        public ViewHolder(FragmentCoursesBinding binding) {
            super(binding.getRoot());
            context = binding.getRoot().getContext();
            tTitle = itemView.findViewById(R.id.textView43);
            tMentorName = itemView.findViewById(R.id.textView44);
            tLive = itemView.findViewById(R.id.textView39);
            imageView = itemView.findViewById(R.id.imageView9);
            bEdit=binding.button9;
            mainView=binding.mainView;
        }

    }
}