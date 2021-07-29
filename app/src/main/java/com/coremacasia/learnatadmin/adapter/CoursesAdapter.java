package com.coremacasia.learnatadmin.adapter;

import androidx.annotation.NonNull;
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
import com.coremacasia.learnatadmin.dialogs.DF_Add_Course;
import com.coremacasia.learnatadmin.helpers.MentorHelper;
import com.coremacasia.learnatadmin.utility.ImageSetterGlide;
import com.coremacasia.learnatadmin.utility.MyStore;
import com.coremacasia.learnatadmin.utility.Reference;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.WriteBatch;


import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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
        private TextView tStartDate,tDescription,tPrice;
        public ViewHolder(FragmentCoursesBinding binding) {
            super(binding.getRoot());
            context = binding.getRoot().getContext();
            tTitle = itemView.findViewById(R.id.textView43);
            tMentorName = itemView.findViewById(R.id.textView44);
            tLive = itemView.findViewById(R.id.textView39);
            imageView = itemView.findViewById(R.id.imageView9);
            bEdit=binding.button9;
            mainView=binding.mainView;
            tStartDate=binding.textView51;
            tDescription=binding.textView53;
            tPrice=binding.textView52;
        }

    }
}