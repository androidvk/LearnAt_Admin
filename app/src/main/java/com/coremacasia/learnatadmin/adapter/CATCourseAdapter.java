package com.coremacasia.learnatadmin.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.coremacasia.learnatadmin.R;
import com.coremacasia.learnatadmin.commons.CommonDataModel;
import com.coremacasia.learnatadmin.databinding.FragmentCoursesBinding;
import com.coremacasia.learnatadmin.activities.LectureList;
import com.coremacasia.learnatadmin.helpers.CourseHelper;
import com.coremacasia.learnatadmin.dialogs.Dialog_Add_Course;
import com.coremacasia.learnatadmin.helpers.MentorHelper;
import com.coremacasia.learnatadmin.utility.ImageSetterGlide;
import com.coremacasia.learnatadmin.utility.MyStore;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CATCourseAdapter extends RecyclerView.Adapter<CATCourseAdapter.ViewHolder> {

    private CommonDataModel commonDataModel;
    private List<String> list = new ArrayList<>();
    private Context context;

    public void setDataModel(CommonDataModel commonDataModel) {
        this.commonDataModel = commonDataModel;
        list = commonDataModel.getCourse_id();
    }


    public CATCourseAdapter(Activity context) {
        this.context = context;
    }

    @Override
    public CATCourseAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(FragmentCoursesBinding.inflate
                (LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        String course_id = list.get(position);
        for (CourseHelper helper : MyStore.getCourseData().getAll_courses()) {
            if (helper.getCourse_id().equals(course_id)) {

                holder.tTitle.setText(helper.getTitle());
                if (helper.isIs_live()) {
                    holder.tLive.setVisibility(View.VISIBLE);
                    if(helper.getStart_date()!=null){
                        holder.tStartDate.setVisibility(View.VISIBLE);
                        String myFormat = "dd-MMMM"; //In which you need put here
                        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                        holder.tStartDate.setText("Starting from: "+sdf.format(helper.getStart_date()));
                    }else holder.tStartDate.setVisibility(View.GONE);
                } else {
                    holder.tLive.setVisibility(View.GONE);
                    holder.tStartDate.setVisibility(View.GONE);
                }
                if(helper.getCourse_price()!=null){
                    holder.tPrice.setText("Rs."+helper.getCourse_price());
                }
                holder.tDescription.setText(helper.getDesc());
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

                        Gson gson = new Gson();
                        String gSonHelper = gson.toJson(helper);
                        holder.context.startActivity(new Intent(
                                holder.context, LectureList.class)
                        .putExtra("helper", gSonHelper));
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

                return;
            }
        }

    }


    @Override
    public int getItemCount() {
        if (list != null) {
            return list.size();
        } else return 0;

    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tTitle, tMentorName, tLive;
        private ImageView imageView;
        private Context context;
        private View mainView;
        private Button bEdit;
        private TextView tStartDate,tDescription,tPrice;
        public ViewHolder(FragmentCoursesBinding binding) {
            super(binding.getRoot());
            context = binding.getRoot().getContext();
            tTitle = itemView.findViewById(R.id.textView43);
            tMentorName = itemView.findViewById(R.id.textView44);
            tLive = itemView.findViewById(R.id.textView39);
            imageView = itemView.findViewById(R.id.imageView9);
            bEdit = binding.button9;
            mainView = binding.mainView;
            tStartDate=binding.textView51;
            tDescription=binding.textView53;
            tPrice=binding.textView52;
        }

    }
}